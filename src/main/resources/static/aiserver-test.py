import json
import logging
import time
import uuid
import argparse
from threading import Thread

import stomp

# Configure logging
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s'
)
logger = logging.getLogger(__name__)

class AIListener(stomp.ConnectionListener):
    """STOMP listener for AI server."""
    
    def __init__(self, ai_server):
        self.ai_server = ai_server
    
    def on_connected(self, frame):
        """Called when connection is established."""
        logger.info("Connected to STOMP broker")
        self.ai_server.connected = True
    
    def on_message(self, frame):
        """Called when a message is received."""
        try:
            body = frame.body
            message = json.loads(body)
            logger.info(f"Received message: {message}")
            
            # Extract the chat ID
            chat_id = message.get('chatId')
            if not chat_id:
                logger.warning("Message missing chatId, cannot process")
                return
            
            # Process message with AI
            response = self.ai_server.process_message(message)
            
            # Send response back to the client's topic
            self.ai_server.send_message(f"/topic/{chat_id}", response)
            
        except Exception as e:
            logger.exception(f"Error processing message: {e}")
    
    def on_error(self, frame):
        """Called when an error occurs."""
        logger.error(f"STOMP error: {frame.body}")
    
    def on_disconnected(self):
        """Called when disconnected from the broker."""
        logger.info("Disconnected from STOMP broker")
        self.ai_server.connected = False

class AIServer:
    """AI Server that processes chat messages."""
    
    def __init__(self, host='localhost', port=61613, user='admin', password='admin'):
        """Initialize the AI server."""
        self.host = host
        self.port = port
        self.user = user
        self.password = password
        self.conn = None
        self.connected = False
    
    def connect(self):
        """Connect to the STOMP broker."""
        try:
            # Create a new connection
            conn = stomp.Connection([(self.host, self.port)])
            
            # Set up the listener
            conn.set_listener('ai_listener', AIListener(self))
            
            # Connect to the broker
            conn.connect(self.user, self.password, wait=True)
            
            # Subscribe to the AI requests topic
            conn.subscribe(
                destination="/topic/ai-requests",
                id=str(uuid.uuid4()),
                ack="auto"
            )
            
            logger.info("Subscribed to /topic/ai-requests")
            self.conn = conn
            return True
            
        except Exception as e:
            logger.exception(f"Connection error: {e}")
            return False
    
    def send_message(self, destination, message):
        """Send a message to the specified destination."""
        if not self.conn:
            logger.error("Not connected to STOMP broker")
            return False
        
        try:
            # Convert message to JSON if it's a dict
            if isinstance(message, dict):
                message = json.dumps(message)
            
            # Send the message
            self.conn.send(
                destination=destination,
                body=message,
                content_type="application/json"
            )
            
            logger.info(f"Sent response to {destination}")
            return True
            
        except Exception as e:
            logger.exception(f"Error sending message: {e}")
            return False
    
    def process_message(self, message):
        # Example AI processing - replace with your actual AI integration
        try:
            # Get the input content
            user_message = message.get('content', '')
            
            # Simulate AI processing time
            time.sleep(1)
            
            # Generate a response - replace with actual AI processing
            ai_response = f"AI response to: {user_message}"
            
            # Create the response object
            response = {
                "chatId": message.get("chatId"),
                "content": ai_response,
                "sender": "AI",
                "timestamp": time.strftime("%Y-%m-%d %H:%M:%S")
            }
            
            logger.info(f"Processed message: {response}")
            return response
            
        except Exception as e:
            logger.exception(f"Error in AI processing: {e}")
            
            # Return an error message
            return {
                "chatId": message.get("chatId"),
                "content": "Sorry, I encountered an error processing your request.",
                "sender": "AI",
                "timestamp": time.strftime("%Y-%m-%d %H:%M:%S")
            }
    
    def run(self):
        """Run the AI server."""
        try:
            # Connect to the broker
            if not self.connect():
                logger.error("Failed to connect to broker. Exiting.")
                return
            
            logger.info("AI Server running. Press Ctrl+C to stop.")
            
            # Keep the main thread alive
            while True:
                if not self.connected:
                    logger.info("Reconnecting...")
                    self.connect()
                time.sleep(1)
                
        except KeyboardInterrupt:
            logger.info("Server shutting down.")
        finally:
            if self.conn:
                self.conn.disconnect()

if __name__ == "__main__":
    # Parse command line arguments
    parser = argparse.ArgumentParser(description='AI Server for chat application')
    parser.add_argument('--host', default='localhost', help='STOMP broker host')
    parser.add_argument('--port', type=int, default=61613, help='STOMP broker port')
    parser.add_argument('--user', default='admin', help='STOMP username')
    parser.add_argument('--password', default='admin', help='STOMP password')
    
    args = parser.parse_args()
    
    # Create and run the AI server
    server = AIServer(
        host=args.host,
        port=args.port,
        user=args.user,
        password=args.password
    )
    
    server.run()
### Requirements
---


1. JDK 17
   1. Navigate to https://www.oracle.com/java/technologies/downloads/#jdk23-windows
   2. Create Oracle account
   3. Install JDK
   4. In Powershell/Iterm/Terminal, run `java -version`. you should find `java version "17.xx.xx" on the screen
2. Spring Boot
   1. Navigate to https://code.visualstudio.com/docs/java/java-spring-boot 
   2. Install the prerequisites (VS Code) : Extension pack for java, and Spring boot extension pack, Spring Boot Dashboard
3. Gradle
   1. Navigate to https://gradle.org/install/#with-a-package-manager
   2. Install Gradle from https://gradle.org/releases/
   3. Follow the **Installing manually** section from (3.1)
   4. Add gradle to environment variable
   5. In Powershell/Iterm/Terminal, run `gradle -v`. you should find the installed gradle version
4. Create .env file
5. Create a Docker container and run PosgreSQL


### Starting Server

---
1. open the Spring Boot Dashboard at the left side of the IDE
2. Click the arrow button newxt to APPS - enus-api-server


### Running docker compose
---
- Create a .env file and fill in the credentials (refer to the .env_template file)
- Load containers: `docker compose up -d`
- Unload containers: `docker compose down`

### Build executable JAR file
---
- `.gradelw bootJar`
- copy the jar file from `build/libs` to the server (Home directory)
- run the jar file: `java -jar <jar file name>`


### API
---
- API Documentation: http://localhost:8080/swagger-ui/index.html
// /*
//  * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
//  * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
//  */

// package com.enus.newsletter.config;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

// import com.mongodb.ConnectionString;
// import com.mongodb.MongoClientSettings;
// import com.mongodb.client.MongoClient;
// import com.mongodb.client.MongoClients;

// /**
//  *
//  * @author idohyeon
//  */
// @Configuration
// public class MongoDbConfig extends AbstractMongoClientConfiguration {
    
//     @Value("${spring.data.mongodb.host}")
//     private String host;

//     @Value("${spring.data.mongodb.port}")
//     private int port;

//     @Value("${spring.data.mongodb.username}")
//     private String username;

//     @Value("${spring.data.mongodb.password}")
//     private String password;

//     @Value("${spring.data.mongodb.database}")
//     private String database;


//     @Override
//     protected String getDatabaseName() {
//         return database;
//     }

//     @Override
//     public MongoClient mongoClient() {
//         ConnectionString connectionString = new ConnectionString(uri);
//         MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
//                 .applyConnectionString(connectionString)
//                 .build();
//         return MongoClients.create(mongoClientSettings);
//     }   

// }

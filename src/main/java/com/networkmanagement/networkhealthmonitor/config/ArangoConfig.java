package com.networkmanagement.networkhealthmonitor.config;

import com.arangodb.ArangoDB;
import com.arangodb.ArangoDatabase;
import com.arangodb.serde.jackson.JacksonSerde;
import com.arangodb.springframework.annotation.EnableArangoRepositories;
import com.arangodb.springframework.config.ArangoConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableArangoRepositories(basePackages = "com.networkmanagement.networkhealthmonitor.repository")
public class ArangoConfig implements ArangoConfiguration {

    @Value("${arangodb.host:localhost}")
    private String host;

    @Value("${arangodb.port:8529}")
    private int port;

    @Value("${arangodb.user:root}")
    private String user;

    @Value("${arangodb.password:root}")
    private String password;

    @Value("${arangodb.database:network_monitoring_system}")
    private String databaseName;

//@Override
//public ArangoDB.Builder arango() {
//    // 1. Setup your custom mapper
//    ObjectMapper objectMapper = new ObjectMapper();
//    objectMapper.registerModule(new JavaTimeModule());
//
//    // 2. FIX: Use .create(objectMapper) instead of .of(objectMapper)
//    // This resolves the "Required: ContentType, Provided: ObjectMapper" error.
//    JacksonSerde serde = JacksonSerde.create(objectMapper);
//
//    // 3. FIX: Return the Builder directly (do NOT call .build())
//    // This resolves the "Required: Builder, Provided: ArangoDB" error.
//    return new ArangoDB.Builder()
//            .host(host, port)
//            .user(user)
//            .password(password)
//            .serde(serde);
//}

    @Override
    public ArangoDB.Builder arango() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        return new ArangoDB.Builder()
                .host(host, port)
                .user(user)
                .password(password)
                .serde(JacksonSerde.create(mapper));
    }

    // 1. Explicitly create the ArangoDB client bean
    @Bean
    public ArangoDB arangoClient() {
        // This calls the arango() method above and builds the actual client
        return arango().build();
    }

    // 2. Now use the bean created above to get the database
    @Bean
    public ArangoDatabase arangoDatabase(ArangoDB arangoClient) {
        return arangoClient.db(databaseName);
    }

    @Override
    public String database() {
        return databaseName;
    }

//    @Bean
//    public ArangoDatabase arangoDatabase(ArangoDB arangoDB) {
//        // This tells Spring: "When someone needs ArangoDatabase,
//        // give them the one named 'network_health_monitoring'"
//        return arangoDB.db(databaseName);
//    }
}

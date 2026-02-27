package com.networkmanagement.networkhealthmonitor;

import com.arangodb.springframework.annotation.EnableArangoRepositories;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
//@EnableArangoRepositories(basePackages = "com.networkmanagement.networkhealthmonitor.repository")
public class NetworkhealthmonitorApplication {

	public static void main(String[] args) {
		SpringApplication.run(NetworkhealthmonitorApplication.class, args);
	}

}

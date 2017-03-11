package com.sectong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
@EnableScheduling
public class YidongyiApplication {

	public static void main(String args[]) {
		SpringApplication.run(YidongyiApplication.class, args);
	}

}

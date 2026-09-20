package com.badiar.badiar_study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class BadiarStudyApplication {

	public static void main(String[] args) {
		SpringApplication.run(BadiarStudyApplication.class, args);
	}

}

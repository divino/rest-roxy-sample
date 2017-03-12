package com.marklogic.rest.examples;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marklogic.rest.examples.pojo.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

/*
* Spring code references:
* http://www.baeldung.com/rest-template
* http://www.baeldung.com/how-to-use-resttemplate-with-basic-authentication-in-spring
* */

@SpringBootApplication
public class Main {

	@Autowired
	private RestTemplateFactory restTemplateFactory;


	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

	@Bean
	public CommandLineRunner run() throws Exception {
		RestTemplate restTemplate = restTemplateFactory.getObject();
		Person person = new Person(
				"julian isabel",
				"rivera",
				"ty",
				121);
		HttpEntity<Person> request = new HttpEntity<>(person);

		try {
			restTemplate.postForObject(
					"http://localhost:28040/v1/resources/crud?rs:uri=thisisit.json",
					request,
					Person.class);
		} catch (HttpStatusCodeException e) {

			if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
				String responseString = e.getResponseBodyAsString();

				System.out.println("Result === " + responseString);
			}
		}
		return null;
	}

}

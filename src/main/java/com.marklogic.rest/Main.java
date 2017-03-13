package com.marklogic.rest.wrappers;

import com.marklogic.rest.pojo.Synonyms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.marklogic.rest.wrappers.pojo.Person;

/*
* Spring code references:
* http://www.baeldung.com/rest-template
* http://www.baeldung.com/how-to-use-resttemplate-with-basic-authentication-in-spring
*
*/

@SpringBootApplication
public class Main implements CommandLineRunner {

	@Autowired
	private Crud crud;

	@Autowired
	private Thesaurus thesaurus;

	@Override
	public void run(String... strings) throws Exception {
		String docUri = "/person/data1.json";

		Person person = new Person(
				"julian isabel koko",
				"rivera",
				"ty",
				121,
				new String[] {"john", "gerry", "antonio"}
		);

		crud.insert(docUri, person);

		Person personResult = crud.retrieve(docUri);

		System.out.println("get " + personResult.toString());

		String thesaurusUri = "samplekangurant.xml";

		thesaurus.load(thesaurusUri, "data/thesaurus.xml");

		String word = "Car";

		Synonyms synonyms = thesaurus.retrieve(thesaurusUri, word);

		System.out.println("word '" + word + "' " + synonyms.toString());
	}

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

}

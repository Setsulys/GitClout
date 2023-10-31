package fr.uge.gitclout.gitclout;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GitcloutApplication {

	public static void main(String[] args) {
		SpringApplication.run(GitcloutApplication.class, args);
//		var db = new DataBase();
//
//		db.insertInTableContributeur("endouij", "Julien");
//		db.insertInTableContributeur("Setsulys", "Steven");
//		db.queryTest();
	}
}

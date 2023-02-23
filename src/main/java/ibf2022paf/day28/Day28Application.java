package ibf2022paf.day28;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ibf2022paf.day28.repositories.AppsRepository;

@SpringBootApplication
public class Day28Application implements CommandLineRunner{

	@Autowired
	private AppsRepository playstoreRepository;
	public static void main(String[] args) {
		SpringApplication.run(Day28Application.class, args);
	}

	@Override
	public void run(String... args){

		List<Document> results = playstoreRepository.groupAppByCategory();

		for (Document d: results)
			System.out.printf(">>> %s\n", d.toJson());
	}

}

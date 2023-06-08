package ma.multitenant;

import ma.multitenant.config.multitenency.TenantContext;
import ma.multitenant.config.multitenency.TenantId;
import ma.multitenant.entities.Author;
import ma.multitenant.entities.Book;
import ma.multitenant.repositories.AuthorRepository;
import ma.multitenant.repositories.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MultitenantApplication {

	public static void main(String[] args) {
		SpringApplication.run(MultitenantApplication.class, args);
	}


	@Bean
	CommandLineRunner commandLineRunner(AuthorRepository authorRepository) {
		return args -> {
			TenantContext.setCurrentTenant(TenantId.CASA);
			Author author = new Author(12,"author1");
			authorRepository.save(author);
		};
	}

}

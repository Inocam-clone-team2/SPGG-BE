package team2.spgg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpggApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpggApplication.class, args);
	}

}

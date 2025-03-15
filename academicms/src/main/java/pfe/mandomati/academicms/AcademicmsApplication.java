package pfe.mandomati.academicms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "pfe.mandomati.academicms.Client")
public class AcademicmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AcademicmsApplication.class, args);
	}

}

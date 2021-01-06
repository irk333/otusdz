package net.anarchy.social.samplesn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@ImportResource({"classpath*:spring-security-login-redirect.xml"})
public class SamplesnApplication {

	public static void main(String[] args) {
		SpringApplication.run(SamplesnApplication.class, args);
	}

}

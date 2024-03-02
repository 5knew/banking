package com.muratkhan.banking;

import com.muratkhan.banking.model.User;
import com.muratkhan.banking.model.enums.Role;
import com.muratkhan.banking.repositories.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
@SpringBootApplication
@EnableScheduling
public class BankingApplication implements CommandLineRunner {
	@Autowired
	private UserRepository userRepository;
	public static void main(String[] args){

		SpringApplication.run(BankingApplication.class, args);
	}
	public void run(String...args){
		User adminAccount = userRepository.findByRole(Role.ROLE_ADMIN);
		if(null == adminAccount){
			User user = new User();

			user.setEmail("admin@gmail.com");
			user.setFirstName("admin");
			user.setLogin("admin");
			user.setLastName("admin");
			user.setRole(Role.ROLE_ADMIN);
			user.setPassword(new BCryptPasswordEncoder().encode("admin"));
			userRepository.save(user);

		}
	}

}

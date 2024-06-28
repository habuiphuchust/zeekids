package com.example.spellingcheck;

import com.example.spellingcheck.repository.RoleRepository;
import com.example.spellingcheck.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@AllArgsConstructor
public class ZeekIdsApplication implements CommandLineRunner {
	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	private RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(ZeekIdsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		List<User> users = userRepository.findAll();
//		List<Role> roleList = roleRepository.findAll();
//		if (roleList.isEmpty()) {
//			Role roleAdmin = new Role();
//			roleAdmin.setId(1L);
//			roleAdmin.setName("ROLE_ADMIN");
//			roleRepository.save(roleAdmin);
//			Role roleUser = new Role();
//			roleUser.setId(2L);
//			roleUser.setName("ROLE_USER");
//			roleRepository.save(roleUser);
//		}
//		if (users.isEmpty()) {
//			User admin = new User();
//			Set<Role> roles = new HashSet<>();
//			Role roleAdmin = new Role();
//			roleAdmin.setId(1L);
//			roleAdmin.setName("ROLE_ADMIN");
//			roles.add(roleAdmin);
//			admin.setUsername("admin");
//			admin.setFullName("ha bui phuc");
//			admin.setPassword(passwordEncoder.encode("123456"));
//			admin.setRoles(roles);
//			userRepository.save(admin);
//		}
	}
}

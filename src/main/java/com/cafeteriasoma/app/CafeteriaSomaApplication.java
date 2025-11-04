package com.cafeteriasoma.app;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.cafeteriasoma.app.entity.Rol;
import com.cafeteriasoma.app.entity.Usuario;
import com.cafeteriasoma.app.repository.RolRepository;
import com.cafeteriasoma.app.repository.UsuarioRepository;

@SpringBootApplication
public class CafeteriaSomaApplication {

	public static void main(String[] args) {
		SpringApplication.run(CafeteriaSomaApplication.class, args);
	}
	@Bean
	CommandLineRunner initAdmin(UsuarioRepository usuarioRepository, 
								RolRepository rolRepository, 
								PasswordEncoder passwordEncoder) {
		return args -> {
			if (usuarioRepository.findByCorreo("admin@soma.com").isEmpty()) {
				Rol rolAdmin = rolRepository.findByNombre("ADMIN")
					.orElseThrow(() -> new RuntimeException("Rol ADMIN no encontrado"));

				Usuario admin = Usuario.builder()
					.nombre("Administrador")
					.correo("admin@soma.com")
					.telefono("123456789")
					.contrasena(passwordEncoder.encode("admin123"))
					.rol(rolAdmin)
					.activo(true)
					.build();

				usuarioRepository.save(admin);
				System.out.println("Administrador creado: admin@soma.com / admin123");
			}
		};
	}

}

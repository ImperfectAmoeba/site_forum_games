package com.example.aulabd.config;

import javax.sql.DataSource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminBootstrap {

    @Bean
    public CommandLineRunner seedAdmin(DataSource dataSource, PasswordEncoder encoder) {
        return args -> {
            JdbcTemplate jdbc = new JdbcTemplate(dataSource);

            // 1. Verifica se o usuário admin já existe
            Integer existing = jdbc.queryForObject(
                "SELECT COUNT(*) FROM usuario WHERE nome = ?", 
                Integer.class, 
                "admin"
            );

            // 2. Se não existir, cria o usuário admin com senha criptografada
            if (existing != null && existing == 0) {
                // Pega a senha da variável de ambiente, ou usa um fallback local
                String senhaAdmin = System.getenv("ADMIN_PASSWORD");
                if (senhaAdmin == null || senhaAdmin.isEmpty()) {
                    senhaAdmin = "admin123"; // fallback para desenvolvimento local
                    System.out.println("⚠️ ADMIN_PASSWORD não configurada. Usando senha padrão admin123");
                }
                
                String senhaCriptografada = encoder.encode(senhaAdmin);
                jdbc.update(
                    "INSERT INTO usuario(nome, email, senha) VALUES (?, ?, ?)",
                    "admin", "admin@gamehelp.com", senhaCriptografada
                );
                System.out.println("✅ Usuário admin criado com sucesso!");
                System.out.println("   Apelido: admin");
                System.out.println("   Senha: (configurada via variável de ambiente)");
            } else {
                System.out.println("ℹ️ Usuário admin já existe.");
            }

            // 3. Busca o ID do usuário admin
            String adminId = jdbc.queryForObject(
                "SELECT id FROM usuario WHERE nome = ?", 
                String.class, 
                "admin"
            );

            // 4. Insere o perfil admin (se não existir)
            Integer perfilExistente = jdbc.queryForObject(
                "SELECT COUNT(*) FROM perfil WHERE usuarioid = ?::uuid",
                Integer.class,
                adminId
            );

            if (perfilExistente == 0) {
                jdbc.update(
                    "INSERT INTO perfil(usuarioid, cargo) VALUES (?::uuid, ?)",
                    adminId, "admin"
                );
                System.out.println("✅ Perfil admin atribuído com sucesso!");
            } else {
                System.out.println("ℹ️ Perfil admin já existe.");
            }
        };
    }
}
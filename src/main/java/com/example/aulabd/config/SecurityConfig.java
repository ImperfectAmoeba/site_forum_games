package com.example.aulabd.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);

        manager.setUsersByUsernameQuery(
            "SELECT nome, senha, true FROM usuario WHERE nome = ?"
        );

        manager.setAuthoritiesByUsernameQuery(
            "SELECT u.nome, p.cargo FROM perfil p " +
            "JOIN usuario u ON p.usuarioid = u.id WHERE u.nome = ?"
        );

        return manager;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService uds,
                                                            PasswordEncoder encoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(uds);
        provider.setPasswordEncoder(encoder);
        return provider;
    }

    @Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            // Páginas públicas
            .requestMatchers("/", "/login", "/css/**", "/js/**", "/usuario", "/listagem", "/post/listar", "/post/{id}").permitAll()
            
            // Categorias - admin OU mod podem criar/editar/excluir
            .requestMatchers("/categoria/cadastrar", "/categoria/listar", "/categoria/{id}/editar", "/categoria/{id}/deletar").hasAnyAuthority("admin", "mod")
            
            // Moderadores (apenas admin pode gerenciar moderadores)
            .requestMatchers("/moderador/**").hasAuthority("admin")
            .requestMatchers("/admin/**").hasAuthority("admin")
            
            // Posts - admin OU mod podem editar/excluir qualquer post
            .requestMatchers("/post/{id}/editar", "/post/{id}/deletar", "/post/{id}/mudar-categoria").hasAnyAuthority("admin", "mod")
            .requestMatchers("/post/criar").authenticated()
            
            // Qualquer outra requisição exige login
            .anyRequest().authenticated()
        )
        .formLogin(form -> form
            .loginPage("/login")
            .loginProcessingUrl("/login")
            .defaultSuccessUrl("/", true)
            .failureUrl("/login?error")
            .permitAll()
        )
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login?logout")
            .permitAll()
        )
        .build();
}

    
}
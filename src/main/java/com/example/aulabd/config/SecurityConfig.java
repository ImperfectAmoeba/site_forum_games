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
                .requestMatchers("/", "/login", "/css/**", "/js/**", "/usuario", "/listagem").permitAll()
                
                .requestMatchers("/categoria/cadastrar", "/categoria/listar", "/categoria/atribuir-moderador").hasAuthority("admin")
                .requestMatchers("/moderador/**").hasAuthority("admin")
                
                .requestMatchers("/post/listar", "/post/{id}").permitAll()
                .requestMatchers("/post/criar", "/post/{id}/editar", "/post/{id}/deletar").authenticated()
                .requestMatchers("/post/{id}/editar", "/post/{id}/deletar").hasAnyAuthority("admin", "mod")
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
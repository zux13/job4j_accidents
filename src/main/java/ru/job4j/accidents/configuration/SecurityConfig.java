package ru.job4j.accidents.configuration;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import ru.job4j.accidents.model.User;
import ru.job4j.accidents.repository.SpringDataAuthorityRepository;
import ru.job4j.accidents.repository.SpringDataUserRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final DataSource dataSource;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager(PasswordEncoder passwordEncoder,
                                                     SpringDataUserRepository userRepository,
                                                     SpringDataAuthorityRepository authorityRepository) {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);

        manager.setUsersByUsernameQuery(
                "select username, password, enabled from users where username = ?"
        );

        manager.setAuthoritiesByUsernameQuery(
                """
                select u.username, a.authority
                from users u
                join authorities a on u.authority_id = a.id
                where u.username = ?
                """
        );

        if (userRepository.findByName("root").isEmpty()) {

            User root = new User();
            root.setName("root");
            root.setPassword(passwordEncoder.encode("secret"));
            root.setAuthority(authorityRepository.findByAuthority("ROLE_ADMIN"));
            root.setEnabled(true);

            userRepository.save(root);
        }

        return manager;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/register").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/")
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .permitAll()
                );

        return http.build();
    }
}

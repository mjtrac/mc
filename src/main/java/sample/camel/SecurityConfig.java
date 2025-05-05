package sample.camel;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.Customizer;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	http
	    .csrf(csrf -> csrf.disable())
	    .authorizeHttpRequests(authz -> authz
			   .requestMatchers("/public/**").permitAll()
			   .anyRequest().authenticated()
				   )
	    .httpBasic(Customizer.withDefaults())
	    .formLogin();
	return http.build();
    }

    @Bean
    public UserDetailsService users() {
	UserDetails user = User.withDefaultPasswordEncoder()
	    .username("admin").password("password").roles("USER").build();
	return new InMemoryUserDetailsManager(user);
    }
}

/*
 * Copyright 2025 Mitch Trachtenberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
	    .formLogin(form -> form
		       .defaultSuccessUrl("/", true) 
		       .permitAll());
	return http.build();
    }

    @Bean
    public UserDetailsService users() {
	UserDetails user = User.withDefaultPasswordEncoder()
	    .username("admin").password("password").roles("USER").build();
	return new InMemoryUserDetailsManager(user);
    }
}

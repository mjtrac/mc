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



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import java.net.URI;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   ClientRegistrationRepository clientRegistrationRepository) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/public/**").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2Login()  // enables OAuth2 login
            .and()
            .logout(logout -> logout
                .logoutSuccessHandler(oidcLogoutSuccessHandler(clientRegistrationRepository))
            );

        return http.build();
    }

    private LogoutSuccessHandler oidcLogoutSuccessHandler(ClientRegistrationRepository clientRegistrationRepository) {
        OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler =
            new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);

        // Replace with your Keycloak logout URL if needed
        oidcLogoutSuccessHandler.setPostLogoutRedirectUri("http://localhost:8080/");
        return oidcLogoutSuccessHandler;
    }
}


/*
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

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


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
		       .defaultSuccessUrl("/ui/routes", true) 
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
*/

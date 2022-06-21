package net.javaguides.springboot.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity //apply the class to the global WebSecurity
public class SecurityConfig{

	@Bean //applied to specify that it returns a bean to be managed by Spring context.
	//a bean exploits the Inversion of Control feature by which an object defines its dependencies without creating them
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		//permitAll() allows anonymous access to the configured endpoint
		http.authorizeRequests().antMatchers("/login").permitAll();

		http.headers().frameOptions().sameOrigin();
		
		return http.build();
    }
	
}

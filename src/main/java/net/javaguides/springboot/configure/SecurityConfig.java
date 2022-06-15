package net.javaguides.springboot.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	
		http.authorizeRequests().antMatchers("/login").permitAll();

		http.headers().frameOptions().sameOrigin();
		
		return http.build();
    }
     
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
    	return (web) -> web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**");
    }
    
	protected void configure(HttpSecurity security) throws Exception {
		// TODO Auto-generated method stub
		security.csrf().disable().authorizeRequests().anyRequest().permitAll();
	}

	
}

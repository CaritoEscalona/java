package com.example.employees.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import io.jsonwebtoken.security.Keys;


@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers("/auth/**").permitAll()
	            .requestMatchers(HttpMethod.POST, "/api/employees").hasRole("ADMIN")
	            .anyRequest().authenticated()
	        )
	        .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	        .oauth2ResourceServer(oauth2 -> oauth2
	            .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
	        )
	        .csrf(AbstractHttpConfigurer::disable);

	    return http.build();
	}

    @Bean
    public PasswordEncoder passwordEncoder() { 
        return new BCryptPasswordEncoder(); 
    }
    
    @Bean
    public JwtDecoder jwtDecoder(@Value("${security.jwt.secret}") String secret) {
      return NimbusJwtDecoder.withSecretKey(Keys.hmacShaKeyFor(secret.getBytes())).build();
    }
    
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
    	JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
    	converter.setAuthoritiesClaimName("roles");
    	converter.setAuthorityPrefix("");
    	JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
    	jwtConverter.setJwtGrantedAuthoritiesConverter(converter);
    	return jwtConverter;
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

}

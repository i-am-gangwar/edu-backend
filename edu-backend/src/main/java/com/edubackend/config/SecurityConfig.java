package com.edubackend.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;


@Configuration
public class SecurityConfig {


    /**
     * Configures the security filter chain.
     * - Disables CSRF for stateless REST APIs.
     * - Configures CORS to allow requests from specific origins.
     * - Permits all HTTP requests (adjust this for specific security needs).
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.println("checking cors for request");
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF protection as it's not needed for stateless APIs.
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Apply custom CORS configuration.
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll() // Allow all requests to access APIs (adjust for specific routes).
                );

        return http.build();
    }

    /**
     * Defines the CORS configuration.
     * - Allows requests only from specific origins (https://www.gsbyvishnusir.com and localhost:5173).
     * - Allows all HTTP methods (GET, POST, etc.).
     * - Allows all headers (e.g., Authorization, Content-Type).
     * - Supports credentials (e.g., cookies or Authorization headers).
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = getCorsConfiguration();

        // Allow sending credentials (e.g., cookies or Authorization headers)
        configuration.setAllowCredentials(true);

        // Register the configuration for all API endpoints
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply CORS rules to all API endpoints.

        return source;
    }

    private static CorsConfiguration getCorsConfiguration() {

        CorsConfiguration configuration = new CorsConfiguration();
        System.out.println("checking allowed cors for request");
        // Specify allowed origins
        configuration.addAllowedOrigin("https://www.gsbyvishnusir.com");
        configuration.addAllowedOrigin("https://gsbyvishnusir.com"); // Allow production domain
        configuration.addAllowedOrigin("http://localhost:5173");
        configuration.addAllowedOrigin("http://localhost:8080");// Allow local development environment


        // Allow all HTTP methods (GET, POST, PUT, DELETE, etc.)
        configuration.addAllowedMethod("*");

        // Allow all headers to support custom headers like Authorization
        configuration.addAllowedHeader("*");
        return configuration;
    }
}

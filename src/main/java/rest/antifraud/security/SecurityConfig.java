package rest.antifraud.security;

import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import rest.antifraud.authentication.RestAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;

    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final HandlerMappingIntrospector introspector;

    @Autowired
    public SecurityConfig(AuthenticationProvider authenticationProvider, RestAuthenticationEntryPoint restAuthenticationEntryPoint, HandlerMappingIntrospector introspector) {
        this.authenticationProvider = authenticationProvider;
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
        this.introspector = introspector;
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(authenticationProvider);

        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(Customizer.withDefaults())
                .csrf(CsrfConfigurer::disable)
                .exceptionHandling(handing ->
                        handing.authenticationEntryPoint(restAuthenticationEntryPoint)
                )
                .headers(headers -> headers.frameOptions().disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(new MvcRequestMatcher(introspector,"/api/auth/user")).permitAll()
                        .requestMatchers(new MvcRequestMatcher(introspector,"/api/auth/user/*")).hasRole("ADMINISTRATOR")
                        .requestMatchers(new MvcRequestMatcher(introspector,"/actuator/shutdown")).permitAll()
                        .requestMatchers(new MvcRequestMatcher(introspector, "/api/auth/list")).hasAnyRole("ADMINISTRATOR", "SUPPORT")
                        .requestMatchers(new MvcRequestMatcher(introspector, "/api/antifraud/transaction")).hasRole("MERCHANT")
                        .requestMatchers(new MvcRequestMatcher(introspector, "/api/auth/access")).hasRole("ADMINISTRATOR")
                        .requestMatchers(new MvcRequestMatcher(introspector, "/api/auth/role")).hasRole("ADMINISTRATOR")
                        .requestMatchers(new MvcRequestMatcher(introspector,"/api/antifraud/suspicious-ip")).hasRole("SUPPORT")
                        .requestMatchers(new MvcRequestMatcher(introspector,"/api/antifraud/suspicious-ip/*")).hasRole("SUPPORT")
                        .requestMatchers(new MvcRequestMatcher(introspector,"/api/antifraud/stolencard")).hasRole("SUPPORT")
                        .requestMatchers(new MvcRequestMatcher(introspector,"/api/antifraud/stolencard/*")).hasRole("SUPPORT")
                        .requestMatchers(new MvcRequestMatcher(introspector,"/api/antifraud/history")).hasRole("SUPPORT")
                        .requestMatchers(new MvcRequestMatcher(introspector,"/api/antifraud/history/*")).hasRole("SUPPORT")
                        .requestMatchers(new MvcRequestMatcher(introspector, "/api/antifraud/transaction")).hasRole("SUPPORT")
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }
}

package br.edu.ifpr.delivery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class Configuracoes {

    @Autowired
    private FiltroToken filtroToken;

    @Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
            .csrf(csrf -> csrf.disable())
            .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests((authorize) ->
                    authorize
                            .requestMatchers(HttpMethod.POST,  "/pedido").permitAll()
                           // .requestMatchers(HttpMethod.POST, "/usuario/").permitAll()  // Permite o cadastro de usuários sem autenticação
                           // .requestMatchers(HttpMethod.POST, "/pedido").authenticated()
                           // .requestMatchers(HttpMethod.POST, "/pedido/**").authenticated() // Permite POST para pedidos autenticados
                            .anyRequest().authenticated()
                            
            )
            .addFilterBefore(filtroToken, UsernamePasswordAuthenticationFilter.class)
            .build();
}
}

package br.edu.ifpr.delivery;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.edu.ifpr.delivery.model.Usuario;
import br.edu.ifpr.delivery.repositorio.UsuarioRepositorio;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FiltroToken extends OncePerRequestFilter {

    @Autowired
    private UsuarioRepositorio _usuarioRepositorio;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var authenticationHeader = request.getHeader("Authorization");

        if (authenticationHeader != null) {
            String token = authenticationHeader; // O token é o próprio valor do cabeçalho
            Optional<Usuario> usuario = this._usuarioRepositorio.findByChave(token);

            if (usuario.isPresent()) {
                // Crie uma lista vazia ou adicione as autoridades reais do usuário aqui
                var authentication = new UsernamePasswordAuthenticationToken(usuario.get(), null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                return; // Finaliza a resposta se o usuário não for encontrado
            }
        } else {
            filterChain.doFilter(request, response); // Continua a cadeia de filtros, já que o header não está presente ou não é necessário
            return; 
        }

        filterChain.doFilter(request, response); // Continua a cadeia de filtros
    }
}
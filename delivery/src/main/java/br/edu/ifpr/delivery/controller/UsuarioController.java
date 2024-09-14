package br.edu.ifpr.delivery.controller;

import br.edu.ifpr.delivery.model.Usuario;
import br.edu.ifpr.delivery.repositorio.UsuarioRepositorio;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioRepositorio _usuarioRepositorio;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);

    @GetMapping(value = "/")
    public List<Usuario> getAll() {
        List<Usuario> usuarios = (List<Usuario>) _usuarioRepositorio.findAll();
        System.out.println(usuarios); // Essa linha vai imprimir os usuários no console
        return usuarios;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Usuario> get(@PathVariable("id") long id) {
        Optional<Usuario> usuario = _usuarioRepositorio.findById(id);
        if (usuario.isPresent()) {
            return new ResponseEntity<>(usuario.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/")
    public ResponseEntity<?> criarUsuario(@RequestBody @Valid Usuario usuario, BindingResult validacao) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (validacao.hasErrors()) {
            map.put("status", HttpStatus.BAD_REQUEST.value());
            map.put("erros", validacao.getAllErrors());
            return new ResponseEntity<Object>(map, HttpStatus.OK);
        } else {
            try {
                usuario.setSenha(encoder.encode(usuario.getSenha()));  // Encripta a senha antes de salvar no banco.

                Usuario user = _usuarioRepositorio.save(usuario);
                map.put("status", HttpStatus.CREATED.value());
                map.put("dados", user);
                return new ResponseEntity<Object>(map, HttpStatus.OK);
            } catch (Exception e) {
                map.put("status", HttpStatus.BAD_REQUEST.value());
                map.put("mensagem", e.getMessage());
                return new ResponseEntity<Object>(map, HttpStatus.OK);
            }

        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deletarUsuario(@PathVariable("id") long id) {
        Optional<Usuario> usuario = _usuarioRepositorio.findById(id);
        if (usuario.isPresent()) {
            _usuarioRepositorio.delete(usuario.get());
            return new ResponseEntity<>(HttpStatus.OK);
        
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> atualizarUsuario(@PathVariable("id") long id, @RequestBody @Valid Usuario usuarioNovo, BindingResult validacao) {
        Optional<Usuario> usuarioAntigo=_usuarioRepositorio.findById(id);
        if (usuarioAntigo.isPresent()) {
            Usuario usuario = usuarioAntigo.get();
            usuario.setNome(usuarioNovo.getNome());
            //usuario.setLogin(usuarioNovo.getLogin());
            //usuario.setSenha(usuarioNovo.getSenha());
            _usuarioRepositorio.save(usuario);
            return new ResponseEntity<Usuario>(usuario, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
    @PostMapping(value ="/login")
    public ResponseEntity<?> login(@RequestBody Usuario usuario) {
        Optional<Usuario> result =_usuarioRepositorio.findByLogin(usuario.getLogin());
        if (result.isPresent()) {
            Usuario user = result.get();
            if (encoder.matches(usuario.getSenha(), user.getSenha())) {
                String chave = KeyGenerators.string().generateKey();
                _usuarioRepositorio.updateChave(chave, user.getId());
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("chave", chave);
                return new ResponseEntity<>(map, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Senha inválida", HttpStatus.NOT_FOUND);
            }
            
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
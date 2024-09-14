package br.edu.ifpr.delivery.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpr.delivery.model.Categoria;
import br.edu.ifpr.delivery.repositorio.CategoriaRepositorio;

@RestController
@RequestMapping("/categoria")
public class CategoriaController {

    @Autowired
    private CategoriaRepositorio categoriaRepositorio;

    @GetMapping
    public List<Categoria> listarCategorias() {
        return categoriaRepositorio.findAll();
    }
}
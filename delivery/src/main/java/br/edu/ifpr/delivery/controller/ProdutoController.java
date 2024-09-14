package br.edu.ifpr.delivery.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ifpr.delivery.model.Produto;
import br.edu.ifpr.delivery.repositorio.ProdutoRepositorio;

@RestController
@RequestMapping("/produto")
public class ProdutoController {

    @Autowired
    private ProdutoRepositorio produtoRepositorio;

    @GetMapping("/categoria/{id}")
    public List<Produto> listarProdutosPorCategoria(@PathVariable Long id) {
        return produtoRepositorio.findByCategoriaId(id);
    }

    @GetMapping
    public List<Produto> listarTodosProdutos() {
        return produtoRepositorio.findAll();
    }
}
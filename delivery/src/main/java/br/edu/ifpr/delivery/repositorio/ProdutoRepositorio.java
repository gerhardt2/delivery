package br.edu.ifpr.delivery.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpr.delivery.model.Produto;

public interface ProdutoRepositorio extends JpaRepository<Produto, Long> {
    List<Produto> findByCategoriaId(Long categoriaId);
}
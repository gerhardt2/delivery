package br.edu.ifpr.delivery.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import br.edu.ifpr.delivery.model.Categoria;

public interface CategoriaRepositorio extends JpaRepository<Categoria, Long> {
}
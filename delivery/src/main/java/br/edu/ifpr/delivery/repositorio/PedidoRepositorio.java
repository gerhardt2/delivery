package br.edu.ifpr.delivery.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import br.edu.ifpr.delivery.model.Pedido;

public interface PedidoRepositorio extends JpaRepository<Pedido, Long> {
}
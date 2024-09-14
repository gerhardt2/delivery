package br.edu.ifpr.delivery.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpr.delivery.model.Pedido;
import br.edu.ifpr.delivery.model.PedidoProduto;

public interface PedidoProdutoRepositorio extends JpaRepository<PedidoProduto, Long> {

    // Método para buscar todos os produtos de um pedido específico
    List<PedidoProduto> findByPedido(Pedido pedido);
}
package br.edu.ifpr.delivery.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.edu.ifpr.delivery.model.Pedido;
import br.edu.ifpr.delivery.model.PedidoProduto;
import br.edu.ifpr.delivery.model.Produto;
import br.edu.ifpr.delivery.repositorio.PedidoProdutoRepositorio;
import br.edu.ifpr.delivery.repositorio.PedidoRepositorio;
import br.edu.ifpr.delivery.repositorio.ProdutoRepositorio;

@RestController
@RequestMapping("/pedido")
public class PedidoController {

    @Autowired
    private PedidoRepositorio pedidoRepositorio;

    @Autowired
    private PedidoProdutoRepositorio pedidoProdutoRepositorio;

    @Autowired
    private ProdutoRepositorio produtoRepositorio;

    @GetMapping
    public List<Pedido> listarPedidos() {
        System.out.println("Listando todos os pedidos...");
        List<Pedido> pedidos = pedidoRepositorio.findAll();
        System.out.println("Pedidos encontrados: " + pedidos.size());
        return pedidos;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPedido(@PathVariable Long id) {
        System.out.println("Buscando pedido com ID: " + id);
        return pedidoRepositorio.findById(id)
                .map(pedido -> {
                    System.out.println("Pedido encontrado: " + pedido);
                    return ResponseEntity.ok().body(pedido);
                })
                .orElseGet(() -> {
                    System.out.println("Pedido não encontrado com ID: " + id);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                });
    }

    @PostMapping(value = "/")
    public Pedido criarPedido(@RequestBody Pedido pedido) {
        System.out.println("Criando novo pedido...");
        Pedido pedidoSalvo = pedidoRepositorio.save(pedido);
        System.out.println("Pedido criado com sucesso: " + pedidoSalvo);
        return pedidoSalvo;
    }

    @PostMapping("/{id}/produto")
    public ResponseEntity<?> adicionarProdutoAoPedido(@PathVariable Long id, @RequestBody PedidoProduto pedidoProduto) {
        System.out.println("Adicionando produto ao pedido com ID: " + id);
        
        // Verifica se o pedido existe
        Pedido pedido = pedidoRepositorio.findById(id).orElse(null);
        if (pedido == null) {
            System.out.println("Pedido não encontrado com ID: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido não encontrado.");
        }
        System.out.println("Pedido encontrado: " + pedido);

        // Verifica se o produto existe
        Long produtoId = pedidoProduto.getProduto().getId();
        System.out.println("Verificando se o produto com ID " + produtoId + " existe...");
        Produto produto = produtoRepositorio.findById(produtoId).orElse(null);
        if (produto == null) {
            System.out.println("Produto não encontrado com ID: " + produtoId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado.");
        }
        System.out.println("Produto encontrado: " + produto);

        // Configura o pedido e o produto no PedidoProduto
        pedidoProduto.setPedido(pedido);
        pedidoProduto.setProduto(produto);

        // Salva o PedidoProduto
        System.out.println("Salvando a relação entre pedido e produto...");
        PedidoProduto savedPedidoProduto = pedidoProdutoRepositorio.save(pedidoProduto);
        System.out.println("Relação pedido-produto salva com sucesso: " + savedPedidoProduto);

        // Adiciona o PedidoProduto ao pedido e atualiza o pedido
        pedido.addProduto(savedPedidoProduto);
        pedidoRepositorio.save(pedido);
        System.out.println("Produto adicionado ao pedido com sucesso!");

        return ResponseEntity.ok().body(savedPedidoProduto);
    }

    // Método para listar os produtos de um pedido específico
    @GetMapping("/{id}/produtos")
    public ResponseEntity<List<PedidoProduto>> listarProdutosDoPedido(@PathVariable Long id) {
        // Busca o pedido pelo ID
        Optional<Pedido> pedidoOpt = pedidoRepositorio.findById(id);
        if (!pedidoOpt.isPresent()) {
            System.out.println("Pedido não encontrado com ID: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Recupera os produtos associados ao pedido
        Pedido pedido = pedidoOpt.get();
        List<PedidoProduto> produtos = pedidoProdutoRepositorio.findByPedido(pedido);
        System.out.println("Produtos encontrados no pedido " + id + ": " + produtos.size());

        // Retorna a lista de produtos
        return ResponseEntity.ok(produtos);
    }
}
package br.edu.ifpr.delivery.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime data;

    private float valor;

    private String entrega;

    @ManyToOne
    private Usuario usuario;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<PedidoProduto> produtos = new ArrayList<>();

    // Construtores, getters e setters
    public Pedido() {}

    public Pedido(LocalDateTime data, float valor, String entrega, Usuario usuario) {
        this.data = data;
        this.valor = valor;
        this.entrega = entrega;
        this.usuario = usuario;
    }

    public void addProduto(PedidoProduto pedidoProduto) {
        produtos.add(pedidoProduto);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public String getEntrega() {
        return entrega;
    }

    public void setEntrega(String entrega) {
        this.entrega = entrega;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<PedidoProduto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<PedidoProduto> produtos) {
        this.produtos = produtos;
    }

    // outros getters e setters
}
package com.example.demo.shared;

import com.example.demo.model.Cliente;
import com.example.demo.model.Pedido;
import com.example.demo.model.PedidoItem;
import com.example.demo.model.Produto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class PedidoDTO {

    private Long id;
    private Date dataPedido;
    private Double descontoTotal;
    private Double total;
    private Cliente cliente;
    private List<PedidoItem> pedidoItens;

    public PedidoDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(Date dataPedido) {
        this.dataPedido = dataPedido;
    }

    public Double getDescontoTotal() {
        return descontoTotal;
    }

    public void setDescontoTotal(Double descontoTotal) {
        this.descontoTotal = descontoTotal;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<PedidoItem> getPedidoItens() {
        return pedidoItens;
    }

    public void setPedidoItens(List<PedidoItem> pedidoItens) {
        this.pedidoItens = pedidoItens;
    }

    public Pedido toPedido() {
        Pedido pedido = new Pedido();
        pedido.setId(this.id);
        pedido.setDataPedido(new Date());
        pedido.setDescontoTotal(this.descontoTotal);
        pedido.setTotal(this.total);
        pedido.setCliente(this.cliente);

        List<PedidoItem> pedidoItens = this.pedidoItens.stream()
                .map(item -> {
                    PedidoItem pedidoItem = new PedidoItem();
                    pedidoItem.setId(item.getId());
                    pedidoItem.setQuantidade(item.getQuantidade());
                    pedidoItem.setPreco(item.getPreco());
                    pedidoItem.setDescontoUnitario(item.getDescontoUnitario());
                    pedidoItem.setSubTotal(item.getSubTotal());
                    Produto produto = new Produto();
                    produto.setNome(item.getProduto().getNome());
                    pedidoItem.setProduto(produto);

                    return pedidoItem;
                })
                .collect(Collectors.toList());

        pedido.setPedidoItens(pedidoItens);

        return pedido;
    }
}

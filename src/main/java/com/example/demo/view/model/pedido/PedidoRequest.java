package com.example.demo.view.model.pedido;

import java.util.List;

public class PedidoRequest {

    private Double descontoTotal;
    private Long clienteId;
    private List<Long> pedidoItensIds;

    public Double getDescontoTotal() {
        return descontoTotal;
    }

    public void setDescontoTotal(Double descontoTotal) {
        this.descontoTotal = descontoTotal;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public List<Long> getPedidoItensIds() {
        return pedidoItensIds;
    }

    public void setPedidoItensIds(List<Long> pedidoItensIds) {
        this.pedidoItensIds = pedidoItensIds;
    }
}

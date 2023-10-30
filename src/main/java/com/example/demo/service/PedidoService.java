package com.example.demo.service;

import com.example.demo.model.Cliente;
import com.example.demo.model.Pedido;
import com.example.demo.model.PedidoItem;
import com.example.demo.model.exception.ResourceNotFoundException;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.repository.PedidoItemRepository;
import com.example.demo.repository.PedidoRepository;
import com.example.demo.shared.PedidoDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    PedidoItemRepository pedidoItemRepository;

    public List<PedidoDTO> buscarTodos() {
        List<Pedido> list = pedidoRepository.findAll();
        return list
                .stream()
                .map(pedido -> new ModelMapper().map(pedido, PedidoDTO.class))
                .collect(Collectors.toList());
    }

    public PedidoDTO buscarPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado."));
        return new ModelMapper().map(pedido, PedidoDTO.class);
    }

    public PedidoDTO adicionar(PedidoDTO dto) {
        dto.setId(null);

        Cliente cliente = clienteRepository.findById(dto.getCliente().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado."));

        Double soma = 0.0;
        List<PedidoItem> pedidoItens = new ArrayList<>();
        for (PedidoItem x : dto.getPedidoItens()) {
            PedidoItem pedidoItem = pedidoItemRepository.findById(x.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Item não encontrado."));

            soma += pedidoItem.getSubTotal();
            pedidoItens.add(pedidoItem);
        }

        dto.setPedidoItens(pedidoItens);

        dto.setTotal(soma - dto.getDescontoTotal());
        dto.setCliente(cliente);

        Pedido pedido = new ModelMapper().map(dto, Pedido.class);
        pedido.setDataPedido(new Date());
        pedido = pedidoRepository.save(pedido);
        dto.setId(pedido.getId());

        return dto;
    }

    public PedidoDTO atualizar(Long id, PedidoDTO dto) {
        dto.setId(id);

        Pedido pedido = new ModelMapper().map(dto, Pedido.class);
        pedido = pedidoRepository.save(pedido);

        return new ModelMapper().map(pedido, PedidoDTO.class);
    }

    public boolean deletar(Long id) {
        if (pedidoRepository.existsById(id)) {
            pedidoRepository.deleteById(id);
        } else {
            return false;
        }
        return true;
    }
}

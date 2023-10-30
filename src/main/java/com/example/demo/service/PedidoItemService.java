package com.example.demo.service;

import com.example.demo.model.PedidoItem;
import com.example.demo.model.Produto;
import com.example.demo.model.exception.ResourceNotFoundException;
import com.example.demo.repository.PedidoItemRepository;
import com.example.demo.repository.ProdutoRepository;
import com.example.demo.shared.PedidoItemDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoItemService {

    @Autowired
    PedidoItemRepository pedidoItemRepository;

    @Autowired
    ProdutoRepository produtoRepository;

    public List<PedidoItemDTO> buscarTodos() {
        List<PedidoItem> list = pedidoItemRepository.findAll();
        return list
                .stream()
                .map(pedidoItem -> new ModelMapper().map(pedidoItem, PedidoItemDTO.class))
                .collect(Collectors.toList());
    }

    public PedidoItemDTO buscarPorId(Long id) {
        PedidoItem item = pedidoItemRepository
                .findById(id).orElseThrow(() -> new ResourceNotFoundException("Item não encontrado."));
        return new ModelMapper().map(item, PedidoItemDTO.class);
    }

    public PedidoItemDTO adicionar(PedidoItemDTO dto) {
        dto.setId(null);

        Produto produto = produtoRepository.findById(dto.getProduto().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado."));

        if (produto.getQtdEstoque() >= dto.getQuantidade()) {

            produto.setQtdEstoque(produto.getQtdEstoque() - dto.getQuantidade());
            produto.setLucro((produto.getValorVenda() - produto.getValorCusto()) * dto.getQuantidade());

            dto.setPreco(produto.getValorVenda());
            dto.setProduto(produto);
            dto.setSubTotal((dto.getPreco() - dto.getDescontoUnitario()) * dto.getQuantidade());

            PedidoItem item = new ModelMapper().map(dto, PedidoItem.class);
            item = pedidoItemRepository.save(item);
            dto.setId(item.getId());

            return new ModelMapper().map(item, PedidoItemDTO.class);
        } else {
            throw new RuntimeException("Estoque insuficiente para o produto: " + produto.getNome());
        }
    }

    public PedidoItemDTO atualizar(Long id, PedidoItemDTO dto) {
        dto.setId(id);

        PedidoItem item = new ModelMapper().map(dto, PedidoItem.class);
        item = pedidoItemRepository.save(item);

        return new ModelMapper().map(item, PedidoItemDTO.class);
    }

    public boolean deletar(Long id) {
        if (pedidoItemRepository.existsById(id)) {
            pedidoItemRepository.deleteById(id);
        } else {
            return false;
        }
        return true;
    }
}

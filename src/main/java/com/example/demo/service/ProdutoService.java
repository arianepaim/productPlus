package com.example.demo.service;

import com.example.demo.model.Produto;
import com.example.demo.model.exception.ResourceNotFoundException;
import com.example.demo.repository.ProdutoRepository;
import com.example.demo.shared.ProdutoDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public List<ProdutoDTO> buscarTodos() {
        List<Produto> list = produtoRepository.findAll();
        return list
                .stream()
                .map(produto -> new ModelMapper().map(produto, ProdutoDTO.class))
                .collect(Collectors.toList());
    }

    public ProdutoDTO buscarPorId(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto com o id: " + id + " não encontrado."));
        return new ModelMapper().map(produto, ProdutoDTO.class);
    }

    public ProdutoDTO adicionar(ProdutoDTO dto) {
        dto.setId(null);

        Produto produto = new ModelMapper().map(dto, Produto.class);
        produto.setDtCadastro(new Date());
        produto = produtoRepository.save(produto);
        dto.setId(produto.getId());

        return dto;
    }

    public ProdutoDTO atualizar(Long id, ProdutoDTO dto) {
        dto.setId(id);

        Produto produto = new ModelMapper().map(dto, Produto.class);
        Produto existingProduto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado."));

        if (existingProduto != null) {
            produto.setDtCadastro(existingProduto.getDtCadastro());
        }
        produto = produtoRepository.save(produto);

        return dto;
    }

    public boolean deletar(Long id) {
        if (produtoRepository.existsById(id)) {
            produtoRepository.deleteById(id);
        } else {
            return false;
        }
        return true;
    }

}

package com.example.demo.service;

import com.example.demo.model.Categoria;
import com.example.demo.model.Produto;
import com.example.demo.model.exception.ResourceNotFoundException;
import com.example.demo.repository.CategoriaRepository;
import com.example.demo.repository.ProdutoRepository;
import com.example.demo.shared.CategoriaDTO;
import com.example.demo.shared.ProdutoDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<CategoriaDTO> buscarTodos() {
        List<Categoria> list = categoriaRepository.findAll();
        return list
                .stream()
                .map(categoria -> new ModelMapper().map(categoria, CategoriaDTO.class))
                .collect(Collectors.toList());
    }

    public CategoriaDTO buscarPorId(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto com o id: " + id + " não encontrado."));
        return new ModelMapper().map(categoria, CategoriaDTO.class);
    }

    public CategoriaDTO adicionar(CategoriaDTO dto) {
        dto.setId(null);

        Categoria categoria = new ModelMapper().map(dto, Categoria.class);
        categoria = categoriaRepository.save(categoria);
        dto.setId(categoria.getId());

        return dto;
    }

    public CategoriaDTO atualizar(Long id, CategoriaDTO dto) {
        if (categoriaRepository.existsById(id)) {
            dto.setId(id);

            Categoria categoria = new ModelMapper().map(dto, Categoria.class);
            categoria = categoriaRepository.save(categoria);

            return new ModelMapper().map(categoria, CategoriaDTO.class);
        } else {
            throw new ResourceNotFoundException("Categoria não existe.");
        }
    }


    public boolean deletar(Long id) {
        if (categoriaRepository.existsById(id)) {
            categoriaRepository.deleteById(id);
        } else {
            return false;
        }
        return true;
    }

}

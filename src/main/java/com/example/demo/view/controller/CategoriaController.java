package com.example.demo.view.controller;

import com.example.demo.model.exception.ResourceNotFoundException;
import com.example.demo.service.CategoriaService;
import com.example.demo.shared.CategoriaDTO;
import com.example.demo.view.model.categoria.CategoriaRequest;
import com.example.demo.view.model.categoria.CategoriaResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService service;

    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> buscarTodos() {
        List<CategoriaDTO> list = service.buscarTodos();
        List<CategoriaResponse> responses = list
                .stream()
                .map(categoriaDTO -> new ModelMapper().map(categoriaDTO, CategoriaResponse.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponse> buscarPorId(@PathVariable Long id) {
        CategoriaDTO dto = service.buscarPorId(id);
        CategoriaResponse response = new ModelMapper().map(dto, CategoriaResponse.class);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<CategoriaResponse> adicionar(@RequestBody CategoriaRequest request) {
        CategoriaDTO dto = new ModelMapper().map(request, CategoriaDTO.class);
        dto = service.adicionar(dto);
        CategoriaResponse response = new ModelMapper().map(dto, CategoriaResponse.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponse> atualizar(@PathVariable Long id, @RequestBody CategoriaRequest request) {
        CategoriaDTO dto = new ModelMapper().map(request, CategoriaDTO.class);
        dto = service.atualizar(id, dto);
        CategoriaResponse response = new ModelMapper().map(dto, CategoriaResponse.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletar(@PathVariable Long id) {
        boolean deletado = service.deletar(id);
        if (deletado) {
            return ResponseEntity.ok("Categoria deletada com sucesso.");
        } else {
            throw new ResourceNotFoundException("Categoria com o id: " + id + " não encontrada.");
        }
    }
}

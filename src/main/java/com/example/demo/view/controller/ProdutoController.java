package com.example.demo.view.controller;

import com.example.demo.model.exception.ResourceNotFoundException;
import com.example.demo.service.CategoriaService;
import com.example.demo.service.ProdutoService;
import com.example.demo.shared.CategoriaDTO;
import com.example.demo.shared.ProdutoDTO;
import com.example.demo.view.model.categoria.CategoriaRequest;
import com.example.demo.view.model.categoria.CategoriaResponse;
import com.example.demo.view.model.produto.ProdutoRequest;
import com.example.demo.view.model.produto.ProdutoResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService service;

    @GetMapping
    public ResponseEntity<List<ProdutoResponse>> buscarTodos() {
        List<ProdutoDTO> list = service.buscarTodos();
        List<ProdutoResponse> responses = list
                .stream()
                .map(produtoDTO -> new ModelMapper().map(produtoDTO, ProdutoResponse.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponse> buscarPorId(@PathVariable Long id) {
        ProdutoDTO dto = service.buscarPorId(id);
        ProdutoResponse response = new ModelMapper().map(dto, ProdutoResponse.class);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<ProdutoResponse> adicionar(@RequestBody ProdutoRequest request) {
        ProdutoDTO dto = new ModelMapper().map(request, ProdutoDTO.class);
        dto = service.adicionar(dto);
        ProdutoResponse response = new ModelMapper().map(dto, ProdutoResponse.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponse> atualizar(@PathVariable Long id, @RequestBody ProdutoRequest request) {
        ProdutoDTO dto = new ModelMapper().map(request, ProdutoDTO.class);
        dto = service.atualizar(id, dto);
        ProdutoResponse response = new ModelMapper().map(dto, ProdutoResponse.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletar(@PathVariable Long id) {
        boolean deletado = service.deletar(id);
        if (deletado) {
            return ResponseEntity.ok("Produto deletado com sucesso.");
        } else {
            throw new ResourceNotFoundException("Produto com o id: " + id + " não encontrado.");
        }
    }
}

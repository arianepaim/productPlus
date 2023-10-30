package com.example.demo.view.controller;

import com.example.demo.model.Produto;
import com.example.demo.model.exception.ResourceNotFoundException;
import com.example.demo.service.PedidoItemService;
import com.example.demo.shared.PedidoItemDTO;
import com.example.demo.view.model.pedidoItem.PedidoItemRequest;
import com.example.demo.view.model.pedidoItem.PedidoItemResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pedidoItens")
public class PedidoItemController {

    @Autowired
    PedidoItemService service;

    @GetMapping
    public ResponseEntity<List<PedidoItemResponse>> buscarTodos() {
        List<PedidoItemDTO> list = service.buscarTodos();
        List<PedidoItemResponse> responses = list
                .stream()
                .map(pedidoDto -> new ModelMapper().map(pedidoDto, PedidoItemResponse.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoItemResponse> buscarPorId(@PathVariable Long id) {
        PedidoItemDTO dto = service.buscarPorId(id);
        PedidoItemResponse response = new ModelMapper().map(dto, PedidoItemResponse.class);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<PedidoItemResponse> adicionar(@RequestBody PedidoItemRequest request) {
        PedidoItemDTO dto = new ModelMapper().map(request, PedidoItemDTO.class);

        if (request.getProdutoId() != null) {
            dto.getProduto().setId(request.getProdutoId());
        }

        dto = service.adicionar(dto);

        PedidoItemResponse response = new ModelMapper().map(dto, PedidoItemResponse.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoItemResponse> atualizar(@PathVariable Long id, @RequestBody PedidoItemRequest request) {
        PedidoItemDTO dto = new ModelMapper().map(request, PedidoItemDTO.class);

        dto = service.atualizar(id, dto);

        PedidoItemResponse response = new ModelMapper().map(dto, PedidoItemResponse.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletar(@PathVariable Long id) {
        boolean deletado = service.deletar(id);
        if (deletado) {
            return ResponseEntity.ok("Item deletado com sucesso.");
        } else {
            throw new ResourceNotFoundException("Item não encontrado.");
        }
    }
}

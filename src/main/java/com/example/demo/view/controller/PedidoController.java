package com.example.demo.view.controller;

import com.example.demo.model.PedidoItem;
import com.example.demo.model.exception.ResourceNotFoundException;
import com.example.demo.service.EmailService;
import com.example.demo.service.PedidoService;
import com.example.demo.shared.PedidoDTO;
import com.example.demo.view.model.pedido.PedidoRequest;
import com.example.demo.view.model.pedido.PedidoResponse;
import com.example.demo.view.model.pedidoItem.PedidoItemResponse;
import jakarta.mail.MessagingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    PedidoService service;

    @Autowired
    EmailService emailService;

    @GetMapping
    public ResponseEntity<List<PedidoResponse>> buscarTodos() {
        List<PedidoDTO> list = service.buscarTodos();
        List<PedidoResponse> responses = list
                .stream()
                .map(pedidoDto -> new ModelMapper().map(pedidoDto, PedidoResponse.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> buscarPorId(@PathVariable Long id) {
        PedidoDTO dto = service.buscarPorId(id);
        PedidoResponse response = new ModelMapper().map(dto, PedidoResponse.class);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<PedidoResponse> adicionar(@RequestBody PedidoRequest request) {
        PedidoDTO dto = new ModelMapper().map(request, PedidoDTO.class);

        if (request.getClienteId() != null) {
            dto.getCliente().setId(request.getClienteId());
        }

        if (request.getPedidoItensIds() != null) {
            List<Long> pedidoItemIds = request.getPedidoItensIds();
            List<PedidoItem> pedidoItens = pedidoItemIds.stream()
                    .map(itemId -> {
                        PedidoItem pedidoItem = new PedidoItem();
                        pedidoItem.setId(itemId);
                        return pedidoItem;
                    })
                    .collect(Collectors.toList());
            dto.setPedidoItens(pedidoItens);
        }

        dto = service.adicionar(dto);

        try {
            emailService.enviarEmailPedidoCriado(dto.getCliente(), dto.toPedido());
        } catch (MessagingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        PedidoResponse response = new ModelMapper().map(dto, PedidoResponse.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoResponse> atualizar(@PathVariable Long id, @RequestBody PedidoRequest request) {
        PedidoDTO dto = new ModelMapper().map(request, PedidoDTO.class);

        dto = service.atualizar(id, dto);

        PedidoResponse response = new ModelMapper().map(dto, PedidoResponse.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletar(@PathVariable Long id) {
        boolean deletado = service.deletar(id);
        if (deletado) {
            return ResponseEntity.ok("Pedido deletado com sucesso.");
        } else {
            throw new ResourceNotFoundException("Pedido não econtrado");
        }
    }
}

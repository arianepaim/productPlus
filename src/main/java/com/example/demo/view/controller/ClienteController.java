package com.example.demo.view.controller;

import com.example.demo.model.Endereco;
import com.example.demo.model.exception.ResourceNotFoundException;
import com.example.demo.service.ClienteService;
import com.example.demo.shared.ClienteDTO;
import com.example.demo.util.ViaCepWs;
import com.example.demo.view.model.cliente.ClienteRequest;
import com.example.demo.view.model.cliente.ClienteResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService service;

    @Autowired
    private ViaCepWs viaCep;

    @GetMapping
    public ResponseEntity<List<ClienteResponse>> buscarTodos() {
        List<ClienteDTO> list = service.buscarTodos();
        List<ClienteResponse> responses = list
                .stream()
                .map(clienteDto -> new ModelMapper().map(clienteDto, ClienteResponse.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> buscarPorId(@PathVariable Long id) {
        ClienteDTO dto = service.buscarPorId(id);
        ClienteResponse response = new ModelMapper().map(dto, ClienteResponse.class);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> adicionar(@RequestBody ClienteRequest request) {
        Endereco endereco = this.viaCep.consultarCep(request.getCep());
        endereco.setComplemento(request.getComplemento());
        endereco.setNumero(request.getNumero());

        ClienteDTO dto = new ModelMapper().map(request, ClienteDTO.class);
        dto.setEndereco(endereco);
        dto = service.adicionar(dto);

        ClienteResponse response = new ModelMapper().map(dto, ClienteResponse.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> atualizar(@PathVariable Long id, @RequestBody ClienteRequest request) {
        Endereco endereco = this.viaCep.consultarCep(request.getCep());
        endereco.setComplemento(request.getComplemento());
        endereco.setNumero(request.getNumero());

        ClienteDTO dto = new ModelMapper().map(request, ClienteDTO.class);
        dto.setEndereco(endereco);
        dto = service.atualizar(id, dto);

        ClienteResponse response = new ModelMapper().map(dto, ClienteResponse.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletar(@PathVariable Long id) {
        boolean deletado = service.deletar(id);
        if (deletado) {
            return ResponseEntity.ok("Cliente deletado com sucesso.");
        } else {
            throw new ResourceNotFoundException("Cliente não encontrado.");
        }
    }

}

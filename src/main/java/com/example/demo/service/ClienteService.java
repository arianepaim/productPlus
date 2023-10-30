package com.example.demo.service;

import com.example.demo.model.Cliente;
import com.example.demo.model.exception.ResourceNotFoundException;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.shared.ClienteDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<ClienteDTO> buscarTodos() {
        List<Cliente> list = clienteRepository.findAll();
        list.forEach(Cliente::preencherPedidoIds);
        return list
                .stream()
                .map(cliente -> new ModelMapper().map(cliente, ClienteDTO.class))
                .collect(Collectors.toList());
    }

    public ClienteDTO buscarPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente com o id: " + id + " não encontrado."));
        cliente.preencherPedidoIds();
        return new ModelMapper().map(cliente, ClienteDTO.class);
    }

    public ClienteDTO adicionar(ClienteDTO dto) {
        dto.setId(null);

        Cliente cliente = new ModelMapper().map(dto, Cliente.class);
        cliente = clienteRepository.save(cliente);
        dto.setId(cliente.getId());

        return new ModelMapper().map(cliente, ClienteDTO.class);
    }

    public ClienteDTO atualizar(Long id, ClienteDTO dto) {
        if (!clienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente com o id: " + id + " não encontrado.");
        }

        dto.setId(id);

        Cliente cliente = new ModelMapper().map(dto, Cliente.class);
        cliente = clienteRepository.save(cliente);

        return new ModelMapper().map(cliente, ClienteDTO.class);
    }

    public boolean deletar(Long id) {
        if (clienteRepository.existsById(id)) {
            clienteRepository.deleteById(id);
        } else {
            return false;
        }
        return true;
    }


}

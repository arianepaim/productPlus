package com.example.demo.view.model.cliente;

import com.example.demo.model.Endereco;

import java.util.Date;
import java.util.List;

public class ClienteResponse {

    private Long id;
    private String nome;
    private String telefone;
    private String cpf;
    private String email;
    private Date dtNascimento;
    private Endereco endereco;
    private List<Long> pedidoIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDtNascimento() {
        return dtNascimento;
    }

    public void setDtNascimento(Date dtNascimento) {
        this.dtNascimento = dtNascimento;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public List<Long> getPedidoIds() {
        return pedidoIds;
    }

    public void setPedidoIds(List<Long> pedidoIds) {
        this.pedidoIds = pedidoIds;
    }
}

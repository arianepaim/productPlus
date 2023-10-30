package com.example.demo.service;

import com.example.demo.model.Cliente;
import com.example.demo.model.MessageEmail;
import com.example.demo.model.Pedido;
import com.example.demo.model.PedidoItem;
import com.example.demo.view.model.pedido.PedidoResponse;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Collections;

import static jakarta.mail.Transport.send;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${mail.smtp.username}")
    private String senderEmail;

    public void send(MessageEmail email) throws MessagingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

        helper.setFrom(email.getSender());
        helper.setSubject(email.getTopic());
        helper.setText(email.getMessage(), true);
        helper.setTo(email.getAddressee().toArray(new String[0]));

        javaMailSender.send(mimeMessage);
    }

    public String construirMensagemEmail(Pedido pedido) {
        StringBuilder mensagem = new StringBuilder();

        mensagem.append("Olá ").append(pedido.getCliente().getNome()).append(",<br><br>");
        mensagem.append("O seu pedido foi criado com sucesso!<br><br>");
        mensagem.append("Detalhes do Pedido:<br>");
        mensagem.append("Número do Pedido: ").append(pedido.getId()).append("<br>");
        mensagem.append("Data do Pedido: ").append(pedido.getDataPedido()).append("<br>");
        mensagem.append("Desconto Total: R$ ").append(String.format("%.2f", pedido.getDescontoTotal())).append("<br>");
        mensagem.append("Total: R$ ").append(String.format("%.2f", pedido.getTotal())).append("<br><br>");

        mensagem.append("Itens do Pedido:<br>");
        for (PedidoItem item : pedido.getPedidoItens()) {
            mensagem.append("Produto: ").append(item.getProduto().getNome()).append("<br>");
            mensagem.append("Quantidade: ").append(item.getQuantidade()).append("<br>");
            mensagem.append("Preço Unitário: R$ ").append(String.format("%.2f", item.getPreco())).append("<br>");
            mensagem.append("Desconto Unitário: R$ ").append(String.format("%.2f", item.getDescontoUnitario())).append("<br>");
            mensagem.append("Subtotal: R$ ").append(String.format("%.2f", item.getSubTotal())).append("<br><br>");
        }

        mensagem.append("Obrigado por escolher os nossos produtos!<br>");
        mensagem.append("Atenciosamente,<br>Lojão");

        return mensagem.toString();
    }



    public void enviarEmailPedidoCriado(Cliente cliente, Pedido pedido) throws MessagingException {
        MessageEmail email = new MessageEmail();
        email.setSender(senderEmail);
        email.setTopic("Envio de Pedido");

        String mensagem = construirMensagemEmail(pedido);

        email.setMessage(mensagem);
        email.setAddressee(Collections.singletonList(cliente.getEmail()));

        send(email);
    }
}


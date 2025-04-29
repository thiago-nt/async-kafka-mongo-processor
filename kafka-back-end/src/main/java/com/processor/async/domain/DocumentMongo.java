package com.processor.async.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "mensagens")
@Data
public class DocumentMongo {
    @Id
    private String id;
    private String conteudo;
    private Instant dataHora;

    public DocumentMongo(String conteudo, Instant dataHora) {
        this.conteudo = conteudo;
        this.dataHora = dataHora;
    }
}

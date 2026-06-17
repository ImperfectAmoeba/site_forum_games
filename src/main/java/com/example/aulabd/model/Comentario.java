package com.example.aulabd.model;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public class Comentario {
    private String id;
    private String conteudo;
    private String postId;
    private String autorId;
    private String autorNome;
    private String comentarioPaiId;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private int curtidas;

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getConteudo() { return conteudo; }
    public void setConteudo(String conteudo) { this.conteudo = conteudo; }
    public String getPostId() { return postId; }
    public void setPostId(String postId) { this.postId = postId; }
    public String getAutorId() { return autorId; }
    public void setAutorId(String autorId) { this.autorId = autorId; }
    public String getAutorNome() { return autorNome; }
    public void setAutorNome(String autorNome) { this.autorNome = autorNome; }
    public String getComentarioPaiId() { return comentarioPaiId; }
    public void setComentarioPaiId(String comentarioPaiId) { this.comentarioPaiId = comentarioPaiId; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }
    public int getCurtidas() { return curtidas; }
    public void setCurtidas(int curtidas) { this.curtidas = curtidas; }

    public static Comentario converter(Map<String, Object> row) {
        Comentario c = new Comentario();
        c.setId(((UUID) row.get("id")).toString());
        c.setConteudo((String) row.get("conteudo"));
        c.setPostId(((UUID) row.get("post_id")).toString());
        c.setAutorId(((UUID) row.get("autor_id")).toString());
        
        if (row.containsKey("autor_nome") && row.get("autor_nome") != null) {
            c.setAutorNome((String) row.get("autor_nome"));
        }
        
        if (row.get("comentario_pai_id") != null) {
            c.setComentarioPaiId(((UUID) row.get("comentario_pai_id")).toString());
        }
        
        if (row.get("data_criacao") != null) {
            c.setDataCriacao(((java.sql.Timestamp) row.get("data_criacao")).toLocalDateTime());
        }
        
        if (row.get("data_atualizacao") != null) {
            c.setDataAtualizacao(((java.sql.Timestamp) row.get("data_atualizacao")).toLocalDateTime());
        }
        
        if (row.containsKey("curtidas") && row.get("curtidas") != null) {
            c.setCurtidas(((Number) row.get("curtidas")).intValue());
        }
        
        return c;
    }
}
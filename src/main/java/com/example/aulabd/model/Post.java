package com.example.aulabd.model;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public class Post {
    private String id;
    private String titulo;
    private String conteudo;
    private String categoriaId;
    private String categoriaNome;
    private String autorId;
    private String autorNome;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    public Post() {}

    public Post(String id, String titulo, String conteudo, String categoriaId, String autorId) {
        this.id = id;
        this.titulo = titulo;
        this.conteudo = conteudo;
        this.categoriaId = categoriaId;
        this.autorId = autorId;
    }

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getConteudo() { return conteudo; }
    public void setConteudo(String conteudo) { this.conteudo = conteudo; }
    public String getCategoriaId() { return categoriaId; }
    public void setCategoriaId(String categoriaId) { this.categoriaId = categoriaId; }
    public String getCategoriaNome() { return categoriaNome; }
    public void setCategoriaNome(String categoriaNome) { this.categoriaNome = categoriaNome; }
    public String getAutorId() { return autorId; }
    public void setAutorId(String autorId) { this.autorId = autorId; }
    public String getAutorNome() { return autorNome; }
    public void setAutorNome(String autorNome) { this.autorNome = autorNome; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }

    // Conversor a partir do ResultSet/Map
    public static Post converter(Map<String, Object> row) {
        Post post = new Post();
        post.setId(((UUID) row.get("id")).toString());
        post.setTitulo((String) row.get("titulo"));
        post.setConteudo((String) row.get("conteudo"));
        
        if (row.get("categoria_id") != null) {
            post.setCategoriaId(((UUID) row.get("categoria_id")).toString());
        }
        if (row.get("autor_id") != null) {
            post.setAutorId(((UUID) row.get("autor_id")).toString());
        }
        
        // Campos opcionais (joins)
        if (row.containsKey("categoria_nome") && row.get("categoria_nome") != null) {
            post.setCategoriaNome((String) row.get("categoria_nome"));
        }
        if (row.containsKey("autor_nome") && row.get("autor_nome") != null) {
            post.setAutorNome((String) row.get("autor_nome"));
        }
        
        if (row.get("data_criacao") != null) {
            post.setDataCriacao(((java.sql.Timestamp) row.get("data_criacao")).toLocalDateTime());
        }
        if (row.get("data_atualizacao") != null) {
            post.setDataAtualizacao(((java.sql.Timestamp) row.get("data_atualizacao")).toLocalDateTime());
        }
        
        return post;
    }
}
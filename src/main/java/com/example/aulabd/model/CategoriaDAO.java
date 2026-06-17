package com.example.aulabd.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;

@Repository
public class CategoriaDAO {

    @Autowired
    DataSource dataSource;
    
    JdbcTemplate jdbc;
    
    @PostConstruct
    private void initialize() {
        jdbc = new JdbcTemplate(dataSource);
    }
    
    public void inserirCategoria(Categoria categoria) {
        String sql = "INSERT INTO categoria (nome, descricao) VALUES (?, ?)";
        jdbc.update(sql, categoria.getNome(), categoria.getDescricao());
    }
    
    public List<Categoria> listarCategorias() {
        String sql = "SELECT * FROM categoria";
        List<Map<String, Object>> rows = jdbc.queryForList(sql);
        List<Categoria> categorias = new ArrayList<>();
        
        for (Map<String, Object> row : rows) {
            Categoria c = new Categoria();
            c.setId(((java.util.UUID) row.get("id")).toString());
            c.setNome((String) row.get("nome"));
            c.setDescricao((String) row.get("descricao"));
            categorias.add(c);
        }
        return categorias;
    }

    public Categoria buscarCategoriaPorId(String id) {
    String sql = "SELECT * FROM categoria WHERE id = ?::uuid";
    Map<String, Object> row = jdbc.queryForMap(sql, id);
    Categoria c = new Categoria();
    c.setId(((java.util.UUID) row.get("id")).toString());
    c.setNome((String) row.get("nome"));
    c.setDescricao((String) row.get("descricao"));
    return c;
}

public void atualizarCategoria(Categoria categoria) {
    String sql = "UPDATE categoria SET nome = ?, descricao = ? WHERE id = ?::uuid";
    jdbc.update(sql, categoria.getNome(), categoria.getDescricao(), categoria.getId());
}

public void deletarCategoria(String id) {
    String sql = "DELETE FROM categoria WHERE id = ?::uuid";
    jdbc.update(sql, id);
}
    
}
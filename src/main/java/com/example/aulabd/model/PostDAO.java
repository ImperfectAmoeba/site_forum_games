package com.example.aulabd.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;

@Repository
public class PostDAO {

    @Autowired
    DataSource dataSource;
    
    JdbcTemplate jdbc;
    
    @PostConstruct
    private void initialize() {
        jdbc = new JdbcTemplate(dataSource);
    }
    
    public void inserirPost(Post post) {
        String sql = "INSERT INTO post (id, titulo, conteudo, categoria_id, autor_id, data_criacao, data_atualizacao) " +
                     "VALUES (gen_random_uuid(), ?, ?, ?::uuid, ?::uuid, ?, ?)";
        LocalDateTime now = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(now);
        jdbc.update(sql, post.getTitulo(), post.getConteudo(), post.getCategoriaId(), 
                    post.getAutorId(), timestamp, timestamp);
    }
    
    public List<Post> listarPosts() {
        String sql = "SELECT p.*, c.nome as categoria_nome, u.nome as autor_nome " +
                     "FROM post p " +
                     "LEFT JOIN categoria c ON p.categoria_id = c.id " +
                     "LEFT JOIN usuario u ON p.autor_id = u.id " +
                     "ORDER BY p.data_criacao DESC";
        
        List<Map<String, Object>> rows = jdbc.queryForList(sql);
        List<Post> posts = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            posts.add(Post.converter(row));
        }
        return posts;
    }
    
    public Post buscarPostPorId(String id) {
        String sql = "SELECT p.*, c.nome as categoria_nome, u.nome as autor_nome " +
                     "FROM post p " +
                     "LEFT JOIN categoria c ON p.categoria_id = c.id " +
                     "LEFT JOIN usuario u ON p.autor_id = u.id " +
                     "WHERE p.id = ?::uuid";
        Map<String, Object> row = jdbc.queryForMap(sql, id);
        return Post.converter(row);
    }
    
    public void atualizarPost(Post post) {
        String sql = "UPDATE post SET titulo = ?, conteudo = ?, categoria_id = ?::uuid, data_atualizacao = ? WHERE id = ?::uuid";
        jdbc.update(sql, post.getTitulo(), post.getConteudo(), post.getCategoriaId(), 
                    Timestamp.valueOf(LocalDateTime.now()), post.getId());
    }
    
    public void deletarPost(String id) {
        String sql = "DELETE FROM post WHERE id = ?::uuid";
        jdbc.update(sql, id);
    }
    
    public List<Post> listarPostsPorCategoria(String categoriaId) {
        String sql = "SELECT p.*, c.nome as categoria_nome, u.nome as autor_nome " +
                     "FROM post p " +
                     "LEFT JOIN categoria c ON p.categoria_id = c.id " +
                     "LEFT JOIN usuario u ON p.autor_id = u.id " +
                     "WHERE p.categoria_id = ?::uuid " +
                     "ORDER BY p.data_criacao DESC";
        
        List<Map<String, Object>> rows = jdbc.queryForList(sql, categoriaId);
        List<Post> posts = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            posts.add(Post.converter(row));
        }
        return posts;
    }

    public List<Map<String, Object>> listarCategoriasMaisPostadas(int limite) {
    String sql = "SELECT c.id, c.nome, COUNT(p.id) as total " +
                 "FROM categoria c " +
                 "LEFT JOIN post p ON p.categoria_id = c.id " +
                 "GROUP BY c.id, c.nome " +
                 "ORDER BY total DESC " +
                 "LIMIT ?";
    return jdbc.queryForList(sql, limite);
}
}
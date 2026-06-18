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
public class ComentarioDAO {

    @Autowired
    DataSource dataSource;
    
    JdbcTemplate jdbc;
    
    @PostConstruct
    private void initialize() {
        jdbc = new JdbcTemplate(dataSource);
    }
    
    public void inserirComentario(Comentario comentario) {
        String sql = "INSERT INTO comentario (id, conteudo, post_id, autor_id, comentario_pai_id, data_criacao, data_atualizacao) " +
                     "VALUES (gen_random_uuid(), ?, ?::uuid, ?::uuid, ?::uuid, ?, ?)";
        LocalDateTime now = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(now);
        jdbc.update(sql, comentario.getConteudo(), comentario.getPostId(), 
                    comentario.getAutorId(), comentario.getComentarioPaiId(), 
                    timestamp, timestamp);
    }
    
    public List<Comentario> listarComentariosPorPost(String postId) {
    String sql = "SELECT c.*, u.nome as autor_nome, " +
                 "(SELECT COUNT(*) FROM curtida_comentario WHERE comentario_id = c.id AND (tipo IS NULL OR tipo = 'curtir')) as curtidas " +
                 "FROM comentario c " +
                 "LEFT JOIN usuario u ON c.autor_id = u.id " +
                 "WHERE c.post_id = ?::uuid " +
                 "ORDER BY curtidas DESC, c.data_criacao ASC";
    
    List<Map<String, Object>> rows = jdbc.queryForList(sql, postId);
    List<Comentario> comentarios = new ArrayList<>();
    for (Map<String, Object> row : rows) {
        Comentario comentario = Comentario.converter(row);
        comentarios.add(comentario);
    }
    return comentarios;
}
    
    public void deletarComentario(String id) {
        String sql = "DELETE FROM comentario WHERE id = ?::uuid";
        jdbc.update(sql, id);
    }
    
    public void curtirComentario(String comentarioId, String usuarioId) {
    String sql = "INSERT INTO curtida_comentario (comentario_id, usuario_id) VALUES (?::uuid, ?::uuid) " +
                 "ON CONFLICT (comentario_id, usuario_id) DO NOTHING";
    jdbc.update(sql, comentarioId, usuarioId);
}
    
    public void descurtirComentario(String comentarioId, String usuarioId) {
    String sql = "DELETE FROM curtida_comentario WHERE comentario_id = ?::uuid AND usuario_id = ?::uuid";
    jdbc.update(sql, comentarioId, usuarioId);
}
    
    public int contarCurtidas(String comentarioId) {
        String sql = "SELECT COUNT(*) FROM curtida_comentario WHERE comentario_id = ?::uuid";
        return jdbc.queryForObject(sql, Integer.class, comentarioId);
    }
    
    public Comentario buscarComentarioPorId(String id) {
    String sql = "SELECT c.*, u.nome as autor_nome FROM comentario c " +
                 "LEFT JOIN usuario u ON c.autor_id = u.id " +
                 "WHERE c.id = ?::uuid";
    Map<String, Object> row = jdbc.queryForMap(sql, id);
    return Comentario.converter(row);
}

    public void atualizarComentario(Comentario comentario) {
    String sql = "UPDATE comentario SET conteudo = ?, data_atualizacao = ? WHERE id = ?::uuid";
    jdbc.update(sql, comentario.getConteudo(), Timestamp.valueOf(LocalDateTime.now()), comentario.getId());
}
}
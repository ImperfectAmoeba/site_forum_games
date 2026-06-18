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
public class UsuarioDAO {

    @Autowired
    DataSource dataSource;
    
    JdbcTemplate jdbc;
    
    @PostConstruct
    private void initialize() {
        jdbc = new JdbcTemplate(dataSource);
    }
    
    public void inserirUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuario(nome,email,senha) VALUES (?,?,?)";
        Object[] obj = new Object[3];
        obj[0] = usuario.getNome();
        obj[1] = usuario.getEmail();
        obj[2] = usuario.getSenha();
        jdbc.update(sql, obj);
    }

    public Usuario mostrarUsuario(String uuid) {
        String sql = "SELECT * FROM usuario where id=?::uuid";
        return Usuario.converter(jdbc.queryForMap(sql, uuid));
    }

    public ArrayList<Usuario> listar_usuarios() {
    String sql = "SELECT u.*, p.cargo FROM usuario u " +
                 "LEFT JOIN perfil p ON p.usuarioid = u.id";
    return Usuario.converterTodos(jdbc.queryForList(sql));
}

    public Usuario buscarPorNomeESenha(String nome, String senha) {
        String sql = "SELECT * FROM usuario WHERE nome = ? AND senha = ?";
        try {
            java.util.Map<String, Object> resultado = jdbc.queryForMap(sql, nome, senha);
            return Usuario.converter(resultado);
        } catch (Exception e) {
            return null;
        }
    }

    public void deletar(String id) {
        String sql = "DELETE FROM usuario WHERE id = ?::uuid";
        jdbc.update(sql, id);
    }

    public void atualizarNome(String id, String novoNome) {
        String sql = "UPDATE usuario SET nome = ? WHERE id = ?::uuid";
        jdbc.update(sql, novoNome, id);
    }

    public void inserirPerfil(String usuarioid, String cargo) {
        String sql = "INSERT INTO perfil (usuarioid, cargo) VALUES (?::uuid, ?)";
        jdbc.update(sql, usuarioid, cargo);
    }

        public List<Usuario> listarModeradores() {
    String sql = "SELECT u.* FROM usuario u " +
                 "JOIN perfil p ON p.usuarioid = u.id " +
                 "WHERE p.cargo = 'mod'";
    List<Map<String, Object>> rows = jdbc.queryForList(sql);
    List<Usuario> moderadores = new ArrayList<>();
    for (Map<String, Object> row : rows) {
        moderadores.add(Usuario.converter(row));
    }
    return moderadores;
}

public void atribuirModerador(String usuarioId) {
    String checkSql = "SELECT COUNT(*) FROM perfil WHERE usuarioid = ?::uuid";
    int count = jdbc.queryForObject(checkSql, Integer.class, usuarioId);
    
    if (count > 0) {
        String sql = "UPDATE perfil SET cargo = 'mod' WHERE usuarioid = ?::uuid";
        jdbc.update(sql, usuarioId);
    } else {
        String sql = "INSERT INTO perfil (usuarioid, cargo) VALUES (?::uuid, 'mod')";
        jdbc.update(sql, usuarioId);
    }
}

public void removerModerador(String usuarioId) {
    String sql = "DELETE FROM perfil WHERE usuarioid = ?::uuid AND cargo = 'mod'";
    jdbc.update(sql, usuarioId);
}

public Usuario buscarPorNome(String nome) {
    String sql = "SELECT * FROM usuario WHERE nome = ?";
    try {
        Map<String, Object> resultado = jdbc.queryForMap(sql, nome);
        return Usuario.converter(resultado);
    } catch (Exception e) {
        return null;
    }
}

public void atualizarSenha(String id, String novaSenha) {
    String sql = "UPDATE usuario SET senha = ? WHERE id = ?::uuid";
    jdbc.update(sql, novaSenha, id);
}

public void promoverModerador(String usuarioId) {
    // Primeiro verifica se o registro existe
    String checkSql = "SELECT COUNT(*) FROM perfil WHERE usuarioid = ?::uuid";
    int count = jdbc.queryForObject(checkSql, Integer.class, usuarioId);
    
    if (count > 0) {
        // Se existe, atualiza
        String sql = "UPDATE perfil SET cargo = 'mod' WHERE usuarioid = ?::uuid";
        jdbc.update(sql, usuarioId);
    } else {
        // Se não existe, insere
        String sql = "INSERT INTO perfil (usuarioid, cargo) VALUES (?::uuid, 'mod')";
        jdbc.update(sql, usuarioId);
    }
}

public void rebaixarModerador(String usuarioId) {
    String sql = "DELETE FROM perfil WHERE usuarioid = ?::uuid AND cargo = 'mod'";
    jdbc.update(sql, usuarioId);
}

}
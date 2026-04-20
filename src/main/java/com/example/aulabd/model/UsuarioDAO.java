package com.example.aulabd.model;

import java.util.ArrayList;

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
		String sql = "INSERT INTO usuario(nome,email,senha)" +
	                 "VALUES (?,?,?)";
		Object[] obj = new Object[3];
		//primeiro ?
		obj[0] = usuario.getNome();
		//segundo ?
		obj[1] = usuario.getEmail();
		//terceiro ?
		obj[2] = usuario.getSenha();
		jdbc.update(sql, obj);
	}

	public Usuario mostrarUsuario(String uuid){
		String sql = "SELECT * FROM usuario where id=?::uuid";
		return Usuario.converter(jdbc.queryForMap(sql,uuid));
	}

	public ArrayList<Usuario> listar_usuarios(){
		String sql = "SELECT * FROM usuario";
		return Usuario.converterTodos(jdbc.queryForList(sql));
	}

	public Usuario buscarPorNomeESenha(String nome, String senha) {
    String sql = "SELECT * FROM usuario WHERE nome = ? AND senha = ?";
    
    try {
        // Retorna um Map com os dados do usuário
        java.util.Map<String, Object> resultado = jdbc.queryForMap(sql, nome, senha);
        return Usuario.converter(resultado);
    } catch (Exception e) {
        // Se não encontrar, retorna null
        return null;
    }
}

public void deletar(String id) {
    String sql = "DELETE FROM usuario WHERE id = ?::uuid";
    jdbc.update(sql, id);
}
}

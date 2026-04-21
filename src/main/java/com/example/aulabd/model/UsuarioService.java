package com.example.aulabd.model;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    UsuarioDAO usuarioDAO;

    public void inserirUsuario(Usuario usuario){
        usuarioDAO.inserirUsuario(usuario);
    }

    public Usuario mostrarUsuario(String uuid){
        return usuarioDAO.mostrarUsuario(uuid);
    }

    public ArrayList<Usuario> listar_usuarios(){
        return usuarioDAO.listar_usuarios();
    }

    public Usuario buscarPorNomeESenha(String nome, String senha) {
    return usuarioDAO.buscarPorNomeESenha(nome, senha);
}

public void deletarUsuario(String id) {
    usuarioDAO.deletar(id);
}

public void atualizarNome(String id, String novoNome) {
    usuarioDAO.atualizarNome(id, novoNome);
}
}

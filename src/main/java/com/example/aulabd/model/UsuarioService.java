package com.example.aulabd.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    UsuarioDAO usuarioDAO;

    public void inserirUsuario(Usuario usuario) {
        usuarioDAO.inserirUsuario(usuario);
    }

    public Usuario mostrarUsuario(String uuid) {
        return usuarioDAO.mostrarUsuario(uuid);
    }

    public ArrayList<Usuario> listar_usuarios() {
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

    public void inserirPerfil(String usuarioid, String cargo) {
        usuarioDAO.inserirPerfil(usuarioid, cargo);
    }

    public List<Usuario> listarModeradores() {
        return usuarioDAO.listarModeradores();
    }

    public void atribuirModerador(String usuarioId) {
        usuarioDAO.atribuirModerador(usuarioId);
    }

    public void removerModerador(String usuarioId) {
        usuarioDAO.removerModerador(usuarioId);
    }

    public Usuario buscarPorNome(String nome) {
    return usuarioDAO.buscarPorNome(nome);
    }

    public void atualizarSenha(String id, String novaSenha) {
        usuarioDAO.atualizarSenha(id, novaSenha);
    }

    public void promoverModerador(String usuarioId) {
    usuarioDAO.promoverModerador(usuarioId);
}

public void rebaixarModerador(String usuarioId) {
    usuarioDAO.rebaixarModerador(usuarioId);
}
}
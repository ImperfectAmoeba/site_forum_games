package com.example.aulabd.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComentarioService {

    @Autowired
    private ComentarioDAO comentarioDAO;

    public void criarComentario(Comentario comentario) {
        comentarioDAO.inserirComentario(comentario);
    }

    public List<Comentario> listarComentariosPorPost(String postId) {
        return comentarioDAO.listarComentariosPorPost(postId);
    }

    public void deletarComentario(String id) {
        comentarioDAO.deletarComentario(id);
    }

    public void curtirComentario(String comentarioId, String usuarioId) {
        comentarioDAO.curtirComentario(comentarioId, usuarioId);
    }

    public void descurtirComentario(String comentarioId, String usuarioId) {
        comentarioDAO.descurtirComentario(comentarioId, usuarioId);
    }

    public Comentario buscarComentarioPorId(String id) {
        return comentarioDAO.buscarComentarioPorId(id);
    }

    public void atualizarComentario(Comentario comentario) {
        comentarioDAO.atualizarComentario(comentario);
    }
}
package com.example.aulabd.model;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    @Autowired
    private PostDAO postDAO;

    public void criarPost(Post post) {
        postDAO.inserirPost(post);
    }

    public List<Post> listarPosts() {
        return postDAO.listarPosts();
    }

    public Post buscarPostPorId(String id) {
        return postDAO.buscarPostPorId(id);
    }

    public void atualizarPost(Post post) {
        postDAO.atualizarPost(post);
    }

    public void deletarPost(String id) {
        postDAO.deletarPost(id);
    }

    public List<Post> listarPostsPorCategoria(String categoriaId) {
        return postDAO.listarPostsPorCategoria(categoriaId);
    }

    public List<Map<String, Object>> listarCategoriasMaisPostadas(int limite) {
    return postDAO.listarCategoriasMaisPostadas(limite);
}
}
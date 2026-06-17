package com.example.aulabd.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaDAO categoriaDAO;

    public void inserirCategoria(Categoria categoria) {
        categoriaDAO.inserirCategoria(categoria);
    }

    public List<Categoria> listarCategorias() {
        return categoriaDAO.listarCategorias();
    }

    public Categoria buscarCategoriaPorId(String id) {
    return categoriaDAO.buscarCategoriaPorId(id);
}

public void atualizarCategoria(Categoria categoria) {
    categoriaDAO.atualizarCategoria(categoria);
}

public void deletarCategoria(String id) {
    categoriaDAO.deletarCategoria(id);
}
}
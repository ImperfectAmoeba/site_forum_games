package com.example.aulabd.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.aulabd.model.Categoria;
import com.example.aulabd.model.CategoriaService;
import com.example.aulabd.model.Usuario;
import com.example.aulabd.model.UsuarioService;

@Controller
public class CategoriaController {

    @Autowired
    private ApplicationContext context;

    @GetMapping("/categoria/cadastrar")
    public String formCategoria(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "formcategoria";
    }
    
    @PostMapping("/categoria/cadastrar")
    public String postCategoria(@ModelAttribute Categoria categoria) {
        CategoriaService cs = context.getBean(CategoriaService.class);
        cs.inserirCategoria(categoria);
        return "redirect:/categoria/listar";
    }

    @GetMapping("/categoria/listar")
public String listarCategorias(Model model) {
    CategoriaService cs = context.getBean(CategoriaService.class);
    List<Categoria> categorias = cs.listarCategorias();
    
    // Garante que nunca seja null./mvnw spring-boot:run "-Dspring-boot.run.profiles=local"
    if (categorias == null) {
        categorias = new ArrayList<>();
    }
    
    model.addAttribute("categorias", categorias);
    return "listarcategorias";
}

@GetMapping("/categoria/{id}/editar")
public String formEditarCategoria(@PathVariable("id") String id, Model model) {
    CategoriaService cs = context.getBean(CategoriaService.class);
    Categoria categoria = cs.buscarCategoriaPorId(id);
    model.addAttribute("categoria", categoria);
    return "form-editar-categoria";
}

@PostMapping("/categoria/{id}/editar")
public String editarCategoria(@PathVariable("id") String id,
                              @ModelAttribute Categoria categoria) {
    categoria.setId(id);
    CategoriaService cs = context.getBean(CategoriaService.class);
    cs.atualizarCategoria(categoria);
    return "redirect:/categoria/listar";
}

@PostMapping("/categoria/{id}/deletar")
public String deletarCategoria(@PathVariable("id") String id) {
    CategoriaService cs = context.getBean(CategoriaService.class);
    cs.deletarCategoria(id);
    return "redirect:/categoria/listar";
}



}
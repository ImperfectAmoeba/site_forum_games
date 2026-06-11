package com.example.aulabd.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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

}
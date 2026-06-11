package com.example.aulabd.controller;

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
public class ModeradorController {

    @Autowired
    private ApplicationContext context;

    // Listar usuários com cargo 'mod'
    @GetMapping("/moderador/listar")
    public String listarModeradores(Model model) {
        UsuarioService usuarioService = context.getBean(UsuarioService.class);
        List<Usuario> moderadores = usuarioService.listarModeradores();
        model.addAttribute("moderadores", moderadores);
        return "listarmoderador";
    }

    // Formulário para tornar um usuário moderador
    @GetMapping("/moderador/cadastrar")
    public String formModerador(Model model) {
        UsuarioService usuarioService = context.getBean(UsuarioService.class);
        List<Usuario> usuarios = usuarioService.listar_usuarios();
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("moderador", new Usuario());
        return "formmoderador";
    }

    @PostMapping("/moderador/cadastrar")
    public String postModerador(@RequestParam("usuarioId") String usuarioId) {
        UsuarioService usuarioService = context.getBean(UsuarioService.class);
        usuarioService.atribuirModerador(usuarioId);
        return "redirect:/moderador/listar";
    }

    // Remover cargo de moderador
    @PostMapping("/moderador/{id}/deletar")
    public String deletarModerador(@PathVariable("id") String id) {
        UsuarioService usuarioService = context.getBean(UsuarioService.class);
        usuarioService.removerModerador(id);
        return "redirect:/moderador/listar";
    }

    // Atribuir moderador a categoria (igual ao professor)
    @GetMapping("/moderador/atribuir")
    public String atribuirModeradorForm(Model model) {
        UsuarioService usuarioService = context.getBean(UsuarioService.class);
        CategoriaService categoriaService = context.getBean(CategoriaService.class);
        
        List<Usuario> moderadores = usuarioService.listarModeradores();
        List<Categoria> categorias = categoriaService.listarCategorias();
        
        model.addAttribute("moderadores", moderadores);
        model.addAttribute("categorias", categorias);
        return "atribuirmoderador";
    }
}
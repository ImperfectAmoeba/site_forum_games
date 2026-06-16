package com.example.aulabd.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.aulabd.model.Categoria;
import com.example.aulabd.model.CategoriaService;
import com.example.aulabd.model.Post;
import com.example.aulabd.model.PostService;
import com.example.aulabd.model.Usuario;
import com.example.aulabd.model.UsuarioService;

@Controller
@RequestMapping("/post")
public class PostController {

    @Autowired
    private ApplicationContext context;

    @GetMapping("/listar")
    public String listarPosts(Model model) {
        PostService ps = context.getBean(PostService.class);
        List<Post> posts = ps.listarPosts();
        model.addAttribute("posts", posts);
        return "listar-posts";
    }

    @GetMapping("/criar")
    public String formPost(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String nomeLogado = auth.getName();
        
        if ("anonymousUser".equals(nomeLogado)) {
            return "redirect:/login";
        }
        
        CategoriaService cs = context.getBean(CategoriaService.class);
        List<Categoria> categorias = cs.listarCategorias();
        
        model.addAttribute("post", new Post());
        model.addAttribute("categorias", categorias);
        return "form-post";
    }

    @PostMapping("/criar")
    public String criarPost(@ModelAttribute Post post, @RequestParam("categoriaId") String categoriaId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String nomeLogado = auth.getName();
        
        UsuarioService us = context.getBean(UsuarioService.class);
        Usuario autor = us.buscarPorNome(nomeLogado);
        
        post.setAutorId(autor.getId());
        post.setCategoriaId(categoriaId);
        
        PostService ps = context.getBean(PostService.class);
        ps.criarPost(post);
        
        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String verPost(@PathVariable("id") String id, Model model) {
        PostService ps = context.getBean(PostService.class);
        Post post = ps.buscarPostPorId(id);
        model.addAttribute("post", post);
        return "detalhes-post";
    }

    @GetMapping("/{id}/editar")
    public String formEditarPost(@PathVariable("id") String id, Model model) {
        PostService ps = context.getBean(PostService.class);
        CategoriaService cs = context.getBean(CategoriaService.class);
        
        Post post = ps.buscarPostPorId(id);
        List<Categoria> categorias = cs.listarCategorias();
        
        model.addAttribute("post", post);
        model.addAttribute("categorias", categorias);
        return "form-editar-post";
    }

    @PostMapping("/{id}/editar")
    public String editarPost(@PathVariable("id") String id, 
                             @ModelAttribute Post post,
                             @RequestParam("categoriaId") String categoriaId) {
        post.setId(id);
        post.setCategoriaId(categoriaId);
        
        PostService ps = context.getBean(PostService.class);
        ps.atualizarPost(post);
        
        return "redirect:/post/" + id;
    }

    @PostMapping("/{id}/deletar")
    public String deletarPost(@PathVariable("id") String id) {
        PostService ps = context.getBean(PostService.class);
        ps.deletarPost(id);
        return "redirect:/";
    }
}
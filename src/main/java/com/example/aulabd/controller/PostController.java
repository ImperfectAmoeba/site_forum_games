package com.example.aulabd.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.example.aulabd.model.Comentario;
import com.example.aulabd.model.ComentarioService;
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
    CategoriaService cs = context.getBean(CategoriaService.class);
    ComentarioService coms = context.getBean(ComentarioService.class);
    
    Post post = ps.buscarPostPorId(id);
    List<Categoria> categorias = cs.listarCategorias();
    List<Comentario> comentarios = coms.listarComentariosPorPost(id);
    
    // Organizar comentários por pai (para respostas aninhadas)
    Map<String, List<Comentario>> respostasPorComentario = new HashMap<>();
    List<Comentario> comentariosPrincipais = new ArrayList<>();
    
    for (Comentario c : comentarios) {
        if (c.getComentarioPaiId() == null) {
            comentariosPrincipais.add(c);
        } else {
            respostasPorComentario.computeIfAbsent(c.getComentarioPaiId(), k -> new ArrayList<>()).add(c);
        }
    }
    
    model.addAttribute("post", post);
    model.addAttribute("categorias", categorias);
    model.addAttribute("comentarios", comentariosPrincipais);
    model.addAttribute("comentariosRespostas", respostasPorComentario);
    
    return "detalhes-duvida";
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

    @PostMapping("/{id}/mudar-categoria")
public String mudarCategoria(@PathVariable("id") String id,
                             @RequestParam("categoriaId") String categoriaId) {
    PostService ps = context.getBean(PostService.class);
    Post post = ps.buscarPostPorId(id);
    post.setCategoriaId(categoriaId);
    ps.atualizarPost(post);
    return "redirect:/post/" + id;
}
}
package com.example.aulabd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.aulabd.model.Comentario;
import com.example.aulabd.model.ComentarioService;
import com.example.aulabd.model.Usuario;
import com.example.aulabd.model.UsuarioService;

@Controller
public class ComentarioController {

    @Autowired
    private ApplicationContext context;

    @PostMapping("/comentario/criar")
    public String criarComentario(@RequestParam String postId,
                                   @RequestParam String conteudo,
                                   @RequestParam(required = false) String comentarioPaiId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String nomeLogado = auth.getName();
        
        UsuarioService us = context.getBean(UsuarioService.class);
        Usuario autor = us.buscarPorNome(nomeLogado);
        
        Comentario comentario = new Comentario();
        comentario.setPostId(postId);
        comentario.setConteudo(conteudo);
        comentario.setAutorId(autor.getId());
        
        if (comentarioPaiId != null && !comentarioPaiId.isEmpty()) {
            comentario.setComentarioPaiId(comentarioPaiId);
        }
        
        ComentarioService cs = context.getBean(ComentarioService.class);
        cs.criarComentario(comentario);
        
        return "redirect:/post/" + postId;
    }

    @PostMapping("/comentario/{id}/deletar")
    public String deletarComentario(@PathVariable("id") String id,
                                     @RequestParam String postId) {
        ComentarioService cs = context.getBean(ComentarioService.class);
        cs.deletarComentario(id);
        return "redirect:/post/" + postId;
    }

    @PostMapping("/comentario/{id}/curtir")
    public String curtirComentario(@PathVariable("id") String id,
                                    @RequestParam String postId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String nomeLogado = auth.getName();
        
        UsuarioService us = context.getBean(UsuarioService.class);
        Usuario usuario = us.buscarPorNome(nomeLogado);
        
        ComentarioService cs = context.getBean(ComentarioService.class);
        cs.curtirComentario(id, usuario.getId());
        
        return "redirect:/post/" + postId;
    }

    @PostMapping("/comentario/{id}/descurtir")
    public String descurtirComentario(@PathVariable("id") String id,
                                       @RequestParam String postId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String nomeLogado = auth.getName();
        
        UsuarioService us = context.getBean(UsuarioService.class);
        Usuario usuario = us.buscarPorNome(nomeLogado);
        
        ComentarioService cs = context.getBean(ComentarioService.class);
        cs.descurtirComentario(id, usuario.getId());
        
        return "redirect:/post/" + postId;
    }
}
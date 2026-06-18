    package com.example.aulabd.controller;

    import java.util.ArrayList;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.context.ApplicationContext;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.ModelAttribute;
    import org.springframework.web.bind.annotation.PathVariable;
    import org.springframework.web.bind.annotation.PostMapping;
    import org.springframework.web.bind.annotation.RequestParam;

    import com.example.aulabd.model.PostService;
    import com.example.aulabd.model.Post;
    import java.util.List;
import java.util.Map;

import com.example.aulabd.model.Usuario;
    import com.example.aulabd.model.UsuarioService;
    import com.example.aulabd.model.Post;
    import com.example.aulabd.model.CategoriaService;
    import java.util.List;
    import com.example.aulabd.model.Categoria;
    import java.util.List;

    import jakarta.servlet.http.HttpServletRequest;

    @Controller
    public class PaginaController {

        @Autowired
        private ApplicationContext context;

        @GetMapping("/")
public String index(Model model) {
    PostService ps = context.getBean(PostService.class);
    
    // Lista os posts
    List<Post> posts = ps.listarPosts();
    model.addAttribute("posts", posts);
    
    // Lista as categorias mais postadas (top 10)
    List<Map<String, Object>> topCategorias = ps.listarCategoriasMaisPostadas(10);
    model.addAttribute("topCategorias", topCategorias);
    
    return "index";
}

        @GetMapping("/nova-duvida")
    public String nova_duvida(Model model) {
        CategoriaService cs = context.getBean(CategoriaService.class);
        List<Categoria> categorias = cs.listarCategorias();
        model.addAttribute("post", new Post());
        model.addAttribute("categorias", categorias);
        return "nova-duvida";
    }

        @GetMapping("/detalhes-duvida")
        public String detalhes_duvida(){
            return "detalhes-duvida";
        }
        
        @GetMapping("/perfil/{uuid}")
        public String verPerfil(@PathVariable String uuid, Model model){
            UsuarioService cs = context.getBean(UsuarioService.class);
            Usuario aluno = cs.mostrarUsuario(uuid);
            model.addAttribute("nomeUsuario",aluno.getNome());
            model.addAttribute("idUsuario",aluno.getId());
            model.addAttribute("emailUsuario",aluno.getEmail());
            model.addAttribute("senhaUsuario",aluno.getSenha());
            return "paginaaluno";
        }

        @GetMapping("/listagem")
        public String listar_usuarios(Model model){
            UsuarioService cs = context.getBean(UsuarioService.class);
            ArrayList<Usuario> usuarios = (ArrayList<Usuario>) cs.listar_usuarios();
            model.addAttribute("usuarios", usuarios);
            return "listagem";
        }
        
        @GetMapping("/usuario")
        public String formRegistro(Model model) {
            model.addAttribute("usuario", new Usuario());
            return "formusuario";
        }

        @PostMapping("/usuario")
        public String postCliente(@ModelAttribute Usuario usuario, Model model) {
            UsuarioService cs = context.getBean(UsuarioService.class);
            
            // Criptografa a senha
            PasswordEncoder encoder = context.getBean(PasswordEncoder.class);
            String senhaCriptografada = encoder.encode(usuario.getSenha());
            usuario.setSenha(senhaCriptografada);
            
            // Insere o usuário
            cs.inserirUsuario(usuario);
            
            // Busca o ID do usuário recém-criado pelo nome (não pela senha)
            Usuario novoUsuario = cs.buscarPorNome(usuario.getNome());
            
            if (novoUsuario != null) {
                cs.inserirPerfil(novoUsuario.getId(), "user");
            }
            
            return "sucesso";
        }

        @GetMapping("/excluir")
    public String formExclusao(Model model) {
        // Pega o usuário logado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String nomeLogado = auth.getName();
        
        UsuarioService cs = context.getBean(UsuarioService.class);
        Usuario usuario = cs.buscarPorNome(nomeLogado);
        
        model.addAttribute("usuario", usuario);
        return "excluir";
    }

    @PostMapping("/excluir")
    public String processarExclusao(@RequestParam String senha, Model model, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String nomeLogado = auth.getName();
        
        UsuarioService cs = context.getBean(UsuarioService.class);
        Usuario usuario = cs.buscarPorNome(nomeLogado);
        
        if (usuario == null) {
            return "redirect:/login";
        }
        
        PasswordEncoder encoder = context.getBean(PasswordEncoder.class);
        
        if (encoder.matches(senha, usuario.getSenha())) {
            cs.deletarUsuario(usuario.getId());
            
            // INVALIDA A SESSÃO COMPLETAMENTE
            SecurityContextHolder.clearContext();
            request.getSession().invalidate();
            
            return "redirect:/login?excluido=sucesso";
        } else {
            model.addAttribute("erro", "Senha incorreta!");
            model.addAttribute("usuario", usuario);
            return "excluir";
        }
    }

        @GetMapping("/editar")
    public String formEdicao(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String nomeLogado = auth.getName();
        
        UsuarioService cs = context.getBean(UsuarioService.class);
        Usuario usuario = cs.buscarPorNome(nomeLogado);
        
        if (usuario == null) {
            // Se não encontrar, redireciona para login
            return "redirect:/login";
        }
        
        model.addAttribute("usuario", usuario);
        return "editar";
    }

        @PostMapping("/editar")
    public String processarEdicao(@RequestParam(required = false) String novoNome,
                                @RequestParam(required = false) String novaSenha,
                                @RequestParam(required = false) String confirmarSenha,
                                Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String nomeLogado = auth.getName();
        
        UsuarioService cs = context.getBean(UsuarioService.class);
        Usuario usuario = cs.buscarPorNome(nomeLogado);
        
        if (usuario == null) {
            model.addAttribute("erro", "Usuário não encontrado!");
            model.addAttribute("usuario", new Usuario());
            return "editar";
        }
        
        boolean nomeAlterado = false;
        
        // Atualiza nome se foi enviado
        if (novoNome != null && !novoNome.trim().isEmpty() && !novoNome.equals(usuario.getNome())) {
            cs.atualizarNome(usuario.getId(), novoNome);
            nomeAlterado = true;
        }
        
        // Atualiza senha se foi enviada e as senhas coincidem
        if (novaSenha != null && !novaSenha.trim().isEmpty()) {
            if (novaSenha.equals(confirmarSenha)) {
                PasswordEncoder encoder = context.getBean(PasswordEncoder.class);
                String senhaCriptografada = encoder.encode(novaSenha);
                cs.atualizarSenha(usuario.getId(), senhaCriptografada);
            } else {
                model.addAttribute("erro", "As senhas não coincidem!");
                model.addAttribute("usuario", usuario);
                return "editar";
            }
        }
        
        // SE O NOME FOI ALTERADO, ATUALIZA A SESSÃO
        if (nomeAlterado) {
            Usuario usuarioAtualizado = cs.buscarPorNome(novoNome);
            
            UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(
                usuarioAtualizado.getNome(),
                auth.getCredentials(),
                auth.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(newAuth);
        }
        
        return "redirect:/?editado=sucesso";
    }

@PostMapping("/admin/usuario/{id}/promover")
public String promoverModerador(@PathVariable("id") String id) {
    UsuarioService cs = context.getBean(UsuarioService.class);
    cs.promoverModerador(id);
    return "redirect:/listagem";
}

@PostMapping("/admin/usuario/{id}/rebaixar")
public String rebaixarModerador(@PathVariable("id") String id) {
    UsuarioService cs = context.getBean(UsuarioService.class);
    cs.rebaixarModerador(id);
    return "redirect:/listagem";
}

@PostMapping("/admin/usuario/{id}/deletar")
public String deletarUsuarioAdmin(@PathVariable("id") String id) {
    UsuarioService cs = context.getBean(UsuarioService.class);
    cs.deletarUsuario(id);
    return "redirect:/listagem";
}
    }

package com.example.aulabd.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.aulabd.model.Usuario;
import com.example.aulabd.model.UsuarioService;





@Controller
public class PaginaController {

    @Autowired
    private ApplicationContext context;

    @GetMapping("/")
    public String index(){
        return "index";
    }

	@GetMapping("/nova-duvida")
	public String nova_duvida(){
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
		cs.inserirUsuario(usuario);
		return "sucesso";
	}

	@GetMapping("/excluir")
public String formExclusao(Model model) {
    model.addAttribute("usuario", new Usuario());
    return "excluir";
}

	@PostMapping("/excluir")
public String processarExclusao(@RequestParam String nome, 
                                 @RequestParam String senha,
                                 Model model) {
    UsuarioService cs = context.getBean(UsuarioService.class);
    Usuario usuario = cs.buscarPorNomeESenha(nome, senha);
    
    if (usuario != null) {
        cs.deletarUsuario(usuario.getId());
        return "redirect:/listagem?excluido=sucesso";
    } else {
        model.addAttribute("erro", "Nome ou senha incorretos!");
        return "excluir";
    }
}

@GetMapping("/editar")
public String formEdicao(Model model) {
    model.addAttribute("usuario", new Usuario());
    return "editar";
}

@PostMapping("/editar")
public String processarEdicao(@RequestParam String nome,
                               @RequestParam String novaSenha,
                               @RequestParam String confirmarSenha,
                               Model model) {
    UsuarioService cs = context.getBean(UsuarioService.class);
    
    Usuario usuario = cs.buscarPorNomeESenha(nome, novaSenha);
    
    if (usuario == null) {
        model.addAttribute("erro", "Nome ou senha incorretos!");
        return "editar";  // Fica na mesma página (editar.html)
    }
    
    if (!novaSenha.equals(confirmarSenha)) {
        model.addAttribute("erro", "As senhas não coincidem!");
        return "editar";  // Fica na mesma página (editar.html)
    }
    
    // VERIFICAÇÃO OK! Vai para página de novo nome
    model.addAttribute("usuario", usuario);
    model.addAttribute("nomeAtual", usuario.getNome());
    return "novo-nome";
}

@PostMapping("/novo-nome")
	public String salvarEdicaoNome(@RequestParam String id,
									@RequestParam String novoNome,
									Model model) {
		UsuarioService cs = context.getBean(UsuarioService.class);
		cs.atualizarNome(id, novoNome);
		return "redirect:/listagem?editado=sucesso";
	}
}
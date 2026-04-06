
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
	

    // @GetMapping("/aluno")
	// public String formAluno(Model model) {
	// 	model.addAttribute("aluno",new Aluno());
	// 	return "formaluno";
	// }
	
	@PostMapping("/usuario")
	public String postCliente(@ModelAttribute Usuario usuario, Model model) {
		UsuarioService cs = context.getBean(UsuarioService.class);
		cs.inserirUsuario(usuario);
		return "sucesso";
	}
	
	// @PostMapping("/aluno")
	// public String postCliente(@ModelAttribute Aluno aluno,
	// 						  Model model) {
	// 	//AlunoService eh feito via autowired
	// 	AlunoService cs = context.getBean(AlunoService.class);
	// 	cs.inserirAluno(aluno);
	// 	return "sucesso";
	// }

}

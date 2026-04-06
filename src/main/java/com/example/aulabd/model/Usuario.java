
package com.example.aulabd.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Usuario {

    private String id, nome, email, senha;

    public Usuario(){
        
    }

    public Usuario(String id, String nome, String email, String senha) {
        this.id = id;
        this.nome = nome;
        this.senha = senha;
    }

    public Usuario(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha(){
        return senha;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenha(String senha){
        this.senha = senha;
    }

    public static Usuario converter(Map<String,Object> registro){
    String nome = (String) registro.get("nome");
    
    // CORREÇÃO AQUI: O id vem como UUID do banco
    Object idObj = registro.get("id");
    String id;
    
    if (idObj instanceof UUID) {
        id = ((UUID) idObj).toString();  // Converte UUID para String
    } else {
        id = (String) idObj;  // Se já for String, usa direto
    }
    
    String email = (String) registro.get("email");
    String senha = (String) registro.get("senha");
    
    return new Usuario(id, nome, email, senha);
}

    public static ArrayList<Usuario> converterTodos(List<Map<String,Object>> registros){
        ArrayList<Usuario> aux = new ArrayList<>();
        for(Map<String,Object> registro : registros){
            aux.add(converter(registro));
        }
        return aux;
    }

}

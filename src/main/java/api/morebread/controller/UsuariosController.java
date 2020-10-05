package api.morebread.controller;

import api.morebread.model.Usuario;

public class UsuariosController {
  public static Usuario cadastraUsuario(String nome, String email, String cargo, String senha) {
    Usuario test = new Usuario();
    
    test.setNome(nome);
    test.setEmail(email);
    test.setCargo(cargo);
    test.setSenha(senha);

    return test;
  }
}

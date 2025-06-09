package com.udea.petstore.Usuario;


import com.udea.petstore.Rol.Rol;
import com.udea.petstore.Rol.RolRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import com.udea.petstore.Utilities.PasswordEncryptor;

import java.util.List;

@Controller
public class UsuarioResolver {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncryptor passwordEncryptor;
    private final RolRepository rolRepository;

    public UsuarioResolver(UsuarioRepository usuarioRepository, PasswordEncryptor passwordEncryptor, RolRepository rolRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncryptor = passwordEncryptor;
        this.rolRepository = rolRepository;
    }

    @QueryMapping
    public List<Usuario> usuarios() {
        return usuarioRepository.findAll();
    }

    @QueryMapping
    public Usuario usuario(@Argument Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public record UsuarioInput(String contrasenia, String nombreusuario,Integer rol) {}

    @MutationMapping(name = "insertarUsuario")
    public Usuario insertarUsuario(@Argument UsuarioInput usuarioInput) {
        Rol rol = rolRepository.findById(usuarioInput.rol()).orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        Usuario usuario = new Usuario();
        usuario.setRol(rol);
        usuario.setContrasenia(passwordEncryptor.encrypt(usuarioInput.contrasenia()));
        usuario.setNombreusuario(usuarioInput.nombreusuario());
        return usuarioRepository.save(usuario);
    }

    @MutationMapping
    public Boolean deleteUsuario(@Argument Long id) {
        usuarioRepository.deleteById(id);
        return true;
    }

    @MutationMapping
    public Usuario updateUsuario(@Argument Long id, @Argument UsuarioInput usuarioInput) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("usuario no encontrado"));
        Rol rol = rolRepository.findById(usuarioInput.rol()).orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        usuario.setRol(rol);
        usuario.setContrasenia(passwordEncryptor.encrypt(usuarioInput.contrasenia()));
        usuario.setNombreusuario(usuarioInput.nombreusuario());
        return usuarioRepository.save(usuario);
    }

    public boolean validarLogin(String nombreusuario, String contrasenia) {
        Usuario usuario = usuarioRepository.findByNombreusuario(nombreusuario).orElse(null);
        if (usuario == null) return false;
        try{
            return passwordEncryptor.matches(contrasenia, usuario.getContrasenia());
        }catch (Exception e) {
            return false;}
    }

    @MutationMapping(name = "loginUsuario")
    public Boolean login(@Argument String nombreusuario, @Argument String contrasenia) {
        return validarLogin(nombreusuario, contrasenia);
    }


    
}

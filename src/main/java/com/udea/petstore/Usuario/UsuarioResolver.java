package com.udea.petstore.Usuario;

import com.udea.petstore.Compra.Compra;
import com.udea.petstore.Compra.CompraResolver;
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

    public UsuarioResolver(UsuarioRepository usuarioRepository, PasswordEncryptor passwordEncryptor) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncryptor = passwordEncryptor;
    }

    @QueryMapping
    public List<Usuario> usuarios() {
        return usuarioRepository.findAll();
    }

    @QueryMapping
    public Usuario usuario(@Argument Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Compra no encontrada"));
    }

    public record UsuarioInput(String contrasenia, String nombreusuario) {}

    @MutationMapping(name = "insertarUsuario")
    public Usuario insertarUsuario(@Argument UsuarioInput usuarioInput) {
        Usuario usuario = new Usuario();
        // Encriptar la contraseña antes de guardar
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
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Venta no encontrada"));
        // Encriptar la contraseña antes de guardar
                usuario.setContrasenia(passwordEncryptor.encrypt(usuarioInput.contrasenia()));
        usuario.setNombreusuario(usuarioInput.nombreusuario());
        return usuarioRepository.save(usuario);
    }

    public boolean validarLogin(String nombreusuario, String contrasenia) {
        Usuario usuario = usuarioRepository.findByNombreusuario(nombreusuario).orElse(null);
        if (usuario == null) return false;
        return passwordEncryptor.matches(contrasenia, usuario.getContrasenia());
    }

    @MutationMapping(name = "loginUsuario")
    public Boolean login(@Argument String nombreusuario, @Argument String contrasenia) {
        return validarLogin(nombreusuario, contrasenia);
    }
    
}

package com.udea.petstore.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.graphql.data.GraphQlRepository;
import java.util.Optional;

@GraphQlRepository
    public interface UsuarioRepository extends JpaRepository<Usuario,Long>, QueryByExampleExecutor<Usuario> {
        Optional<Usuario> findByNombreusuario(String nombreusuario);

    }

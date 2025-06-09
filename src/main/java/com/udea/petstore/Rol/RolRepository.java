package com.udea.petstore.Rol;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.graphql.data.GraphQlRepository;

@GraphQlRepository
public interface RolRepository extends JpaRepository<Rol, Integer>, QueryByExampleExecutor<Rol> {

}

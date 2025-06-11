package com.udea.petstore.Producto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.graphql.data.GraphQlRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import java.util.List;

@GraphQlRepository
public interface ProductoRepository extends JpaRepository<Producto, Long>, QueryByExampleExecutor<Producto> {
    List<Producto> findTop5ByOrderByProductoVendidosDesc();
}


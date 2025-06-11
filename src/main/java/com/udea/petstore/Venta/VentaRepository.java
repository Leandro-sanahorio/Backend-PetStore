package com.udea.petstore.Venta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.graphql.data.GraphQlRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

@GraphQlRepository
public interface VentaRepository extends JpaRepository<Venta, Long>, QueryByExampleExecutor<Venta> {

    List<Venta> findByFechaCreacionAfter(LocalDateTime fecha);

    List<Venta> findByFechaCreacionBetween(LocalDateTime inicio, LocalDateTime fin);
}

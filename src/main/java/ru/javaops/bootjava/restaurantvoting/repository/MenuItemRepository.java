package ru.javaops.bootjava.restaurantvoting.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.bootjava.restaurantvoting.model.MenuItem;

import java.time.LocalDate;

// https://blog.codecentric.de/en/2017/08/parsing-of-localdate-query-parameters-in-spring-boot/
@Transactional(readOnly = true)
@RepositoryRestResource(path = "menu-items")
public interface MenuItemRepository extends JpaRepository<MenuItem, Integer> {
    @RestResource(rel = "by-date", path = "by-date")
    @Query("SELECT mi FROM MenuItem mi WHERE mi.date=:date")
    Page<MenuItem> getAllByDate(@Param("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, Pageable page);
}
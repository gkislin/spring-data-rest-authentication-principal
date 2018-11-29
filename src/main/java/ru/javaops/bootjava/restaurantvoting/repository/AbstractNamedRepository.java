package ru.javaops.bootjava.restaurantvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.bootjava.restaurantvoting.model.AbstractNamedEntity;

import java.util.List;
import java.util.Optional;

//http://blog.netgloo.com/2014/12/18/handling-entities-inheritance-with-spring-data-jpa/
@Transactional(readOnly = true)
@NoRepositoryBean
public interface AbstractNamedRepository<NE extends AbstractNamedEntity> extends JpaRepository<NE, Integer> {
    @RestResource(rel = "by-name", path = "by-name")
    Optional<NE> findByName(@Param("name") String name);

    //    https://stackoverflow.com/questions/25362540/like-query-in-spring-jparepository
    @RestResource(rel = "like", path = "like")
    List<NE> findByNameContainingIgnoreCase(@Param("name") String name);
}
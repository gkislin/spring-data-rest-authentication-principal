package ru.javaops.bootjava.restaurantvoting.repository;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.javaops.bootjava.restaurantvoting.model.LunchRef;

@RepositoryRestResource(path = "lunch-refs")
public interface LunchRefRepository extends AbstractNamedRepository<LunchRef> {
}
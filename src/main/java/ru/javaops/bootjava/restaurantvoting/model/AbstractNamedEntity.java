package ru.javaops.bootjava.restaurantvoting.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@MappedSuperclass
@NoArgsConstructor
@Getter
@Setter
public abstract class AbstractNamedEntity extends AbstractBaseEntity {

    @NotBlank
    @Size(max = 512)
    @Column(name = "name", unique = true, nullable = false)
    @NotNull
    protected String name;

    protected AbstractNamedEntity(Integer id, String name) {
        super(id);
        this.name = name;
    }

    @Override
    public String toString() {
        return super.toString() + '(' + name + ')';
    }
}
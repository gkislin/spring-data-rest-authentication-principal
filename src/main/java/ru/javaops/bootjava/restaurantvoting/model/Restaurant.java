package ru.javaops.bootjava.restaurantvoting.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "restaurant")
@NoArgsConstructor
@Getter
@Setter
@RestResource(path = "restaurants")
public class Restaurant extends AbstractNamedEntity {

    public Restaurant(Integer id, String name, String address) {
        super(id, name);
        this.address = address;
    }

    @Column(name = "address")
    @Size(max = 512)
    @Nullable
    private String address;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @RestResource(path = "lunch-refs")
    private Set<LunchRef> lunchRefs;
}
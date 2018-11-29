package ru.javaops.bootjava.restaurantvoting.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "lunch_ref")
@NoArgsConstructor
@Getter
@Setter
public class LunchRef extends AbstractNamedEntity {

    public LunchRef(Integer id, String name, int priceInCents, String description) {
        super(id, name);
        this.price = priceInCents;
        this.description = description;
    }

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "description", nullable = true)
    @Size(max = 5000)
    @Nullable
    private String description;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotNull
    private Restaurant restaurant;
}
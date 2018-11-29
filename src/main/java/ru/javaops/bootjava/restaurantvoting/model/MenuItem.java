package ru.javaops.bootjava.restaurantvoting.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "menu_item", uniqueConstraints = {@UniqueConstraint(columnNames = {"date", "lunch_ref_id"}, name = "uk_menu_item")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuItem extends AbstractBaseEntity {

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "lunch_ref_id", nullable = false)
    @NotNull
    @RestResource(path = "lunch-refs")
    private LunchRef lunchRef;
}

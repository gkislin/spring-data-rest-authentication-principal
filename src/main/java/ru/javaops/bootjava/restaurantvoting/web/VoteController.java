package ru.javaops.bootjava.restaurantvoting.web;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.javaops.bootjava.restaurantvoting.model.Vote;
import ru.javaops.bootjava.restaurantvoting.repository.VoteRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/votes", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
// TODO replace 1 to authorized User
public class VoteController {

    private final VoteRepository repository;

    @GetMapping
    public List<Vote> history() {
        return repository.getAllByUserId(1);
    }

    @GetMapping("/current")
    public ResponseEntity<?> current() {
        Optional<Vote> optionalResult = repository.getByUserIdAndDate(1, LocalDate.now());
        return optionalResult.map(ResponseEntity::ok).orElse(ResponseEntity.noContent().build());
    }
}

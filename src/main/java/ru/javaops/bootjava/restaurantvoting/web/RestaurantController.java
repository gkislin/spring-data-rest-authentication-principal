package ru.javaops.bootjava.restaurantvoting.web;

import lombok.AllArgsConstructor;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.javaops.bootjava.restaurantvoting.AuthUser;
import ru.javaops.bootjava.restaurantvoting.model.Restaurant;
import ru.javaops.bootjava.restaurantvoting.model.User;
import ru.javaops.bootjava.restaurantvoting.model.Vote;
import ru.javaops.bootjava.restaurantvoting.repository.RestaurantRepository;
import ru.javaops.bootjava.restaurantvoting.repository.VoteRepository;
import ru.javaops.bootjava.restaurantvoting.util.RevoteDeadlineException;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

/**
 * @see <a href="https://docs.spring.io/spring-data/rest/docs/current/reference/html/#customizing-sdr.overriding-sdr-response-handlers">Overriding Spring Data REST Response Handlers</a>
 */
@RepositoryRestController
@AllArgsConstructor
public class RestaurantController {
    private static final LocalTime REVOTE_DEADLINE_TIME = LocalTime.of(11, 0);

    private final RestaurantRepository restaurantRepository;
    private final VoteController.VoteAssembler voteAssembler;
    private final VoteRepository voteRepository;

    @GetMapping(value = "/restaurants/{id}/vote", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<Resource<Vote>> vote(@PathVariable int id, @AuthenticationPrincipal AuthUser authUser) {
//        https://jira.spring.io/browse/DATAREST-1312
        User user = authUser.getUser();
        Optional<Vote> optionalResult = voteRepository.getByUserIdAndDate(user.getId(), LocalDate.now());
        Restaurant restaurant = restaurantRepository.getOne(id);
        LocalTime now = LocalTime.now();
        Vote vote = optionalResult
                .map(v -> {
                    if (now.isAfter(REVOTE_DEADLINE_TIME)) {
                        throw new RevoteDeadlineException();
                    }
                    v.setTime(now);
                    v.setRestaurant(restaurant);
                    return v;
                })
                .orElseGet(() -> new Vote(user, LocalDate.now(), now, restaurant));

        return (vote.isNew() ? ResponseEntity.created(URI.create(voteAssembler.getLinkToCurrent().toString())) : ResponseEntity.ok())
                .body(voteAssembler.toResource(voteRepository.save(vote)));
    }
}

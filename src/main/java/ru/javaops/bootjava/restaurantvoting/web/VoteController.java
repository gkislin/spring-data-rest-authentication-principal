package ru.javaops.bootjava.restaurantvoting.web;

import lombok.AllArgsConstructor;
import org.springframework.data.rest.webmvc.RepositoryLinksResource;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.javaops.bootjava.restaurantvoting.model.Vote;
import ru.javaops.bootjava.restaurantvoting.repository.VoteRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Do not use {@link org.springframework.data.rest.webmvc.BasePathAwareController}
 * cause of bugs:
 * <a href="https://github.com/spring-projects/spring-hateoas/issues/434">data.rest.base-path missed in HAL links</a><br>
 * <a href="https://jira.spring.io/browse/DATAREST-748">Two endpoints created</a></li>
 * <p>
 * RequestMapping("/${spring.data.rest.base-path}/votes") give NPE in process()
 */
@RestController
@AllArgsConstructor
// TODO replace 1 to authorized User
@RequestMapping(value = "/api/votes", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class VoteController implements ResourceProcessor<RepositoryLinksResource> {

    private final VoteRepository repository;

    @GetMapping
    public Resources<Resource<Vote>> history() {
        var votes = repository.getAllByUserId(1);
        var now = LocalDate.now();

        List<Resource<Vote>> resourceList = votes.stream()
                .map(vote -> vote.getDate().equals(now) ?
                        new Resource<>(vote, linkTo(methodOn(VoteController.class).current()).withSelfRel()) :
                        new Resource<>(vote))
                .collect(Collectors.toList());

        return new Resources<>(resourceList,
                linkTo(methodOn(VoteController.class).history()).withSelfRel(),
                linkTo(methodOn(VoteController.class).current()).withRel("current"));
    }

    @GetMapping("/current")
    public ResponseEntity<?> current() {
        Optional<Vote> optionalResult = repository.getByUserIdAndDate(1, LocalDate.now());
        return optionalResult
                .map(vote -> ResponseEntity.ok(
                        new Resource<>(vote, linkTo(methodOn(VoteController.class).current()).withSelfRel())))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    //  https://stackoverflow.com/questions/23135756/how-to-add-links-to-root-resource-in-spring-data-rest
    @Override
    public RepositoryLinksResource process(RepositoryLinksResource resource) {
        resource.add(ControllerLinkBuilder.linkTo(VoteController.class).withRel("votes"));
        return resource;
    }
}

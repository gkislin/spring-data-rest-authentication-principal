package ru.javaops.bootjava.restaurantvoting.web;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.RepositoryLinksResource;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.*;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.javaops.bootjava.restaurantvoting.AuthUser;
import ru.javaops.bootjava.restaurantvoting.model.Vote;
import ru.javaops.bootjava.restaurantvoting.repository.VoteRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.util.Assert.notNull;

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
@RequestMapping(value = "/api/votes", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class VoteController implements ResourceProcessor<RepositoryLinksResource> {

    private final VoteAssembler voteAssembler;
    private final VoteRepository repository;

    @GetMapping
    public PagedResources<Resource> history(@AuthenticationPrincipal AuthUser authUser, Pageable page, PagedResourcesAssembler<Vote> pagedAssembler) {
        notNull(authUser, "no authenticationPrincipal");
        Page<Vote> pageResult = repository.getAllByUserId(authUser.getUser().getId(), page);
        PagedResources<Resource> resources = pagedAssembler.toResource(pageResult, voteAssembler);
        resources.add(voteAssembler.getLinkToCurrent().withRel("current"));
        return resources;
    }

    @GetMapping("/current")
    public ResponseEntity<?> current(@AuthenticationPrincipal AuthUser authUser) {
        notNull(authUser, "no authenticationPrincipal");
        Optional<Vote> optionalResult = repository.getByUserIdAndDate(authUser.getUser().getId(), LocalDate.now());
        return optionalResult
                .map(vote -> ResponseEntity.ok(voteAssembler.toResource(vote)))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    //  https://stackoverflow.com/questions/23135756/how-to-add-links-to-root-resource-in-spring-data-rest
    @Override
    public RepositoryLinksResource process(RepositoryLinksResource resource) {
        resource.add(new Link(linkTo(VoteController.class).toString() + "{?page,size,sort}").withRel("votes"));
        return resource;
    }

    @Component
    public static class VoteAssembler extends ResourceAssemblerSupport<Vote, Resource> {
        private final EntityLinks entityLinks;

        public VoteAssembler(EntityLinks entityLinks) {
            super(VoteController.class, Resource.class);
            this.entityLinks = entityLinks;
        }

        @Override
        public Resource<Vote> toResource(Vote vote) {
            Resource<Vote> resource = new Resource<>(vote);
            if (vote.getDate().equals(LocalDate.now())) {
                resource.add(getLinkToCurrent().withSelfRel());
            }
            resource.add(entityLinks.linkToSingleResource(vote.getRestaurant()).withRel("restaurant"));
            return resource;
        }

        public ControllerLinkBuilder getLinkToCurrent() {
            return linkTo(methodOn(VoteController.class).current(null));
        }
    }
}

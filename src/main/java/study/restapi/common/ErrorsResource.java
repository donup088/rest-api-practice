package study.restapi.common;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.validation.Errors;
import study.restapi.index.IndexController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class ErrorsResource extends RepresentationModel {
    private Errors errors;

    public ErrorsResource(Errors errors) {
        this.errors = errors;
        add(linkTo(methodOn(IndexController.class).index()).withRel("index"));
    }

    public Errors getErrors() {
        return errors;
    }
}

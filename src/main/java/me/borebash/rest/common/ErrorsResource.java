package me.borebash.rest.common;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.validation.Errors;

import me.borebash.rest.index.IndexContoller;

public class ErrorsResource extends EntityModel<Errors>{
    
    public ErrorsResource(Errors error, Link... links) {
        super(error, links);
        add(linkTo(methodOn(IndexContoller.class).index()).withRel("index"));
    }
}
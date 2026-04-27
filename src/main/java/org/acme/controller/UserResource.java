package org.acme.controller;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.domain.User;
import org.acme.dto.CreateUserRequest;
import org.acme.repository.UserRepository;

import java.util.List;

@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    UserRepository userRepository;

    @GET
    public Response getUsers() {
        List<User> users = userRepository.listAll();
        return Response.ok(users).build();
    }

    @POST
    @Transactional
    public Response createUser(@Valid CreateUserRequest request) {
        User user = new User();
        user.setFullName(request.fullName());
        user.setEmail(request.email());
        userRepository.persist(user);
        return Response.status(Response.Status.CREATED).entity(user).build();
    }
}

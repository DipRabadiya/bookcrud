package org.crud.LDAP;

import jakarta.json.JsonArray;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.management.Query;
import java.util.List;
import java.util.stream.Collectors;

@RegisterRestClient(baseUri = "http://localhost:8081")
@Path("/ldap")
public interface LDAPRestClient {

//    http://localhost:8081/ldap/users

    @GET
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    List<User> getAllUsers();

    @POST
    @Path("/user")
    @Consumes(MediaType.APPLICATION_JSON)
    Response addUser(User user);

    @DELETE
    @Path("/user/{cn}")
    Response deleteUser(@PathParam("cn") String cn);

    @POST
    @Path("/user/auth")
    @Consumes(MediaType.APPLICATION_JSON)
    Response authUser(User user);

    @PUT
    @Path("/user/password")
    @Consumes(MediaType.APPLICATION_JSON)
    Response updateUserPassword(User user);

    @PUT
    @Path("/user/details")
    @Consumes(MediaType.APPLICATION_JSON)
    Response updateUserDetails(User user);

    @GET
    @Path("/check")
    @Produces(MediaType.TEXT_PLAIN)
    Response checkConnection();
}

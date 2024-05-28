package org.crud.LDAP;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.crud.Service.BookService;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.logging.Logger;

@Path("/")
public class LDAPResource {

    @RestClient
    LDAPRestClient ldapRestClient;

    private static final Logger LOGGER = Logger.getLogger(LDAPResource.class.getName());

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUser() {
        LOGGER.info("Fetching all users from LDAP");
        return Response.ok(ldapRestClient.getAllUsers()).build();
    }

    @POST
    @Path("/user")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addUser(User user) {
        LOGGER.info("Adding new user to LDAP: " + user.getCn());
        Response response = ldapRestClient.addUser(user);
        LOGGER.info("User added: " + response.getStatus());
        return response;
    }

    @DELETE
    @Path("/user/{cn}")
    public Response deleteUser(@PathParam("cn") String cn) {
        LOGGER.info("Deleting user from LDAP: " + cn);
        Response response = ldapRestClient.deleteUser(cn);
        LOGGER.info("User deleted: " + response.getStatus());
        return response;
    }

    @POST
    @Path("/user/auth")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response authUser(User user) {
        LOGGER.info("Authenticating user: " + user.getCn());
        Response response = ldapRestClient.authUser(user);
        LOGGER.info("User authentication status: " + response.getStatus());
        return response;
    }

    @PUT
    @Path("/user/password")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUserPassword(User user) {
        LOGGER.info("Updating password for user: " + user.getCn());
        Response response = ldapRestClient.updateUserPassword(user);
        LOGGER.info("Password updated: " + response.getStatus());
        return response;
    }

    @PUT
    @Path("/user/details")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUserDetails(User user) {
        LOGGER.info("Updating details for user: " + user.getCn());
        Response response = ldapRestClient.updateUserDetails(user);
        LOGGER.info("User details updated: " + response.getStatus());
        return response;
    }

    @GET
    @Path("/check")
    @Produces(MediaType.TEXT_PLAIN)
    public Response checkConnection() {
        LOGGER.info("Checking LDAP connection");
        Response response = ldapRestClient.checkConnection();
        LOGGER.info("LDAP connection status: " + response.getStatus());
        return response;
    }
}

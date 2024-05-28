package org.crud.LDAP;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.crud.Service.BookService;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.time.temporal.ChronoUnit;
import java.util.logging.Logger;

@Path("/")
public class LDAPResource {

    @RestClient
    LDAPRestClient ldapRestClient;

    private static final Logger LOGGER = Logger.getLogger(LDAPResource.class.getName());

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/users")
    @Fallback(fallbackMethod = "getAllUserFallback")
//    @Timeout(1000)
//    @Retry(maxRetries = 3)
    @CircuitBreaker(requestVolumeThreshold = 4, failureRatio = 0.5, delay = 10, delayUnit = ChronoUnit.SECONDS)
    @Counted(name = "CountOfGetUsers", description = "How many time this method is called")
    @Timed(name = "TimeTakeForGetUsers", description = "How much time api take to response", unit = MetricUnits.MILLISECONDS)
    @Metered(name = "MeteredGetUsers", description = "how freq this api is called")
    public Response getAllUser() {
        LOGGER.info("Fetching all users from LDAP");
        return Response.ok(ldapRestClient.getAllUsers()).build();
    }

    public Response getAllUserFallback(){
        return Response.ok("Server is down, Please wait").build();
    }

    @POST
    @Path("/user")
    @Consumes(MediaType.APPLICATION_JSON)
    @Fallback(fallbackMethod = "addUserFallback")
    public Response addUser(User user) {
        LOGGER.info("Adding new user to LDAP: " + user.getCn());
        Response response = ldapRestClient.addUser(user);
        LOGGER.info("User added: " + response.getStatus());
        return response;
    }

    public Response addUserFallback(User user){
        return Response.ok("Server is down, Please wait").build();
    }

    @DELETE
    @Path("/user/{cn}")
    @Fallback(fallbackMethod = "deleteUserFallback")
    public Response deleteUser(@PathParam("cn") String cn) {
        LOGGER.info("Deleting user from LDAP: " + cn);
        Response response = ldapRestClient.deleteUser(cn);
        LOGGER.info("User deleted: " + response.getStatus());
        return response;
    }

    public Response deleteUserFallback(String cn){
        return Response.ok("Server is down, Please wait").build();
    }

    @POST
    @Path("/user/auth")
    @Consumes(MediaType.APPLICATION_JSON)
    @Fallback(fallbackMethod = "authUserFallback")
    public Response authUser(User user) {
        LOGGER.info("Authenticating user: " + user.getCn());
        Response response = ldapRestClient.authUser(user);
        LOGGER.info("User authentication status: " + response.getStatus());
        return response;
    }

    public Response authUserFallback(User user){
        return Response.ok("Server is down, Please wait").build();
    }

    @PUT
    @Path("/user/password")
    @Consumes(MediaType.APPLICATION_JSON)
    @Fallback(fallbackMethod = "updateUserPasswordFallback")
    public Response updateUserPassword(User user) {
        LOGGER.info("Updating password for user: " + user.getCn());
        Response response = ldapRestClient.updateUserPassword(user);
        LOGGER.info("Password updated: " + response.getStatus());
        return response;
    }

    public Response updateUserPasswordFallback(User user){
        return Response.ok("Server is down, Please wait").build();
    }

    @PUT
    @Path("/user/details")
    @Consumes(MediaType.APPLICATION_JSON)
    @Fallback(fallbackMethod = "updateUserDetailsFallback")
    public Response updateUserDetails(User user) {
        LOGGER.info("Updating details for user: " + user.getCn());
        Response response = ldapRestClient.updateUserDetails(user);
        LOGGER.info("User details updated: " + response.getStatus());
        return response;
    }

    public Response updateUserDetailsFallback(User user){
        return Response.ok("Server is down, Please wait").build();
    }

    @GET
    @Path("/check")
    @Produces(MediaType.TEXT_PLAIN)
    @Fallback(fallbackMethod = "checkConnectionFallback")
    public Response checkConnection() {
        LOGGER.info("Checking LDAP connection");
        Response response = ldapRestClient.checkConnection();
        LOGGER.info("LDAP connection status: " + response.getStatus());
        return response;
    }

    public Response checkConnectionFallback(){
        return Response.ok("Server is down, Please wait").build();
    }
}

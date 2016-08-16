/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Finder;

import com.google.gson.Gson;
import ResponseData.Response;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.DELETE;

/**
 * REST Web Service
 * DeleteLocationAPIcall is responsible for handling deleteLocation aPI call
 * Has a DELETE method that handles deleting a location.
 *
 * @author cancola
 */
@Path("deleteLocation/{userID}/{LocationID}/{authToken}")
public class DeleteLocationAPIcall {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of DeleteLocationAPIcall
     */
    public DeleteLocationAPIcall() {
    }

    /**
     * Retrieves representation of an instance of Finder.DeleteLocationAPIcall
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getJson() {
        throw new UnsupportedOperationException();
    }

    /**
     * DELETE method for for deleting a Location from the database
     * @param userID - users id, LocationID - the location id of location that user wants to delete, token - users authentication token
     * @return an JSON Response object with the status value of "OK", "TOKENCLEARED", or "ERROR".
     */
    @DELETE
    public String putJson(@PathParam("userID")int userID, @PathParam("LocationID") int LocationID, @PathParam("authToken")String token) {
        Database db = new Database();
        String status = (db.deleteLocation(userID, LocationID, token));
        Response response = new Response(status);
        Gson g = new Gson();
        String myReturnJSON = g.toJson(response);   
        return myReturnJSON;
    }
}

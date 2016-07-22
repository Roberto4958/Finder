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
import javax.ws.rs.POST;

/**
 * REST Web Service
 *
 * @author cancola
 */
@Path("logOut/{userID}/{authToken}")
public class LogoutAPIcall {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of LogoutAPIcall
     */
    public LogoutAPIcall() {
    }

    /**
     * Retrieves representation of an instance of Finder.LogoutAPIcall
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getJson() {
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of LogoutAPIcall
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @POST
    public String putJson(@PathParam("userID")int userID, @PathParam("authToken") String token) {
        Database db = new Database();
        String status = (db.logout(userID, token));
        Response response = new Response(status);
        Gson g = new Gson();
        String myReturnJSON = g.toJson(response);   
        return myReturnJSON;
        
    }
}

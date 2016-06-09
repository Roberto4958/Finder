/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Finder;

import com.google.gson.Gson;
import DataModel.Location;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;

/**
 * REST Web Service
 *
 * @author cancola
 */
@Path("findlocation/{userID}/{authToken}")
public class FindLocationAPIcall {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of FindLocationAPIcall
     */
    public FindLocationAPIcall() {
    }

    /**
     * Retrieves representation of an instance of Finder.FindLocationAPIcall
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getJson(@PathParam("userID") int userID, @PathParam("authToken") String token) {
        Database db  = new Database();
        Location location = db.findLocation(userID, token);
       
        if(location != null){
            Gson g = new Gson();
            String myReturnJSON = g.toJson(location);   
            return myReturnJSON;
        }
                
        else return "Sorry sothing went wrong";
    }

    /**
     * PUT method for updating or creating an instance of FindLocationAPIcall
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
}

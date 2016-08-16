/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Finder;

import ResponseData.LocationResponse;
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
 * FindLocationAPIcall is responsible for findLocation API call.
 * Has a GET method that handles getting location from the database.
 *
 * @author Roberto Aguilar
 */
@Path("findLocation/{userID}/{authToken}")
public class FindLocationAPIcall {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of FindLocationAPIcall
     */
    public FindLocationAPIcall() {
    }

    /**
     * GET method that handles getting the last location of user from database.
     * @param userID - users ID, token - users authentication token
     * @return an JSON LocationResponse object
     */
    @GET
    @Produces("application/json")
    public String getJson(@PathParam("userID") int userID, @PathParam("authToken") String token) {
        Database db  = new Database();
        Location location = db.findLocation(userID, token);
       
        if(location != null){ // if there are no locations in the database
            if(location.locationID == -1){
                LocationResponse response = new LocationResponse(null, "OK");
                Gson g = new Gson();
                String myReturnJSON = g.toJson(response);   
                return myReturnJSON;
            }
            if(location.locationID == -3){ // if authToken and useID do not match to a row
                LocationResponse response = new LocationResponse(null, "TOKENCLEARED");
                Gson g = new Gson();
                String myReturnJSON = g.toJson(response);   
                return myReturnJSON;
            } 
            LocationResponse response = new LocationResponse(location, "OK");
            Gson g = new Gson();
            String myReturnJSON = g.toJson(response);   
            return myReturnJSON;
        }
        
                
        else {
            LocationResponse response = new LocationResponse(null, "ERROR");
            Gson g = new Gson();
            String myReturnJSON = g.toJson(response);   
            return myReturnJSON;
        }
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

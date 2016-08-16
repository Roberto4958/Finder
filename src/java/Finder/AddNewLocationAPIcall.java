/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Finder;

import com.google.gson.Gson;
import ResponseData.Response;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;

/**
 * REST Web Service
 * This class is responsible for handling addNewLocation API call. 
 * Has a PUT method handling adding locations.
 *
 * @author Roberto Aguilar
 */
@Path("addNewLocation/{place}/{locationX}/{locationY}/{userID}/{authToken}")
public class AddNewLocationAPIcall {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of TheFinder
     */
    public AddNewLocationAPIcall() {
    }

    /**
     * Retrieves representation of an instance of Finder.AddNewLocationAPIcall
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String SendLocation() {
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for adding a new location to the database
     * @param place - name of users location, x - users latitude, y - users longitude, userID - users id, token - users authentication Token
     * @return an JSON response of a Response object with the status of value "OK", "TOKENCLEARED", or "ERROR"
     */
    @PUT
    public String putJson(@PathParam("place") String place, @PathParam("locationX") double x, @PathParam("locationY") double y, @PathParam("userID") int userID, @PathParam("authToken")String token ) {
        try {
            Database db = new Database();
            place = URLDecoder.decode(place.toString(),"UTF-8");
            String status = db.addNewLocation(userID, place, x, y, token);
            Response response = new Response(status);
            Gson g = new Gson();
            String myReturnJSON = g.toJson(response);
            return myReturnJSON;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(AddNewLocationAPIcall.class.getName()).log(Level.SEVERE, null, ex);
            Response response = new Response("ERROR");
            Gson g = new Gson();
            String myReturnJSON = g.toJson(response);
            return myReturnJSON;
        }
    }
}

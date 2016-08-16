/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Finder;

import ResponseData.HistoryResponse;
import java.util.ArrayList;
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
 * GetHistoryAPIcall handles API calls from history.
 * Has a GET request that handles getting all the locations of user
 * 
 * @author Roberto Aguilar
 */
@Path("history/{userID}/{authToken}")
public class GetHistoryAPIcall {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of GetHistoryAPIcall
     */
    public GetHistoryAPIcall() {
    }

    /**
     * GET method that handles getting a ArrayList of users Location
     * @param userID - users id, token - users authentication token
     * @return an JSON HistoryResponse object
     */
    @GET
    @Produces("application/json")
    public String getJson(@PathParam("userID") int userID, @PathParam("authToken") String token) {
        Database db = new Database();  
        ArrayList<Location> history = db.getHistory(userID, token);
        
        if(history.size()>0 && history.get(0).locationID == -3){
            HistoryResponse response = new HistoryResponse(history, "TOKENCLEARED");
            Gson g = new Gson();
            String myReturnJSON = g.toJson(response);   
            return myReturnJSON;
        }
        else if(history != null){
            HistoryResponse response = new HistoryResponse(history, "OK");
            Gson g = new Gson();
            String myReturnJSON = g.toJson(response);   
            return myReturnJSON;
        }    
        else{
            HistoryResponse response = new HistoryResponse(null, "ERROR");
            Gson g = new Gson();
            String myReturnJSON = g.toJson(response);   
            return myReturnJSON;
        }
    }

    /**
     * PUT method for updating or creating an instance of GetHistoryAPIcall
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
}

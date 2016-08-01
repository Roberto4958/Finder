/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Finder;

import ResponseData.UserResponse;
import DataModel.User;
import com.google.gson.Gson;
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
import javax.ws.rs.Produces;

/**
 * REST Web Service
 *
 * @author cancola
 */
@Path("createAccount/{userName}/{password}/{firstName}/{lastName}")
public class CreateAccountAPIcall {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of CreateAccountAPIcall
     */
    public CreateAccountAPIcall() {
    }

    /**
     * Retrieves representation of an instance of Finder.CreateAccountAPIcall
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getJson() {
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of CreateAccountAPIcall
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    public String putJson(@PathParam("userName")String username, @PathParam("password")String pass, @PathParam("firstName") String firstname, @PathParam("lastName") String lastname) {
       Database db = new Database();
       try {
            username = URLDecoder.decode(username.toString(),"UTF-8");
            pass = URLDecoder.decode(pass.toString(),"UTF-8");
            firstname = URLDecoder.decode(firstname.toString(),"UTF-8");
            lastname = URLDecoder.decode(lastname.toString(),"UTF-8");
       }
       catch (UnsupportedEncodingException ex) {
            UserResponse response = new UserResponse(null, "ERROR");
            Gson g = new Gson();
            String myReturnJSON = g.toJson(response);   
            return myReturnJSON; 
       }
       User user =  db.createAccount(username, pass, firstname, lastname);
       if(user != null){
            if(user.ID == -1){
                UserResponse response = new UserResponse(null, "OK");
                Gson g = new Gson();
                String myReturnJSON = g.toJson(response);   
                return myReturnJSON; 
           } 
            UserResponse response = new UserResponse(user, "OK");
            Gson g = new Gson();
            String myReturnJSON = g.toJson(response);   
            return myReturnJSON;           
       }
       else{
            UserResponse response = new UserResponse(null, "ERROR");
            Gson g = new Gson();
            String myReturnJSON = g.toJson(response);   
            return myReturnJSON;
       }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Finder;

import ResponseData.UserResponse;
import com.google.gson.Gson;
import DataModel.User;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
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
import javax.ws.rs.POST;


/**
 * REST Web Service
 * LogInAPIcall is responsible for handling logIn API call.
 * Has a POST request that handles modifying users authentication token 
 * 
 * @author Roberto Aguilar
 */
@Path("hashPasswords/{auth}")
public class HashALLPasswords {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of LogInAPIcall2
     */
    public HashALLPasswords() {
    }

    /**
     * Retrieves representation of an instance of Finder.LogInAPIcall
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getJson()
    {   
        return null;
    }
    
    /**
     * PUT method for logging in the user by modifying authentication token
     * @param name - users username, pass - users password
     * @return an JSON object of USerResponse
     */
    @POST
    public String putJson(@PathParam("auth") String auth) {
       
        Database db = new Database();           
        
        db.HashAllPass();
        UserResponse response = new UserResponse(null,"OK");
        Gson g = new Gson();
        String myReturnJSON = g.toJson(response);   
        return myReturnJSON;
         
    }
}

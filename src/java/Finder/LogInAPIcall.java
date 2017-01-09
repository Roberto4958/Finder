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
@Path("logIn/{username}/{password}")
public class LogInAPIcall {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of LogInAPIcall2
     */
    public LogInAPIcall() {
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

    private String hashPassword(String pass){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            md.update(pass.getBytes());
            byte[] b = md.digest();
            StringBuffer sb = new StringBuffer();
            for(byte b1: b){
		sb.append(Integer.toHexString(b1 & 0Xff).toString());
            }
            return sb.toString();
			
	} catch (NoSuchAlgorithmException e) {
            return "Did not work";
	}
    }
    /**
     * PUT method for logging in the user by modifying authentication token
     * @param name - users username, pass - users password
     * @return an JSON object of USerResponse
     */
    @POST
    public String putJson(@PathParam("username") String name, @PathParam("password") String pass) {
       
        Database db = new Database();   
        try {
            name = URLDecoder.decode(name.toString(),"UTF-8");
            pass = URLDecoder.decode(pass.toString(),"UTF-8");
        } 
        catch (UnsupportedEncodingException ex) {
            Logger.getLogger(LogInAPIcall.class.getName()).log(Level.SEVERE, null, ex);
            UserResponse response = new UserResponse(null,"ERROR");
            Gson g = new Gson();
            String myReturnJSON = g.toJson(response);   
            return myReturnJSON;
        }    
        User user = db.LogIn(name, pass);
 
        if(user != null){
            if(user.ID==-1){ // if userName and password do not match to a user
                UserResponse response = new UserResponse(null,"OK");
                Gson g = new Gson();
                String myReturnJSON = g.toJson(response);   
                return myReturnJSON;  
            }
            UserResponse response = new UserResponse(user,"OK");
            Gson g = new Gson();
            String myReturnJSON = g.toJson(response);   
            return myReturnJSON;
        }  
        else {
            UserResponse response = new UserResponse(null,"ERROR");
            Gson g = new Gson();
            String myReturnJSON = g.toJson(response);   
            return myReturnJSON;
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Finder;

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
    public String getJson(@PathParam("username") String name, @PathParam("password") String pass)
    {                        
        Database db = new Database();
        return db.LogIn(name, pass);
        //throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of LogInAPIcall
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
}

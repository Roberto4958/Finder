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
@Path("find/{locationX}/{locationY}")
public class TheFinder {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of TheFinder
     */
    public TheFinder() {
    }

    /**
     * Retrieves representation of an instance of Finder.TheFinder
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("text/html")
    public String SendLocation(@PathParam("locationX") int x, @PathParam("locationY") int y ) {
       return "Your location is "+x+", "+y;
        //throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of TheFinder
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("text/html")
    public void putHtml(String content) {
    }
}

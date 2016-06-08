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
import javax.ws.rs.DELETE;

/**
 * REST Web Service
 *
 * @author cancola
 */
@Path("deleteLocation/{ID}")
public class DeleteLocationAPIcall {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of DeleteLocationAPIcall
     */
    public DeleteLocationAPIcall() {
    }

    /**
     * Retrieves representation of an instance of Finder.DeleteLocationAPIcall
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of DeleteLocationAPIcall
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @DELETE
    public void putJson(@PathParam("ID") int id) {
        Database db = new Database();
        db.deleteLocation(id);  
    }
}

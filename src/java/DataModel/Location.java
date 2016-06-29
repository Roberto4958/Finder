/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataModel;

/**
 *
 * @author cancola
 */
public class Location {
    
    public double latitude;
    public double longtitude;
    public int locationID;
    public String place;
    
    public Location(String p, double lat, double longt, int lID){      
        place = p;
        latitude = lat;
        longtitude = longt;
        locationID = lID;   
    }
}

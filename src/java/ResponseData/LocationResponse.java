/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ResponseData;

import DataModel.Location;
/**
 * LocationResponse is responsible for holding the http request status and a Location object.
 * This class gets converted to a JSON string and gets sent when a API call is made. 
 *
 * @author Roberto Aguilar
 */
public class LocationResponse extends Response{
    
    public Location locationInfo;
    
   public LocationResponse(Location l, String s){
        super(s);
        locationInfo = l;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ResponseData;

import DataModel.Location;
/**
 *
 * @author cancola
 */
public class LocationResponse extends Response{
    
    public Location result;
    
   public LocationResponse(Location l, String s){
        
        super(s);
        result = l;
    }
}

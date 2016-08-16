/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ResponseData;

import DataModel.Location;
import java.util.ArrayList;
/**
 * HistoryResponse is responsible for holding the http request status and hold a ArrayList of Location.
 * This class gets converted to a JSON string and gets sent when a history API call is made.
 *
 * @author Roberto Aguilar
 */
public class HistoryResponse extends Response{
    
    public ArrayList<Location> UserLocations;
    
    public HistoryResponse(ArrayList<Location> l, String s){
        
        super(s);
        UserLocations = l;      
    }
}

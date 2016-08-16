/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ResponseData;

import DataModel.Location;
import DataModel.User;
/**
 * Response is responsible for holding the http request status.  
 * This class gets converted to a JSON string and gets sent when a API call is made.
 * 
 * @author Roberto Aguilar
 */
public class Response {
   
    public String status;
    
    public Response(String s){
        status = s;
    }
}

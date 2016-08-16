/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ResponseData;

import DataModel.User;
/**
 * UserResponse is responsible for holding the http request status, and a User object.
 * This class gets converted to a JSON string and gets sent when a API call is made.
 *
 * @author Roberto Aguilar
 */
public class UserResponse extends Response{
    
    public User userInfo;
    
    public UserResponse(User u, String s){
       
        super(s);
        userInfo = u;     
    }
    
}

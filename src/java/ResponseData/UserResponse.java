/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ResponseData;

import DataModel.User;
/**
 *
 * @author cancola
 */
public class UserResponse {
    
    public User results;
    public String status;
    
    public UserResponse(User u, String s){
        results = u;
        status = s;
    }
    
}
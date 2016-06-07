/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Testers;

import Finder.Database;

/**
 *
 * @author cancola
 */
public class DatabaseTester {
    
    public static void main(String args[])
    {
        Database db = new Database();
        System.out.println(db.createAccount("user6", "pass6", "Julie", "Walker"));
    }
    
    
}

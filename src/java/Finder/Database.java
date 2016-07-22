/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Finder;

import DataModel.Location;
import DataModel.User;
import java.sql.Connection;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;
/**
 *
 * @author cancola
 */
public class Database {
    
    private String connString = "jdbc:mysql://"; //localhost:3306/TheFinder    
    private String hostname = "";
    private String port = "";
    private String databaseName = "";
    private String username = ""; // MySQL username
    private String password = ""; // MySQL password

    public Database() {
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        }
        catch(Exception e){
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date currentTime = new Date();
            System.out.println(dateFormat.format(currentTime) + " : " + "Error connecting to database:");
            System.out.println(e.getMessage());      
        }
            hostname = System.getProperty("RDS_HOSTNAME");
            port = System.getProperty("RDS_PORT");
            databaseName = System.getProperty("RDS_FINDER_DB_NAME");
            username = System.getProperty("RDS_USERNAME");
            password = System.getProperty("RDS_PASSWORD");           
            connString = connString + hostname + ":" + port + "/" + databaseName;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(connString, username, password);
    }

    public String getName(int id) {
       
        Connection conn = null;
        try {
            conn = getConnection();

            String select = "select a.firstName, b.Score from Users a, Scores b where b.userID = a.idnew_table and a.idnew_table = ?;";
            PreparedStatement selectStmt = null;

            try {
                selectStmt = conn.prepareStatement(select);
                selectStmt.setInt(1, id);
                ResultSet rs = selectStmt.executeQuery();
                if (rs != null && rs.next()) {
                    
                    System.out.println(rs.getString("firstName") + " Score is: " + rs.getString("Score"));
                }
            } finally {
                if (selectStmt != null) {
                    selectStmt.close();
                }
            }
        } catch (SQLException e) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date currentTime = new Date();
            System.out.println(dateFormat.format(currentTime) + " : " + "SQLException Occurred in the punchIn Function in the Database_Driver class. Driver username: " + username);
            System.out.println(e.getMessage());
            System.out.println(e.getSQLState());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date currentTime = new Date();
                    System.out.println(dateFormat.format(currentTime) + " : " + "SQLException Occurred in the punchIn Function closing connection in the Database_Driver class. Driver username " + username);
                    System.out.println(e.getMessage());
                    System.out.println(e.getSQLState());
                }
            }
        }
        return null;
    }

    public User LogIn(String userName, String password) {
        User user = null;
        Connection conn = null;
        try {
            conn = getConnection();

            String select = "select userName, password, firstName, lastName, ID, authToken from Users where userName = ? and password = ?;";
            PreparedStatement selectStmt = null;

            try {
        
                selectStmt = conn.prepareStatement(select);
                selectStmt.setString(1, userName);
                selectStmt.setString(2, password);
                ResultSet rs = selectStmt.executeQuery();
                if (rs != null && rs.next()) {             
                    user = new User(rs.getString("userName"), rs.getString("firstName"), rs.getString("lastName"), setAuthToken(rs.getInt("ID")), rs.getInt("ID"));                           
                } 
            } finally {
                if (selectStmt != null) {
                    selectStmt.close();
                }
            }
        } catch (SQLException e) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date currentTime = new Date();
            System.out.println(dateFormat.format(currentTime) + " : " + "SQLException Occurred in the punchIn Function in the Database_Driver class. Driver username: " + username);
            System.out.println(e.getMessage());
            System.out.println(e.getSQLState());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date currentTime = new Date();
                    System.out.println(dateFormat.format(currentTime) + " : " + "SQLException Occurred in the punchIn Function closing connection in the Database_Driver class. Driver username " + username);
                    System.out.println(e.getMessage());
                    System.out.println(e.getSQLState());
                }
            }
        }
        return user;
    }

    public Location findLocation(int userID, String authToken) {
        
        if(verifyLogInStatus(userID, authToken)){                      
            Location location = null;
            Connection conn = null;
            try {
                conn = getConnection();
                String select = "select l.place, l.latitude, l.longitude, l.ID from Locations l, Users u where u.ID =? and l.userID = u.ID order by l.ID desc limit 1;";
                PreparedStatement selectStmt = null;

                try {
                    selectStmt = conn.prepareStatement(select);
                    selectStmt.setInt(1, userID);
                    ResultSet rs = selectStmt.executeQuery();
                    if (rs != null && rs.next()) {
                        location = new Location(rs.getString("place"), rs.getDouble("latitude"),rs.getDouble("longitude"), rs.getInt("ID"));
                    }
                    else location = new Location(null, 0,0, -1);
                } finally {
                    if (selectStmt != null) {
                        selectStmt.close();
                    }
                }
            } catch (SQLException e) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date currentTime = new Date();
                System.out.println(dateFormat.format(currentTime) + " : " + "SQLException Occurred in the punchIn Function in the Database_Driver class. Driver username: " + username);
                System.out.println(e.getMessage());
                System.out.println(e.getSQLState());
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date currentTime = new Date();
                        System.out.println(dateFormat.format(currentTime) + " : " + "SQLException Occurred in the punchIn Function closing connection in the Database_Driver class. Driver username " + username);
                        System.out.println(e.getMessage());
                        System.out.println(e.getSQLState());
                    }
                }
            }
            return location;
        }
        else if(authTokenMismatch(userID)){
            deleteToken(userID);
            return new Location(null, 0, 0, -3);
        }
        else return null;
    }

    public String addNewLocation(int userID, String place, double latitude, double longitude, String authToken) {
       
        String successful = "ERROR";
        if(verifyLogInStatus(userID, authToken)){
            Connection conn = null;
            try {
                conn = getConnection();

                String select = "insert into Locations(place, latitude, longitude, userID)values(?, ?, ?, ?);";
                PreparedStatement selectStmt = null;

                try {
                    selectStmt = conn.prepareStatement(select);
                    selectStmt.setString(1, place);
                    selectStmt.setDouble(2, latitude);
                    selectStmt.setDouble(3, longitude);
                    selectStmt.setInt(4, userID);
                    selectStmt.executeUpdate();
                    successful ="OK";

                } finally {
                    if (selectStmt != null) {
                        selectStmt.close();
                    }
                }
            } catch (SQLException e) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date currentTime = new Date();
                System.out.println(dateFormat.format(currentTime) + " : " + "SQLException Occurred in the punchIn Function in the Database_Driver class. Driver username: " + username);
                System.out.println(e.getMessage());
                System.out.println(e.getSQLState());
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date currentTime = new Date();
                        System.out.println(dateFormat.format(currentTime) + " : " + "SQLException Occurred in the punchIn Function closing connection in the Database_Driver class. Driver username " + username);
                        System.out.println(e.getMessage());
                        System.out.println(e.getSQLState());
                    }
                }
            }
            return successful;
        }
        else if(authTokenMismatch(userID)){
            deleteToken(userID);
            return "TOKENCLEARED";
        }
       else return "ERROR";
    }

    public ArrayList<Location> getHistory(int userID, String authToken) {
        
        if(verifyLogInStatus(userID, authToken)){
            ArrayList<Location> history = null;
            Connection conn = null;
            try {
                conn = getConnection();
                
                String select = "select ID, place, latitude, longitude from Locations where userID =?;";
                PreparedStatement selectStmt = null;

                try {
                    selectStmt = conn.prepareStatement(select);
                    selectStmt.setInt(1, userID);
                    ResultSet rs = selectStmt.executeQuery();
                    history = new ArrayList<Location>();
                    while (rs != null && rs.next()) {                       
                        Location l = new Location(rs.getString("place"), rs.getDouble("latitude"), rs.getDouble("longitude"), rs.getInt("ID"));
                        history.add(l);                       
                    }
                } finally {
                    if (selectStmt != null) {
                        selectStmt.close();
                    }
                }
            } catch (SQLException e) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date currentTime = new Date();
                System.out.println(dateFormat.format(currentTime) + " : " + "SQLException Occurred in the punchIn Function in the Database_Driver class. Driver username: " + username);
                System.out.println(e.getMessage());
                System.out.println(e.getSQLState());
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date currentTime = new Date();
                        System.out.println(dateFormat.format(currentTime) + " : " + "SQLException Occurred in the punchIn Function closing connection in the Database_Driver class. Driver username " + username);
                        System.out.println(e.getMessage());
                        System.out.println(e.getSQLState());
                    }
                }
            }

            return history;
        }
        else if(authTokenMismatch(userID)){
            deleteToken(userID);
            ArrayList<Location> collection = new ArrayList<Location>();
            collection.add(new Location(null, 0, 0, -3));
            return collection;
        }
        else return null;
    }
    
    public int verifyIfAcountCreated(String userName, String password) {

        int newUserID = 0;
        Connection conn = null;
        try {
            conn = getConnection();

            String select = "select ID from Users where userName = ? and password =?;";
            PreparedStatement selectStmt = null;

            try {
                selectStmt = conn.prepareStatement(select);
                selectStmt.setString(1, userName);
                selectStmt.setString(2, password);
                ResultSet rs = selectStmt.executeQuery();
                if (rs != null && rs.next()) {                   
                    newUserID = rs.getInt("ID");
                }
            } finally {
                if (selectStmt != null) {
                    selectStmt.close();
                }
            }
        } catch (SQLException e) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date currentTime = new Date();
            System.out.println(dateFormat.format(currentTime) + " : " + "SQLException Occurred in the punchIn Function in the Database_Driver class. Driver username: " + username);
            System.out.println(e.getMessage());
            System.out.println(e.getSQLState());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date currentTime = new Date();
                    System.out.println(dateFormat.format(currentTime) + " : " + "SQLException Occurred in the punchIn Function closing connection in the Database_Driver class. Driver username " + username);
                    System.out.println(e.getMessage());
                    System.out.println(e.getSQLState());
                }
            }
        }
        return newUserID;
    }
    
    public User createAccount(String username, String pass, String firstname, String lastname) {

        User user = null;
        Connection conn = null;
        try {
            conn = getConnection();
            String select = "insert into Users(userName, password, firstName, lastName, authToken) values(?, ?, ?, ?, ?);";
            PreparedStatement selectStmt = null;

            try {
                String authToken = createRadomString();                
                selectStmt = conn.prepareStatement(select);
                selectStmt.setString(1, username);
                selectStmt.setString(2, pass);
                selectStmt.setString(3, firstname);
                selectStmt.setString(4, lastname);
                selectStmt.setString(5, authToken);                
                selectStmt.executeUpdate();
                int newUserID = verifyIfAcountCreated(username, pass);
                
                if(newUserID != 0) user = new User(username, firstname, lastname, authToken, newUserID);            
                
            } finally {
                if (selectStmt != null) {
                    selectStmt.close();
                }
            }
        } catch (SQLException e) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date currentTime = new Date();
            System.out.println(dateFormat.format(currentTime) + " : " + "SQLException Occurred in the punchIn Function in the Database_Driver class. Driver username: " + username);
            System.out.println(e.getMessage());
            System.out.println(e.getSQLState());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date currentTime = new Date();
                    System.out.println(dateFormat.format(currentTime) + " : " + "SQLException Occurred in the punchIn Function closing connection in the Database_Driver class. Driver username " + username);
                    System.out.println(e.getMessage());
                    System.out.println(e.getSQLState());
                }
            }
        }
        return user;
    }

    public boolean checkIfDoesNotExist(int locationID) {
       
        boolean successful = true;
            Connection conn = null;
            try {
                conn = getConnection();

                String select = "select ID from Locations where ID = ?;";
                PreparedStatement selectStmt = null;

                try {
                    selectStmt = conn.prepareStatement(select);
                    selectStmt.setDouble(1, locationID);
                    ResultSet rs = selectStmt.executeQuery();
                if (rs != null && rs.next()) {
                    successful = false;
                }
                } finally {
                    if (selectStmt != null) {
                        selectStmt.close();
                    }
                }
            } catch (SQLException e) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date currentTime = new Date();
                System.out.println(dateFormat.format(currentTime) + " : " + "SQLException Occurred in the punchIn Function in the Database_Driver class. Driver username: " + username);
                System.out.println(e.getMessage());
                System.out.println(e.getSQLState());
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date currentTime = new Date();
                        System.out.println(dateFormat.format(currentTime) + " : " + "SQLException Occurred in the punchIn Function closing connection in the Database_Driver class. Driver username " + username);
                        System.out.println(e.getMessage());
                        System.out.println(e.getSQLState());
                    }
                }
            }
            return successful;
        
    }
    
    public String deleteLocation(int userID, int locationID, String authToken) {
        
        String succesful ="ERROR";
        if(verifyLogInStatus(userID, authToken)){
           // if(checkIfDoesNotExist(locationID))return false;
            Connection conn = null;
            try {
                conn = getConnection();

                String select = "delete from Locations where ID = ? and userID = ?;";
                PreparedStatement selectStmt = null;

                try {
                    selectStmt = conn.prepareStatement(select);
                    selectStmt.setInt(1, locationID);
                    selectStmt.setInt(2,userID);
                    selectStmt.executeUpdate();
                    succesful = "OK";
                   // if(!checkIfDoesNotExist(locationID)) succesful = false;

                } finally {
                    if (selectStmt != null) {
                        selectStmt.close();
                    }
                }
            } catch (SQLException e) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date currentTime = new Date();
                System.out.println(dateFormat.format(currentTime) + " : " + "SQLException Occurred in the punchIn Function in the Database_Driver class. Driver username: " + username);
                System.out.println(e.getMessage());
                System.out.println(e.getSQLState());
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date currentTime = new Date();
                        System.out.println(dateFormat.format(currentTime) + " : " + "SQLException Occurred in the punchIn Function closing connection in the Database_Driver class. Driver username " + username);
                        System.out.println(e.getMessage());
                        System.out.println(e.getSQLState());
                    }
                }
            }
            return succesful; 
        }
        else if(authTokenMismatch(userID)){
            deleteToken(userID);
            return "TOKENCLEARED";
        }
        return "ERROR";
    }

    public String setAuthToken(int UserID) {

        String RandomToken = null;
        Connection conn = null;
        try {
            conn = getConnection();

            String select = "update Users set authToken = ? where ID = ?;";
            PreparedStatement selectStmt = null;

            try {
                RandomToken = createRadomString();
                selectStmt = conn.prepareStatement(select);                           
                selectStmt.setString(1,RandomToken);
                selectStmt.setInt(2, UserID);
                selectStmt.executeUpdate();
            } finally {
                if (selectStmt != null) {
                    selectStmt.close();
                }
            }
        } catch (SQLException e) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date currentTime = new Date();
            System.out.println(dateFormat.format(currentTime) + " : " + "SQLException Occurred in the punchIn Function in the Database_Driver class. Driver username: " + username);
            System.out.println(e.getMessage());
            System.out.println(e.getSQLState());
            RandomToken = null;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date currentTime = new Date();
                    System.out.println(dateFormat.format(currentTime) + " : " + "SQLException Occurred in the punchIn Function closing connection in the Database_Driver class. Driver username " + username);
                    System.out.println(e.getMessage());
                    System.out.println(e.getSQLState());
                }
            }
        }
        return RandomToken;
    }
    
    private String createRadomString(){
                //start building a random token
                StringBuilder tmp = new StringBuilder();
                for (char ch = '0'; ch <= '9'; ++ch)
                    tmp.append(ch);
                for (char ch = 'a'; ch <= 'z'; ++ch)
                    tmp.append(ch);
                char[] symbols = tmp.toString().toCharArray();
                char[] token = new char[30];
                Random  random  = new Random();
                for(int i=0; i < token.length; i++){
                    token[i] = symbols[random.nextInt(symbols.length)];
                }
                return new String(token);
    }
    
    public boolean verifyLogInStatus(int userID, String authToken) {

        boolean success = false;
        Connection conn = null;
        try {
            conn = getConnection();

            String select = "Select userName from Users where ID = ? and authToken = ?;";
            PreparedStatement selectStmt = null;

            try {
                selectStmt = conn.prepareStatement(select);
                selectStmt.setInt(1, userID);
                selectStmt.setString(2, authToken);
                ResultSet rs = selectStmt.executeQuery();
                if (rs != null && rs.next()) {
                    success = true;
                }
            } finally {
                if (selectStmt != null) {
                    selectStmt.close();
                }
            }
        } catch (SQLException e) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date currentTime = new Date();
            System.out.println(dateFormat.format(currentTime) + " : " + "SQLException Occurred in the punchIn Function in the Database_Driver class. Driver username: " + username);
            System.out.println(e.getMessage());
            System.out.println(e.getSQLState());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date currentTime = new Date();
                    System.out.println(dateFormat.format(currentTime) + " : " + "SQLException Occurred in the punchIn Function closing connection in the Database_Driver class. Driver username " + username);
                    System.out.println(e.getMessage());
                    System.out.println(e.getSQLState());
                }
            }
        }
        return success;
    }
    
    public String logout(int userID, String token) {

        String successful = "ERROR";
        if (verifyLogInStatus(userID, token)){
            Connection conn = null;
            try {
                conn = getConnection();

                String select = "update Users set authToken = '' where ID = ?;";
                PreparedStatement selectStmt = null;

                try {
                    selectStmt = conn.prepareStatement(select);
                    selectStmt.setInt(1, userID);
                    selectStmt.executeUpdate();
                    successful = "OK";
                    
                } finally {
                    if (selectStmt != null) {
                        selectStmt.close();
                    }
                }
            } catch (SQLException e) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date currentTime = new Date();
                System.out.println(dateFormat.format(currentTime) + " : " + "SQLException Occurred in the punchIn Function in the Database_Driver class. Driver username: " + username);
                System.out.println(e.getMessage());
                System.out.println(e.getSQLState());
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date currentTime = new Date();
                        System.out.println(dateFormat.format(currentTime) + " : " + "SQLException Occurred in the punchIn Function closing connection in the Database_Driver class. Driver username " + username);
                        System.out.println(e.getMessage());
                        System.out.println(e.getSQLState());
                    }
                }
            }
            return successful;
        }
        else if(authTokenMismatch(userID)){
            deleteToken(userID);
            return "TOKENCLEARED";
        }
        return "ERROR";
    }
    
    public boolean authTokenMismatch(int id) {
       
       boolean mismatch = false;  
        Connection conn = null;
        try {
            conn = getConnection();

            String select = "SELECT ID, authToken from Users WHERE ID = ?;";
            PreparedStatement selectStmt = null;

            try {
                selectStmt = conn.prepareStatement(select);
                selectStmt.setInt(1, id);
                ResultSet rs = selectStmt.executeQuery();
                if (rs != null && rs.next()) {
                    if(rs.getInt("ID")== id){
                        mismatch = true;
                    }
                }
            } finally {
                if (selectStmt != null) {
                    selectStmt.close();
                }
            }
        } catch (SQLException e) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date currentTime = new Date();
            System.out.println(dateFormat.format(currentTime) + " : " + "SQLException Occurred in the punchIn Function in the Database_Driver class. Driver username: " + username);
            System.out.println(e.getMessage());
            System.out.println(e.getSQLState());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date currentTime = new Date();
                    System.out.println(dateFormat.format(currentTime) + " : " + "SQLException Occurred in the punchIn Function closing connection in the Database_Driver class. Driver username " + username);
                    System.out.println(e.getMessage());
                    System.out.println(e.getSQLState());
                }
            }
        }
        return mismatch;
    }
    
    public void deleteToken(int id) {
       
        Connection conn = null;
        try {
            conn = getConnection();

            String select = "UPDATE Users Set authToken = \"\" Where ID = ?;";
            PreparedStatement selectStmt = null;

            try {
                selectStmt = conn.prepareStatement(select);
                selectStmt.setInt(1, id);
                selectStmt.executeUpdate();
              
            } finally {
                if (selectStmt != null) {
                    selectStmt.close();
                }
            }
        } catch (SQLException e) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date currentTime = new Date();
            System.out.println(dateFormat.format(currentTime) + " : " + "SQLException Occurred in the punchIn Function in the Database_Driver class. Driver username: " + username);
            System.out.println(e.getMessage());
            System.out.println(e.getSQLState());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date currentTime = new Date();
                    System.out.println(dateFormat.format(currentTime) + " : " + "SQLException Occurred in the punchIn Function closing connection in the Database_Driver class. Driver username " + username);
                    System.out.println(e.getMessage());
                    System.out.println(e.getSQLState());
                }
            }
        }
    }
}

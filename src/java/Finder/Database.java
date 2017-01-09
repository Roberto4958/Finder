/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Finder;

import DataModel.Location;
import DataModel.User;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
 * This class is responsible for creating a database connection and modifying the database based on the request.
 * 
 * @author Roberto Aguilar
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
    
    //@desc: makes a connection with the database
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(connString, username, password);
    }
    
    /**
     * @desc: Logs in user
     * @param: userName - users userName, password - users password
     * @returns: If both user name and password are correct returns user object. 
     * If username or password do not match to a user returns a user with null values and a id of of -1
     */
    public User LogIn(String userName, String password) {
        User user = null;
        Connection conn = null;
        String salt = getSalt(userName);
        password = hashString(password,salt);
        
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
                else user = new User(null, null, null, null,-1);
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
    
    /*
    * @param: userName: user name of current user
    * @desc: this method is used to get the password = password+salt
    * @return: the salt of user
    */
    private String getSalt(String userName) {

        String salt = null;
        Connection conn = null;
        try {
            conn = getConnection();

            String select = "select Salt from Users where username = ?;";
            PreparedStatement selectStmt = null;

            try {
                selectStmt = conn.prepareStatement(select);
                selectStmt.setString(1, userName);
                ResultSet rs = selectStmt.executeQuery();
                if (rs != null && rs.next()) {                   
                    salt = rs.getString("Salt");
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
        System.out.println(salt);
        return salt;
    }

    /**
     * @desc: Finds users last location
     * @param: userID - users ID, authToken - users authentication token
     * @return: If everything goes well this returns users last location object.
     * If user does not have any locations it returns a null location object with id = -1.
     * If the authToken does not match the ID it returns a null location object with id = -3
     */
    public Location findLocation(int userID, String authToken) {
        
        if(checkIfMatchToUser(userID, authToken)){                      
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
        else if(userWithIDExist(userID)){
            deleteToken(userID);
            return new Location(null, 0, 0, -3);
        }
        else return null;
    }

    /**
     * @desc: Adds users location to the database
     * @param: userID - users ID, place - locations name, latitude - users latitude, longitude - users longitude, authToken - users authentication token
     * @return: If the location is added to the database then it returns "OK"
     * If the authTeoken does not match the id then it returns "TOKENCLEARED"
     * else it returns "ERROR"
     */
    public String addNewLocation(int userID, String place, double latitude, double longitude, String authToken) {
       
        String successful = "ERROR";
        if(checkIfMatchToUser(userID, authToken)){
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
        else if(userWithIDExist(userID)){
            deleteToken(userID);
            return "TOKENCLEARED";
        }
       else return "ERROR";
    }

    /**
     * @desc: Gets all of users locations 
     * @param: userID - user ID, authToken - users authentication token
     * @return: If all conditions are met returns a ArrayList of location objects
     * If authtoken does not match ID the it returns a ArrayList with only one location object with the ID of -3
     * If something goes wrong trying to get the Locations then it returns null
     */
    public ArrayList<Location> getHistory(int userID, String authToken) {
        
        if(checkIfMatchToUser(userID, authToken)){
            ArrayList<Location> history = null;
            Connection conn = null;
            try {
                conn = getConnection();
                
                String select = "select ID, place, latitude, longitude from Locations where userID =? order by ID DESC;";
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
        else if(userWithIDExist(userID)){
            deleteToken(userID);
            ArrayList<Location> collection = new ArrayList<Location>();
            collection.add(new Location(null, 0, 0, -3));
            return collection;
        }
        else return null;
    }
    
    /**
     * @desc: checks if there is already a account with the username. 
     * @param: userName - users name
     * @return: If there is a account that already exist then it returns the ID of user.
     * If the account does not exist then it return 0.
     */
    private int verifyIfAcountExsist(String userName) {

        int newUserID = 0;
        Connection conn = null;
        try {
            conn = getConnection();

            String select = "select ID from Users where userName = ?;";
            PreparedStatement selectStmt = null;

            try {
                selectStmt = conn.prepareStatement(select);
                selectStmt.setString(1, userName);
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
    
    /**
     * @desc: Creates a account and stores it in the database.
     * @param: username - users userName, pass - users password, firstname - users first name, lastname - users last name
     * @return: If users account is stored to the database successfully then it returns a user object.
     * If there is already a user with the same userName of username then it returns a user object with null values and id = -1
     * If something goes wrong trying to put the users account to the database it returns null.
     */
    public User createAccount(String username, String pass, String firstname, String lastname) {

        
        String salt = createRadomString(20);
        System.out.println("pass: "+ pass + ", salt: "+ salt);
        pass = hashString(pass, salt);

        if(pass == null) return null;
                
        if(verifyIfAcountExsist(username) != 0){
            return new User(null, null, null, null, -1);
        }
        User user = null;
        Connection conn = null;
        try {
            conn = getConnection();
            String select = "insert into Users(userName, password, firstName, lastName, authToken, Salt) values(?, ?, ?, ?, ?, ?);";
            PreparedStatement selectStmt = null;

            try {
                String authToken = createRadomString(30);                
                selectStmt = conn.prepareStatement(select);
                selectStmt.setString(1, username);
                selectStmt.setString(2, pass);
                selectStmt.setString(3, firstname);
                selectStmt.setString(4, lastname);
                selectStmt.setString(5, authToken);  
                selectStmt.setString(6, salt);
                selectStmt.executeUpdate();
                int newUserID = verifyIfAcountExsist(username);
                
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
    
    /**
     * @desc: Deletes users location
     * @param: userID - users ID, locationID - location id that user whats to delete, authToken - users authentication Token
     * @return: If users location is deleted it returns "OK"
     * If authToken does not match userID then returns "TOKENCLEARED"
     * If something goes wrong trying to delete location it returns "ERROR". 
     */
    public String deleteLocation(int userID, int locationID, String authToken) {
        
        String succesful ="ERROR";
        if(checkIfMatchToUser(userID, authToken)){
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
        else if(userWithIDExist(userID)){
            deleteToken(userID);
            return "TOKENCLEARED";
        }
        return "ERROR";
    }

    /**
     * @desc: Sets a authentication token for the user and updates the database to have the token. 
     * @param UserID - users ID
     * @return If successfully updates the database to have the token then it returns token.
     * If not successful in updating the database then returns null. 
     */
    private String setAuthToken(int UserID) {

        String RandomToken = null;
        Connection conn = null;
        try {
            conn = getConnection();

            String select = "update Users set authToken = ? where ID = ?;";
            PreparedStatement selectStmt = null;

            try {
                RandomToken = createRadomString(30);
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
    
    /**
     * @desc: Constructs a string of random 30 characters. The String only has numbers and letters.  
     * @return: A random String
     */
    private String createRadomString(int length){
                //start building a random token
                StringBuilder tmp = new StringBuilder();
                for (char ch = '0'; ch <= '9'; ++ch)
                    tmp.append(ch);
                for (char ch = 'a'; ch <= 'z'; ++ch)
                    tmp.append(ch);
                char[] symbols = tmp.toString().toCharArray();
                char[] token = new char[length];
                Random  random  = new Random();
                for(int i=0; i < token.length; i++){
                    token[i] = symbols[random.nextInt(symbols.length)];
                }
                return new String(token);
    }
    
    /**
     * @desc: Checks if userID and AuthToken match to a user 
     * @param: userID - users ID, authToken - users authentication token
     * @return: If userID and AuthToken match to a user returns true and if they do not it returns false
     */
    private boolean checkIfMatchToUser(int userID, String authToken) {

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
    
    /**
     * @desc: Logs out the user by clearing their authentication token.
     * @param: userID, AuthToken - users authentication token
     * @return: If token is cleared it returns "OK"
     * If AuthToken and userID do not match it returns "TOKENCLEARED"
     * If something goes wrong trying to delete token it returns "ERROR"
     */
    public String logout(int userID, String AuthToken) {

        String successful = "ERROR";
        if (checkIfMatchToUser(userID, AuthToken)){
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
        else if(userWithIDExist(userID)){
            deleteToken(userID);
            return "TOKENCLEARED";
        }
        return "ERROR";
    }
    
    /**
     * @desc: Checks if user with the ID of userID exist 
     * @param: userID - users id
     * @return: If the user exist then it returns true, and if the user does not exist it return false
     */
    private boolean userWithIDExist(int userID) {
       
       boolean exist = false;  
        Connection conn = null;
        try {
            conn = getConnection();

            String select = "SELECT ID, authToken from Users WHERE ID = ?;";
            PreparedStatement selectStmt = null;

            try {
                selectStmt = conn.prepareStatement(select);
                selectStmt.setInt(1, userID);
                ResultSet rs = selectStmt.executeQuery();
                if (rs != null && rs.next()) {
                    if(rs.getInt("ID")== userID){
                        exist = true;
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
        return exist;
    }
    
    /**
     * @desc: Clears users authToken from the database.
     * @param userID - users id
     */
    private void deleteToken(int userID) {
       
        Connection conn = null;
        try {
            conn = getConnection();

            String select = "UPDATE Users Set authToken = \"\" Where ID = ?;";
            PreparedStatement selectStmt = null;

            try {
                selectStmt = conn.prepareStatement(select);
                selectStmt.setInt(1, userID);
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
    
    /*
    * @param: pass - password of user
    * @param: salt - random generated string of length 20
    * @return: a hash of pass + salt 
    */
    private String hashString(String pass, String salt){
        
        try {
            pass = pass + salt;
            
            MessageDigest md = MessageDigest.getInstance("SHA1");
            md.update(pass.getBytes());
            byte[] b = md.digest();
            StringBuffer sb = new StringBuffer();
            for(byte b1: b){
		sb.append(Integer.toHexString(b1 & 0Xff).toString());
            }
            return sb.toString();
			
	} catch (NoSuchAlgorithmException e) {
            return null;
	}
    }
}

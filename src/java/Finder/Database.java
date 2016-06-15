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
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            hostname = System.getProperty("RDS_HOSTNAME");
            port = System.getProperty("RDS_PORT");
            databaseName = System.getProperty("RDS_DB_NAME");
            username = System.getProperty("RDS_USERNAME");
            password = System.getProperty("RDS_PASSWORD");           
            connString = connString + hostname + ":" + port + "/" + databaseName;
            
        } catch (Exception e) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date currentTime = new Date();
            System.out.println(dateFormat.format(currentTime) + " : " + "Error connecting to database:");
            System.out.println(e.getMessage());
        }
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

            String select = "select userName, password, firstName, lastName, ID from Users where userName = ? and password = ?;";
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

                String select = "select l.latitude, l.longitude, l.ID from Locations l, Users u where u.ID =? and l.userID = u.ID order by l.ID desc limit 1;";
                PreparedStatement selectStmt = null;

                try {
                    selectStmt = conn.prepareStatement(select);
                    selectStmt.setInt(1, userID);
                    ResultSet rs = selectStmt.executeQuery();
                    if (rs != null && rs.next()) {
                        location = new Location( rs.getDouble("latitude"),rs.getDouble("longitude"), rs.getInt("ID"));
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
            return location;
        }
        else return null;
    }

    public boolean addNewLocation(int userID, double latitude, double longitude, String authToken) {
       
        boolean successful = false;
        if(verifyLogInStatus(userID, authToken)){
            Connection conn = null;
            try {
                conn = getConnection();

                String select = "insert into Locations(latitude, longitude, userID)values(?, ?, ?);";
                PreparedStatement selectStmt = null;

                try {
                    selectStmt = conn.prepareStatement(select);
                    selectStmt.setDouble(1, latitude);
                    selectStmt.setDouble(2, longitude);
                    selectStmt.setInt(3, userID);

                    selectStmt.executeUpdate();
                    successful =true;

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
        return false;
    }

    public ArrayList<Location> getHistory(int userID, String authToken) {
        
        if(verifyLogInStatus(userID, authToken)){
            ArrayList<Location> history = null;
            Connection conn = null;
            try {
                conn = getConnection();
                
                String select = "select ID, latitude, longitude from Locations where userID =?;";
                PreparedStatement selectStmt = null;

                try {
                    selectStmt = conn.prepareStatement(select);
                    selectStmt.setInt(1, userID);
                    ResultSet rs = selectStmt.executeQuery();
                    history = new ArrayList<Location>();
                    while (rs != null && rs.next()) {
                        
                        Location l = new Location(rs.getDouble("latitude"), rs.getDouble("longitude"), rs.getInt("ID"));
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

    public boolean verifyDeletion(int locationID) {
       
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
    
    public boolean deleteLocation(int userID, int locationID, String authToken) {
        
        boolean succesful =false;
        if(verifyLogInStatus(userID, authToken)){
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
                    succesful = true;
                    if(!verifyDeletion(locationID)) succesful = false;

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
        return false;
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
    
    public boolean logout(int userID, String token) {

        boolean successful = false;
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
                    successful = true;
                    
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
        return false;
    }
}

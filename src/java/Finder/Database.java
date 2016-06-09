/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Finder;

import java.sql.Connection;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Random;

/**
 *
 * @author cancola
 */
public class Database {

    private static final String connString = "jdbc:mysql://localhost:3306/Finder"; // MySQL database
    private static final String username = "root"; // MySQL username
    private static final String password = "root"; // MySQL password

    public Database() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
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

    public boolean LogIn(String userName, String password) {
        boolean succesful = false;
        Connection conn = null;
        try {
            conn = getConnection();

            String select = "select * from Users where userName = ? and password = ?;";
            PreparedStatement selectStmt = null;

            try {
                selectStmt = conn.prepareStatement(select);
                selectStmt.setString(1, userName);
                selectStmt.setString(2, password);
                ResultSet rs = selectStmt.executeQuery();
                if (rs != null && rs.next()) {
                    succesful = true;
                    setAuthToken(rs.getInt("ID"));
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
        return succesful;
    }

    public int findLastID(int userID) {
        int ID = 0;
        Connection conn = null;
        try {
            conn = getConnection();

            String select = "select max(ID) from Locations where  UserID = ?;";
            PreparedStatement selectStmt = null;

            try {
                selectStmt = conn.prepareStatement(select);
                selectStmt.setInt(1, userID);
                ResultSet rs = selectStmt.executeQuery();
                if (rs != null && rs.next()) {
                    ID = rs.getInt("max(ID)");
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
        return ID;
    }

    public String findLocation(int userID, String authToken) {
        
        if(verifyLogInStatus(userID, authToken)){
            
            int highestLocationID = findLastID(userID);
            String location = "Your last location was ";
            Connection conn = null;
            try {
                conn = getConnection();

                String select = "select latitude, longitude from Locations where ID = ?;";
                PreparedStatement selectStmt = null;

                try {
                    selectStmt = conn.prepareStatement(select);
                    selectStmt.setInt(1, highestLocationID);
                    ResultSet rs = selectStmt.executeQuery();
                    if (rs != null && rs.next()) {
                        location = "Your location is " + rs.getDouble("latitude") + ", " + rs.getDouble("longitude");
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
        else return "Invalide authToken";
    }

    public String sendLocation(int userID, double latitude, double longitude, String authToken) {
       
        String messege = "You Successfully sent your location";
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
                messege = "Sorry something went wrong";
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
            return messege;
        }
        return "Invalide authToken";
    }

    public String getHistory(int userID, String authToken) {
        
        if(verifyLogInStatus(userID, authToken)){
            String history = "Here is your history \n";
            Connection conn = null;
            try {
                conn = getConnection();

                String select = "select latitude, longitude from Locations where userID =?;";
                PreparedStatement selectStmt = null;

                try {
                    selectStmt = conn.prepareStatement(select);
                    selectStmt.setInt(1, userID);
                    ResultSet rs = selectStmt.executeQuery();
                    while (rs != null && rs.next()) {
                        history += rs.getString("latitude") + ", " + rs.getString("longitude") + "\n";
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
        else return "Sorry invalide authToken";
    }
    
    public String createAccount(String username, String pass, String firstname, String lastname) {

        String messege = "you successfully created a account";
        Connection conn = null;
        try {
            conn = getConnection();

            String select = "insert into Users(userName, password, firstName, lastName, authToken) values(?, ?, ?, ?, '');";
            PreparedStatement selectStmt = null;

            try {
                selectStmt = conn.prepareStatement(select);
                selectStmt.setString(1, username);
                selectStmt.setString(2, pass);
                selectStmt.setString(3, firstname);
                selectStmt.setString(4, lastname);
                
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
            messege = "Sorry something went wrong";
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
        return messege;
    }

    public String deleteLocation(int userID, int locationID, String authToken) {
        
        String messege ="Last location was successfully deleted";
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
                messege = "Sorry something went wrong";
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
            return messege; 
        }
        return "invalid authToken";
    }

    public String setAuthToken(int UserID) {

        
        Connection conn = null;
        try {
            conn = getConnection();

            String select = "update Users set authToken = ? where ID = ?;";
            PreparedStatement selectStmt = null;

            try {
                selectStmt = conn.prepareStatement(select);
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
                String RandomToken = new String(token);
                  
                selectStmt.setString(1, RandomToken);
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
    
    public boolean verifyLogInStatus(int userID, String authToken) {

        boolean success = false;
        Connection conn = null;
        try {
            conn = getConnection();

            String select = "Select * from Users where ID = ? and authToken = ?;";
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

        String messege = "you succesfully loged out";
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
                messege = "Sorry something went wrong";
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
            return messege;
        }
        return "invalid authToken";
    }
}

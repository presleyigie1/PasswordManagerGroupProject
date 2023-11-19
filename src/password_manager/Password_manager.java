/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package password_manager;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import javax.swing.JOptionPane;

/**
 *
 * @author presl
 */
//https://www.youtube.com/watch?v=T0P-cfsD45o
//https://www.youtube.com/watch?v=YD-uDuFrf8s
public class Password_manager {
    
    private static String currentUser;
    private static int userId;
    
    
    /**
     * @param args the command line arguments
     * @return 
     */
    //figured out way to use less code and to essentially use this once instead of pasting it under ever class
    //caused errors along the way
    /*public Password_manager(){
        Properties properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream("database.properties")) {
            properties.load(fileInputStream);
        } 
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load database properties");
  
        }

        // Retrieve properties - stashed to secure from threats
        String dbUrl = properties.getProperty("dbUrl");
        String dbUser = properties.getProperty("dbUser");
        String dbPassword = properties.getProperty("dbPassword"); 
        
        String[] userInfo = null;
        String selectQuery = "SELECT password, security_key FROM users WHERE username = ?";
        
    }*/
    
    //account resgistration
    public static boolean insertNewUserIntoDB(int genId ,String username, String passwordHashed, String securityKeyHashed){
        
        Properties properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream("database.properties")) {
            properties.load(fileInputStream);
        } 
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load database properties");
  
        }

        // Retrieve properties - stashed to secure from threats
        String dbUrl = properties.getProperty("dbUrl");
        String dbUser = properties.getProperty("dbUser");
        String dbPassword = properties.getProperty("dbPassword"); 
        
        String insertQuery = "INSERT INTO users (user_id ,username, password, security_key) VALUES (?, ?, ?, ?)";

        //making to the database
        try(Connection dbConnection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            PreparedStatement preparedStatement = dbConnection.prepareStatement(insertQuery)){
            
            //inserting users into their specified columns with a user ID generated - used to be in order of who created account first e.g 1,2,3 - logic was changed for it to be randomized
            preparedStatement.setInt(1, genId);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, passwordHashed);
            preparedStatement.setString(4, securityKeyHashed);
            
            //executing the sql statement
            int rowsAffected = preparedStatement.executeUpdate();
            
            
            return rowsAffected > 0;
        }
        
        catch(SQLException e){
            e.printStackTrace();
            //return false within the exception because the application failed to insert a new user
            return false;
        }

    }
    
    //verify  user, show their id to able to add password properly
    public static boolean verifyUser(String username, String password){
            Properties properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream("database.properties")) {
            properties.load(fileInputStream);
        } 
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load database properties");
  
        }

        // Retrieve properties - stashed to secure from threats
        String dbUrl = properties.getProperty("dbUrl");
        String dbUser = properties.getProperty("dbUser");
        String dbPassword = properties.getProperty("dbPassword"); 
        
        String selectQuery = "SELECT user_id FROM users WHERE username = ? AND password = ?";
        try(Connection dbConnection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                PreparedStatement prepStatement = dbConnection.prepareStatement(selectQuery)){
            
            prepStatement.setString(1, username);
            prepStatement.setString(2,password);
            
            ResultSet resultSet = prepStatement.executeQuery();
            
            if(resultSet.next()){
                userId = resultSet.getInt("user_id");
                return true;
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    
    //get the current user id
    //error thatv was returning 0 for userId which caused an SQL error of cannot find or update child row from foreign key, this si because it was settting the userId at 0 instead of the associated with username
   /* public static int getCurrentUserId() {
        return userId;
    }*/

    //previously i was having difficulty in getting the user ID, i was trying to get itby creating a new instance of HomeGUI bvut was having difficulty
    //after 7 hours of troubleshooting, came to conclusion to try and get the UserId based on the username, did this and userId was not returning 0 anymiore and credentials could be added
    public static int getUserIdByUsername(String username) {
        Properties properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream("database.properties")) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load database properties");
        }

        String dbUrl = properties.getProperty("dbUrl");
        String dbUser = properties.getProperty("dbUser");
        String dbPassword = properties.getProperty("dbPassword");

        String selectQuery = "SELECT user_id FROM users WHERE username = ?";
        try (Connection dbConnection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement prepStatement = dbConnection.prepareStatement(selectQuery)) {

            prepStatement.setString(1, username);

            ResultSet resultSet = prepStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("user_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    
    
    //login to the password manager
    public static String[] findUsername(String username){
         Properties properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream("database.properties")) {
            properties.load(fileInputStream);
        } 
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load database properties");
  
        }

        // Retrieve properties - stashed to secure from threats
        String dbUrl = properties.getProperty("dbUrl");
        String dbUser = properties.getProperty("dbUser");
        String dbPassword = properties.getProperty("dbPassword"); 
        
        String[] userInfo = null;
        String selectQuery = "SELECT password, security_key FROM users WHERE username = ?";
        
        try(Connection dbConnection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            PreparedStatement preparedStatement = dbConnection.prepareStatement(selectQuery)){
                    preparedStatement.setString(1, username);
                    
                    //execute statement
                    //conatins the result of executed query
                    ResultSet resultSet = preparedStatement.executeQuery();
                    
                    //so if the user is found, get the hashed password and sec key from the database
                    if(resultSet.next()){
                       String passwordHashed = resultSet.getString("password");
                       String securityKeyHashed =resultSet.getString("security_key");
                       
                       userInfo = new String[]{passwordHashed, securityKeyHashed};
                    }
                }catch (SQLException e ){
                        e.printStackTrace();
                        }
        return userInfo;
    }
    
    //addingn to password manager
    public static boolean addCredentials(int userId, String siteHashed, String hashedStoredPassword){
        //access the properties file for database login credentials
        Properties properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream("database.properties")) {
            properties.load(fileInputStream);
        } 
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load database properties");
  
        }

        // Retrieve properties - stashed to secure from threats
        String dbUrl = properties.getProperty("dbUrl");
        String dbUser = properties.getProperty("dbUser");
        String dbPassword = properties.getProperty("dbPassword"); 
        
       
        
        ///creating sql inset query, will add site and stored passwords for user thats logged in via user id
        String addQuery = "INSERT INTO credentials (user_id, site, stored_password) VALUES (?, ?, ?)";
        
        try(Connection dbConnection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                PreparedStatement prepStatement = dbConnection.prepareStatement(addQuery)){
            prepStatement.setInt(1,userId);
            
            
            //so it is allowed to be null, was casuing errors before because it was not defined
            if(siteHashed == null){
            prepStatement.setNull(2, java.sql.Types.VARCHAR);
            }
            else{
                prepStatement.setString(2,siteHashed);
            }
              
            if(hashedStoredPassword == null){
                prepStatement.setNull(3, java.sql.Types.VARCHAR);
            }
            else{
            prepStatement.setString(3, hashedStoredPassword);
            }
            
            int rowsAffected = prepStatement.executeUpdate();
            
            return rowsAffected > 0;
        }
        catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean showCredentials(int userId){
        Properties properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream("database.properties")) {
            properties.load(fileInputStream);
        } 
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load database properties");
  
        }

        // Retrieve properties - stashed to secure from threats
        String dbUrl = properties.getProperty("dbUrl");
        String dbUser = properties.getProperty("dbUser");
        String dbPassword = properties.getProperty("dbPassword");
        
        String selectQuery = "SELECT site, stored_password FROM credentials WHERE user_id = ?";
        
        try(Connection dbConnection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                PreparedStatement prepStatement = dbConnection.prepareStatement(selectQuery)){
            prepStatement.setInt(1,userId);
            
            //will execute the query
            ResultSet resultSet = prepStatement.executeQuery();
            
            StringBuilder showCredentials = new StringBuilder();
            
                while(resultSet.next()){
                    String site = resultSet.getString("site");
                    String storedPassword = resultSet.getString("stored_password");
                    
                    
                    
                    //display site and drop a new line so that the password can be dsiplayed
                    showCredentials.append("Site: ").append(site).append("/n");
                    showCredentials.append("Password: ").append(storedPassword).append("/n");

                }
                if(showCredentials.length() > 0){
                    JOptionPane.showMessageDialog(null, showCredentials.toString());
                    return true;
                }
                else{
                    JOptionPane.showMessageDialog(null, "You haven't addded any passwords");
                    return false;
                }
                
        } catch(SQLException e){
                    e.printStackTrace();
                    return false;              
        }
                    
    }
     
    public static void main(String[] args) {
        
        
    /*
        //teesting to see if database is connectyed properly
        
            Properties properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream("database.properties")) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load database properties");
            return;
        }

        // Retrieve properties - stashed to secure from threats
        String dbUrl = properties.getProperty("dbUrl");
        String dbUser = properties.getProperty("dbUser");
        String dbPassword = properties.getProperty("dbPassword");
        
        
        //creating the code to connect with the mySql database using a jbdc driver
        System.out.println("Connecting to the database...");
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users");
            
             //testing to see if table user id and username shows to nsure databse is connected
            while (resultSet.next()) {
                System.out.println(resultSet.getInt(1) + " " + resultSet.getString(2) + " "+ resultSet.getString(3) +" "+ resultSet.getInt(4));
            }
             System.out.flush();
            
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Connection to the database failed");
        }
        
        */
        
        // TODO code application logic here
        //making the GUI's objects so visibility can be set
        LoginGUI loginGUI = new LoginGUI();
        RegisterGUI registerGUI = new RegisterGUI();
        //this will basically allow me to use a selected HomeGUi if the username is required
        HomeGUI homeGUI = new HomeGUI();
        HomeGUI homeGUIUser  = new HomeGUI(currentUser);
       // HomeGUI homeGUIUserId = new HomeGUI(userId);
        
        
        //setting the visibility
        //this logic so that when the application is run you are brought to login
        loginGUI.setVisible(true);
        registerGUI.setVisible(false);
        homeGUI.setVisible(false);
        homeGUIUser.setVisible(false);
        //homeGUIUserId.setVisible(false);
        
        
        
        
    }
}

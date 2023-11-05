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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 *
 * @author presl
 */
public class Password_manager {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
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
        
        
        // TODO code application logic here
        //making the GUI's objects so visibility can be set
        LoginGUI loginGUI = new LoginGUI();
        RegisterGUI registerGUI = new RegisterGUI();
        HomeGUI HomeGUI = new HomeGUI();
        
        //setting the visibility
        //this logic so that when the application is run you are brought to login
        loginGUI.setVisible(true);
        registerGUI.setVisible(false);
        HomeGUI.setVisible(false);
        
        
    }

}

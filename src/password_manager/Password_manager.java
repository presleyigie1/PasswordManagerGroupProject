/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package password_manager;

/**
 *
 * @author presl
 */
public class Password_manager {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
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

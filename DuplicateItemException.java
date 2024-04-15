/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package giuseppevitolo;

import javafx.scene.control.Alert;

/**
 *
 * @author giuseppe
 */
public class DuplicateItemException extends RuntimeException{ 


    public DuplicateItemException(){
        super();
        Alert a = new Alert(Alert.AlertType.ERROR); 
        a.setTitle("Error");
        a.setContentText("elemento già presente");
        a.showAndWait();

    } public DuplicateItemException(String s){
        super(s);
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Error");
        a.setContentText("elemento già presente");
        a.showAndWait();
    }

}
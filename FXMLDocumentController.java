/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package giuseppevitolo;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 *
 * @author giuseppe
 */
public class FXMLDocumentController implements Initializable {
    
   @FXML
    private Button btnaggiungititolo;


    @FXML
    private Button btnsblocca;

    @FXML
    private Label lberror;

    @FXML
    private AnchorPane login;

    @FXML
    private MenuItem miesci;

    @FXML
    private MenuItem mipasswordb;

    @FXML
    private MenuItem misalva;

    @FXML
    private AnchorPane page;

    @FXML
    private MenuItem micancellaelemento;


    @FXML
    private MenuItem micopiapassword;

    @FXML
    private MenuItem micopiausername;

    @FXML
    private TableView<Item> tableview;

    @FXML
    private TableColumn<Item, String> tcUtente;

    @FXML
    private TableColumn<Item, String> tcpassword;

    @FXML
    private TableColumn<Item, String > tctitolo;

    @FXML
    private TextField tfnomeutente;

    @FXML
    private TextField tfpassword;

    @FXML
    private PasswordField tfppassword;

    @FXML
    private TextField tftitolo;

    ObservableList<Item> ol; 
    private String password;

    OTPGenerator og;
    Thread t ;

    List<Item> l ;

    @FXML
    void onaconactionmicopiausername(ActionEvent event) {
        Item i = tableview.getSelectionModel().getSelectedItem();
        if(i != null) {
            final Clipboard clipboard = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();
            content.putString(i.getNomeutente());
            clipboard.setContent(content);
        }
    }

    @FXML
    void onactionmicancellaelemento(ActionEvent event) {
        Item i = tableview.getSelectionModel().getSelectedItem();
        ol.remove(i);

    }

    @FXML
    void onactionmicopiapassword(ActionEvent event) {
        Item i = tableview.getSelectionModel().getSelectedItem();
        if(i != null) {
            final Clipboard clipboard = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();
            content.putString(i.getPassword());
            clipboard.setContent(content);
        }

    }

    @FXML
    void onactionaggiungititolo(ActionEvent event) {
        Item i = new Item(tftitolo.getText(),tfnomeutente.getText(),tfpassword.getText());
        if(ol.contains(i)){
            throw new DuplicateItemException("elemento già presente");

        }
        else{
            ol.add(i);
            tftitolo.setText(" ");
            tfnomeutente.setText(" ");
            tfpassword.setText(" ");
           
            
        }

    }
    @FXML
    void btnonaction(ActionEvent event) {
        if(tfppassword.getText().equals(password+og.getR())){
            t.interrupt();
            page.setDisable(false);
            page.setVisible(true);
            login.setVisible(false);
            login.setDisable(true);
        }
        else{
            lberror.setText("Password errata riprovare");
        }

    }

    @FXML
    void onactionmiesci(ActionEvent event) {
        Platform.exit();

    }

    @FXML
    void onactionmipasswordb(ActionEvent event) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.getButtonTypes().add(new ButtonType("annulla"));
        TextField tf = new TextField();
        HBox hb = new HBox(new Label("inserisci password "),tf);
        a.getDialogPane().setContent(hb);
        a.setTitle("Cambia Password");
        a.setHeaderText("Vuoi Cambiare password all'archivio");
        Optional<ButtonType> o = a.showAndWait();
        if(o.get() == ButtonType.OK){
            if(!tf.getText().isEmpty()){
                password = tf.getText();
            }
        }
    }

    @FXML
    void onactionmisalva(ActionEvent event) {
        try(ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("saved.txt")))){
            oos.writeObject(password);
            l = new ArrayList<>(ol);
            oos.writeObject(l);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    @FXML
    void oneditcommitutente(TableColumn.CellEditEvent<Item,String> event) {
        Item i = tableview.getSelectionModel().getSelectedItem();
        String np = event.getNewValue();
        if(!ol.contains(new Item(i.getTitolo(),np,i.getPassword()))){
            i.setNomeutente(np);
            tableview.refresh();
        }
        else{
            i.setNomeutente(event.getOldValue());
            tableview.refresh();
            throw new DuplicateItemException("elemento già presente");

        }
    }

    @FXML
    void oneditcommittcpassword(TableColumn.CellEditEvent<Item,String> event) {
        Item i = tableview.getSelectionModel().getSelectedItem();
        String np = event.getNewValue();
        i.setPassword(np);
        tableview.refresh();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        login.setVisible(true);
        login.setDisable(false);
        page.setVisible(false);
        page.setDisable(true);

        og = new OTPGenerator();
        t = new Thread(og);
        t.start();

        ol = FXCollections.observableArrayList();
        if(new File("saved.txt").exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream("saved.txt")))) {
                password = (String) ois.readObject();
                l = (List<Item>) ois.readObject();
                ol.setAll(l);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        else{
            password = "pass";
        }
        tcpassword.setCellFactory(TextFieldTableCell.forTableColumn());
        tcUtente.setCellFactory(TextFieldTableCell.forTableColumn());
        tcpassword.setCellValueFactory(new PropertyValueFactory<>("Password"));
        tctitolo.setCellValueFactory(new PropertyValueFactory<>("Titolo"));
        tcUtente.setCellValueFactory(new PropertyValueFactory<>("Nomeutente"));

        tableview.setItems(ol);

        btnsblocca.disableProperty().bind(tfppassword.textProperty().isEmpty());
        btnaggiungititolo.disableProperty().bind(tfpassword.textProperty().isEmpty().or(tfnomeutente.textProperty().isEmpty()).or(tftitolo.textProperty().isEmpty()));
       
    }
}

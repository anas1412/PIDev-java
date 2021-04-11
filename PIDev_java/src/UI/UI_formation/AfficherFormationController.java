/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI.UI_formation;


import Entity.Formation;
import Services.FormationService;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.stage.StageStyle;
import javafx.util.Callback;





 



/**
 * FXML Controller class
 *
 * @author User
 */
public class AfficherFormationController implements Initializable {

    


    @FXML
    private Button btn_gotoModifForm;
    
    
    @FXML
    private TableView<Formation> TableFormation;
    
    @FXML
    private TableColumn<Formation, String> Tab_titre_id;

    @FXML
    private TableColumn<Formation, String> tab_url_id;

    @FXML
    private TableColumn<Formation, String> tab_desc_id;

    @FXML
    private TableColumn<Formation, String> tab_domaine_id;

    @FXML
    private TableColumn<Formation, String> tab_gestion_id;

    
     @FXML
    private Button btn_gotoFormations;
    
    ObservableList<Formation>  FormationList = FXCollections.observableArrayList();

    String query = null;
    Connection cnx = null;
    Connection connection = cnx ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    Formation formation = null ;
   
        
    
    
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        loadDate();
       
        
         
        }
        
     
    
    
    
    
    
    
    
    @FXML
        void btn_Refresh_Forma() {

        
         try {
           // FormationList.clean();
            
            query = "SELECT * FROM video";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()){
                FormationList.add(new Formation(resultSet.getString("url"),
                        resultSet.getString("title"),
                        resultSet.getTimestamp("publish_date"),
                        resultSet.getString("description"),
                        resultSet.getString("domaine")));
                
                TableFormation.setItems(FormationList);
                
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(AfficherFormationController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

        
        
   
    
    

    
 
 @FXML
    void  btn_gotoFormations(ActionEvent event) throws IOException {
       
      
        Parent root = FXMLLoader.load(getClass().getResource("/UI/UI_formation/Formations.fxml"));
        Stage Window = (Stage) btn_gotoFormations.getScene().getWindow();
        Window.setScene(new Scene(root));

      
    }
 
 
 
 
 
    @FXML
    void btn_gotoModifierForma(ActionEvent event) throws IOException {
       
      
        Parent root = FXMLLoader.load(getClass().getResource("/UI/UI_formation/ModifierFormation.fxml"));
        Stage Window = (Stage) btn_gotoModifForm.getScene().getWindow();
        Window.setScene(new Scene(root));

      
    }

    private void loadDate() {
        

        
        
        
        ObservableList<Formation> listForm = FXCollections.observableArrayList();
        Tab_titre_id.setCellValueFactory(new PropertyValueFactory<>("Title"));
        tab_url_id.setCellValueFactory(new PropertyValueFactory<>("Url"));
        tab_desc_id.setCellValueFactory(new PropertyValueFactory<>("Description"));
        tab_domaine_id.setCellValueFactory(new PropertyValueFactory<>("Domaine"));
        FormationService fc = new FormationService();
        List old = fc.getAll();
        listForm.addAll(old);
         TableFormation.setItems(listForm);
         
         
         //add cell of button edit 
         Callback<TableColumn<Formation, String>, TableCell<Formation, String>> cellFoctory = (TableColumn<Formation, String> param) -> {
            // make cell containing buttons
            final TableCell<Formation, String> cell = new TableCell<Formation, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    //that cell created only on non-empty rows
                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {

                        FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
                        FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL_SQUARE);

                        deleteIcon.setStyle(
                                " -fx-cursor: hand ;"
                                + "-glyph-size:28px;"
                                + "-fx-fill:#ff1744;"
                        );
                        editIcon.setStyle(
                                " -fx-cursor: hand ;"
                                + "-glyph-size:28px;"
                                + "-fx-fill:#00E676;"
                        );
                        
                        
                        deleteIcon.setOnMouseClicked(e -> {
                            
                            Formation v = TableFormation.getSelectionModel().getSelectedItem();
                            TableFormation.getItems().removeAll(TableFormation.getSelectionModel().getSelectedItem());
                            FormationService sp = new FormationService();
                            System.out.println(v);
                            sp.supprimerVideo(v);
                            System.out.println("deleted");
                            
                           

                            
                        });
                        
                        
                        
                        
                        
                        editIcon.setOnMouseClicked(e -> {
                            
                            
                            
                             Formation v = TableFormation.getSelectionModel().getSelectedItem();

                            try {
                                FXMLLoader loader= new FXMLLoader(getClass().getResource("/UI/UI_formation/ModifierFormation.fxml"));
                        Parent root = loader.load();
                        ModifierFormationController mF = loader.getController();


                        mF.settfUrlMod(v.getUrl());
                        mF.settfTitreMod(v.getTitle());
                        mF.settfDescriptionMod(v.getDomaine());
                        mF.settfDomaineMod(v.getDescription());
                        


                        Stage stage = new Stage();
                        stage.setTitle("Modifier évenement");
                        stage.setScene(new Scene(root));
                        stage.show();
                        } catch (IOException ex) {
                         System.out.println(ex.getMessage());
                        }
                            
                            
                            
                            
                         /*   formation = TableFormation.getSelectionModel().getSelectedItem();
                            FXMLLoader loader = new FXMLLoader ();
                            loader.setLocation(getClass().getResource("/UI/UI_formation/AjouterFormation.fxml"));
                            try {
                                loader.load();
                            } catch (IOException ex) {
                                Logger.getLogger(AfficherFormationController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            
                            AjouterFormationController AjouterFormationController = loader.getController();
                            AjouterFormationController.setUpdate(true);
                            AjouterFormationController.setTextField(formation.getTitle(), 
                                    formation.getUrl(),formation.getDescription(), formation.getDomaine());
                            Parent parent = loader.getRoot();
                            Stage stage = new Stage();
                            stage.setScene(new Scene(parent));
                            stage.initStyle(StageStyle.UTILITY);
                            stage.show();*/
                            

                           

                        });

                        HBox managebtn = new HBox(editIcon, deleteIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        setGraphic(managebtn);

                        setText(null);

                    }
                }

            };

            return cell;
        };
         tab_gestion_id.setCellFactory(cellFoctory);
         TableFormation.setItems(listForm);
         
         
    }

   
         
    
    @FXML
    private void btn_gotoAjouterForma(javafx.scene.input.MouseEvent event) throws IOException {
        
         try {
            Parent parent = FXMLLoader.load(getClass().getResource("/UI/UI_formation/AjouterFormation.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(AfficherFormationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    
    
    
    @FXML
    private void btn_print_formation(javafx.scene.input.MouseEvent event) {
    }

    
    
    
    
    
    
    @FXML
    private void btn_X_close(javafx.scene.input.MouseEvent event) {
        
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }


    
    

    
    
    
    
}

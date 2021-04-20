/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import Entity.Quiz;
import Services.QuizService;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Bou3dila
 */
public class QuizListController implements Initializable {

    @FXML
    private GridPane grid;
    
    private QuizService serviceQuiz = new QuizService();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            fillGrid();
        } catch (SQLException ex) {
            Logger.getLogger(QuizListController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    
    public void fillGrid() throws SQLException{
        List<Quiz> listQuiz = new ArrayList();
        
        listQuiz = serviceQuiz.getAllQuiz();
        
        for(int i = 0; i < listQuiz.size(); i++){
            Label lb_quiz = new Label(listQuiz.get(i).getNom_quiz());
            Quiz quiz = listQuiz.get(i);
            int h = i;
            lb_quiz.setOnMouseClicked(e->{
                try {
                     Node node = (Node) e.getSource();
                     FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ShowQuiz.fxml"));
                    Stage stage = (Stage) node.getScene().getWindow();
                    
                    Scene scene = new Scene(loader.load());  
                    ShowQuizController quizController = loader.getController();
                    quizController.initData(quiz);
                    stage.setScene(scene);
                } catch (IOException ex) {
                    Logger.getLogger(QuizListController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(QuizListController.class.getName()).log(Level.SEVERE, null, ex);
                }
        
       
            });
            grid.addRow(i,lb_quiz);
             grid.getRowConstraints().add(new RowConstraints(50));
            
        }
        
        
    }
    
}

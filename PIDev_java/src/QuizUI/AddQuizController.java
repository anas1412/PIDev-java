/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QuizUI;

import Entity.Offre;
import Entity.Question;
import Entity.Quiz;
import Entity.Reponse;
import Services.OffreDao.OffreService;
import Services.QuestionService;
import Services.QuizService;
import Services.ReponseService;
import Services.UserSession;
import UI.OffreUI.OffreFXMLController;
//import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import utils.DataSource;


/**
 * FXML Controller class
 *
 * @author Bou3dila
 */
public class AddQuizController implements Initializable {

    private int nb = 5;

    @FXML
    private TextField tf_text;

    @FXML
    private HBox hb_btns;

    @FXML
    private Label lb_error;

    @FXML
    private Button btn_next;

    @FXML
    private Label lb_text;

    @FXML
    private AnchorPane main_pane;

    @FXML
    private GridPane grid;
    
    private Label lb_error_ques = new Label() ;

    private int nb_question = 0;
    private int nb_reponse;

    private int id_quiz = 0;

    Quiz quiz = null;
    
    private Offre o = new Offre();
    OffreService os = new OffreService();
    private QuizService quizService = new QuizService();
    private QuestionService questionService = new QuestionService();
    private ReponseService reponseService = new ReponseService();

    private List<Question> listQuestion = new ArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        lb_error_ques.setVisible(false);
        lb_error_ques.setTextFill(Color.web("#ff0000", 0.8));
        lb_error.setTextFill(Color.web("#ff0000", 0.8));
        main_pane.setStyle("-fx-background-color: #0000;");
        lb_text.setText("Titre de Quizz");
        lb_text.setStyle("-fx-font: normal bold 15px 'serif'");
        grid.setAlignment(Pos.TOP_CENTER);
        btn_next.setStyle("-fx-background-color: #ad0505; -fx-text-fill: white;");
        btn_next.setPrefSize(89, 31);
        hb_btns.setAlignment(Pos.CENTER_LEFT);
        tf_text.setPrefWidth(200);
        tf_text.setStyle("-fx-background-color: #a9a9a9 , white , white; -fx-background-insets: 0 -1 -1 -1, 0 0 0 0, 0 -1 3 -1;");
    }

    public void initData(Offre offre) throws SQLException {
        o = offre;

    }

    @FXML
    public void nextQuestion(ActionEvent event) throws SQLException {

        if (nb_question == 0) {
            if (tf_text.getText().length() > 4) {
                quiz = new Quiz(tf_text.getText(), 0);
                    id_quiz = quizService.addQuizAndGetItsId(quiz);
                    o.setQuiz(id_quiz);
                    os.addQuizzToOffre(id_quiz, o.getId());
                Button finish = new Button("Terminer");
                finish.setOnAction(e -> {
                    try {
                        nextQuestion(e);
                        System.exit(0);
                    } catch (SQLException ex) {
                        Logger.getLogger(AddQuizController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
                finish.setStyle("-fx-background-color: #ad0505; -fx-text-fill: white;");
                finish.setPrefSize(89, 31);
                hb_btns.getChildren().add(finish);
                hb_btns.setAlignment(Pos.CENTER_LEFT);
                hb_btns.setMargin(finish, new Insets(0, 0, 0, 5));
                main_pane.getChildren().removeAll(lb_error, lb_text, tf_text);

                fillGrid();

            } else {

                lb_error.setText("Le titre de quiz > 4 char");
                lb_error.setVisible(true);

            }
        } else if (testInput()) {

            Question question = new Question(0, id_quiz, ((TextField) ((VBox) getNodeByRowColumnIndex(0, 1, grid)).getChildren().get(0)).getText(), nb_reponse - 1, 2);
            int id_ques = questionService.addQuestionAndGetItsId(question);
            question.setId(id_ques);
            for (Node node : grid.getChildren()) {
                if (grid.getColumnIndex(node) == 1 && grid.getRowIndex(node) != 0) {
                    Reponse reponse = new Reponse(id_ques, ((TextField) ((VBox) node).getChildren().get(0)).getText());
                    int id_reponse = reponseService.addReponseAndGetItsId(reponse);
                    if (((RadioButton) getNodeByRowColumnIndex(grid.getRowIndex(node), 0, grid)).isSelected()) {
                        question.setRep_just_id(id_reponse);
                        questionService.updateQuestion(question);
                    }
                }
            }
            if (((Button) event.getTarget()).getText().equals("Terminer")) {
                quiz.setId(id_quiz);
                quiz.setNomb_question(nb_question);
                quizService.updateQuiz(quiz);
                String role = "";
        String request0 = "SELECT * from `user` WHERE `user`.`id` = " + UserSession.getIdSession() + ";";
        Connection con = DataSource.getInstance().getCnx();
        java.sql.PreparedStatement ps0 = con.prepareStatement(request0);
        ResultSet rs0 = ps0.executeQuery();
        if (rs0.next()) {
            role = rs0.getString("roles");
        }

        if (role.equals("[\"Candidat\"]")) {
                    Node node = (Node) event.getSource();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/OffreUI/OffreCandidatFXML.fxml"));
        Stage stage = (Stage) node.getScene().getWindow();
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (IOException ex) {
            Logger.getLogger(OffreFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        stage.setScene(scene);
        }
        if (role.equals("[\"Employeur\"]")) {
            Node node = (Node) event.getSource();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/UI/OffreUI/OffreFXML.fxml"));
                Stage stage = (Stage) node.getScene().getWindow();
                Scene scene = null;  
                try {
                    scene = new Scene(loader.load());
                } catch (IOException ex) {
                    Logger.getLogger(OffreFXMLController.class.getName()).log(Level.SEVERE, null, ex);
                }
                 stage.setScene(scene);
        }
            }
            fillGrid();

        }
    }

    public void fillGrid() throws SQLException {

        nb_reponse = 3;
        grid.getChildren().clear();
        FontAwesomeIconView btn_add = new FontAwesomeIconView();
        btn_add.setOnMouseClicked(ev -> {
            if (nb_reponse < 5) {
                TextField tf_reponse = new TextField();
                tf_reponse.setStyle("-fx-background-color: #a9a9a9 , white , white; -fx-background-insets: 0 -1 -1 -1, 0 0 0 0, 0 -1 3 -1;");
                VBox vb = new VBox();
                Label lb_error = new Label();
                lb_error.setTextFill(Color.web("#ff0000", 0.8));
                vb.getChildren().addAll(tf_reponse, lb_error);
                RadioButton radio = new RadioButton();
                radio.setOnAction(e -> selectReponse(radio));
                FontAwesomeIconView btn_remove = new FontAwesomeIconView();
                btn_remove.setGlyphName("MINUS_SQUARE");
                btn_remove.setStyle("-fx-font-family: FontAwesome; -fx-font-size: 25.0; -fx-fill: #b45959;");
                grid.addRow(nb++, radio, vb, btn_remove);
//                btn_remove.setAlignment(Pos.TOP_CENTER);

                btn_remove.setOnMouseClicked(e -> {
                    lb_error_ques.setVisible(false);
                    grid.getChildren().removeAll(radio, vb, btn_remove);
                    nb_reponse--;
                });
                nb_reponse++;
                alignGrid();
            }
            else{
                lb_error_ques.setText("Tu peut pas ajouter plus que 4 reponse");
                lb_error_ques.setVisible(true);
            }
        });

        RadioButton radio = new RadioButton();
//        radio.setStyle("-fx-border-color: #15171c; -fx-border-radius: 130px; -fx-background-insets: 0;");
        radio.setOnAction(e -> selectReponse(radio));
        VBox vb = new VBox();
        TextField tf_question = new TextField();
        
        btn_add.setGlyphName("PLUS_SQUARE");
        btn_add.setStyle("-fx-font-family: FontAwesome; -fx-font-size: 25.0; -fx-fill: #00af00;");
//        btn_add.setSize("25px");
        vb.getChildren().addAll(tf_question, lb_error_ques);
        Label lb_question = new Label("Question");
        lb_question.setStyle("-fx-font: normal bold 15px 'serif'");
        grid.addRow(0, lb_question, vb, btn_add);
        vb = new VBox();
        TextField tf_reponse = new TextField();
        tf_reponse.setStyle("-fx-background-color: #a9a9a9 , white , white; -fx-background-insets: 0 -1 -1 -1, 0 0 0 0, 0 -1 3 -1;");
        Label lb_error = new Label();
        lb_error.setTextFill(Color.web("#ff0000", 0.8));
        vb.getChildren().addAll(tf_reponse, lb_error);
        grid.addRow(1, radio, vb);
        vb = new VBox();
        RadioButton radio1 = new RadioButton();
        radio1.setOnAction(e -> selectReponse(radio1));
        tf_reponse = new TextField();
        tf_reponse.setStyle("-fx-background-color: #a9a9a9 , white , white; -fx-background-insets: 0 -1 -1 -1, 0 0 0 0, 0 -1 3 -1;");
        lb_error = new Label();
        lb_error.setTextFill(Color.web("#ff0000", 0.8));
        vb.getChildren().addAll(tf_reponse, lb_error);
        grid.addRow(2, radio1, vb);

        nb_question++;
        alignGrid();

    }

    public Node getNodeByRowColumnIndex(final int row, final int column, GridPane gridPane) {
        Node result = null;
        ObservableList<Node> childrens = gridPane.getChildren();

        for (Node node : childrens) {
            if (gridPane.getRowIndex(node) == row && gridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }

        return result;
    }

       public boolean testInput() {

        boolean test = true;
        for (Node node : grid.getChildren()) {
            if (grid.getRowIndex(node) == 0 && grid.getColumnIndex(node) == 1) {
                if (((TextField) ((VBox) node).getChildren().get(0)).getText().length() < 8) {
                    ((Label) ((VBox) node).getChildren().get(1)).setText("la question doit etre > 8 char");
                    test = false;
                } else {
                    ((Label) ((VBox) node).getChildren().get(1)).setText("");
                }
            } else if (grid.getColumnIndex(node) == 1) {
                if (((TextField) ((VBox) node).getChildren().get(0)).getText().length() < 1) {
                    ((Label) ((VBox) node).getChildren().get(1)).setText("la reponse doit etre > 1 char");
                    ((Label) ((VBox) node).getChildren().get(1)).setVisible(true);
                    test = false;
                } else {
                    ((Label) ((VBox) node).getChildren().get(1)).setVisible(false);
                }
            }
        }
        boolean test_radio = false;
        
        for (Node node : grid.getChildren()) {
            if (node instanceof RadioButton) {
                if( ((RadioButton) node).isSelected())
                    test_radio = true;
            }
        }
        
        if(test_radio == false){
           Label lb = ((Label) ((VBox) getNodeByRowColumnIndex(0, 1, grid)).getChildren().get(1));
           if(lb.getText().length() == 0){
               lb.setText("choisissez la reponse correct");
               lb.setVisible(true);
           }
           else{
               lb.setText(lb.getText()+"\n choisissez la reponse correct");
           }
        }
        
        return test && test_radio;
    }

    public void alignGrid() {
        for (Node node : grid.getChildren()) {
            if (node instanceof RadioButton) {
                grid.setHalignment(node, HPos.CENTER);
            }
            grid.setValignment(node, VPos.TOP);
        }
    }

    public void selectReponse(RadioButton radio) {
        for (Node node : grid.getChildren()) {
            if (node instanceof RadioButton) {
                ((RadioButton) node).setSelected(false);
            }
        }
        radio.setSelected(true);
    }

}

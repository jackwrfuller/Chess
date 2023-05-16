package chess.gui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import chess.ChessGame;


public class ChessGUI extends Application {
    ChessGame game;
    VBox root = new VBox();
    BoardGUI board;
    HBox controls = new HBox();




    void drawControls(){
        Button restart = new Button("Restart");
        controls.getChildren().add(restart);
        Button flipBoard = new Button("Flip Board");
        controls.getChildren().add(flipBoard);
        controls.setAlignment(Pos.CENTER);

    }

    public void start(Stage stage){
        game = new ChessGame();
        board = new BoardGUI(game);
        Scene scene = new Scene(root);
        stage.setTitle("Chess");
        stage.setScene(scene); // Place in scene in the stage
        root.getChildren().add(board);
        root.getChildren().add(controls);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

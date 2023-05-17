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



    void newGame(){
        root.getChildren().clear();
        game = new ChessGame();
        board = new BoardGUI(game);
        // add to root and show
        root.getChildren().addAll(board, controls);
    }

    void drawControls(){
        Button newGame = new Button("New Game");
        newGame.setOnMouseClicked(event -> {
            newGame();
        });
        controls.getChildren().add(newGame);
        Button flipBoard = new Button("Flip Board");
        flipBoard.setOnMouseClicked(e -> {
            board.flipBoard();
        });

        controls.getChildren().add(flipBoard);
        controls.setAlignment(Pos.CENTER);

    }

    public void start(Stage stage){
        // Place in scene in the stage
        Scene scene = new Scene(root);
        stage.setTitle("Chess");
        stage.setScene(scene);
        //create and draw elements of application
        game = new ChessGame();
        board = new BoardGUI(game);

        drawControls();
        // add to root and show
        root.getChildren().addAll(board, controls);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

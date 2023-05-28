package chess.gui;


import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import chess.ChessGame;


public class ChessGUI extends Application {
    ChessGame game;
    VBox root = new VBox();

    HBox boardAndLabels = new HBox();
    VBox boardAndFileLetters = new VBox();
    BoardGUI board;
    HBox fileLetters = new HBox();
    VBox rankNumbers = new VBox();
    HBox controls = new HBox();


    void drawRankNumbers() {
        rankNumbers.getChildren().clear();
        boolean orientation = board.isFlipped;
        for (int i = 0; i < 8; i++) {
            Region padding1 = new Region();
            VBox.setVgrow(padding1, Priority.ALWAYS);
            Text letter;
            if (!orientation) {
                letter = new Text(Integer.toString(8-i));
            } else {
                letter = new Text(Integer.toString(i+1));
            }
            Region padding2 = new Region();
            VBox.setVgrow(padding2, Priority.ALWAYS);
            rankNumbers.getChildren().addAll(padding1, letter, padding2);
        }
    }


    void drawFileLetters() {
        fileLetters.getChildren().clear();
        boolean orientation = board.isFlipped;
        for (int i = 0; i < 8; i++) {
            Region padding1 = new Region();
            HBox.setHgrow(padding1, Priority.ALWAYS);
            Text letter;
            if (orientation) {
                letter = new Text(getCharForNumber(7-i));
            } else {
                letter = new Text(getCharForNumber(i));
            }
            Region padding2 = new Region();
            HBox.setHgrow(padding2, Priority.ALWAYS);
            fileLetters.getChildren().addAll(padding1, letter, padding2);
        }
    }
    private String getCharForNumber(int i) {
        return i > -1 && i < 26 ? String.valueOf((char)(i + 'A' )) : null;
    }

    void newGame(){
        root.getChildren().clear();
        boardAndFileLetters.getChildren().clear();
        boardAndLabels.getChildren().clear();
        game = new ChessGame();
        board = new BoardGUI(game);
        drawFileLetters();
        drawRankNumbers();
        boardAndFileLetters.getChildren().addAll(board, fileLetters);
        boardAndLabels.getChildren().addAll(rankNumbers, boardAndFileLetters);
        drawControls();
        // add to root and show
        root.getChildren().addAll(boardAndLabels, controls);
    }

    void drawControls(){
        controls.getChildren().clear();

        Button newGame = new Button("New Game");
        newGame.setOnMouseClicked(event -> {
            newGame();
        });
        controls.getChildren().add(newGame);
        Button flipBoard = new Button("Flip Board");
        flipBoard.setOnMouseClicked(e -> {
            board.flipBoard();
            drawFileLetters();
            drawRankNumbers();

        });
        controls.getChildren().add(flipBoard);

        Button undoMove = new Button("Undo move");
        undoMove.setOnMouseClicked(e -> {
            board.undoMove();
        });
        controls.getChildren().add(undoMove);

        controls.setAlignment(Pos.CENTER);

    }

    public void start(Stage stage){
        Scene scene = new Scene(root);


        scene.addEventFilter(MouseEvent.DRAG_DETECTED , new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                scene.startFullDrag();
            }
        });

        stage.setTitle("Chess");
        stage.setScene(scene);
        newGame();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

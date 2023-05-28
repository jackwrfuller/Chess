package chess.gui;

import chess.ChessGame;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.w3c.dom.css.Rect;

import java.util.Stack;


public class ChessGUIv2 extends Application{


    ChessGame game;
    /**
     * The application component sizes are all in proportion to the chessboard square size
     */
    int SQUARE_SIZE = 60;
    HBox root = new HBox();
    /**
     * All nodes in LHS of application, called the 'playable area'
     */
    BorderPane playArea = new BorderPane();
    BoardGUI board;
    VBox playAreaLeft = new VBox();
    VBox playAreaRight = new VBox();
    HBox playAreaBelow = new HBox();
    HBox playAreaAbove = new HBox();
    /**
     * All nodes on the RHS called the 'console area' that contains controls and information
     * display
     */
    Pane consoleArea = new Pane();
    TextFlow blackClock = new TextFlow();
    StackPane moveHistory = new StackPane();
    StackPane controls = new StackPane();
    TextFlow whiteClock = new TextFlow();

    void drawControls() {
        controls.getChildren().clear();
        Rectangle background = new Rectangle(5*SQUARE_SIZE, SQUARE_SIZE);
        background.setStyle("-fx-fill: green");
        HBox buttons = new HBox();
        controls.getChildren().addAll(background, buttons);

        Button newGame = new Button("New Game");
        newGame.setOnMouseClicked(event -> {
            newGame();
        });
        buttons.getChildren().add(newGame);
        Button flipBoard = new Button("Flip Board");
        flipBoard.setOnMouseClicked(e -> {
            board.flipBoard();
            drawFileChars();
            drawRankNumbers();

        });
        buttons.getChildren().add(flipBoard);

        Button undoMove = new Button("Undo move");
        undoMove.setOnMouseClicked(e -> {
            board.undoMove();
        });
        buttons.getChildren().add(undoMove);

        buttons.setAlignment(Pos.CENTER);
    }
    public void drawMoveHistory() {
        moveHistory.getChildren().clear();
        Rectangle background = new Rectangle(5*SQUARE_SIZE, 5*SQUARE_SIZE+5);
        background.setStyle("-fx-background-color: yellow;");
        TextFlow moveLog = new TextFlow();
        moveLog.setMaxWidth(5*SQUARE_SIZE);
        moveLog.setMaxHeight(5*SQUARE_SIZE);

        String moveHistoryString = game.board.printMoveHistory();
        //Text mh = new Text(moveHistoryString);
        Text mh = new Text("1. Nf3 Nf6 2. c4 g6 3. Nc3 Bg7 4. d4 O-O 5. Bf4 d5 6. Qb3 dxc4 7. Qxc4 c6 8. e4 Nbd7 9. Rd1 Nb6 10. Qc5 Bg4 11. Bg5 Na4 12. Qa3 Nxc3 13. bxc3 Nxe4 14. Bxe7 Qb6 15. Bc4 Nxc3 16. Bc5 Rfe8+ 17. Kf1 Be6 18. Bxb6 Bxc4+ 19. Kg1 Ne2+ 20. Kf1 Nxd4+ 21. Kg1 Ne2+ 22. Kf1 Nc3+ 23. Kg1 axb6 24. Qb4 Ra4 25. Qxb6 Nxd1 26. h3 Rxa2 27. Kh2 Nxf2 28. Re1 Rxe1 29. Qd8+ Bf8 30. Nxe1 Bd5 31. Nf3 Ne4 32. Qb8 b5 33. h4 h5 34. Ne5 Kg7 35. Kg1 Bc5+ 36. Kf1 Ng3+ 37. Ke1 Bb4+ 38. Kd1 Bb3+ 39. Kc1 Ne2+ 40. Kb1 Nc3+ 41. Kc1 Rc2# 0-1");
        moveLog.getChildren().add(mh);
        moveHistory.getChildren().addAll(background, moveLog);
    }

    void drawConsoleArea() {
        consoleArea.getChildren().clear();
        Rectangle r = new Rectangle(5*SQUARE_SIZE, 9*SQUARE_SIZE+8);
        r.setStyle("-fx-fill: white;");

        drawMoveHistory();
        moveHistory.setLayoutX(0);
        moveHistory.setLayoutY(1.5*SQUARE_SIZE+1);


        drawControls();
        controls.setLayoutX(0);
        controls.setLayoutY(6.5*SQUARE_SIZE + 6);


        consoleArea.getChildren().addAll(r, controls, moveHistory);
    }
    void drawTopMargin() {
        playAreaAbove.getChildren().clear();
        Rectangle r = new Rectangle(9*SQUARE_SIZE+8, SQUARE_SIZE/2);
        r.setStyle("-fx-fill: blue;");
        playAreaAbove.getChildren().add(r);
    }
    void drawRightMargin() {
        playAreaRight.getChildren().clear();
        Rectangle r = new Rectangle(SQUARE_SIZE/2, 8*SQUARE_SIZE+8);
        r.setStyle("-fx-fill: yellow;");
        playAreaRight.getChildren().add(r);
    }
    void drawRankNumbers() {
        playAreaLeft.getChildren().clear();
        boolean orientation = board.isFlipped;
        for (int i = 0; i < 8; i++) {
            StackPane label = new StackPane();
            Rectangle r = new Rectangle(SQUARE_SIZE/2, SQUARE_SIZE+1);
            r.setStyle("-fx-fill: green;");
            Text t = new Text();
            if (orientation) {
                t.setText(Integer.toString(i+1));
            } else {
                t.setText(Integer.toString(8-i));
            }
            label.getChildren().addAll(r, t);
            playAreaLeft.getChildren().add(label);
        }
    }
    void drawFileChars() {
        playAreaBelow.getChildren().clear();
        boolean orientation = board.isFlipped;
        Rectangle r1 = new Rectangle(SQUARE_SIZE/2, SQUARE_SIZE/2);
        r1.setStyle("-fx-fill: orange;");
        playAreaBelow.getChildren().add(r1);
        for (int i = 0; i < 8; i++) {
            StackPane label = new StackPane();
            Rectangle r = new Rectangle(SQUARE_SIZE+1, SQUARE_SIZE/2);
            r.setStyle("-fx-fill: orange;");
            Text t = new Text();
            if (orientation) {
                t.setText(getCharForNumber(7-i));
            } else {
                t.setText(getCharForNumber(i));
            }
            label.getChildren().addAll(r, t);
            playAreaBelow.getChildren().add(label);
        }
        Rectangle r2 = new Rectangle(SQUARE_SIZE/2, SQUARE_SIZE/2);
        r2.setStyle("-fx-fill: orange;");
        playAreaBelow.getChildren().add(r2);
    }
    private String getCharForNumber(int i) {
        return i > -1 && i < 26 ? String.valueOf((char)(i + 'A' )) : null;
    }

    void drawPlayArea() {
        playArea.getChildren().clear();
        playArea.setCenter(board);
        drawRankNumbers();
        playArea.setLeft(playAreaLeft);
        drawFileChars();
        playArea.setBottom(playAreaBelow);
        drawTopMargin();
        playArea.setTop(playAreaAbove);
        drawRightMargin();
        playArea.setRight(playAreaRight);
    }
    void newGame() {
        root.getChildren().clear();
        game = new ChessGame();
        board = new BoardGUI(game);
        drawPlayArea();
        drawConsoleArea();
        root.getChildren().addAll(playArea, consoleArea);
    }

    public void start(Stage stage) {
        Scene scene = new Scene(root);
        stage.setTitle("Chess");
        stage.setScene(scene);
        newGame();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

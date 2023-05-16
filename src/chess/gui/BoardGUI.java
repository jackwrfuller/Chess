package chess.gui;

import chess.board.pieces.*;
import chess.ChessGame;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;


/**
 * Class that encapsulates the JavaFX logic of the game chess.board.
 */
public class BoardGUI extends GridPane {

    ChessGame game; // Chess game instance which this chess.board will display
    int SQUARE_SIZE = 50; // Size of a chessboard square in pixels
    // Holds all 64 chessboard squares, noting squares[x][y] is the x file and y rank
    Square[][] squares;

    public BoardGUI(ChessGame game){
        this.game = game;
        createSquares();


    }

    /**
     * Encapsulates a chessboard square within a chess.board instance. Each instance
     * of BoardGuI will have 64 Squares.
     */
    class Square extends StackPane {
        // Position on the chessboard this square is located
        final int file;
        final int rank;
        // The piece currently placed on square
        Piece piece;
        
        // holds the piece image
        private ImageView imageOccupier;
        // Holds the background colouring
        private Background background;
        // ensures image of piece fits within the square size
        double IMAGE_SIZE = 0.9 * SQUARE_SIZE;

        public Square(int file, int rank) {
            this.file = file;
            this.rank = rank;
            this.piece = game.board.squares[file][rank].getOccupier();
            background = new Background();
            setSquareColour();
            placePiece();
            this.getChildren().add(background);
            if (imageOccupier != null) {
                this.getChildren().add(imageOccupier);
            }

        }

        /**
         * Determines what colour the square should be and sets it
         */
        void setSquareColour(){
            if ( (file + rank + 1) % 2 == 0 ) {
                background.setStyle("-fx-fill: green; -fx-stroke: black; -fx-stroke-width: 1;");
            }
        }

        /**
         * Figures out which piece image needs to be added to the square
         */
        void placePiece() {
            // White pieces
            if (piece != null) {
                if (piece.getColour() == 0) {
                    if (piece instanceof King) {
                        Image image = new Image("pieces/White_King.png", IMAGE_SIZE, IMAGE_SIZE, false, false);
                        imageOccupier = new ImageView(image);
                    }
                    if (piece instanceof Queen) {
                        Image image = new Image("pieces/White_Queen.png", IMAGE_SIZE, IMAGE_SIZE, false, false);
                        imageOccupier = new ImageView(image);
                    }
                    if (piece instanceof Bishop) {
                        Image image = new Image("pieces/White_Bishop.png", IMAGE_SIZE, IMAGE_SIZE, false, false);
                        imageOccupier = new ImageView(image);
                    }
                    if (piece instanceof Knight) {
                        Image image = new Image("pieces/White_Knight.png", IMAGE_SIZE, IMAGE_SIZE, false, false);
                        imageOccupier = new ImageView(image);
                    }
                    if (piece instanceof Rook) {
                        Image image = new Image("pieces/White_Rook.png", IMAGE_SIZE, IMAGE_SIZE, false, false);
                        imageOccupier = new ImageView(image);
                    }
                    if (piece instanceof Pawn) {
                        Image image = new Image("pieces/White_Pawn.png", IMAGE_SIZE, IMAGE_SIZE, false, false);
                        imageOccupier = new ImageView(image);
                    }
                } else { // Black pieces
                    if (piece instanceof King) {
                        Image image = new Image("pieces/Black_King.png", IMAGE_SIZE, IMAGE_SIZE, false, false);
                        imageOccupier = new ImageView(image);
                    }
                    if (piece instanceof Queen) {
                        Image image = new Image("pieces/Black_Queen.png", IMAGE_SIZE, IMAGE_SIZE, false, false);
                        imageOccupier = new ImageView(image);
                    }
                    if (piece instanceof Bishop) {
                        Image image = new Image("pieces/Black_Bishop.png", IMAGE_SIZE, IMAGE_SIZE, false, false);
                        imageOccupier = new ImageView(image);
                    }
                    if (piece instanceof Knight) {
                        Image image = new Image("pieces/Black_Knight.png", IMAGE_SIZE, IMAGE_SIZE, false, false);
                        imageOccupier = new ImageView(image);
                    }
                    if (piece instanceof Rook) {
                        Image image = new Image("pieces/Black_Rook.png", IMAGE_SIZE, IMAGE_SIZE, false, false);
                        imageOccupier = new ImageView(image);
                    }
                    if (piece instanceof Pawn) {
                        Image image = new Image("pieces/Black_Pawn.png", IMAGE_SIZE, IMAGE_SIZE, false, false);
                        imageOccupier = new ImageView(image);
                    }
                }
            }
        }

        /**
         * Inner class which holds the background colour
         */
        class Background extends Rectangle {
            public Background(){
                super(SQUARE_SIZE, SQUARE_SIZE);
                this.setStyle("-fx-fill: white; -fx-stroke: black; -fx-stroke-width: 1;");
            }
        }

    }

    /**
     * Create all 64 board squares and add to the board
     */
    void createSquares() {
        squares = new Square[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Square square = new Square(i, j);
                squares[i][j] = square;
                this.add(square, i, j);
            }
        }
    }






}

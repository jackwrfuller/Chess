package chess.gui;

import chess.board.pieces.*;
import chess.ChessGame;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.event.EventHandler;


/**
 * Class that encapsulates the JavaFX logic of the game chess.board.
 */
public class BoardGUI extends GridPane {
    // Chess game instance which this chess.board will display
    ChessGame game;
    // Size of a chessboard square in pixels
    int SQUARE_SIZE = 60;
    // Holds all 64 chessboard squares, noting squares[x][y] is the x file and y rank
    Square[][] squares;

    // Stores if some square is currently selected by the user
    Square selectedSquare;
    // Stores selected squares for the purpose of making moves
    Square toSquare;
    Square fromSquare;

    // Holds whether the board is in the flipped position
    boolean isFlipped = false;

    public BoardGUI(ChessGame game){
        this.game = game;
        drawBoard(isFlipped);

        this.addEventFilter(MouseEvent.MOUSE_CLICKED, dehighlightSquare);
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
        // Holds the highlight layer
        private Rectangle highlightLayer = new Rectangle(SQUARE_SIZE, SQUARE_SIZE);

        // ensures image of piece fits within the square size
        double IMAGE_SIZE = 0.9 * SQUARE_SIZE;
        // Stores if the square is currently selected by the user
        boolean isCurrentlySelected = false;

        public Square(int file, int rank) {
            this.file = file;
            this.rank = rank;
            this.piece = game.board.squares[file][rank].getOccupier();
            background = new Background();
            highlightLayer.setFill(Color.TRANSPARENT);
            setSquareColour();
            placePiece();
            this.getChildren().addAll(background, highlightLayer);
            if (imageOccupier != null) {
                this.getChildren().add(imageOccupier);
            }
            // Add event filters
            this.addEventFilter(MouseEvent.MOUSE_CLICKED, highlightSquare);
            this.addEventFilter(MouseEvent.MOUSE_CLICKED, listenForMove);
            this.addEventFilter(MouseEvent.MOUSE_CLICKED, printInfo);
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

        /**
         * Handles logic for highlighting squares
         */
        EventHandler<MouseEvent> highlightSquare = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                isCurrentlySelected ^= true;
                if (isCurrentlySelected) {
                    highlightLayer.setStyle("-fx-fill: yellow; -fx-stroke: black; -fx-stroke-width: 1;");
                    selectedSquare = Square.this;
                } else {
                    highlightLayer.setStyle("-fx-background-color: transparent;");
                }
            }
        };
        /**
         * Handle logic for moving pieces
         */
        EventHandler<MouseEvent> listenForMove = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (fromSquare == null) {
                    fromSquare = Square.this;
                } else if (toSquare == null) {
                    toSquare = Square.this;
                    movePiece(fromSquare.file, fromSquare.rank, toSquare.file, toSquare.rank);
                    //fromSquare = null;
                    //toSquare = null;
                } else {
                    toSquare = null;
                    fromSquare = Square.this;
                }

            }
        };

        EventHandler<MouseEvent> printInfo = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String pieceName = "";
                Piece occupier = game.board.squares[file][rank].getOccupier();
                if (occupier != null) {
                    pieceName = occupier.getColour() == 0 ? "White" : "Black";
                }
                System.out.println("(" + file + "," + rank + ") " + pieceName);

            }
        };
    }

    /**
     * Create all 64 board squares and add to the board
     */
    void drawBoard(boolean isFlipped) {
        squares = new Square[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Square square = new Square(i, j);
                squares[i][j] = square;
                if (isFlipped) {
                    this.add(square, 7 - i, 7 - j);
                } else {
                    this.add(square, i, j);
                }
            }
        }
    }

    void flipBoard() {
        isFlipped ^= true;
        drawBoard(isFlipped);
    }


    void movePiece(int fromFile, int fromRank, int toFile, int toRank) {
        if (game.board.movePiece(fromFile, fromRank, toFile, toRank)) {
            drawBoard(isFlipped);
            Square lastHighlighted = squares[toFile][toRank];
            lastHighlighted.highlightLayer.setStyle("-fx-fill: yellow; -fx-stroke: black; -fx-stroke-width: 1;");
            this.selectedSquare = lastHighlighted;


        }
        System.out.println(game.board.toString());
    }

    /**
     * Ensures only one square is highlighted at a time
     */
    EventHandler<MouseEvent> dehighlightSquare = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            // handles logic for clicking a square when no square has been clicked
            if (selectedSquare != null) {
                selectedSquare.isCurrentlySelected = false;
                selectedSquare.highlightLayer.setStyle("-fx-background-color: transparent;");
                selectedSquare = null;
            }
        }
    };





}

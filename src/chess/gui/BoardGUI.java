package chess.gui;

import chess.board.pieces.*;
import chess.ChessGame;

import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.event.EventHandler;
import javafx.scene.shape.StrokeType;


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
        private PieceImage imageOccupier = new PieceImage();
        // Holds the background colouring
        private Background background;
        // Holds the highlight layer
        private Rectangle highlightLayer = new Rectangle(SQUARE_SIZE, SQUARE_SIZE);
        private Rectangle hoverLayer = new Rectangle(SQUARE_SIZE, SQUARE_SIZE);

        // ensures image of piece fits within the square size
        double IMAGE_SIZE = 0.9 * SQUARE_SIZE;
        // Stores if the square is currently selected by the user
        boolean isCurrentlySelected = false;

        private void paintHighlight(){
            if (isCurrentlySelected) {
                highlightLayer.setStyle("-fx-fill: #baca44; -fx-stroke: black; -fx-stroke-width: 1;");
            } else {
                highlightLayer.setStyle("-fx-background-color: transparent;");
            }
        }

        public Square(int file, int rank) {
            this.file = file;
            this.rank = rank;
            this.piece = game.board.squares[file][rank].getOccupier();
            background = new Background();
            highlightLayer.setFill(Color.TRANSPARENT);
            hoverLayer.setFill(Color.TRANSPARENT);
            setSquareColour();
            placePiece();
            this.getChildren().addAll(background, highlightLayer, hoverLayer);

            if (imageOccupier != null) {
                this.getChildren().add(imageOccupier);
            }
            // Add event filters
            this.addEventFilter(MouseEvent.MOUSE_ENTERED, EnterSquare);
            this.addEventFilter(MouseEvent.MOUSE_EXITED, LeaveSquare);
            this.addEventFilter(MouseEvent.MOUSE_CLICKED, clickSquare);
            // TODO
//            this.addEventFilter(MouseDragEvent.MOUSE_DRAG_ENTERED, EnterSquare);
//            this.addEventFilter(MouseDragEvent.MOUSE_DRAG_EXITED, LeaveSquare);


//            this.addEventFilter(MouseEvent.MOUSE_CLICKED, listenForMove);
            this.addEventFilter(MouseEvent.MOUSE_CLICKED, printInfo);
//            this.addEventFilter(MouseEvent.MOUSE_RELEASED, releaseHand);
        }

        /**
         * Determines what colour the square should be and sets it
         */
        void setSquareColour(){
            if ( (file + rank + 1) % 2 == 0 ) {
                background.setStyle("-fx-fill: #769656; -fx-stroke: black; -fx-stroke-width: 1;");
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
                        imageOccupier = new PieceImage(image);
                    }
                    if (piece instanceof Queen) {
                        Image image = new Image("pieces/White_Queen.png", IMAGE_SIZE, IMAGE_SIZE, false, false);
                        imageOccupier = new PieceImage(image);
                    }
                    if (piece instanceof Bishop) {
                        Image image = new Image("pieces/White_Bishop.png", IMAGE_SIZE, IMAGE_SIZE, false, false);
                        imageOccupier = new PieceImage(image);
                    }
                    if (piece instanceof Knight) {
                        Image image = new Image("pieces/White_Knight.png", IMAGE_SIZE, IMAGE_SIZE, false, false);
                        imageOccupier = new PieceImage(image);
                    }
                    if (piece instanceof Rook) {
                        Image image = new Image("pieces/White_Rook.png", IMAGE_SIZE, IMAGE_SIZE, false, false);
                        imageOccupier = new PieceImage(image);
                    }
                    if (piece instanceof Pawn) {
                        Image image = new Image("pieces/White_Pawn.png", IMAGE_SIZE, IMAGE_SIZE, false, false);
                        imageOccupier = new PieceImage(image);
                    }
                } else { // Black pieces
                    if (piece instanceof King) {
                        Image image = new Image("pieces/Black_King.png", IMAGE_SIZE, IMAGE_SIZE, false, false);
                        imageOccupier = new PieceImage(image);
                    }
                    if (piece instanceof Queen) {
                        Image image = new Image("pieces/Black_Queen.png", IMAGE_SIZE, IMAGE_SIZE, false, false);
                        imageOccupier = new PieceImage(image);
                    }
                    if (piece instanceof Bishop) {
                        Image image = new Image("pieces/Black_Bishop.png", IMAGE_SIZE, IMAGE_SIZE, false, false);
                        imageOccupier = new PieceImage(image);
                    }
                    if (piece instanceof Knight) {
                        Image image = new Image("pieces/Black_Knight.png", IMAGE_SIZE, IMAGE_SIZE, false, false);
                        imageOccupier = new PieceImage(image);
                    }
                    if (piece instanceof Rook) {
                        Image image = new Image("pieces/Black_Rook.png", IMAGE_SIZE, IMAGE_SIZE, false, false);
                        imageOccupier = new PieceImage(image);
                    }
                    if (piece instanceof Pawn) {
                        Image image = new Image("pieces/Black_Pawn.png", IMAGE_SIZE, IMAGE_SIZE, false, false);
                        imageOccupier = new PieceImage(image);
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
                this.setStyle("-fx-fill: #eeeed2 ; -fx-stroke: black; -fx-stroke-width: 1;");
            }
        }

        class PieceImage extends ImageView {
            Image image;
            public double startDragX;
            double startDragY;
            public PieceImage() {
                super();
            }

            /**
             * Drag and drop functionality
             *
             */

            EventHandler<MouseEvent> changeCursorToHeld = new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    PieceImage.this.setCursor(Cursor.CLOSED_HAND);
                }
            };
            EventHandler<MouseEvent> changeCursorToOpen = new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    PieceImage.this.setCursor(Cursor.HAND);
                }
            };
            EventHandler<MouseEvent> changeCursorToDefault = new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    Square.PieceImage.this.setCursor(Cursor.DEFAULT);
                }
            };

            public PieceImage(Image image) {
                super(image);
               // this.addEventFilter(MouseEvent.MOUSE_PRESSED, changeCursorToHeld);
                //this.addEventFilter(MouseEvent.MOUSE_RELEASED, changeCursorToOpen);
                //this.addEventFilter(MouseEvent.MOUSE_EXITED, changeCursorToDefault);

//                this.setOnMousePressed(e -> {
//                    startDragX = e.getSceneX();
//                    startDragY = e.getSceneY();
//                });
//
//                this.setOnMouseDragged(e -> {
//                    BoardGUI.Square.this.toFront();
//                    this.setTranslateX(e.getSceneX() - startDragX);
//                    this.setTranslateY(e.getSceneY() - startDragY);
//                });
            }
        }
        /**
         * Handles logic for highlighting squares
         */
        EventHandler<MouseEvent> clickSquare = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (Square.this.piece != null) {
                    isCurrentlySelected = true;
                    highlightLayer.setStyle("-fx-fill: #baca44; -fx-stroke: black; -fx-stroke-width: 1;");
                    selectedSquare = Square.this;
                }
                // check to make move
                if (fromSquare != null) {
                    toSquare = Square.this;
                    boolean tryMove = movePiece(fromSquare.file, fromSquare.rank, toSquare.file, toSquare.rank);
                    if (tryMove) {
                        fromSquare = null;
                        toSquare = null;
                    } else {
                        fromSquare = Square.this;
                    }
                } else {
                    fromSquare = Square.this;
                }
            }
        };
        EventHandler<MouseEvent> releaseHand = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (Square.this.piece != null) {
                    Square.this.setCursor(Cursor.HAND);
                }
            }
        };


        EventHandler<MouseEvent> EnterSquare = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                hoverLayer.setStrokeType(StrokeType.INSIDE);
                hoverLayer.setStyle("-fx-background-color: transparent; -fx-stroke: grey; -fx-stroke-width: 3");
                if (Square.this.piece != null && Square.this.getCursor() != Cursor.CLOSED_HAND ) {
                    Square.this.setCursor(Cursor.HAND);
                }

            }
        };
        EventHandler<MouseEvent> LeaveSquare = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                hoverLayer.setStyle("-fx-background-color: transparent;");
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


    boolean movePiece(int fromFile, int fromRank, int toFile, int toRank) {
        if (game.board.movePiece(fromFile, fromRank, toFile, toRank)) {
            drawBoard(isFlipped);
            Square lastHighlighted = squares[toFile][toRank];
            lastHighlighted.highlightLayer.setStyle("-fx-fill: yellow; -fx-stroke: black; -fx-stroke-width: 1;");
            this.selectedSquare = lastHighlighted;
            return true;

        } else {
            return false;
        }
        //System.out.println(game.board.toString());
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

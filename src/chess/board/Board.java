package chess.board;

import chess.MoveChecker;
import chess.board.pieces.*;

public class Board {

    public static final int SIZE = 8;

    // Tracks whose move it is
    public boolean whiteToMove = true;

    public class Square {
        public Piece getOccupier() {
            return occupier;
        }

        public void setOccupier(Piece occupier) {
            this.occupier = occupier;
        }

        Piece occupier = null;

        public Square(Piece p) {
            this.occupier = occupier;
        }

        public Square() {
        }
    }

    /**
     * array of Sqaure which represents the chessboard. Note this is of the form square[file][rank],
     * with 0,0 representing the top-left square
     */
    public Square[][] squares = new Square[SIZE][SIZE];

    public Board() {
        createEmptyBoard();
        placeDefaultSetup();

    }

    public void createEmptyBoard() {
        for (int file = 0; file < SIZE; file++) {
            for (int rank = 0; rank < SIZE; rank++) {
                var s = new Square();
                squares[file][rank] = s;
            }
        }
    }

    public void placeDefaultSetup() {
        //place pawns on second and seventh ranks
        for (int file = 1; file <= SIZE; file++) {
            var wp = new Pawn(0);
            placePiece(wp, file, 7);
            Pawn bp = new Pawn(1);
            placePiece(bp, file, 2);
        }
        // WHITE
        placePiece(new Rook(0), 1, 8);
        placePiece(new Knight(0), 2, 8);
        placePiece(new Bishop(0), 3, 8);
        placePiece(new Queen(0), 4, 8);
        placePiece(new King(0), 5, 8);
        placePiece(new Bishop(0), 6, 8);
        placePiece(new Knight(0), 7, 8);
        placePiece(new Rook(0), 8, 8);
        // BLACK
        placePiece(new Rook(1), 1, 1);
        placePiece(new Knight(1), 2, 1);
        placePiece(new Bishop(1), 3, 1);
        placePiece(new Queen(1), 4, 1);
        placePiece(new King(1), 5, 1);
        placePiece(new Bishop(1), 6, 1);
        placePiece(new Knight(1), 7, 1);
        placePiece(new Rook(1), 8, 1);



    }


    /**
     * Given a piece and a target location
     * @param file
     * @param rank
     */
    public void placePiece(Piece p, int file, int rank){
        squares[file-1][rank-1].setOccupier(p);
    }

    public void clearSquare(int file, int rank) {
        squares[file-1][rank-1].setOccupier(null);
    }

    public boolean legallyMovePiece(int fromFile, int fromRank, int toFile, int toRank) {
        // Check move is legal
        if (MoveChecker.isMoveLegal(this, fromFile, fromRank, toFile, toRank)) {
            return movePiece(fromFile, fromRank, toFile, toRank);
        } else {
            return false;
        }

    }

    public boolean movePiece(int fromFile, int fromRank, int toFile, int toRank) {
            Piece piece = squares[fromFile][fromRank].occupier;
            if (piece == null) {
                return false;
            } else {
                piece.hasMoved = true;
                squares[toFile][toRank].occupier = piece;
                squares[fromFile][fromRank].occupier = null;
                whiteToMove ^= true; // flip whose move it is
                return true;
            }
    }




    public String toString() {
        StringBuilder str = new StringBuilder();

        for (int rank = 0; rank < SIZE; rank++) {
            for (int file = 0; file < SIZE; file++){
                if (squares[file][rank].occupier == null) {
                    str.append("*");
                } else {
                    if (squares[file][rank].occupier instanceof Pawn) {
                        str.append("P");
                    }
                    if (squares[file][rank].occupier instanceof Rook) {
                        str.append("R");
                    }
                    if (squares[file][rank].occupier instanceof Knight) {
                        str.append("N");
                    }
                    if (squares[file][rank].occupier instanceof Bishop) {
                        str.append("B");
                    }
                    if (squares[file][rank].occupier instanceof King) {
                        str.append("K");
                    }
                    if (squares[file][rank].occupier instanceof Queen) {
                        str.append("Q");
                    }
                }
                str.append(" ");
            }
            str.append("\n");
        }
        return str.toString();
    }
}

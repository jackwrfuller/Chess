package chess.board;

import chess.MoveChecker;
import chess.board.pieces.*;
import javafx.util.Pair;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Board {

    public static final int SIZE = 8;

    // Tracks whose move it is
    public boolean whiteToMove = true;
    // Track castling rights
    public boolean whiteKingsideCastleRight = true;
    public boolean whiteQueensideCastleRight = true;
    public boolean blackKingsideCastleRight = true;
    public boolean blackQueensideCastleRight = true;
    // Track number of moves since last pawn advance or piece capture, aka 'halfmove clock'
    public int halfmoveClock = 0;
    // Number of times players have moved; incremented each time black moves
    public int fullmoveNumber = 0;

    /**
     * array of Sqaure which represents the chessboard. Note this is of the form square[file][rank],
     * with 0,0 representing the top-left square
     */
    public Square[][] squares = new Square[SIZE][SIZE];
    /**
     * Stores if there is an pawn that can be targeted for an en passant move. Note there can be at most one such pawn
     * at any given time.
     */
    public Pair<Integer, Integer> enPassantTarget;


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



    public Board() {
        createEmptyBoard();
        placeDefaultSetup();

    }

    /**
     * Creates a new board as a copy of another
     * @param b
     */
    public Board(Board b){
        this.squares = b.squares;
        this.whiteToMove = b.whiteToMove;
       this.whiteKingsideCastleRight = b.whiteKingsideCastleRight;
        this.whiteQueensideCastleRight = b.whiteQueensideCastleRight;
        this.blackKingsideCastleRight = b.blackKingsideCastleRight;
        this.blackQueensideCastleRight = blackKingsideCastleRight;
        // Track number of moves since last pawn advance or piece capture, aka 'halfmove clock'
        this.halfmoveClock = b.halfmoveClock;
        // Number of times players have moved; incremented each time black moves
        this.fullmoveNumber = b.fullmoveNumber;
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
            if (enPassantTarget != null) {
                System.out.println("EP Target: " + enPassantTarget);
            }

            return movePiece(fromFile, fromRank, toFile, toRank);
        } else {
            return false;
        }

    }

    /**
     * Moves a piece from a given square to a target square, with no regard for legality. If the given square is null,
     * do nothing.
     * @param fromFile file of given square
     * @param fromRank rank of given square
     * @param toFile file of target square
     * @param toRank rank of target square
     * @return true if piece was moved, false if given square was empty
     */
    public boolean movePiece(int fromFile, int fromRank, int toFile, int toRank) {
        Piece piece = squares[fromFile][fromRank].occupier;
            if (piece == null) {
                return false;
            } else {

                // Handle if move if an en passant
                if (isEnPassant(fromFile, fromRank, toFile, toRank)) {
                    whiteToMove ^= true;
                    System.out.println("Executing en passant");
                    squares[toFile][toRank].occupier = piece;
                    squares[fromFile][fromRank].occupier = null;
                    squares[enPassantTarget.getKey()][enPassantTarget.getValue()].occupier = null;
                    enPassantTarget = null;
                    return true;
                } else {
                // Update that piece has now moved
                piece.hasMoved = true;
                squares[toFile][toRank].occupier = piece;
                squares[fromFile][fromRank].occupier = null;
                // flip whose move it is
                whiteToMove ^= true;
                // If pawn has moved two squares, track that it is now en passant target. If not, clear target.
                    if (piece instanceof Pawn && Math.abs(fromRank - toRank) == 2) {
                        System.out.println("Pawn was moved two squares, is now an EP target");
                        enPassantTarget = new Pair<>(toFile, toRank);
                    } else {
                        enPassantTarget = null;
                    }

                return true;
                }
            }
    }

    boolean isEnPassant(int fromFile, int fromRank, int toFile, int toRank) {
        Piece p = squares[fromFile][fromRank].getOccupier();
        if (enPassantTarget == null || !(p instanceof Pawn)) {
            System.out.println("No EP target or piece isnt a pawn");
            return false;
        } else {
            int passantFile = enPassantTarget.getKey();
            int passantRank = enPassantTarget.getValue();
            if (Math.abs(passantFile - fromFile) == 1 && Math.abs(passantRank - toRank) == 1 && toFile == passantFile) {
                return true;
            } else {
                return false;
            }
        }
    }


    /**
     * Converts numeric file/rank to algebraic, e.g (0,0) is a8 and (7,7) is h1.
     * @return two character string composed of a char and an int in the usual chess algebraic notation
     */
    public static String toAlgebraicNotation(int file, int rank){
        assert (file >=0 && rank >= 0 && file <= 7 && rank <= 7) : "Invalid location";
        int algRank = 8 - rank;
        String algFile = getCharFromFile(file);
        return  algFile + Integer.toString(algRank);
    }
    public static String toAlgebraicNotation(Pair<Integer, Integer> loc){
        int file = loc.getKey();
        int rank = loc.getValue();
        assert (file >=0 && rank >= 0 && file <= 7 && rank <= 7) : "Invalid location";
        int algRank = 8 - rank;
        String algFile = getCharFromFile(file);
        return  algFile + Integer.toString(algRank);
    }

    /**
     * Converts file number into character representation
     * @param i is internal game logic index of file, i.e 0 to 7.
     * @return algebraic notation equivalent, i.e 0 is the 'a' file, 1 is the 'b' file, etc
     */
    private static String getCharFromFile(int i) {
        return i > -1 && i < 26 ? String.valueOf((char)(i + 'a' )) : null;
    }

    public ArrayList<Pair<Integer, Integer>> getPawnLocations(){
        ArrayList<Pair<Integer, Integer>> allPawnLocations = new ArrayList<>();
        int pawnCount = 0;
        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8 && pawnCount <= 16; rank++) {
                Piece p = this.squares[file][rank].getOccupier();
                if (p instanceof Pawn) {
                    Pair<Integer, Integer> loc = new Pair<>(file, rank);
                    allPawnLocations.add(loc);
                }
            }
        }
        return allPawnLocations;
    }
    public ArrayList<Pair<Integer, Integer>> getPawnPassantLocations(){
        ArrayList<Pair<Integer, Integer>> allPawnLocations = new ArrayList<>();
        int pawnCount = 0;
        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8 && pawnCount <= 16; rank++) {
                Piece p = this.squares[file][rank].getOccupier();
                if (p instanceof Pawn && ((Pawn) p).isEnPassantTarget) {
                    Pair<Integer, Integer> loc = new Pair<>(file, rank);
                    allPawnLocations.add(loc);
                }
            }
        }
        return allPawnLocations;
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

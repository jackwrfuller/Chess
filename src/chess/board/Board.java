package chess.board;

import chess.MoveChecker;
import chess.board.pieces.*;
import javafx.util.Pair;
import chess.Move;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Board {

    public static final int SIZE = 8;

    // Tracks whose move it is
    public boolean whiteToMove = true;
    // Store if the player to move is currently in check
    public boolean isCheck = false;

    public boolean whiteInCheck = false;
    public boolean blackInCheck = false;

    // Track castling rights
    public boolean whiteKingsideCastleRight = true;
    public boolean whiteQueensideCastleRight = true;
    public boolean blackKingsideCastleRight = true;
    public boolean blackQueensideCastleRight = true;
    // Track number of moves since last pawn advance or piece capture, aka 'halfmove clock'
    public int halfmoveClock = 0;
    // Number of times players have moved; incremented each time black moves
    public int moveNumber = 0;

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

    /**
     * Store current game history
     */
    public ArrayList<Move> moveHistory = new ArrayList<>();
    /**
     * Stores all squares attacked by the previous player to move. I.e when it is white to move, this contains all the
     * squares attacked by black. It is used to check for Checks.
     */
    public ArrayList<Pair<Integer, Integer>> attackedSquares = new ArrayList<>();


    public class Square {
        public Piece getOccupier() {
            return occupier;
        }

        public void setOccupier(Piece occupier) {
            this.occupier = occupier;
        }

        public Piece occupier = null;

        public Square(Piece p) {
            this.occupier = occupier;
        }

        public Square() {
        }
    }



    public Board() {
        clearBoard();
        placeDefaultSetup();

    }

    public Board(String fen) {
        clearBoard();
        load(fen);
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
        this.moveNumber = b.moveNumber;
        this.moveHistory = b.moveHistory;
    }



    public void clearBoard() {
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

    public boolean makeLegalMove(Move m) {
        // Check move is legal
        if (MoveChecker.isMoveLegal(m.board, m.fromFile, m.fromRank, m.toFile, m.toRank)) {
            // Add move to move history
            moveHistory.add(m);
            this.moveNumber++;
            makeMove(m);
            return true;
        } else {
            return false;
        }

    }

    /**
     * Moves a piece from a given square to a target square, with no regard for legality. If the given square is null,
     * do nothing.
     * @param m move to be made
     * @return true if piece was moved, false if given square was empty
     */
    public static void makeMove(Move m) {
        System.out.println("making move...");
        Board b = m.board;
        int fromFile = m.fromFile;
        int fromRank = m.fromRank;
        int toFile = m.toFile;
        int toRank = m.toRank;
        Piece piece = m.pieceMoved;
            if (piece == null) {
                return;
            } else {
                // Handle if move if an en passant
                if (isEnPassant(m)) {
                    b.whiteToMove ^= true;
                    b.squares[toFile][toRank].occupier = piece;
                    b.squares[fromFile][fromRank].occupier = null;
                    b.squares[b.enPassantTarget.getKey()][b.enPassantTarget.getValue()].occupier = null;
                    b.enPassantTarget = null;
                    b.updateAttackedSquares();
                    b.updateChecks();
                    return;
                } else {
                    // Update that piece has now moved
                    piece.nMoves++;
                    b.squares[toFile][toRank].occupier = piece;
                    b.squares[fromFile][fromRank].occupier = null;
                    // flip whose move it is
                    b.whiteToMove ^= true;
                    // If pawn has moved two squares, track that it is now en passant target. If not, clear target.
                    if (piece instanceof Pawn && Math.abs(fromRank - toRank) == 2) {
                        b.enPassantTarget = new Pair<>(toFile, toRank);
                    } else {
                        b.enPassantTarget = null;
                    }
                    b.updateAttackedSquares();
                    b.updateChecks();
                    return;
                }
            }
    }

    static boolean isEnPassant(Move m) {
        Piece p = m.pieceMoved;
        if (m.board.enPassantTarget == null || !(p instanceof Pawn)) {
            return false;
        } else {
            int passantFile = m.board.enPassantTarget.getKey();
            int passantRank = m.board.enPassantTarget.getValue();
            if (Math.abs(passantFile - m.fromFile) == 1 && Math.abs(passantRank - m.toRank) == 1 && m.toFile == passantFile) {
                return true;
            } else {
                return false;
            }
        }
    }

    public static boolean undoMove(Move m) {
        if (m.pieceMoved == null) return false;
        m.board.squares[m.fromFile][m.fromRank].occupier = m.pieceMoved;
        m.board.squares[m.toFile][m.toRank].occupier = m.pieceCaptured;
        // remove move from move history
        m.board.moveHistory.remove(m.board.moveHistory.size()-1);
        // flip whose turn to move
        m.board.whiteToMove ^= true;
        m.pieceMoved.nMoves--;
        m.board.moveNumber--;
        m.board.updateAttackedSquares();
        m.board.updateChecks();
        return true;
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
     * Converts algebraic notation of a square to the rank/file index, e.g "a8" -> (0,0)
     * and "a1" -> (0,7), "h8" -> (8,0)
     * @param str algebraic notation for square
     * @return Pair of integers representing the index location
     */
    public static Pair<Integer, Integer> fromAlgebraicToCartesian(String str) {
        assert str.matches("\\w\\d");
        String[] temp = str.split("");
        int rank = Integer.parseInt(temp[1]);
        char[] fileChars = temp[0].toCharArray();
        char fileChar = fileChars[0];
        int file = fileChar - 'a';
        return new Pair<>(file, rank);
    }

    /**
     * Converts file number into character representation
     * @param i is internal game logic index of file, i.e 0 to 7.
     * @return algebraic notation equivalent, i.e 0 is the 'a' file, 1 is the 'b' file, etc
     */
    public static String getCharFromFile(int i) {
        return i > -1 && i < 26 ? String.valueOf((char)(i + 'a' )) : null;
    }

    public ArrayList<Pair<Integer, Integer>> getPieceLocations(boolean whiteToMove){
        int colour = whiteToMove ? 0 : 1;
        ArrayList<Pair<Integer, Integer>> allPieceLocations = new ArrayList<>();
        int pieceCount = 0; // TODO can be optimised by being aware of how many pieces the player currently has
        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8 && pieceCount <= 16; rank++) {
                Piece p = this.squares[file][rank].getOccupier();
                if (p != null && p.getColour() == colour) {
                    Pair<Integer, Integer> loc = new Pair<>(file, rank);
                    allPieceLocations.add(loc);
                }
            }
        }
        return allPieceLocations;
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

    /**
     * Calculates all the squares attacked by the player not currently to move.
     */
    public void updateAttackedSquares() {
        this.attackedSquares.clear();
        var pieceLocations = getPieceLocations(!this.whiteToMove);
        //System.out.println("Pieces are at locations: " + printSquares(pieceLocations));
        for (var loc : pieceLocations) {
             this.attackedSquares.addAll(MoveChecker.getLegalAttacks(this, loc.getKey(), loc.getValue()));
        }
    }

    public void updateChecks() {
        this.whiteInCheck = checkForWhiteInCheck();
        this.blackInCheck = checkForBlackInCheck();

        System.out.println(this.whiteInCheck ? "White is in check" : "White is not in check");
        System.out.println(this.blackInCheck ? "Black is in check" : "Black is not in check");
    }

    boolean checkForWhiteInCheck() {
        ArrayList<Pair<Integer, Integer>> attackedSquares = new ArrayList<>();
        var pieceLocations = getPieceLocations(false);
        for (var loc : pieceLocations) {
            attackedSquares.addAll(MoveChecker.getLegalAttacks(this, loc.getKey(), loc.getValue()));
        }
        for (var loc : attackedSquares) {
            Piece p = squares[loc.getKey()][loc.getValue()].occupier;
            int colour = 0;
            if (p != null && p instanceof King && p.getColour() == colour) {
                System.out.println("White King is in check");
                return true;
            }
        }
        return false;
    }

    boolean checkForBlackInCheck() {
        ArrayList<Pair<Integer, Integer>> attackedSquares = new ArrayList<>();
        var pieceLocations = getPieceLocations(true);
        for (var loc : pieceLocations) {
            attackedSquares.addAll(MoveChecker.getLegalAttacks(this, loc.getKey(), loc.getValue()));
        }
        for (var loc : attackedSquares) {
            Piece p = squares[loc.getKey()][loc.getValue()].occupier;
            int colour = 1;
            if (p != null && p instanceof King && p.getColour() == colour) {
                System.out.println("Black King is in check");
                return true;
            }
        }
        return false;
    }



    public static String printSquares(ArrayList<Pair<Integer, Integer>> locs) {
        StringBuilder str = new StringBuilder();
        for (Pair loc : locs) {
            str.append(toAlgebraicNotation(loc) + ", ");
        }
        return str.toString();
    }

    public String printMoveHistory() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < moveHistory.size(); i++) {
            if (i % 2 == 0) {
                str.append(Integer.toString(i / 2 + 1) + ". ");
            }
            str.append(moveHistory.get(i).toString() + " ");
        }
        return str.toString();
    }

    /**
     * Loads a position from a valid FEN string. Note it does not check the validity
     * of the string.
     * @param fen FEN string to load a game
     */
    public void load(String fen) {
        this.clearBoard();
        String[] fenComponents = fen.split(" ");
        assert fenComponents.length == 6 : "Invalid FEN";
        String layout = fenComponents[0];
        processFenLayout(layout);
        // Process whose turn it is
        String move = fenComponents[1];
        this.whiteToMove = move.equals("w");
        //Process castling rights
        String castlingRights = fenComponents[2];
        processFenCastling(castlingRights);
        // Process en passant pawn, if there is one
        String enPassantPawn = fenComponents[3];
        if (!enPassantPawn.equals("-")) {
             this.enPassantTarget = fromAlgebraicToCartesian(enPassantPawn);
        }
        // Set halfmove clock
        String halfmoveClock = fenComponents[4];
        this.halfmoveClock = Integer.parseInt(halfmoveClock);
        // Set fullmove number
        String fullmove = fenComponents[5];
        this.moveNumber = Integer.parseInt(fullmove);
    }

    void processFenLayout(String layout) {
        this.clearBoard();
        String[] rows = layout.split("/");
        for (int i = 0; i < 8; i++) {
            loadRow(i, rows[i]);
        }
    }

    void loadRow(int row, String fenRow) {
        String[] rowArr = fenRow.split("");
        int i = 0;
        int index = 0;
        while (i < 8) {
            String s = rowArr[index];
            if (s.matches("\\d")) {
                int spaces = Integer.parseInt(s);
                i += spaces;
            } else {
                this.squares[i][row].occupier = strToPiece(s);
                i++;
            }
            index++;
        }
    }


    public static Piece strToPiece(String str) {
        Piece p = new Pawn(0); // Dummy initialisation
        switch (str) {
            case "p": {p = new Pawn(1); break;}
            case "P": {p = new Pawn(0); break;}
            case "r": {p = new Rook(1); break;}
            case "R": {p = new Rook(0); break;}
            case "n": {p = new Knight(1); break;}
            case "N": {p = new Knight(0); break;}
            case "b": {p = new Bishop(1); break;}
            case "B": {p = new Bishop(0); break;}
            case "q": {p = new Queen(1); break;}
            case "Q": {p = new Queen(0); break;}
            case "k": {p = new King(1); break;}
            case "K": {p = new King(0); break;}
        }
        return p;
    }


    void processFenCastling(String castlingRights) {
        String[] temp = castlingRights.split("");
        Set<String> cr = new HashSet<>(Arrays.asList(temp));
        this.whiteKingsideCastleRight = cr.contains("K");
        this.whiteQueensideCastleRight = cr.contains("Q");
        this.blackKingsideCastleRight = cr.contains("k");
        this.blackQueensideCastleRight = cr.contains("q");
    }





    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("\n");
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
        str.append("\n");
        str.append(printMoveHistory());

        str.append("\n" + "Attacked Squares: " + printSquares(this.attackedSquares));
        return str.toString();
    }
}

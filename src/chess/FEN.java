package chess;

import chess.board.Board;
import chess.board.pieces.*;
import javafx.util.Pair;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FEN {

    /*-------------------------------------------------------------------------
     * Convert a chess game instance into a FEN string
     * ------------------------------------------------------------------------
     */
    public static String toFEN(Board board) {
        StringBuilder enc = new StringBuilder();
        enc.append(toFirstFEN(board));
        enc.append(" ");
        enc.append(toMoveFEN(board));
        enc.append(" ");
        enc.append(toCastlingFEN(board));
        enc.append(" ");
        enc.append(toPassantFEN(board));
        enc.append(" ");
        enc.append(toNumberFEN(board));
        return enc.toString();
    }

    public static String toFirstFEN(Board board){
        System.out.println(board);
        StringBuilder enc = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            if (i != 0) {
                enc.append("/");
            }
            int emptyCount = 0;
            for (int j = 0; j < 8; j++) {
                Piece target = board.squares[j][i].getOccupier();
                if (target != null) {
                    if (emptyCount != 0) {
                        enc.append(Integer.toString(emptyCount));
                        emptyCount = 0;
                    }
                    char c = pieceToChar(target);
                    enc.append(c);
                } else {
                    emptyCount++;
                }
            }
            if (emptyCount != 0) {
                enc.append(Integer.toString(emptyCount));
            }
        }
        return enc.toString();
    }
    public static char toMoveFEN(Board board) {
        return board.whiteToMove ? 'w' : 'b';
    }
    public static String toCastlingFEN(Board board) {
        StringBuilder enc = new StringBuilder();
        if (!board.whiteKingsideCastleRight && !board.whiteQueensideCastleRight
                && !board.blackKingsideCastleRight && !board.blackQueensideCastleRight) {
            enc.append('-');
        }
        if (board.whiteKingsideCastleRight) enc.append('K');
        if (board.whiteQueensideCastleRight) enc.append('Q');
        if (board.blackKingsideCastleRight) enc.append('k');
        if (board.blackQueensideCastleRight) enc.append('q');
        return enc.toString();
    }
    public static String toPassantFEN(Board board) {
        var allPawns = board.getPawnPassantLocations();
        if (allPawns.size() == 0) {return "-";}
        StringBuilder locs = new StringBuilder();
        for (var pawn : allPawns) {
            Pawn p = (Pawn) board.squares[pawn.getKey()][pawn.getValue()].getOccupier();
            if (p.isEnPassantTarget){
            String loc = Board.toAlgebraicNotation(pawn);
            locs.append(loc);
            }
        }
        return locs.toString();
    }
    public static String toNumberFEN(Board board) {
        return Integer.toString(board.halfmoveClock) + " " + Integer.toString(board.moveNumber);
    }

    static char pieceToChar(Piece p) {
        if (p.getColour() == 0) {
            if (p instanceof King) {
                return 'K';
            } else if (p instanceof Queen) {
                return 'Q';
            } else if (p instanceof Bishop) {
                return 'B';
            } else if (p instanceof Knight) {
                return 'N';
            } else if (p instanceof Rook) {
                return 'R';
            } else {
                return 'P';
            }
        } else {
            if (p instanceof King) {
                return 'k';
            } else if (p instanceof Queen) {
                return 'q';
            } else if (p instanceof Bishop) {
                return 'b';
            } else if (p instanceof Knight) {
                return 'n';
            } else if (p instanceof Rook) {
                return 'r';
            } else {
                return 'p';
            }
        }
    }

    /*-------------------------------------------------------------------------
     * Create a new chess game instance from a FEN string
     * ------------------------------------------------------------------------
     */
    public static Board load(String fen) {
        Board board = new Board();
        board.clearBoard();
        String[] fenComponents = fen.split(" ");
        assert fenComponents.length == 6 : "Invalid FEN";
        String layout = fenComponents[0];
        processFenLayout(board, layout);
        // Process whose turn it is
        String move = fenComponents[1];
        board.whiteToMove = move.equals("w");
        //Process castling rights
        String castlingRights = fenComponents[2];
        processFenCastling(board, castlingRights);
        // Process en passant pawn, if there is one
        String enPassantPawn = fenComponents[3];
        if (!enPassantPawn.equals("-")) {
            board.enPassantTarget = fromAlgebraicToCartesian(enPassantPawn);
        }
        // Set halfmove clock
        String halfmoveClock = fenComponents[4];
        board.halfmoveClock = Integer.parseInt(halfmoveClock);
        // Set fullmove number
        String fullmove = fenComponents[5];
        board.moveNumber = Integer.parseInt(fullmove);
        return board;
    }
    static void processFenLayout(Board board, String layout) {
        board.clearBoard();
        String[] rows = layout.split("/");
        for (int i = 0; i < 8; i++) {
            loadRow(board, i, rows[i]);
        }
    }
    static void processFenCastling(Board board, String castlingRights) {
        String[] temp = castlingRights.split("");
        Set<String> cr = new HashSet<>(Arrays.asList(temp));
        board.whiteKingsideCastleRight = cr.contains("K");
        board.whiteQueensideCastleRight = cr.contains("Q");
        board.blackKingsideCastleRight = cr.contains("k");
        board.blackQueensideCastleRight = cr.contains("q");
    }
    static void loadRow(Board board, int row, String fenRow) {
        String[] rowArr = fenRow.split("");
        int i = 0;
        int index = 0;
        while (i < 8) {
            String s = rowArr[index];
            if (s.matches("\\d")) {
                int spaces = Integer.parseInt(s);
                i += spaces;
            } else {
                board.squares[i][row].occupier = strToPiece(s);
                i++;
            }
            index++;
        }
    }
    public static Piece strToPiece(String str) {
        Piece p = new Pawn(0); // Dummy initialisation
        switch (str) {
            case "p": {
                p = new Pawn(1);
                break;
            }
            case "P": {
                p = new Pawn(0);
                break;
            }
            case "r": {
                p = new Rook(1);
                break;
            }
            case "R": {
                p = new Rook(0);
                break;
            }
            case "n": {
                p = new Knight(1);
                break;
            }
            case "N": {
                p = new Knight(0);
                break;
            }
            case "b": {
                p = new Bishop(1);
                break;
            }
            case "B": {
                p = new Bishop(0);
                break;
            }
            case "q": {
                p = new Queen(1);
                break;
            }
            case "Q": {
                p = new Queen(0);
                break;
            }
            case "k": {
                p = new King(1);
                break;
            }
            case "K": {
                p = new King(0);
                break;
            }
        }
        return p;
    }
    public static Pair<Integer, Integer> fromAlgebraicToCartesian(String str) {
        assert str.matches("\\w\\d");
        String[] temp = str.split("");
        int rank = Integer.parseInt(temp[1]);
        char[] fileChars = temp[0].toCharArray();
        char fileChar = fileChars[0];
        int file = fileChar - 'a';
        return new Pair<>(file, rank);
    }


}

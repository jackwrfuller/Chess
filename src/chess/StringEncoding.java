package chess;

import chess.board.Board;
import chess.board.pieces.*;

public class StringEncoding {

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
        StringBuilder enc = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            if (i != 0) enc.append("/");
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

}

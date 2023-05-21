package chess;

import chess.board.Board;
import chess.board.pieces.Pawn;
import chess.board.pieces.Piece;
import javafx.util.Pair;

import java.util.ArrayList;

public class MoveChecker {

    public static boolean isMoveLegal(Board board, int fromFile, int fromRank, int toFile, int toRank){
        Piece piece = board.squares[fromFile][fromRank].getOccupier();
        if (piece == null) return false;
        boolean pieceColour = (piece.getColour() == 0);
        // Check correct player is making move
        if (!pieceColour == board.whiteToMove) {
            return false;
        }
        // Check if From square is same as To square
        if (!checkSameSquare(fromFile, fromRank, toFile, toRank)){
            return false;
        }
        // Check if move would result in taking own piece (this is illegal)
        if (!checkTakeOwnPiece(board, fromFile, fromRank, toFile, toRank)) {
            return false;
        }
        // Check if move is legal
        var legalMoves = getLegalMoves(board, fromFile, fromRank);
        var moveToBeTried = new Pair<>(toFile, toRank);
        if (!legalMoves.contains(moveToBeTried)) {
            return false;
        }
        return true;
    }
    static boolean checkSameSquare(int fromFile, int fromRank, int toFile, int toRank){
        return !(fromFile == toFile && fromRank == toRank);
    }
    static boolean checkTakeOwnPiece(Board board, int fromFile, int fromRank, int toFile, int toRank){
        Piece fromPiece = board.squares[fromFile][fromRank].getOccupier();
        Piece toPiece = board.squares[toFile][toRank].getOccupier();
        if (fromPiece == null || toPiece == null) {
            return true;
        }
        int fromColour = fromPiece.getColour();
        int toColour =  toPiece.getColour();
        if (fromColour == toColour) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Provides a list of the legal moves that the piece at the specified location can make
     * @param board
     * @param fromFile
     * @param fromRank
     * @return
     */
    public static ArrayList<Pair<Integer, Integer>> getLegalMoves(Board board, int fromFile, int fromRank){
        ArrayList<Pair<Integer, Integer>> legalMoves = new ArrayList<>();
        Piece piece = board.squares[fromFile][fromRank].getOccupier();

        var pawnLegalMoves = getPawnLegalMoves(board, fromFile, fromRank);
        legalMoves.addAll(pawnLegalMoves);


        System.out.println("Legal Moves:");
        for (var move : legalMoves) {
            System.out.println(move.toString());
        }
        return legalMoves;
    }

    /**
     * Provides a list of legal moves the specified pawn can make
     * @param board
     * @param fromFile
     * @param fromRank
     * @return
     */
    public static ArrayList<Pair<Integer, Integer>> getPawnLegalMoves(Board board, int fromFile, int fromRank) {
        ArrayList<Pair<Integer, Integer>> pawnMoves = new ArrayList<>();
        Pawn pawn = (Pawn) board.squares[fromFile][fromRank].getOccupier();
        boolean isWhite = (pawn.getColour() == 0);
        if (isWhite) {
            // First check pawn is not on the edge of the board, in which case there are no legal moves
            if (fromRank == 0) {return pawnMoves;}
            // Check one square in front, can move to it if it is not occupied
            if (board.squares[fromFile][fromRank - 1].getOccupier() == null) {
                var square = new Pair<>(fromFile, fromRank - 1);
                pawnMoves.add(square);
                // If the pawn has not already moved, check two squares in front also
                if (!pawn.hasMoved && board.squares[fromFile][fromRank - 2].getOccupier() == null) {
                    var square1 = new Pair<>(fromFile, fromRank - 2);
                    pawnMoves.add(square1);
                }
            }
            // Taking pieces logic
            if (fromFile == 0) { // left flank pawn
                Piece target = board.squares[fromFile + 1][fromRank - 1].getOccupier();
                if (target != null && target.getColour() == 1){
                    var m1 = new Pair<>(fromFile + 1, fromRank - 1);
                    pawnMoves.add(m1);
                }
            } else if (fromFile == 7) { // right flank pawn
                Piece target = board.squares[fromFile - 1][fromRank - 1].getOccupier();
                if (target != null && target.getColour() == 1){
                    var m1 = new Pair<>(fromFile - 1, fromRank - 1);
                    pawnMoves.add(m1);
                }
            } else {
                Piece t1 = board.squares[fromFile - 1][fromRank - 1].getOccupier();
                if (t1 != null && t1.getColour() == 1){
                    var m1 = new Pair<>(fromFile - 1, fromRank - 1);
                    pawnMoves.add(m1);
                }
                Piece t2 = board.squares[fromFile + 1][fromRank - 1].getOccupier();
                if (t2 != null && t2.getColour() == 1){
                    var m1 = new Pair<>(fromFile + 1, fromRank - 1);
                    pawnMoves.add(m1);
                }
            }
        } else {
            // First check pawn is not on the edge of the board, in which case there are no legal moves
            if (fromRank == 7) {return pawnMoves;}
            // Check one square in front, can move to it if it is not occupied
            if (board.squares[fromFile][fromRank + 1].getOccupier() == null) {
                var square = new Pair<>(fromFile, fromRank + 1);
                pawnMoves.add(square);
                // If the pawn has not already moved, check two squares in front also
                if (!pawn.hasMoved && board.squares[fromFile][fromRank + 2].getOccupier() == null) {
                    var square1 = new Pair<>(fromFile, fromRank + 2);
                    pawnMoves.add(square1);
                }
            }
            // Taking pieces logic
            if (fromFile == 0) { // left flank pawn
                Piece target = board.squares[fromFile + 1][fromRank + 1].getOccupier();
                if (target != null && target.getColour() == 0){
                    var m1 = new Pair<>(fromFile + 1, fromRank + 1);
                    pawnMoves.add(m1);
                }
            } else if (fromFile == 7) { // right flank pawn
                Piece target = board.squares[fromFile - 1][fromRank + 1].getOccupier();
                if (target != null && target.getColour() == 0){
                    var m1 = new Pair<>(fromFile - 1, fromRank + 1);
                    pawnMoves.add(m1);
                }
            } else {
                Piece t1 = board.squares[fromFile - 1][fromRank + 1].getOccupier();
                if (t1 != null && t1.getColour() == 0){
                    var m1 = new Pair<>(fromFile - 1, fromRank + 1);
                    pawnMoves.add(m1);
                }
                Piece t2 = board.squares[fromFile + 1][fromRank + 1].getOccupier();
                if (t2 != null && t2.getColour() == 0){
                    var m1 = new Pair<>(fromFile + 1, fromRank + 1);
                    pawnMoves.add(m1);
                }
            }
        }
        return pawnMoves;
    }




}

package chess;

import chess.board.*;
import chess.board.pieces.*;

import javafx.util.Pair;

import java.lang.reflect.Array;
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

        if (piece instanceof Pawn) {
            var pawnLegalMoves = getPawnLegalMoves(board, fromFile, fromRank);
            legalMoves.addAll(pawnLegalMoves);
        } else if (piece instanceof King) {
            var kingLegalMoves = getKingLegalMoves(board, fromFile, fromRank);
            legalMoves.addAll(kingLegalMoves);
        } else if (piece instanceof Rook) {
            var rookLegalMoves = getRookLegalMoves(board, fromFile, fromRank);
            legalMoves.addAll(rookLegalMoves);
        }



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

    /**
     * Specifies all legals moves a king can make
     * @param board
     * @param fromFile
     * @param fromRank
     * @return
     */
    public static ArrayList<Pair<Integer, Integer>> getKingLegalMoves(Board board, int fromFile, int fromRank) {
        ArrayList<Pair<Integer, Integer>> kingMoves = new ArrayList<>();
        King king = (King) board.squares[fromFile][fromRank].getOccupier();

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++){
                int adjFile = fromFile + i;
                int adjRank = fromRank + j;
                // ensure king cant move off the board
                if (!isOnBoard(adjFile, adjRank)){
                    continue;
                }
                var target = board.squares[adjFile][adjRank].getOccupier();
                // ensure king doesnt take own pieces
                if (target != null && target.getColour() == king.getColour()) {
                    System.out.println("cant take own piece");
                    continue;
                } else if (hasKingNeighbour(board, adjFile, adjRank, king.getColour())) {
                    System.out.println("Cant move next to enemy king");
                    continue;
                } else {
                    // Otherwise, this move is valid
                    Pair move = new Pair<>(adjFile, adjRank);
                    kingMoves.add(move);
                }
            }
        }
        return kingMoves;
    }
    /**
     * Checks if at a given location there is an enemy king in a neighbouring square
     * @param board
     * @param file
     * @param rank
     * @return
     */

    public static boolean hasKingNeighbour(Board board, int file, int rank, int colour) {
        boolean hasKing = false;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++){
                int adjFile = file + i;
                int adjRank = rank + j;
                // check 'neighbour' is on the board
                if (!isOnBoard(adjFile, adjRank)) continue;
                // Otherwise, check
                Piece piece = board.squares[adjFile][adjRank].getOccupier();
                if (piece == null) continue;
                if (piece instanceof King && piece.getColour() != colour) {
                    hasKing = true;
                }
            }
        }
        return hasKing;
    }

    
    public static ArrayList<Pair<Integer, Integer>> getRookLegalMoves(Board board, int fromFile, int fromRank) {
        ArrayList<Pair<Integer, Integer>> rookMoves = new ArrayList<>();
        Rook rook = (Rook) board.squares[fromFile][fromRank].getOccupier();

        rookMoves.addAll(getVerticalMoves(board, fromFile, fromRank, true));
        rookMoves.addAll(getVerticalMoves(board, fromFile, fromRank, false));
        rookMoves.addAll(getHorizontalMoves(board, fromFile, fromRank, true));
        rookMoves.addAll(getHorizontalMoves(board, fromFile, fromRank, false));
        return rookMoves;
    }

    public static ArrayList<Pair<Integer, Integer>> getVerticalMoves(Board board, int fromFile, int fromRank, boolean toNorth){
        ArrayList<Pair<Integer, Integer>> verticalMoves = new ArrayList<>();
        Piece piece = board.squares[fromFile][fromRank].getOccupier();
        if (toNorth) {
           for (int i = 1; i < 8; i++) {
               int targetRank = fromRank - i;
               if (targetRank < 0) {break;}
               Piece target = board.squares[fromFile][targetRank].getOccupier();
               if (target == null) {
                   var move = new Pair<>(fromFile, targetRank);
                   verticalMoves.add(move);
               } else if (target != null && target.getColour() == piece.getColour()) {
                   break;
               } else if (target != null && target.getColour() != piece.getColour()){
                   var move = new Pair<>(fromFile, targetRank);
                   verticalMoves.add(move);
                   break;
               }
           }
        } else {
            for (int i = 1; i < 8; i++) {
                int targetRank = fromRank + i;
                if (targetRank > 7) {break;}
                Piece target = board.squares[fromFile][targetRank].getOccupier();
                if (target == null) {
                    var move = new Pair<>(fromFile, targetRank);
                    verticalMoves.add(move);
                } else if (target != null && target.getColour() == piece.getColour()) {
                    break;
                } else if (target != null && target.getColour() != piece.getColour()){
                    var move = new Pair<>(fromFile, targetRank);
                    verticalMoves.add(move);
                    break;
                }
            }
        }



        return verticalMoves;
    }

    public static ArrayList<Pair<Integer, Integer>> getHorizontalMoves(Board board, int fromFile, int fromRank, boolean toEast) {
        ArrayList<Pair<Integer, Integer>> horizontalMoves = new ArrayList<>();
        Piece piece = board.squares[fromFile][fromRank].getOccupier();
        if (toEast) {
            for (int i = 1; i < 8; i++) {
                int targetFile = fromFile + i;
                if (targetFile > 7) {break;}
                Piece target = board.squares[targetFile][fromRank].getOccupier();
                if (target == null) {
                    var move = new Pair<>(targetFile, fromRank);
                    horizontalMoves.add(move);
                } else if (target != null && target.getColour() == piece.getColour()) {
                    break;
                } else if (target != null && target.getColour() != piece.getColour()){
                    var move = new Pair<>(targetFile, fromRank);
                    horizontalMoves.add(move);
                    break;
                }
            }
        } else {
            for (int i = 1; i < 8; i++) {
                int targetFile = fromFile - i;
                if (targetFile < 0) {break;}
                Piece target = board.squares[targetFile][fromRank].getOccupier();
                if (target == null) {
                    var move = new Pair<>(targetFile, fromRank);
                    horizontalMoves.add(move);
                } else if (target != null && target.getColour() == piece.getColour()) {
                    break;
                } else if (target != null && target.getColour() != piece.getColour()){
                    var move = new Pair<>(targetFile, fromRank);
                    horizontalMoves.add(move);
                    break;
                }
            }
        }
        return horizontalMoves;
    }





    /**
     * Asserts a given index is on the board, i.e it will not throw an index out of bounds error
     * @param file
     * @param rank
     * @return
     */
    public static boolean isOnBoard(int file, int rank) {
        return (file >= 0 && file <= 7 && rank >= 0 && rank <= 7);
    }




}

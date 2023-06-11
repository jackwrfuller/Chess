package chess;

import chess.board.*;
import chess.board.pieces.*;

import javafx.util.Pair;

import java.util.ArrayList;

public class MoveChecker {

    public static boolean exposesCheck(Move m) {
        boolean isWhiteToMove = m.board.whiteToMove;
        Board boardOneAhead = simulateMove(m);
        boolean isCheck = isWhiteToMove ? boardOneAhead.whiteInCheck : boardOneAhead.blackInCheck;
        return isCheck;
    }


    public static boolean isMoveLegal(Board board, int fromFile, int fromRank, int toFile, int toRank) {
        Move m = new Move(board, fromFile, fromRank, toFile, toRank);
        // Check if move exposes king to check
        if (exposesCheck(m)) {
            return false;
        }

        // Check if king is still in check after making move
        boolean isCheck = board.whiteToMove ? board.whiteInCheck : board.blackInCheck;
        if (isCheck) {
            System.out.println("King is in check!");
            Board boardOneAhead = simulateMove(m);
            System.out.println("In check: " + boardOneAhead.isCheck);
            boolean isCheckAhead = !boardOneAhead.whiteToMove ? boardOneAhead.whiteInCheck : boardOneAhead.blackInCheck;
            if (isCheckAhead) {
                System.out.println("Cant make this move, king is still in check!");
                return false;
            }
        }

        Piece piece = board.squares[fromFile][fromRank].getOccupier();
        if (piece == null) {
            System.out.println("No Piece here!");
            return false;
        }
        boolean pieceColour = (piece.getColour() == 0);
        // Check correct player is making move
        if (!pieceColour == board.whiteToMove) {
            System.out.println("Wrong colour!");
            return false;
        }
        // Check if From square is same as To square
        if (isSameSquare(fromFile, fromRank, toFile, toRank)) {
            System.out.println("Cant move to the same square!");
            return false;
        }
        // Check if move would result in taking own piece (this is illegal)
        if (isTakingOwnPiece(board, fromFile, fromRank, toFile, toRank)) {
            System.out.println("Cant take own piece!");
            return false;
        }
        // Check if move is legal (except checks)
        var legalMoves = getLegalMoves(board, fromFile, fromRank);
        var moveToBeTried = new Pair<>(toFile, toRank);
        return legalMoves.contains(moveToBeTried);
    }

    static boolean isSameSquare(int fromFile, int fromRank, int toFile, int toRank) {
        return (fromFile == toFile && fromRank == toRank);
    }

    static boolean isTakingOwnPiece(Board board, int fromFile, int fromRank, int toFile, int toRank) {
        Piece fromPiece = board.squares[fromFile][fromRank].getOccupier();
        Piece toPiece = board.squares[toFile][toRank].getOccupier();
        if (fromPiece == null || toPiece == null) {
            return false;
        }
        int fromColour = fromPiece.getColour();
        int toColour = toPiece.getColour();
        return fromColour == toColour;
    }

    /**
     * Provides a list of the legal moves that the piece at the specified location can make
     */
    public static ArrayList<Pair<Integer, Integer>> getLegalMoves(Board board, int fromFile, int fromRank) {
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
        } else if (piece instanceof Bishop) {
            var bishopLegalMoves = getBishopLegalMoves(board, fromFile, fromRank);
            legalMoves.addAll(bishopLegalMoves);
        } else if (piece instanceof Queen) {
            var queenLegalMoves = getQueenLegalMoves(board, fromFile, fromRank);
            legalMoves.addAll(queenLegalMoves);
        } else if (piece instanceof Knight) {
            var knightMoves = getKnightLegalMoves(board, fromFile, fromRank);
            legalMoves.addAll(knightMoves);
        }
        return legalMoves;
    }

    public static ArrayList<Pair<Integer, Integer>> getLegalAttacks(Board board, int fromFile, int fromRank) {
        ArrayList<Pair<Integer, Integer>> legalMoves = new ArrayList<>();
        Piece piece = board.squares[fromFile][fromRank].getOccupier();

        if (piece instanceof Pawn) {
            var pawnAttacks = getPawnAttacks(board, fromFile, fromRank);
            legalMoves.addAll(pawnAttacks);
        } else if (piece instanceof King) {
            var kingLegalMoves = getKingLegalMoves(board, fromFile, fromRank);
            legalMoves.addAll(kingLegalMoves);
        } else if (piece instanceof Rook) {
            var rookLegalMoves = getRookLegalMoves(board, fromFile, fromRank);
            legalMoves.addAll(rookLegalMoves);
        } else if (piece instanceof Bishop) {
            var bishopLegalMoves = getBishopLegalMoves(board, fromFile, fromRank);
            legalMoves.addAll(bishopLegalMoves);
        } else if (piece instanceof Queen) {
            var queenLegalMoves = getQueenLegalMoves(board, fromFile, fromRank);
            legalMoves.addAll(queenLegalMoves);
        } else if (piece instanceof Knight) {
            var knightMoves = getKnightLegalMoves(board, fromFile, fromRank);
            legalMoves.addAll(knightMoves);
        }
        return legalMoves;
    }

    /**
     * Return a list the squares a specified pawn is currently attacking
     *
     * @param board
     * @param file
     * @param rank
     * @return
     */
    public static ArrayList<Pair<Integer, Integer>> getPawnAttacks(Board board, int file, int rank) {
        ArrayList<Pair<Integer, Integer>> pawnAttacks = new ArrayList<>();
        Pawn pawn = (Pawn) board.squares[file][rank].getOccupier();
        boolean isWhite = (pawn.getColour() == 0);
        if (isWhite) {
            Pair<Integer, Integer> leftAttack = new Pair<>(file - 1, rank - 1);
            if (isOnBoard(leftAttack)) {
                pawnAttacks.add(leftAttack);
            }
            Pair<Integer, Integer> rightAttack = new Pair<>(file + 1, rank - 1);
            if (isOnBoard(rightAttack)) {
                pawnAttacks.add(rightAttack);
            }
        } else {
            Pair<Integer, Integer> leftAttack = new Pair<>(file - 1, rank + 1);
            if (isOnBoard(leftAttack)) {
                pawnAttacks.add(leftAttack);
            }
            Pair<Integer, Integer> rightAttack = new Pair<>(file + 1, rank + 1);
            if (isOnBoard(rightAttack)) {
                pawnAttacks.add(rightAttack);
            }
        }
        return pawnAttacks;
    }

    /**
     * Provides a list of legal moves the specified pawn can make
     *
     * @param board
     * @param fromFile
     * @param fromRank
     * @return
     */
    public static ArrayList<Pair<Integer, Integer>> getPawnLegalMoves(Board board, int fromFile, int fromRank) {
        ArrayList<Pair<Integer, Integer>> pawnMoves = new ArrayList<>();
        Pawn pawn = (Pawn) board.squares[fromFile][fromRank].getOccupier();
        boolean isWhite = (pawn.getColour() == 0);
        // Add en passant moves
        var tar = board.enPassantTarget;
        if (tar != null) {
            if (fromRank == tar.getValue() && Math.abs(fromFile - tar.getKey()) == 1) {
                if (tar.getValue() == 4) {
                    pawnMoves.add(new Pair<>(tar.getKey(), 5));
                }
                if (tar.getValue() == 3) {
                    pawnMoves.add(new Pair<>(tar.getKey(), 2));
                }
            }
        }

        if (isWhite) {
            // First check pawn is not on the edge of the board, in which case there are no legal moves
            if (fromRank == 0) {
                return pawnMoves;
            }
            // Check one square in front, can move to it if it is not occupied
            if (board.squares[fromFile][fromRank - 1].getOccupier() == null) {
                var square = new Pair<>(fromFile, fromRank - 1);
                pawnMoves.add(square);
                // If the pawn has not already moved, check two squares in front also
                if (pawn.nMoves == 0 && board.squares[fromFile][fromRank - 2].getOccupier() == null) {
                    var square1 = new Pair<>(fromFile, fromRank - 2);
                    pawnMoves.add(square1);
                }
            }
            // Taking pieces logic
            if (fromFile == 0) { // left flank pawn
                Piece target = board.squares[fromFile + 1][fromRank - 1].getOccupier();
                if (target != null && target.getColour() == 1) {
                    var m1 = new Pair<>(fromFile + 1, fromRank - 1);
                    pawnMoves.add(m1);
                }
            } else if (fromFile == 7) { // right flank pawn
                Piece target = board.squares[fromFile - 1][fromRank - 1].getOccupier();
                if (target != null && target.getColour() == 1) {
                    var m1 = new Pair<>(fromFile - 1, fromRank - 1);
                    pawnMoves.add(m1);
                }
            } else {
                Piece t1 = board.squares[fromFile - 1][fromRank - 1].getOccupier();
                if (t1 != null && t1.getColour() == 1) {
                    var m1 = new Pair<>(fromFile - 1, fromRank - 1);
                    pawnMoves.add(m1);
                }
                Piece t2 = board.squares[fromFile + 1][fromRank - 1].getOccupier();
                if (t2 != null && t2.getColour() == 1) {
                    var m1 = new Pair<>(fromFile + 1, fromRank - 1);
                    pawnMoves.add(m1);
                }
            }
        } else {
            // First check pawn is not on the edge of the board, in which case there are no legal moves
            if (fromRank == 7) {
                return pawnMoves;
            }
            // Check one square in front, can move to it if it is not occupied
            if (board.squares[fromFile][fromRank + 1].getOccupier() == null) {
                var square = new Pair<>(fromFile, fromRank + 1);
                pawnMoves.add(square);
                // If the pawn has not already moved, check two squares in front also
                if (pawn.nMoves == 0 && board.squares[fromFile][fromRank + 2].getOccupier() == null) {
                    var square1 = new Pair<>(fromFile, fromRank + 2);
                    pawnMoves.add(square1);
                }
            }
            // Taking pieces logic
            if (fromFile == 0) { // left flank pawn
                Piece target = board.squares[fromFile + 1][fromRank + 1].getOccupier();
                if (target != null && target.getColour() == 0) {
                    var m1 = new Pair<>(fromFile + 1, fromRank + 1);
                    pawnMoves.add(m1);
                }
            } else if (fromFile == 7) { // right flank pawn
                Piece target = board.squares[fromFile - 1][fromRank + 1].getOccupier();
                if (target != null && target.getColour() == 0) {
                    var m1 = new Pair<>(fromFile - 1, fromRank + 1);
                    pawnMoves.add(m1);
                }
            } else {
                Piece t1 = board.squares[fromFile - 1][fromRank + 1].getOccupier();
                if (t1 != null && t1.getColour() == 0) {
                    var m1 = new Pair<>(fromFile - 1, fromRank + 1);
                    pawnMoves.add(m1);
                }
                Piece t2 = board.squares[fromFile + 1][fromRank + 1].getOccupier();
                if (t2 != null && t2.getColour() == 0) {
                    var m1 = new Pair<>(fromFile + 1, fromRank + 1);
                    pawnMoves.add(m1);
                }
            }
        }
        return pawnMoves;
    }

    /**
     * Specifies all legals moves a king can make
     */
    public static ArrayList<Pair<Integer, Integer>> getKingLegalMoves(Board board, int fromFile, int fromRank) {
        ArrayList<Pair<Integer, Integer>> kingMoves = new ArrayList<>();
        King king = (King) board.squares[fromFile][fromRank].getOccupier();
        // Add valid castling moves
        // TODO prevent king moving through check to castle
        if (king.getColour() == 0) {
            if (board.whiteKingsideCastleRight) {
                //if (!board.attackedSquares.contains())
                kingMoves.add(new Pair<>(fromFile + 2, fromRank));
            }
            if (board.whiteQueensideCastleRight) {
                kingMoves.add(new Pair<>(fromFile - 2, fromRank));
            }
        }
        if (king.getColour() == 1) {
            if (board.whiteKingsideCastleRight) {
                kingMoves.add(new Pair<>(fromFile + 2, fromRank));
            }
            if (board.whiteQueensideCastleRight) {
                kingMoves.add(new Pair<>(fromFile - 2, fromRank));
            }
        }

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int adjFile = fromFile + i;
                int adjRank = fromRank + j;
                // ensure king cant move off the board
                if (!isOnBoard(adjFile, adjRank)) {
                    continue;
                }
                var target = board.squares[adjFile][adjRank].getOccupier();
                // ensure king doesnt take own pieces
                if (target != null && target.getColour() == king.getColour()) {
                    continue;
                } else if (hasKingNeighbour(board, adjFile, adjRank, king.getColour())) {
                    continue;
                } else if (isSqureAttacked(board, adjFile, adjRank)) {
                    continue;
                } else {
                    // Otherwise, this move is valid
                    Pair<Integer, Integer> move = new Pair<>(adjFile, adjRank);
                    kingMoves.add(move);
                }
            }
        }
        return kingMoves;
    }


    static boolean isSqureAttacked(Board board, int file, int rank) {
        for (Pair<Integer, Integer> loc : board.attackedSquares) {
            if (loc.getKey() == file && loc.getValue() == rank) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if at a given location there is an enemy king in a neighbouring square
     *
     * @param board chessboard of given game
     * @param file  zero-indexed integer
     * @param rank  zero-indexed integer
     * @return true if the square has the enemy king on any of the 9 neighbouring locations, false otherwise.
     */

    public static boolean hasKingNeighbour(Board board, int file, int rank, int colour) {
        boolean hasKing = false;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int adjFile = file + i;
                int adjRank = rank + j;
                // check 'neighbour' is on the board
                if (!isOnBoard(adjFile, adjRank)) {
                    continue;
                }
                // Otherwise, check
                Piece piece = board.squares[adjFile][adjRank].getOccupier();
                if (piece == null) {
                    continue;
                }
                if (piece instanceof King && piece.getColour() != colour) {
                    hasKing = true;
                }
            }
        }
        return hasKing;
    }


    public static ArrayList<Pair<Integer, Integer>> getRookLegalMoves(Board board, int fromFile, int fromRank) {
        ArrayList<Pair<Integer, Integer>> rookMoves = new ArrayList<>();
        rookMoves.addAll(getVerticalMoves(board, fromFile, fromRank, true));
        rookMoves.addAll(getVerticalMoves(board, fromFile, fromRank, false));
        rookMoves.addAll(getHorizontalMoves(board, fromFile, fromRank, true));
        rookMoves.addAll(getHorizontalMoves(board, fromFile, fromRank, false));
        return rookMoves;
    }

    public static ArrayList<Pair<Integer, Integer>> getVerticalMoves(Board board, int fromFile, int fromRank, boolean toNorth) {
        ArrayList<Pair<Integer, Integer>> verticalMoves = new ArrayList<>();
        Piece piece = board.squares[fromFile][fromRank].getOccupier();
        if (toNorth) {
            for (int i = 1; i < 8; i++) {
                int targetRank = fromRank - i;
                if (targetRank < 0) {
                    break;
                }
                Piece target = board.squares[fromFile][targetRank].getOccupier();
                if (target == null) {
                    var move = new Pair<>(fromFile, targetRank);
                    verticalMoves.add(move);
                } else if (target != null && target.getColour() == piece.getColour()) {
                    break;
                } else if (target != null && target.getColour() != piece.getColour()) {
                    var move = new Pair<>(fromFile, targetRank);
                    verticalMoves.add(move);
                    break;
                }
            }
        } else {
            for (int i = 1; i < 8; i++) {
                int targetRank = fromRank + i;
                if (targetRank > 7) {
                    break;
                }
                Piece target = board.squares[fromFile][targetRank].getOccupier();
                if (target == null) {
                    var move = new Pair<>(fromFile, targetRank);
                    verticalMoves.add(move);
                } else if (target != null && target.getColour() == piece.getColour()) {
                    break;
                } else if (target != null && target.getColour() != piece.getColour()) {
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
                if (targetFile > 7) {
                    break;
                }
                Piece target = board.squares[targetFile][fromRank].getOccupier();
                if (target == null) {
                    var move = new Pair<>(targetFile, fromRank);
                    horizontalMoves.add(move);
                } else if (target != null && target.getColour() == piece.getColour()) {
                    break;
                } else if (target != null && target.getColour() != piece.getColour()) {
                    var move = new Pair<>(targetFile, fromRank);
                    horizontalMoves.add(move);
                    break;
                }
            }
        } else {
            for (int i = 1; i < 8; i++) {
                int targetFile = fromFile - i;
                if (targetFile < 0) {
                    break;
                }
                Piece target = board.squares[targetFile][fromRank].getOccupier();
                if (target == null) {
                    var move = new Pair<>(targetFile, fromRank);
                    horizontalMoves.add(move);
                } else if (target != null && target.getColour() == piece.getColour()) {
                    break;
                } else if (target != null && target.getColour() != piece.getColour()) {
                    var move = new Pair<>(targetFile, fromRank);
                    horizontalMoves.add(move);
                    break;
                }
            }
        }
        return horizontalMoves;
    }

    public static ArrayList<Pair<Integer, Integer>> getBishopLegalMoves(Board board, int fromFile, int fromRank) {
        ArrayList<Pair<Integer, Integer>> bishopMoves = new ArrayList<>();
        bishopMoves.addAll(getRightDiagonalMoves(board, fromFile, fromRank, true));
        bishopMoves.addAll(getRightDiagonalMoves(board, fromFile, fromRank, false));
        bishopMoves.addAll(getLeftDiagonalMoves(board, fromFile, fromRank, true));
        bishopMoves.addAll(getLeftDiagonalMoves(board, fromFile, fromRank, false));
        return bishopMoves;
    }

    public static ArrayList<Pair<Integer, Integer>> getRightDiagonalMoves(Board board, int fromFile, int fromRank, boolean toNorthEast) {
        ArrayList<Pair<Integer, Integer>> diagonalMoves = new ArrayList<>();
        Piece piece = board.squares[fromFile][fromRank].getOccupier();
        if (toNorthEast) {
            for (int i = 1; i < 8; i++) {
                int targetFile = fromFile + i;
                int targetRank = fromRank - i;
                if (targetFile > 7 || targetRank < 0) {
                    break;
                }
                Piece target = board.squares[targetFile][targetRank].getOccupier();
                if (target == null) {
                    var move = new Pair<>(targetFile, targetRank);
                    diagonalMoves.add(move);
                } else if (target != null && target.getColour() == piece.getColour()) {
                    break;
                } else if (target != null && target.getColour() != piece.getColour()) {
                    var move = new Pair<>(targetFile, targetRank);
                    diagonalMoves.add(move);
                    break;
                }
            }
        } else {
            for (int i = 1; i < 8; i++) {
                int targetFile = fromFile - i;
                int targetRank = fromRank + i;
                if (targetFile < 0 || targetRank > 7) {
                    break;
                }
                Piece target = board.squares[targetFile][targetRank].getOccupier();
                if (target == null) {
                    var move = new Pair<>(targetFile, targetRank);
                    diagonalMoves.add(move);
                } else if (target != null && target.getColour() == piece.getColour()) {
                    break;
                } else if (target != null && target.getColour() != piece.getColour()) {
                    var move = new Pair<>(targetFile, targetRank);
                    diagonalMoves.add(move);
                    break;
                }
            }
        }
        return diagonalMoves;
    }

    public static ArrayList<Pair<Integer, Integer>> getLeftDiagonalMoves(Board board, int fromFile, int fromRank, boolean toNorthWest) {
        ArrayList<Pair<Integer, Integer>> diagonalMoves = new ArrayList<>();
        Piece piece = board.squares[fromFile][fromRank].getOccupier();
        if (toNorthWest) {
            for (int i = 1; i < 8; i++) {
                int targetFile = fromFile - i;
                int targetRank = fromRank - i;
                if (targetFile < 0 || targetRank < 0) {
                    break;
                }
                Piece target = board.squares[targetFile][targetRank].getOccupier();
                if (target == null) {
                    var move = new Pair<>(targetFile, targetRank);
                    diagonalMoves.add(move);
                } else if (target != null && target.getColour() == piece.getColour()) {
                    break;
                } else if (target != null && target.getColour() != piece.getColour()) {
                    var move = new Pair<>(targetFile, targetRank);
                    diagonalMoves.add(move);
                    break;
                }
            }
        } else {
            for (int i = 1; i < 8; i++) {
                int targetFile = fromFile + i;
                int targetRank = fromRank + i;
                if (targetFile > 7 || targetRank > 7) {
                    break;
                }
                Piece target = board.squares[targetFile][targetRank].getOccupier();
                if (target == null) {
                    var move = new Pair<>(targetFile, targetRank);
                    diagonalMoves.add(move);
                } else if (target != null && target.getColour() == piece.getColour()) {
                    break;
                } else if (target != null && target.getColour() != piece.getColour()) {
                    var move = new Pair<>(targetFile, targetRank);
                    diagonalMoves.add(move);
                    break;
                }
            }
        }
        return diagonalMoves;
    }

    public static ArrayList<Pair<Integer, Integer>> getQueenLegalMoves(Board board, int fromFile, int fromRank) {
        ArrayList<Pair<Integer, Integer>> queenMoves = new ArrayList<>();
        queenMoves.addAll(getVerticalMoves(board, fromFile, fromRank, true));
        queenMoves.addAll(getVerticalMoves(board, fromFile, fromRank, false));
        queenMoves.addAll(getHorizontalMoves(board, fromFile, fromRank, true));
        queenMoves.addAll(getHorizontalMoves(board, fromFile, fromRank, false));
        queenMoves.addAll(getRightDiagonalMoves(board, fromFile, fromRank, true));
        queenMoves.addAll(getRightDiagonalMoves(board, fromFile, fromRank, false));
        queenMoves.addAll(getLeftDiagonalMoves(board, fromFile, fromRank, true));
        queenMoves.addAll(getLeftDiagonalMoves(board, fromFile, fromRank, false));
        return queenMoves;
    }

    public static ArrayList<Pair<Integer, Integer>> getKnightLegalMoves(Board board, int fromFile, int fromRank) {
        ArrayList<Pair<Integer, Integer>> knightMoves = new ArrayList<>();
        Piece knight = board.squares[fromFile][fromRank].getOccupier();
        // North and south knight moves
        for (int i = -2; i <= 2; i = i + 2) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 || j == 0) {
                    continue;
                }
                int targetFile = fromFile + j;
                int targetRank = fromRank + i;
                if (MoveChecker.isOnBoard(targetFile, targetRank)) {
                    Piece target = board.squares[targetFile][targetRank].getOccupier();
                    if (target == null || target.getColour() != knight.getColour()) {
                        var move = new Pair<>(targetFile, targetRank);
                        knightMoves.add(move);
                    }
                }
            }
        }
        // East and west knight moves
        for (int i = -2; i <= 2; i = i + 2) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 || j == 0) {
                    continue;
                }
                int targetFile = fromFile + i;
                int targetRank = fromRank + j;
                if (MoveChecker.isOnBoard(targetFile, targetRank)) {
                    Piece target = board.squares[targetFile][targetRank].getOccupier();
                    if (target == null || target.getColour() != knight.getColour()) {
                        var move = new Pair<>(targetFile, targetRank);
                        knightMoves.add(move);
                    }
                }
            }
        }
        return knightMoves;
    }


    /**
     * Asserts a given index is on the board, i.e it will not throw an index out of bounds error
     *
     * @param file zero-indexed integer
     * @param rank zero-indexed integer
     * @return true if coordinate is on the chess board, false otherwise
     */
    public static boolean isOnBoard(int file, int rank) {
        return (file >= 0 && file <= 7 && rank >= 0 && rank <= 7);
    }

    public static boolean isOnBoard(Pair<Integer, Integer> loc) {
        return isOnBoard(loc.getKey(), loc.getValue());
    }

    /**
     * Simulate a move on the board. Given a move, it returns a copy of the board with that move made.
     *
     * @param move move to be simulated
     * @return Copy of the board with the move made
     */
    public static Board simulateMove(Move move) {
        String fen = StringEncoding.toFEN(move.board);
        Board copyOfBoard = new Board(fen);
        Move copyOfMove = new Move(copyOfBoard, move.fromFile, move.fromRank, move.toFile, move.toRank);
        copyOfBoard.makeMove(copyOfMove);
        return copyOfBoard;
    }

}

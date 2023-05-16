package chess;

import chess.board.Board;
import chess.board.pieces.Piece;

public class MoveChecker {

    static boolean checkSameSquare(int fromFile, int fromRank, int toFile, int toRank){
        return (fromFile == toFile && fromRank == toRank);
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

    public static boolean isMoveLegal(Board board, int fromFile, int fromRank, int toFile, int toRank){
        // Check if From square is same as To square
        if (!checkSameSquare(fromFile, fromRank, toFile, toRank)){
            return false;
        }
        // Check if move would result in taking own piece (this is illegal)
        if (!checkTakeOwnPiece(board, fromFile, fromRank, toFile, toRank)) {
            return false;
        }



        return true;
    }
}

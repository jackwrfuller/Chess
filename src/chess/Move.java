package chess;

import chess.board.Board;
import chess.board.pieces.*;

public class Move {
    public Board board;
    public int fromFile;
    public int fromRank;
    public int toFile;
    public int toRank;
    public Piece pieceMoved;
    public Piece pieceCaptured;
    public Move(Board board, int fromFile, int fromRank, int toFile, int toRank) {
        this.board = board;
        this.fromFile = fromFile;
        this.fromRank = fromRank;
        this.toFile = toFile;
        this.toRank = toRank;
        this.pieceMoved = board.squares[fromFile][fromRank].getOccupier();
        this.pieceCaptured = board.squares[toFile][toRank].getOccupier();

    }
    @Override
    public String toString() {
        if (pieceMoved == null) return "";
        boolean isCapture;
        if (board.squares[toFile][toRank].getOccupier() == null) {
            isCapture = false;
        } else  {
            isCapture = true;
        }

        return "";
    }
    char pieceToChar(Piece p) {
        if (p instanceof King) return 'K';
        else if (p instanceof Queen) return 'Q';
        else if (p instanceof Bishop) return 'B';
        else if (p instanceof Knight) return 'N';
        else if (p instanceof Rook) return 'R';
        else return 'p';
    }

}

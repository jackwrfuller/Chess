package chess.board.pieces;

public class Pawn extends Piece{

    boolean isEnPassantTarget = false;

    public Pawn(int owner) {
        super(owner);
    }
}

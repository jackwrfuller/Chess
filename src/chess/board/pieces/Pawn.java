package chess.board.pieces;

public class Pawn extends Piece{

    public boolean isEnPassantTarget = false;

    public Pawn(int owner) {
        super(owner);
    }
}

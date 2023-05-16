package chess.board.pieces;

public abstract class Piece {

    final int owner; // 0 for white, 1 for black

    public int getColour(){
        return this.owner;
    }

    public Piece(int owner) {
        this.owner = owner;
    }
}

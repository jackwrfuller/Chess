package chess.board.pieces;

public abstract class Piece {

    final int COLOUR; // 0 for white, 1 for black

    public int getColour(){
        return this.COLOUR;
    }

    public Piece(int COLOUR) {
        this.COLOUR = COLOUR;
    }
}

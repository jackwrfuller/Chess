package chess;

import chess.board.Board;

public class Main {
    public static void main(String[] args) {
       System.out.println("Hello world!");

       Board b = new Board();

        System.out.println(StringEncoding.toFEN(b));

        System.out.println(Board.toAlgebraicNotation(0,0));
        System.out.println(Board.toAlgebraicNotation(7,7));
        System.out.println(Board.toAlgebraicNotation(1,0));
        System.out.println(Board.toAlgebraicNotation(0,1));

    }
}
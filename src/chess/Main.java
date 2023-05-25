package chess;

import chess.board.Board;

public class Main {
    public static void main(String[] args) {
       System.out.println("Hello world!");

       Board b = new Board();
        System.out.println(b.toString());

        System.out.println(StringEncoding.toFEN(b));


        Board copy = new Board(b);

        System.out.println(copy.toString());





    }
}
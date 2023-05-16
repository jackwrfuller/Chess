package chess;

import chess.board.Board;

public class Main {
    public static void main(String[] args) {
       System.out.println("Hello world!");

       Board b = new Board();

        System.out.println(b);

        b.movePiece(1,1,4,8);
        System.out.println(b);

    }
}
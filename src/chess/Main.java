package chess;

import chess.board.Board;

public class Main {
    public static void main(String[] args) {
       System.out.println("Hello world!");

       Board b = new Board();
       System.out.println(b.toString());

       Move m = new Move(b, 3, 6, 3, 4);
       b.makeLegalMove(m);
       System.out.println(b);

       b.undoMove(m);
        System.out.println(b);

        if (!b.makeLegalMove(m)) System.out.println("Move is not legal.");
        System.out.println(b);




    }
}
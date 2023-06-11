package chess;

import chess.board.Board;
import chess.board.pieces.Piece;
import javafx.util.Pair;

public class Main {
    public static void main(String[] args) {
       System.out.println("Hello world!");

       Piece p = Board.strToPiece("p");

        System.out.println(p);


        Pair<Integer, Integer> p1 = new Pair<>(1,1);
        Pair<Integer, Integer> p2 = new Pair<>(1,1);

        System.out.println(p1.equals(p2));



    }
}
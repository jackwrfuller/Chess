package chess;

import chess.board.Board;
import chess.board.pieces.Piece;

public class Main {
    public static void main(String[] args) {
       System.out.println("Hello world!");

       Piece p = Board.strToPiece("p");

        System.out.println(p);




    }
}
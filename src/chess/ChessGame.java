package chess;

import chess.board.*;
public class ChessGame {

    public Board board;


    public ChessGame() {
        this.board = new Board();
    }

    public ChessGame(String fen) {
        this.board = new Board(fen);
    }
















}

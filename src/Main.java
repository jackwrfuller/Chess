import Board.Board;

public class Main {
    public static void main(String[] args) {
       System.out.println("Hello world!");

       Board b = new Board();

        System.out.println(b);

        b.movePiece(1,1,2,2);
        System.out.println(b);

    }
}
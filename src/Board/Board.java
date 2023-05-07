public class Board {

    public static final int SIZE = 8;



    class Square {
        Piece p = null;
        public Square(Piece p) {
            this.p = p;
        }

        public Square() {
        }
    }

    Square[][] square;

    public Board() {
        for (int file = 0; file < SIZE; file++){
            for (int rank = 0; rank < SIZE; rank++){
                var s = new Square();
                square[file][rank] = s;
            }
        }




    }





}

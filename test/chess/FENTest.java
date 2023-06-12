package chess;

import chess.board.Board;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FENTest {

    static String loadBoardAndOutputFEN(String inputFEN) {
        Board board = FEN.load(inputFEN);
        return FEN.toFEN(board);
    }
    @Test
    void testStartingPosition() {
        String startingPosition = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        String fenOutFromBoard = loadBoardAndOutputFEN(startingPosition);
        assertEquals(startingPosition, fenOutFromBoard);
    }

    @Test
    //@DisplayName("Tests Roger Penrose's postion that is tricky for engines")
    void testPenrosePosition() {
        String fenPenrose = "8/p7/kpP5/qrp1b3/rpP2b2/pP4b1/P3K3/8 w - - 0 1";
        String fenOutFromBoard = loadBoardAndOutputFEN(fenPenrose);
        assertEquals(fenPenrose, fenOutFromBoard);
    }
    @Test
    void testNakamuraRybka2008() {
        String FEN = "1b1r2k1/1q2rpn1/p1p3p1/Pp1p1pPp/1P1P1P1P/2PNP1Q1/2BR4/1K1R4 b - - 46 59";
        String fenOut = loadBoardAndOutputFEN(FEN);
        assertEquals(FEN, fenOut);
    }
    @Test
    void testQueensPawnOpening() {
        String FEN = "rnbqkbnr/pppppppp/8/8/3P4/8/PPP1PPPP/RNBQKBNR b KQkq d3 0 1";
        String fenOut = loadBoardAndOutputFEN(FEN);
        assertEquals(FEN, fenOut);
    }
    @Test
    void testKingsPawnOpening() {
        String FEN = "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1";
        String fenOut = loadBoardAndOutputFEN(FEN);
        assertEquals(FEN, fenOut);
    }

    @Test
    void toFEN() {
        // Loads a board with the default starting position
        Board board = new Board();
        String fenOut = FEN.toFEN(board);
        assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", fenOut);
    }
}
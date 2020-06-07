package com.tests.chess.engine.board;

import static org.junit.Assert.*;

import org.junit.Test;

import com.chess.engine.board.board;
import com.chess.engine.pieces.piece;

public class boardTest {

	@Test
	public void initialBoard() {

        final board board = com.chess.engine.board.board.createstandardboard();
        assertEquals(board.currentplayer().getlegalmoves().size(), 20);
        assertEquals(board.currentplayer().getopponent().getlegalmoves().size(), 20);
        assertFalse(board.currentplayer().isincheck());
        assertFalse(board.currentplayer().isincheckmate());
        assertFalse(board.currentplayer().iscastle());
        //assertTrue(board.currentplayer().isKingSideCastleCapable());
        //assertTrue(board.currentplayer().isQueenSideCastleCapable());
        assertEquals(board.currentplayer(), board.whiteplayer());
        assertEquals(board.currentplayer().getopponent(), board.blackplayer());
        assertFalse(board.currentplayer().getopponent().isincheck());
        assertFalse(board.currentplayer().getopponent().isincheckmate());
        assertFalse(board.currentplayer().getopponent().iscastle());
        //assertTrue(board.currentplayer().getopponent().isKingSideCastleCapable());
        //assertTrue(board.currentplayer().getopponent().isQueenSideCastleCapable());
        //assertEquals(StandardBoardEvaluator.get().evaluate(board, 0), 0);
    }


}

package com.tests.chess.engine.board;

import static org.junit.Assert.*;

import org.junit.Test;

import com.chess.engine.board.board;
import com.chess.engine.board.boardutil;
import com.chess.engine.board.move;
import com.chess.engine.board.move.movefactory;
import com.chess.engine.player.movetransition;

public class testcheckmate {

	@Test
    public void testFoolsMate() {

        final com.chess.engine.board.board board = com.chess.engine.board.board.createstandardboard();
        final movetransition t1 = board.currentplayer()
                .makemove(move.movefactory.createmove(board, boardutil.getcoordinateatposition("f2"),
                                boardutil.getcoordinateatposition("f3")));

        assertTrue(t1.getmovestatus().isdone());

        final movetransition t2 = t1.gettransitionboard()
                .currentplayer()
                .makemove(move.movefactory.createmove(t1.gettransitionboard(), boardutil.getcoordinateatposition("e7"),
                                boardutil.getcoordinateatposition("e5")));

        assertTrue(t2.getmovestatus().isdone());

        final movetransition t3 = t2.gettransitionboard()
                .currentplayer()
                .makemove(move.movefactory.createmove(t2.gettransitionboard(), boardutil.getcoordinateatposition("g2"),
                                boardutil.getcoordinateatposition("g4")));

        assertTrue(t3.getmovestatus().isdone());

        final movetransition t4 = t3.gettransitionboard()
                .currentplayer()
                .makemove(move.movefactory.createmove(t3.gettransitionboard(), boardutil.getcoordinateatposition("d8"),
                               boardutil.getcoordinateatposition("h4")));

        assertTrue(t4.getmovestatus().isdone());

        assertTrue(t4.gettransitionboard().currentplayer().isincheckmate());

    }

}

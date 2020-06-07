package com.chess.engine.player.ai;

import com.chess.engine.board.board;
import com.chess.engine.board.move;

public interface movestrategy {
move execute(board b);
}

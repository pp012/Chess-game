package com.chess.engine.player.ai;

import com.chess.engine.board.board;
import com.chess.engine.player.player;

public final class standardboardevaluator implements boardevaluator {
	private static final int check_bonus=50;
	private static final int check_mate_bonus=10000;
	private static final int depth_bonus=100;
	private static final int castle_bonus = 60;
	@Override
	public int evaluate(final board b,final int depth) 
	{
		return scoreplayer(b,b.whiteplayer(),depth)-scoreplayer(b,b.blackplayer(),depth);
	}

	private int scoreplayer(final board b,final player p,final int depth) {
		return piecevalue(p)+mobility(p)+check(p)+checkmate(p,depth)+castled(p);
	}
	private	static int castled(player p) {
		return p.iscastle()?castle_bonus:0;
	}

	private static int checkmate(player p,int depth) {
		return p.getopponent().isincheckmate()?check_mate_bonus *depthbonus(depth):0;
	}
	private static int depthbonus(int depth)
	{
		return depth==0 ? 1:depth_bonus*depth;
	}
	private static int check(final player p) {
		return p.getopponent().isincheck()?check_bonus:0;
	}

	private static int mobility(final player p) {
		return p.getlegalmoves().size();
	}

	private static int piecevalue(final player p)
	{
		int piecevaluescore=0;
		for(final com.chess.engine.pieces.piece piece:p.getactivepieces())
		{
			piecevaluescore+=piece.getpiecevalue();
		}
		return piecevaluescore;
	}
}

package com.chess.engine;

import com.chess.engine.board.boardutil;
import com.chess.engine.player.blackplayer;
import com.chess.engine.player.player;
import com.chess.engine.player.whiteplayer;

public enum alliance {
white
 {
	@Override
	public int getdirection() {
		return -1;
	}

	@Override
	public boolean iswhite() {
		return true;
	}

	@Override
	public boolean isblack() {
		return false;
	}

	@Override
	public player chooseplayer(final whiteplayer wp,final blackplayer bp) {
		return wp;
	}

	@Override
	public int getoppositedirection() {
		return 1;
	}

	@Override
	public boolean ispawnpromotionsquare(int position) {
		return boardutil.eigth_rank(position);
	}
},
black
{
	@Override
	public int getdirection() {
		return 1;
	}

	@Override
	public boolean iswhite() {
		return false;
	}

	@Override
	public boolean isblack() {
		return true;
	}

	@Override
	public player chooseplayer(final whiteplayer wp,final blackplayer bp) {

		return bp;
	}

	@Override
	public int getoppositedirection() {
		return -1;
	}

	@Override
	public boolean ispawnpromotionsquare(int position) {
		return boardutil.first_rank(position);
	}

};
public abstract int getdirection();
public abstract boolean iswhite();
public abstract boolean isblack();
public abstract player chooseplayer(final whiteplayer wp,final  blackplayer bp);
public abstract int getoppositedirection();
public abstract boolean ispawnpromotionsquare(int position);
}

package com.chess.engine.player;
public enum movestatus {
DONE {
	@Override
	public boolean isdone() {
		return true;
	}
}, illegal_move {
	@Override
	public boolean isdone() {
		// TODO Auto-generated method stub
		return false;
	}
}, leaves_player_in_check {
	@Override
	public boolean isdone() {
	// TODO Auto-generated method stub
		return false;
	}
};
	public abstract boolean isdone();
}

package com.chess.engine.player;
import com.chess.engine.board.board;
import com.chess.engine.board.move;
public class movetransition {
private final board transitionboard;
private final move m;
private final movestatus mstatus;
public movetransition(final board transitionboard,final move m,final movestatus mstatus)
{
	this.transitionboard=transitionboard;
	this.m=m;
	this.mstatus=mstatus;
}
public  movestatus getmovestatus() {
	return this.mstatus;
}
public board gettransitionboard() 
{
	return this.transitionboard;
}
}

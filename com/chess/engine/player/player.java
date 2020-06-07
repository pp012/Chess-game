package com.chess.engine.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.alliance;
import com.chess.engine.board.board;
import com.chess.engine.board.move;
import com.chess.engine.pieces.king;
import com.chess.engine.pieces.piece;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

public abstract class player 
{
	protected final board b;
	protected final king playerking;
	protected final Collection<move> legalmoves;
	private final boolean isincheck;
	player(final board b,final Collection<move> legalmoves,final Collection<move> opponentmoves)
	{
		this.b=b;
		this.playerking=establishking();
		this.legalmoves=ImmutableList.copyOf(Iterables.concat(legalmoves,calculatekingcastes(legalmoves,opponentmoves)));
		this.isincheck=!player.calculateattacksontile(this.playerking.getpieceposition(),opponentmoves).isEmpty();
	}
	public static Collection<move> calculateattacksontile(int pieceposition, Collection<move> moves) {
		
		final List<move> attackmoves=new ArrayList<>();
		for(final move m:moves)
		{
			if(pieceposition==m.getdestinationcoordinate())
			{
				attackmoves.add(m);
			}
		}
		return ImmutableList.copyOf(attackmoves);
	}
	private king establishking() 
	{
		for(final piece p:getactivepieces())
		{
			if(p.getpiecetype().isking())
			{
				return (king) p;
			}
		}
		throw new RuntimeException("Should not reach here!Not a valid board!!");
	}
	public boolean ismovelegal(final move m)
	{
		return this.legalmoves.contains(m);
	}
	public boolean isincheck()
	{
		return this.isincheck;
	}
	public boolean isincheckmate()
	{
		return this.isincheck && !hasescapemoves();
	}
	protected boolean hasescapemoves() {
		for(final move m:this.legalmoves)
		{
			final movetransition transition=makemove(m);
			if(transition.getmovestatus().isdone())
			{
				return true;
			}
		}
		return false;
	}
	public boolean isinstalemate()
	{
		return !this.isincheck &&!hasescapemoves();
	}
	public boolean isqueensidecastlecapable()
	{
		return this.playerking.isqueensidecastlecapable();
	}
	public boolean iskingsidecastlecapable()
	{
		return this.playerking.iskingsidecastlecapable();
	}
	public boolean iscastle()
	{
		return false;
	}
	public movetransition makemove(final move m)
	{
		if(!ismovelegal(m))
		{
			return new movetransition(this.b, m, movestatus.illegal_move);
		}
		final board transitionboard=m.execute();
		final Collection<move> kingattacks=calculateattacksontile(transitionboard.currentplayer().getopponent().getplayerking().getpieceposition(), transitionboard.currentplayer().getlegalmoves());
		if(!kingattacks.isEmpty())
		{
			return new movetransition(this.b, m, movestatus.leaves_player_in_check);
		}
		return new movetransition(transitionboard, m, movestatus.DONE);
	}
	public Collection<move> getlegalmoves() 
	{
		return this.legalmoves;
	}
	private piece getplayerking() {
		return this.playerking;
	}
	public abstract Collection<piece> getactivepieces();
	public abstract alliance getalliance();
	public abstract player getopponent();
	protected abstract Collection<move> calculatekingcastes(Collection<move> playerlegals,Collection<move> opponentlegals);
}

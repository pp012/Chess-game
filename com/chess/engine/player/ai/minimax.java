package com.chess.engine.player.ai;
import com.chess.engine.board.board;
import com.chess.engine.board.move;
import com.chess.engine.player.movetransition;

public class minimax implements movestrategy
{
	private final boardevaluator be;
	private final int searchdepth;
	public minimax(final int searchdepth)
	{
		this.be=new standardboardevaluator();
		this.searchdepth=searchdepth;
	}
	@Override 
	public String toString()
	{
		return "minimax";
	}
	@Override
	public move execute(board b)
	{
		final long starttime=System.currentTimeMillis();
		move bestmove=null;
		int highestseenvalue=Integer.MIN_VALUE;
		int lowestseenvalue=Integer.MAX_VALUE;
		int currentvalue;
		int nummoves=b.currentplayer().getlegalmoves().size();
		for(final move m:b.currentplayer().getlegalmoves())
		{
			final movetransition mt=b.currentplayer().makemove(m);
			if(mt.getmovestatus().isdone())
			{
				currentvalue=b.currentplayer().getalliance().iswhite()?
							 min(mt.gettransitionboard(), this.searchdepth-1):
							 max(mt.gettransitionboard(), this.searchdepth-1);
				if(b.currentplayer().getalliance().iswhite() && currentvalue>=highestseenvalue)
				{
					highestseenvalue=currentvalue;
					bestmove=m;
				}
				else if(b.currentplayer().getalliance().isblack() && currentvalue<=lowestseenvalue)
				{
					lowestseenvalue=currentvalue;
					bestmove=m;
				}
			}
		}
		final long executiontime=System.currentTimeMillis()-starttime;
		return bestmove;
	}
	public int min(final board b,final int depth)
	{
		if(depth==0 || isendgamescenario(b))
		{
			return this.be.evaluate(b, depth);
		}
		int lowestseenvalue=Integer.MAX_VALUE;
		for(final move m:b.currentplayer().getlegalmoves())
		{
			final movetransition mt=b.currentplayer().makemove(m);
			if(mt.getmovestatus().isdone())
			{
				final int currentvalue=max(mt.gettransitionboard(),depth-1);
				if(currentvalue<=lowestseenvalue)
				{
					lowestseenvalue=currentvalue;
				}
			}
		}
		return lowestseenvalue;
	}
	private static boolean isendgamescenario(final board b)
	{
		return b.currentplayer().isincheckmate() ||
			   b.currentplayer().isinstalemate();
	}
	public int max(final board b,final int depth)
	{
		if(depth==0 || isendgamescenario(b))
		{
			return this.be.evaluate(b, depth);
		}
		int highestseenvalue=Integer.MIN_VALUE;
		for(final move m:b.currentplayer().getlegalmoves())
		{
			final movetransition mt=b.currentplayer().makemove(m);
			if(mt.getmovestatus().isdone())
			{
				final int currentvalue=min(mt.gettransitionboard(),depth-1);
				if(currentvalue>=highestseenvalue)
				{
					highestseenvalue=currentvalue;
				}
			}
		}
		return highestseenvalue;
	}
}

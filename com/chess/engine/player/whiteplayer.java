package com.chess.engine.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.alliance;
import com.chess.engine.board.board;
import com.chess.engine.board.move;
import com.chess.engine.board.tile;
import com.chess.engine.pieces.piece;
import com.chess.engine.pieces.rook;
import com.google.common.collect.ImmutableList;

public class whiteplayer extends player 
{
	public whiteplayer(final board b,final Collection<move> whitestandardlegalmoves,final Collection<move> blackstandardlegalmoves)
	{
		super(b,whitestandardlegalmoves,blackstandardlegalmoves);
	}
	@Override
	public Collection<piece> getactivepieces() 
	{
		return this.b.getwhitepieces();
	}
	@Override
	public alliance getalliance() {
		return alliance.white;
	}
	@Override
	public player getopponent() {
		return this.b.blackplayer();
	}
	@Override
	protected Collection<move> calculatekingcastes(final Collection<move> playerlegals,final Collection<move> opponentlegals)
	{
		//white king side castle
		final List<move> kingcastles=new ArrayList<>();
		if(this.playerking.isfirstmove() && !this.isincheck())
		{
			if(!this.b.gettile(61).istilloccupied() && !this.b.gettile(62).istilloccupied())
			{
				final tile rooktile=this.b.gettile(63);
				if(rooktile.istilloccupied() && rooktile.getpiece().isfirstmove())
				{
					if(player.calculateattacksontile(61, opponentlegals).isEmpty() &&
						player.calculateattacksontile(62, opponentlegals).isEmpty() &&
						rooktile.getpiece().getpiecetype().isrook())
					{
						kingcastles.add(new move.kingsidecastlemove(this.b, this.playerking, 62, (rook) rooktile.getpiece(), rooktile.gettilecoordinate(), 61));
					}
				}
				
			}
			if(!this.b.gettile(59).istilloccupied() && !this.b.gettile(58).istilloccupied() && !this.b.gettile(57).istilloccupied())
			{
				final tile rooktile=this.b.gettile(56);
				if(rooktile.istilloccupied() && rooktile.getpiece().isfirstmove())
				{
					if(player.calculateattacksontile(58, opponentlegals).isEmpty() &&
					   player.calculateattacksontile(59, opponentlegals).isEmpty() &&
					   rooktile.getpiece().getpiecetype().isrook())
						{
							kingcastles.add(new move.queensidecastlemove(this.b, this.playerking, 58, (rook)rooktile.getpiece(), rooktile.gettilecoordinate(), 59));
						}
				}
			}
		}
				return ImmutableList.copyOf(kingcastles);
	}
}

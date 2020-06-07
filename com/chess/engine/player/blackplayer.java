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
public class blackplayer extends player 
{
	public blackplayer(final board b,final Collection<move> whitestandardlegalmoves,final Collection<move> blackstandardlegalmoves)
	{
		super(b,blackstandardlegalmoves,whitestandardlegalmoves);
	}
	@Override
	public Collection <piece> getactivepieces() 
	{
		return this.b.getblackpieces();
	}
	@Override
	public alliance getalliance() {
		return alliance.black;
	}
	@Override
	public player getopponent() {
		return this.b.whiteplayer();
	}
	@Override
	protected Collection<move> calculatekingcastes(final Collection<move> playerlegals,final Collection<move> opponentlegals)
	{
		//black king side castle
		final List<move> kingcastles=new ArrayList<>();
		if(this.playerking.isfirstmove() && !this.isincheck())
		{
			if(!this.b.gettile(5).istilloccupied() && !this.b.gettile(6).istilloccupied())
			{
				final tile rooktile=this.b.gettile(7);
				if(rooktile.istilloccupied() && rooktile.getpiece().isfirstmove())
				{
					if(player.calculateattacksontile(5, opponentlegals).isEmpty() &&
						player.calculateattacksontile(6, opponentlegals).isEmpty() &&
						rooktile.getpiece().getpiecetype().isrook())
					{
						kingcastles.add(new move.kingsidecastlemove(this.b, this.playerking, 6, (rook) rooktile.getpiece(), rooktile.gettilecoordinate(),5 ));
					}
				}
				
			}
			if(!this.b.gettile(1).istilloccupied() && !this.b.gettile(2).istilloccupied() && !this.b.gettile(3).istilloccupied())
			{
				final tile rooktile=this.b.gettile(0);
				if(rooktile.istilloccupied() && rooktile.getpiece().isfirstmove())
				{
					if(player.calculateattacksontile(2, opponentlegals).isEmpty() &&
							player.calculateattacksontile(3, opponentlegals).isEmpty() &&
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

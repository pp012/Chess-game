package com.chess.engine.board;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chess.engine.alliance;
import com.chess.engine.pieces.bishop;
import com.chess.engine.pieces.king;
import com.chess.engine.pieces.knight;
import com.chess.engine.pieces.pawn;
import com.chess.engine.pieces.piece;
import com.chess.engine.pieces.queen;
import com.chess.engine.pieces.rook;
import com.chess.engine.player.blackplayer;
import com.chess.engine.player.player;
import com.chess.engine.player.whiteplayer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

public class board {
	private final List<tile> gameboard;
	private final Collection<piece> whitepieces;
	private final Collection<piece> blackpieces;
	private final whiteplayer wp;
	private final blackplayer bp;
	private final player currentplayer;
	private final pawn enpassantpawn;
	private board(final builder bu)
	{
		this.gameboard=creategameboard(bu);
		this.whitepieces=calculateactivepieces(this.gameboard,alliance.white);
		this.blackpieces=calculateactivepieces(this.gameboard,alliance.black);
		this.enpassantpawn=bu.enpassantpawn;
		final Collection<move> whitestandardlegalmoves=calculatelegalmoves(this.whitepieces);
		final Collection<move> blackstandardlegalmoves=calculatelegalmoves(this.blackpieces);
		this.wp=new whiteplayer(this,whitestandardlegalmoves,blackstandardlegalmoves);
		this.bp=new blackplayer(this,whitestandardlegalmoves,blackstandardlegalmoves);
		this.currentplayer=bu.nextmovemaker.chooseplayer(this.wp,this.bp);
	}	
	@Override
	public String toString()
	{
		StringBuilder builder=new StringBuilder();
		for(int i=0;i<boardutil.num_tiles;i++)
		{
			final String tiletext=this.gameboard.get(i).toString();
			builder.append(String.format("%3s", tiletext));
			if((i+1) % boardutil.num_tiles_per_row==0)
			{
				builder.append("\n");
			}
		}
		return builder.toString();
	}
	public player whiteplayer()
	{
		return this.wp;
	}
	public player blackplayer()
	{
		return this.bp;
	}
	public pawn getenpassant()
	{
		return this.enpassantpawn;
	}
	private Collection<move> calculatelegalmoves(final Collection<piece> pieces) 
	{
		final List<move> legalmoves=new ArrayList<>();
		for(final piece p:pieces)
		{
			legalmoves.addAll(p.calculatelegalmoves(this));
		}
		return ImmutableList.copyOf(legalmoves);
	}
	private static Collection<piece> calculateactivepieces(List<tile> gameboard, alliance a) 
	{
		final List<piece> activepieces=new ArrayList<>();
		for(final tile t:gameboard)
		{
			if(t.istilloccupied())
			{
				final piece p=t.getpiece();
				if(p.getpiecealliance()==a)
				{
					activepieces.add(p);
				}
			}
		}
		return ImmutableList.copyOf(activepieces);
		
	}
	public tile gettile(final int tilecoordinate)
	{
		return gameboard.get(tilecoordinate);
	}
	private static List<tile> creategameboard(final builder bu)
	{	
		final tile[] tiles=new tile[boardutil.num_tiles];
		for(int i=0;i<boardutil.num_tiles;i++)
		{
			tiles[i]=tile.createtile(i, bu.boardconfig.get(i));		
		}
		return ImmutableList.copyOf(tiles);
	}
	public static board createstandardboard()
	{
		 final builder bu=new builder();
		 bu.setpiece(new rook(alliance.black,0));
		 bu.setpiece(new knight(alliance.black,1));
		 bu.setpiece(new bishop(alliance.black,2));
		 bu.setpiece(new queen(alliance.black,3));
		 bu.setpiece(new king(alliance.black,4,true,true));
		 bu.setpiece(new bishop(alliance.black,5));
		 bu.setpiece(new knight(alliance.black,6));
		 bu.setpiece(new rook(alliance.black,7));
		 bu.setpiece(new pawn(alliance.black,8));
		 bu.setpiece(new pawn(alliance.black,9));
		 bu.setpiece(new pawn(alliance.black,10));
		 bu.setpiece(new pawn(alliance.black,11));
		 bu.setpiece(new pawn(alliance.black,12));
		 bu.setpiece(new pawn(alliance.black,13));
		 bu.setpiece(new pawn(alliance.black,14));
		 bu.setpiece(new pawn(alliance.black,15));
		 
		 
		 bu.setpiece(new rook(alliance.white,56));
		 bu.setpiece(new knight(alliance.white,57));
		 bu.setpiece(new bishop(alliance.white,58));
		 bu.setpiece(new queen(alliance.white,59));
		 bu.setpiece(new king(alliance.white,60,true,true));
		 bu.setpiece(new bishop(alliance.white,61));
		 bu.setpiece(new knight(alliance.white,62));
		 bu.setpiece(new rook(alliance.white,63));
		 bu.setpiece(new pawn(alliance.white,48));
		 bu.setpiece(new pawn(alliance.white,49));
		 bu.setpiece(new pawn(alliance.white,50));
		 bu.setpiece(new pawn(alliance.white,51));
		 bu.setpiece(new pawn(alliance.white,52));
		 bu.setpiece(new pawn(alliance.white,53));
		 bu.setpiece(new pawn(alliance.white,54));
		 bu.setpiece(new pawn(alliance.white,55));
		 bu.setmovemaker(alliance.white);
		return bu.build();
	}
public static class builder
{
	Map<Integer, piece> boardconfig;
	alliance nextmovemaker;
	pawn enpassantpawn;
	public builder()
	{
		this.boardconfig=new HashMap<>();
	}
	public builder setpiece(final piece p)
	{
		this.boardconfig.put(p.getpieceposition(), p);
		return this;
	}
	public builder setmovemaker(final alliance nextmovemaker)
	{
		this.nextmovemaker=nextmovemaker;
		return this;
	}
	public board build()
	{
		return new board(this);
		
	}
	public void setenpassant(pawn enpassantpawn) {
		this.enpassantpawn=enpassantpawn;
		
	}
}
public Collection<piece> getblackpieces() 
{
	return this.blackpieces;
}
public Collection<piece> getwhitepieces() 
{
	return this.whitepieces;
}
public player currentplayer() {
	
	return this.currentplayer;
}
public Iterable<move> getalllegalmoves() 
{
	return Iterables.unmodifiableIterable(Iterables.concat(this.whiteplayer().getlegalmoves(),this.bp.getlegalmoves()));
}
}

package com.chess.pgn;

import com.chess.engine.board.board;
import com.chess.engine.board.boardutil;
import com.chess.engine.pieces.pawn;

public class fenutilities {
private fenutilities()
{
	throw new RuntimeException("Not instantiable!");
}
private static board creategamefromfen(final String fenstring)
{
	return null;
}
private static String createfenfromgame(final board b)
{
	return calculateboardtext(b)+" "+calculatecurrentplayertext(b)+" "+calculatecastletext(b)+" "+calculateenpassantsquare(b)+" "+"0 1";  
}
private static String calculatecurrentplayertext(final board b)
{
	return b.currentplayer().toString().substring(0, 1).toLowerCase();
}
private static String calculatecastletext(final board b)
{
	final StringBuilder builder=new StringBuilder();
	if(b.whiteplayer().iskingsidecastlecapable())
	{
		builder.append("K");
	}
	if(b.whiteplayer().isqueensidecastlecapable())
	{
		builder.append("Q");
	}
	if(b.blackplayer().iskingsidecastlecapable())
	{
		builder.append("k");
	}
	if(b.whiteplayer().isqueensidecastlecapable())
	{
		builder.append("q");
	}
	final String result=builder.toString();
	return result.isEmpty()?"-":result;
}
private static String calculateenpassantsquare(final board b)
{
	final pawn enpassantpawn=b.getenpassant();
	if(enpassantpawn!=null)
	{
		return boardutil.getpositionatcoordinate(enpassantpawn.getpieceposition()+ 
				8 *enpassantpawn.getpiecealliance().getoppositedirection());
	}
	return "-";
}
private static String calculateboardtext(final board b)
{
	final StringBuilder builder=new StringBuilder();
	for(int i=0;i<boardutil.num_tiles;i++)
	{
		final String tiletext=b.gettile(i).toString();
		builder.append(tiletext);
	}
	builder.insert(8, "/");
	builder.insert(17, "/");
	builder.insert(26, "/");
	builder.insert(35, "/");
	builder.insert(44, "/");
	builder.insert(53, "/");
	builder.insert(62, "/");
	return builder.toString().replaceAll("--------", "8")
							 .replaceAll("-------", "7")
							 .replaceAll("------", "6")
							 .replaceAll("-----", "5")
							 .replaceAll("----", "4")
							 .replaceAll("---","3")
							 .replaceAll("--","2")
							 .replaceAll("-","1");	
}
}

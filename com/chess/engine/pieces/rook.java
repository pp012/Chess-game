package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.alliance;
import com.chess.engine.board.board;
import com.chess.engine.board.boardutil;
import com.chess.engine.board.move;
import com.chess.engine.board.tile;
import com.chess.engine.pieces.piece.piecetype;
import com.google.common.collect.ImmutableList;

public class rook extends piece{
	private static final int[] candidate_move_coordinates= {-1,1,-8,8};
	public rook(alliance piecealliance,final int pieceposition) {
		super(piecetype.ROOK,pieceposition, piecealliance,true);
		// TODO Auto-generated constructor stub
	}
	public rook(final alliance piecealliance,final int pieceposition,final Boolean isfirstmove)
	{
		super(piecetype.ROOK,pieceposition, piecealliance,isfirstmove);
	}
	@Override
	public Collection<move> calculatelegalmoves(board b) 
	{
		final List <move> legalmove=new ArrayList<>();
		for(final int candidatecoordinateoffset:candidate_move_coordinates)
		{
			int candidatedestinationcoordinate=this.pieceposition;
		while(boardutil.isvalidtilecoordinate(candidatedestinationcoordinate))
		{
			if(isfirstcolumnexclusion(candidatedestinationcoordinate, candidatecoordinateoffset) 
					|| iseightcolumnexclusion(candidatedestinationcoordinate, candidatecoordinateoffset))
			{
				break;
			}
			candidatedestinationcoordinate=candidatedestinationcoordinate + candidatecoordinateoffset;
			if(boardutil.isvalidtilecoordinate(candidatedestinationcoordinate))
			{
				final tile candidatedestinationtile=b.gettile(candidatedestinationcoordinate);
				if(!candidatedestinationtile.istilloccupied())
				{
					legalmove.add(new move.majormove(b,this,candidatedestinationcoordinate));
				}
				else
				{
				final piece pieceatdestination=candidatedestinationtile.getpiece();
				final alliance piecealliance=pieceatdestination.getpiecealliance();
				if(this.piecealliance != piecealliance)
				{
					legalmove.add(new move.majorattackmove(b,this,candidatedestinationcoordinate,pieceatdestination));
				}
				break;
				}
			}
			}
		}
		return ImmutableList.copyOf(legalmove);
	}
	private static boolean isfirstcolumnexclusion(final int currentposition,final int candidateoffset)
	{
		return boardutil.first_column[currentposition] && (candidateoffset==-1);
	}
	private static boolean iseightcolumnexclusion(final int currentposition,final int candidateoffset)
	{
		return boardutil.eight_column[currentposition] && (candidateoffset==1);
	}
	@Override
	public String toString()
	{
		return piecetype.ROOK.toString();
	}
	@Override
	public piece movepiece(move m) {
		return new rook(m.getmovedpiece().getpiecealliance(),m.getdestinationcoordinate());
	}

}

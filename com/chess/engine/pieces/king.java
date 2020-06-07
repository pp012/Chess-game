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

public class king extends piece{
	private static final int[] candidate_move_coordinate= {-9,-8,-7,-1,1,7,8,9};
	private final boolean kingsidecastlecapable;
	private final boolean queensidecastlecapable;
	private final boolean iscastled;
	public king(final alliance piecealliance,final int pieceposition,final boolean kingsidecastlecapable,final boolean queensidecastlecapable) {
		super(piecetype.KING,pieceposition, piecealliance,true);
		this.queensidecastlecapable=queensidecastlecapable;
		this.kingsidecastlecapable=kingsidecastlecapable;
		this.iscastled = false;
	}
	public king(final alliance piecealliance,final int pieceposition,final boolean isfirstmove,final boolean iscastled,final boolean kingsidecastlecapable,final boolean queensidecastlecapable) {
		super(piecetype.KING,pieceposition, piecealliance,isfirstmove);
		this.iscastled = iscastled;
		this.queensidecastlecapable=queensidecastlecapable;
		this.kingsidecastlecapable=kingsidecastlecapable;
	}
	public boolean iscastled()
	{
		return this.iscastled;
	}
	public boolean iskingsidecastlecapable()
	{
		return this.kingsidecastlecapable;
	}
	public boolean isqueensidecastlecapable()
	{
		return this.queensidecastlecapable;
	}
	@Override
	public Collection<move> calculatelegalmoves(board b) {
		final List<move> legalmoves=new ArrayList<>();
		for(final int currentcandidateoffset:candidate_move_coordinate)
		{
			final int candidatedestinationcoordinate=this.pieceposition+currentcandidateoffset;
			if(isfirstcolumnexclusion(this.pieceposition, currentcandidateoffset)
						||iseightcolumnexclusion(this.pieceposition, currentcandidateoffset))
			{
				
				continue;
			}
			if(boardutil.isvalidtilecoordinate(candidatedestinationcoordinate))
			{
				final tile candidatedestinationtile=b.gettile(candidatedestinationcoordinate);
				if(!candidatedestinationtile.istilloccupied())
				{
					legalmoves.add(new move.majormove(b,this,candidatedestinationcoordinate));
				}
				else
				{
				final piece pieceatdestination=candidatedestinationtile.getpiece();
				final alliance piecealliance=pieceatdestination.getpiecealliance();
				if(this.piecealliance != piecealliance)
				{
					legalmoves.add(new move.majorattackmove(b,this,candidatedestinationcoordinate,pieceatdestination));
				}
				}
			}
		}
		return ImmutableList.copyOf(legalmoves);
	}
	private static boolean isfirstcolumnexclusion(final int currentposition,final int candidateoffset)
	{
		return boardutil.first_column[currentposition] && (candidateoffset==-9||candidateoffset==-1||candidateoffset==7);
	}
	private static boolean iseightcolumnexclusion(final int currentposition,final int candidateoffset)
	{
		return boardutil.eight_column[currentposition] && (candidateoffset== 9 || candidateoffset == 1 ||candidateoffset==-7);
	}
	@Override
	public String toString()
	{
		return piecetype.KING.toString();
	}
	@Override
	public piece movepiece(move m) {
		return new king(m.getmovedpiece().getpiecealliance(),
						m.getdestinationcoordinate(),
						false,
						m.iscastlingmove(),
						false,
						false);
	}
}

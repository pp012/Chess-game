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

public class knight extends piece{
	private final static int[] candidate_move_coordinates= {-17,-15,-10,-6,6,10,15,17};
	public knight(final	alliance piecealliance,final int pieceposition) {
		super(piecetype.KNIGHT,pieceposition, piecealliance,true);
	}
	public knight(final	alliance piecealliance,final int pieceposition,final Boolean isfirstmove) {
		super(piecetype.KNIGHT,pieceposition, piecealliance,isfirstmove);
	}
	@Override
	public Collection<move> calculatelegalmoves(final board b) {
		List<move> legalmoves=new ArrayList<move>();
		for(final int currentcandidate:candidate_move_coordinates)
		{
			int candidatedestinationcoordinate=this.pieceposition + currentcandidate;
			if(boardutil.isvalidtilecoordinate(candidatedestinationcoordinate))
			{
				if(isfirstcolumnexclusion(this.pieceposition, currentcandidate) 
					|| issecondcolumnexclusion(this.pieceposition, currentcandidate) 
					|| isseventhcolumnexclusion(this.pieceposition, currentcandidate)
					|| iseightcolumnexclusion(this.pieceposition, currentcandidate))
				{
					continue;
				}
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
		return boardutil.first_column[currentposition] && (candidateoffset==-17||candidateoffset==-10||candidateoffset==-6||candidateoffset==15);
	}
	private static boolean issecondcolumnexclusion(final int currentposition,final int candidateoffset)
	{
		return boardutil.second_column[currentposition] && (candidateoffset== -10 || candidateoffset == 6);
	}
	private static boolean isseventhcolumnexclusion(final int currentposition,final int candidateoffset)
	{
		return boardutil.seventh_column[currentposition] && (candidateoffset== -6 || candidateoffset == 10);
	}
	private static boolean iseightcolumnexclusion(final int currentposition,final int candidateoffset)
	{
		return boardutil.eight_column[currentposition] && (candidateoffset== -15 || candidateoffset == -6 ||candidateoffset==10||candidateoffset==17 );
	}
	@Override
	public String toString()
	{
		return piecetype.KNIGHT.toString();
	}
	@Override
	public piece movepiece(move m) {
		return new knight(m.getmovedpiece().getpiecealliance(),m.getdestinationcoordinate());
	}
}

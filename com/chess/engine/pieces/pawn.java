package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.alliance;
import com.chess.engine.board.board;
import com.chess.engine.board.boardutil;
import com.chess.engine.board.move;
import com.chess.engine.board.move.pawnenpassantattackmove;
import com.chess.engine.pieces.piece.piecetype;
import com.google.common.collect.ImmutableList;

public class pawn extends piece
{
	private static final int[] candidate_move_coordinate= {8,16,7,9};
	public pawn(final alliance piecealliance,final int pieceposition) {
		super(piecetype.PAWN,pieceposition, piecealliance,true);
		
	}
	public pawn(final alliance piecealliance,final int pieceposition,final Boolean isfirstmove) {
		super(piecetype.PAWN,pieceposition, piecealliance,isfirstmove);
		
	}
	@Override
	public Collection<move> calculatelegalmoves(board b) 
	{
		final List<move> legalmoves=new ArrayList<>();
		for(final int currentcandidateoffset:candidate_move_coordinate)
		{
			final int candidatedestinationcoordinate=this.pieceposition + (this.getpiecealliance().getdirection() * currentcandidateoffset);		
			if(!boardutil.isvalidtilecoordinate(candidatedestinationcoordinate))
			{
				continue;
			}
			if(currentcandidateoffset==8 && !b.gettile(candidatedestinationcoordinate).istilloccupied())
			{
				if(this.piecealliance.ispawnpromotionsquare(candidatedestinationcoordinate))
				{
					legalmoves.add(new move.pawnpromotion(new move.pawnmove(b, this, candidatedestinationcoordinate)));
				}
				else
				{
				legalmoves.add(new move.pawnmove(b , this, candidatedestinationcoordinate));
				}
			}
			else if(currentcandidateoffset==16 && this.isfirstmove() && 
					((boardutil.seventh_rank(this.pieceposition) && this.getpiecealliance().isblack())|| (boardutil.second_rank(this.pieceposition) && this.getpiecealliance().iswhite())))
			{
				final int behindcandidatedestinationcoordinate=this.piecealliance.getdirection()*8+this.pieceposition;	
				if(!b.gettile(behindcandidatedestinationcoordinate).istilloccupied() && 
						!b.gettile(candidatedestinationcoordinate).istilloccupied())
				{
					legalmoves.add(new move.pawnjump(b, this, candidatedestinationcoordinate));
				}

			}
			else if(currentcandidateoffset==7 && !((boardutil.eight_column(this.pieceposition) 
					&& this.getpiecealliance().iswhite() || (boardutil.first_column(this.pieceposition) && this.piecealliance.isblack()))))
			{
				if(b.gettile(candidatedestinationcoordinate).istilloccupied())
				{
					final piece pieceoncandidate=b.gettile(candidatedestinationcoordinate).getpiece();
					if(this.piecealliance!=pieceoncandidate.getpiecealliance())
					{
						if(this.piecealliance.ispawnpromotionsquare(candidatedestinationcoordinate))
						{
							legalmoves.add(new move.pawnpromotion(new move.pawnmove(b, this, candidatedestinationcoordinate)));
						}
						else
						{
						legalmoves.add(new move.pawnattackmove(b, this, candidatedestinationcoordinate,pieceoncandidate));
						}
					}
				}
				else if(b.getenpassant()!=null)
				{
					if(b.getenpassant().getpieceposition()==(this.pieceposition + (this.piecealliance.getoppositedirection())))
					{
						final piece pieceoncandidate=b.getenpassant();
						if(this.piecealliance!=pieceoncandidate.getpiecealliance())
						{
							legalmoves.add(new move.pawnenpassantattackmove(b, this, candidatedestinationcoordinate, pieceoncandidate));
						}
					}
				}
			}
			else if(currentcandidateoffset==9 && !((boardutil.eight_column(this.pieceposition) 
					&& this.getpiecealliance().iswhite() || (boardutil.first_column(this.pieceposition) && this.piecealliance.isblack()))))
			{
				if(b.gettile(candidatedestinationcoordinate).istilloccupied())
				{
					final piece pieceoncandidate=b.gettile(candidatedestinationcoordinate).getpiece();
					if(this.piecealliance!=pieceoncandidate.getpiecealliance())
					{
						if(this.piecealliance.ispawnpromotionsquare(candidatedestinationcoordinate))
						{
							legalmoves.add(new move.pawnpromotion(new move.pawnmove(b, this, candidatedestinationcoordinate)));
						}
						else
						{
						legalmoves.add(new move.pawnattackmove(b, this, candidatedestinationcoordinate,pieceoncandidate));
						}
					}
				}
				else if(b.getenpassant()!=null)
				{
					if(b.getenpassant().getpieceposition()==(this.pieceposition -(this.piecealliance.getoppositedirection())))
					{
						final piece pieceoncandidate=b.getenpassant();
						if(this.piecealliance!=pieceoncandidate.getpiecealliance())
						{
							legalmoves.add(new move.pawnenpassantattackmove(b, this, candidatedestinationcoordinate, pieceoncandidate));
						}
					}
				}
			}
		}
		return ImmutableList.copyOf(legalmoves);
	}
	@Override
	public String toString()
	{
		return piecetype.PAWN.toString();
	}
	@Override
	public piece movepiece(move m) {
		return new pawn(m.getmovedpiece().getpiecealliance(),m.getdestinationcoordinate());
	}
	public piece getpromotionpiece()
	{
		return new queen(this.piecealliance, this.pieceposition,false);
	}
}

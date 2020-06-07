package com.chess.engine.board;

import com.chess.engine.board.board.builder;
import com.chess.engine.pieces.pawn;
import com.chess.engine.pieces.piece;
import com.chess.engine.pieces.rook;

public abstract class move 
{
	protected final board b;
	protected final piece movepiece;
	protected final int destinationcoordinate;
	protected final boolean isfirstmove;
	public static final move null_move=new nullmove();
	private move(final board b,final piece movepiece,final int destinationcoordinate)
	{
		this.b=b;
		this.movepiece=movepiece;
		this.destinationcoordinate=destinationcoordinate;
		this.isfirstmove=movepiece.isfirstmove();
	}
	private move(final board b,final int destinationcoordinate)
	{
		this.b=b;
		this.destinationcoordinate=destinationcoordinate;
		this.movepiece=null;
		this.isfirstmove=false;
	}
	@Override
	public int hashCode()
	{
		final int prime=31;
		int result=1;
		result=prime * result +this.destinationcoordinate;
		result=prime * result +this.movepiece.hashcode();
		result=prime * result +this.movepiece.getpieceposition();
		return result;
	}
	@Override
	public boolean equals(final Object other)
	{
		if(this==other)
		{
			return true;
		}
		if(!(other instanceof move))
		{
			return false;
		}
		final move othermove=(move) other;
		return getcurrentcoordinate()==othermove.getcurrentcoordinate() &&
			   getdestinationcoordinate()==othermove.getdestinationcoordinate() &&
			   getmovedpiece().equals(othermove.getmovedpiece());
	}
	public board getboard()
	{
		return this.b;
	}
	public boolean isattack()
	{
		return false;
	}
	public boolean iscastlingmove()
	{
		return false;
	}
	public piece getattackedpiece()
	{
		return null;
	}
	public int getcurrentcoordinate()
	{
		return this.getmovedpiece().getpieceposition();
	}
	public piece getmovedpiece()
	{
		return this.movepiece;
	}
	public int getdestinationcoordinate()
	{
		return this.destinationcoordinate;
	}
	public board execute() {
		final board.builder bu=new board.builder();
		for(final piece p:this.b.currentplayer().getactivepieces())
		{
			if(!this.movepiece.equals(p))
			{
				bu.setpiece(p);
			}
		}
		for(final piece p:this.b.currentplayer().getopponent().getactivepieces())
		{
			bu.setpiece(p);
		}
		bu.setpiece(this.movepiece.movepiece(this));
		bu.setmovemaker(this.b.currentplayer().getopponent().getalliance());
		return bu.build();
	}
	public static class majorattackmove extends attackmove
	{
		public majorattackmove(final board b,final piece movepiece,final int destinationcoordiante,final piece pieceattacked)
		{
			super(b, movepiece, destinationcoordiante, pieceattacked);
		}
		@Override
		public boolean equals(final Object other)
		{
			return this==other || other instanceof majorattackmove && super.equals(other);
		}
		@Override
		public String toString()
		{
			return movepiece.getpiecetype() + boardutil.getpositionatcoordinate(this.destinationcoordinate);
		}
	}
	public static final class majormove extends move
	{

		public majormove(final board b,final piece movepiece,final int destinationcoordinate) {
			super(b, movepiece, destinationcoordinate);
		}
		@Override
		public boolean equals(final Object other)
		{
			return this == other||other instanceof majormove && super.equals(other);
		}
		@Override
		public String toString()
		{
			return movepiece.getpiecetype().toString() + boardutil.getpositionatcoordinate(this.destinationcoordinate);
		}
		
	}
	public static class attackmove extends move
	{
		final piece attackedpiece;
		public attackmove(final board b,final piece movepiece,final int destinationcoordinate,final piece attackedpiece) {
			super(b, movepiece, destinationcoordinate);
			this.attackedpiece=attackedpiece;
		}
		@Override
		public int hashCode()
		{
			return this.attackedpiece.hashcode() + super.hashCode();
		}
		@Override
		public boolean equals(final Object other)
		{
			if(this==other)
			{
				return true;
			}
			if(!(other instanceof attackmove))
			{
				return false;
			}
			final attackmove otherattackmove=(attackmove) other;
			return super.equals(otherattackmove) && getattackedpiece().equals(otherattackmove.getattackedpiece());
		}
		@Override
		public boolean isattack()
		{
			return true;
		}
		@Override
		public piece getattackedpiece()
		{
			return this.attackedpiece;
		}
	}
	public static final class pawnmove extends move
	{
		public pawnmove(final board b,final piece movepiece,final int destinationcoordinate)
		{
			super(b,movepiece,destinationcoordinate);
		}
		@Override
		public boolean equals(final Object other)
		{
			return this==other|| other instanceof pawnmove && super.equals(other);
		}
		@Override
		public String toString()
		{
			return boardutil.getpositionatcoordinate(this.destinationcoordinate);
		}
	}	
	public static class pawnattackmove extends attackmove
	{
		public pawnattackmove(final board b,final piece movepiece,final int destinationcoordinate,final piece attackedpiece)
		{
			super(b,movepiece,destinationcoordinate,attackedpiece);
		}
		@Override
		public boolean equals(final Object other)
		{
			return this==other || other instanceof pawnattackmove && super.equals(other);
		}
		@Override
		public String toString()
		{
			return boardutil.getpositionatcoordinate(this.movepiece.getpieceposition()).substring(0, 1) + "x" + boardutil.getpositionatcoordinate(this.destinationcoordinate);
		}
	}
	public static final class pawnenpassantattackmove extends pawnattackmove
	{
		public pawnenpassantattackmove(final board b,final piece movepiece,final int destinationcoordinate,final piece attackedpiece)
		{
			super(b,movepiece,destinationcoordinate,attackedpiece);
		}
		@Override 
		public boolean equals(final Object other)
		{
			return this==other||other instanceof pawnenpassantattackmove && super.equals(other);
		}
		@Override
		public board execute()
		{
			final builder bu=new builder();
			for(final piece p:this.b.currentplayer().getactivepieces())
			{
				if(!this.movepiece.equals(p))
				{
					bu.setpiece(p);
				}
			}
			for(final piece p:this.b.currentplayer().getopponent().getactivepieces())
			{
				if(!p.equals(this.getattackedpiece()))
				{
					bu.setpiece(p);
				}
			}
			bu.setpiece(this.movepiece.movepiece(this));
			bu.setmovemaker(this.b.currentplayer().getopponent().getalliance());
			return bu.build();
		}
	}
	public static class pawnpromotion extends move
	{
		final move decoratedmove;
		final pawn promotedpawn;
		public pawnpromotion(final move decoratedmove)
		{
			super(decoratedmove.getboard(),decoratedmove.getmovedpiece(),decoratedmove.getdestinationcoordinate());
			this.decoratedmove=decoratedmove;
			this.promotedpawn=(pawn) decoratedmove.getmovedpiece();
		}
		@Override
		public int hashCode()
		{
			return decoratedmove.hashCode() +(31* promotedpawn.hashcode());
		}
		@Override
		public boolean equals(final Object other)
		{
			return this==other || other instanceof pawnpromotion && (super.equals(other));
		}
		@Override
		public board execute()
		{
			final board pawnmovedboard=this.decoratedmove.execute();
			final board.builder bu=new builder();
			for(final piece p:pawnmovedboard.currentplayer().getactivepieces())
			{
				if(!this.promotedpawn.equals(p))
				{
					bu.setpiece(p);
				}
			}
			for(final piece p:pawnmovedboard.currentplayer().getopponent().getactivepieces())
			{
				bu.setpiece(p);
			}
			bu.setpiece(this.promotedpawn.getpromotionpiece().movepiece(this));
			bu.setmovemaker(pawnmovedboard.currentplayer().getopponent().getalliance());
			return bu.build();
		}
		@Override
		public boolean isattack()
		{
			return this.decoratedmove.isattack();
		}
		@Override
		public piece getattackedpiece()
		{
			return this.decoratedmove.getattackedpiece();
		}
		@Override
		public String toString()
		{
			return boardutil.getpositionatcoordinate(this.getmovedpiece().getpieceposition())+"-"+boardutil.getpositionatcoordinate(this.destinationcoordinate)+"="+this.promotedpawn;
		}
	}
	public static final class pawnjump extends move
	{
		public pawnjump(final board b,final piece movepiece,final int destinationcoordinate)
		{
			super(b,movepiece,destinationcoordinate);
		}
		@Override
		public board execute()
		{
			final builder bu=new builder();
			for(final piece p:this.b.currentplayer().getactivepieces())
			{
				if(!this.movepiece.equals(p))
				{
				bu.setpiece(p);
				}
			}
			for(final piece p:this.b.currentplayer().getopponent().getactivepieces())
			{
				bu.setpiece(p);
			}
			final pawn movepawn=(pawn) this.movepiece.movepiece(this);
			bu.setpiece(movepawn);
			bu.setenpassant(movepawn);
			bu.setmovemaker(this.b.currentplayer().getopponent().getalliance());
			return bu.build();
		}
		@Override
		public String toString()
		{
			return boardutil.getpositionatcoordinate(this.destinationcoordinate);
		}
	}
	static abstract class castlemove extends move
	{
		protected final rook castlerook;
		protected final int castlerookstart;
		protected final int castlerookdestination;
		public castlemove(final board b,final piece movepiece,final int destinationcoordinate, final rook castlerook,final int castlerookstart,final int castlerookdestination)
		{
			super(b,movepiece,destinationcoordinate);
			this.castlerook=castlerook;
			this.castlerookstart=castlerookstart;
			this.castlerookdestination=castlerookdestination;
		}
		public rook getcastlerook()
		{
			return this.castlerook;
		}
		@Override
		public boolean iscastlingmove()
		{
			return true;
		}
		@Override 
		public board execute()
		{
			final builder bu=new builder();
			for(final piece p:this.b.currentplayer().getactivepieces())
			{
				if(!this.movepiece.equals(p) && !this.castlerook.equals(p))
				{
					bu.setpiece(p);
				}
			}
			for(final piece p:this.b.currentplayer().getopponent().getactivepieces())
			{
				bu.setpiece(p);
			}
			bu.setpiece(this.movepiece.movepiece(this));
			bu.setpiece(new rook(this.castlerook.getpiecealliance(),this.castlerookdestination));
			bu.setmovemaker(this.b.currentplayer().getopponent().getalliance());
			return bu.build();
		}
		@Override 
		public int hashCode()
		{
			final int prime=31;
			int result=super.hashCode();
			result=prime*result+this.castlerook.hashcode();
			result=prime*result+this.castlerookdestination;
			return result;
		}
		@Override
		public boolean equals(final Object other)
		{
			if(this==other)
			{
				return true;
			}
			if(!(other instanceof castlemove))
			{
				return false;
			}
			final castlemove othercastlemove =(castlemove)other;
			return super.equals(othercastlemove) && this.castlerook.equals(othercastlemove.getcastlerook());
		}
	}
	public static final class kingsidecastlemove extends castlemove
	{
		public kingsidecastlemove(final board b,final piece movepiece,final int destinationcoordinate, final rook castlerook,final int castlerookstart,final int castlerookdestination)
		{
			super(b,movepiece,destinationcoordinate,castlerook,castlerookstart,castlerookdestination);
		}
		@Override
		public boolean equals(final Object other)
		{
			return this==other || other instanceof kingsidecastlemove && super.equals(other);
		}
		public String toString()
		{
			return "0-0";
		}
	}
	public static final class queensidecastlemove extends castlemove
	{
		public queensidecastlemove(final board b,final piece movepiece,final int destinationcoordinate, final rook castlerook,final int castlerookstart,final int castlerookdestination)
		{
			super(b,movepiece,destinationcoordinate,castlerook,castlerookstart,castlerookdestination);
		}
		@Override
		public boolean equals(final Object other)
		{
			return this==other || other instanceof queensidecastlemove && super.equals(other);
		}
		@Override 
		public String toString()
		{
			return "0-0-0";
		}
	}
	public static final class nullmove extends move
	{
		public nullmove()
		{
			super(null,65);
		}
		@Override
		public board execute()
		{
			throw new RuntimeException("Cannot execute a null move!");
		}
		@Override
		public int getcurrentcoordinate()
		{
			return -1;
		}
	}
	public static class movefactory
	{
		private movefactory()
		{
			throw new RuntimeException("Not instantiable!");
		}
		public static move createmove(final board b,final int currentcoordidate,int destinationcoordinate)
		{
			for(final move m:b.getalllegalmoves())
			{
				if(m.getcurrentcoordinate()==currentcoordidate &&
					m.getdestinationcoordinate()==destinationcoordinate)
				{
					return m;
				}
			}
			return null_move;
		}
	}
}

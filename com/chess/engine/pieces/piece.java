package com.chess.engine.pieces;
import java.util.Collection;

import com.chess.engine.alliance;
import com.chess.engine.board.board;
import com.chess.engine.board.move;
public abstract class piece {
protected final int pieceposition;
protected final alliance piecealliance;
protected final boolean isfirstmove;
protected final piecetype pt;
private final int cachedhashcode;
piece(final piecetype pt,final int pieceposition,final alliance piecealliance,final Boolean isfirstmove)
{
	this.pt=pt;
	this.pieceposition=pieceposition;
	this.piecealliance=piecealliance;
	this.isfirstmove=isfirstmove;
	this.cachedhashcode=computehashcode();
}
private int computehashcode()
{
	int result=pt.hashCode();
	result=31*result + piecealliance.hashCode();
	result=31* result+ pieceposition;
	result=31* result+ (isfirstmove ? 1:0);
	return result;
}

@Override
public boolean equals(final Object other)
{
	if(this==other)
		{
			return true;
		}
	if(!(other instanceof piece))
			{
				return false;
			}
	final piece otherpiece=(piece ) other;
	return pieceposition==otherpiece.getpieceposition() && pt==otherpiece.getpiecetype() &&
			piecealliance==otherpiece.getpiecealliance() && isfirstmove==otherpiece.isfirstmove();
}
public int hashcode()
{
	return this.cachedhashcode;	
}
public boolean isfirstmove()
{
	return this.isfirstmove;
}
public int getpieceposition()
{
	return this.pieceposition;
}
public alliance getpiecealliance()
{
	return this.piecealliance;
}
public piecetype getpiecetype()
{
	return this.pt;
}
public int getpiecevalue()
{
	return this.pt.getpiecevalue();
}
public abstract piece movepiece(move m);
public abstract	Collection<move> calculatelegalmoves(final board b);
public enum piecetype
{
	PAWN("P",100) {
		@Override
		public boolean isking() {
			return false;
		}

		@Override
		public boolean isrook() {
			return false;
		}
	},
	KNIGHT("N",300) {
		@Override
		public boolean isking() {
			return false;
		}

		@Override
		public boolean isrook() {
			return false;
		}
	},
	BISHOP("B",300) {
		@Override
		public boolean isking() {
			return false;
		}

		@Override
		public boolean isrook() {

			return false;
		}
	},
	ROOK("R",500) {
		@Override
		public boolean isking() {
			return false;
		}

		@Override
		public boolean isrook() {
			return true;
		}
	},
	QUEEN("Q",900) {
		@Override
		public boolean isking() {
			return false;
		}

		@Override
		public boolean isrook() {
			return false;
		}
	},
	KING("K",10000) {
		@Override
		public boolean isking() {
			return true;
		}

		@Override
		public boolean isrook() {
			return false;
		}
	};
	 private String piecename;
	 private int piecevalue;
	piecetype(final String piecename,final int piecevalue)
	{
		this.piecename=piecename;
		this.piecevalue=piecevalue;
	}
	@Override
	public String toString()
	{
		return this.piecename;
	}
	public int getpiecevalue() {
		return this.piecevalue;
	}
	public abstract boolean isking();
	public abstract boolean isrook();
}
}

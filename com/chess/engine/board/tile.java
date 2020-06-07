package com.chess.engine.board;

import java.util.HashMap;
import java.util.Map;

import com.chess.engine.pieces.piece;
import com.google.common.collect.ImmutableMap;

public abstract class tile
 {
protected final int tilecoordinate;
private static final Map<Integer, emptytile>
empty_tiles_cache=createallpossibleemptytiles();
private tile(final int tilecoordinate) 
{	
	this.tilecoordinate=tilecoordinate;
}
public static tile createtile(final int tilecoordinate,final piece p)
{
	return p!=null ? new occupiedtile(tilecoordinate, p):empty_tiles_cache.get(tilecoordinate);
}
private static Map<Integer, emptytile> createallpossibleemptytiles() {
	final Map<Integer,emptytile>
	emptytilemap=new HashMap<>();
	for(int i=0;i<boardutil.num_tiles;i++)
	{
		emptytilemap.put(i, new emptytile(i));
	}
	return ImmutableMap.copyOf(emptytilemap);
}
public int gettilecoordinate()
{
	return this.tilecoordinate;
}
public abstract boolean istilloccupied();
public abstract piece getpiece();
public static final class emptytile extends tile
{
	private emptytile(final int coordinate)
	{
		super(coordinate);
	}
	@Override
	public boolean istilloccupied()
	{
		return false;
	}
	@Override
	public piece getpiece()
	{
		return null;
	}
	@Override
	public String toString()
	{
		return "-";
	}
}
public static final class occupiedtile extends tile
{
	private final piece pieceontile;
	private occupiedtile(final int tilecoordinate,piece pieceontile)
	{
		super(tilecoordinate);
		this.pieceontile=pieceontile;
	}
	@Override
	public boolean istilloccupied() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public piece getpiece() {
		// TODO Auto-generated method stub
		return this.pieceontile;
	}
	@Override
	public String toString()
	{
		return getpiece().getpiecealliance().isblack() ? getpiece().toString().toLowerCase():
			getpiece().toString();
	}
}
}


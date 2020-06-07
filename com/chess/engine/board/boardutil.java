package com.chess.engine.board;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

public class boardutil {
	public static final boolean[] first_column = initcolumn(0);
	public static final boolean[] seventh_column = initcolumn(6);
	public static final boolean[] eight_column = initcolumn(7);
	public static final	 boolean[] second_column=initcolumn(1);
	public static final int num_tiles=64;
	public static final int num_tiles_per_row=8;
	public static final boolean [] eigth_rank=initrow(0);
	public static final boolean [] seventh_rank=initrow(8);
	public static final boolean [] sixth_rank=initrow(16);
	public static final boolean [] fifth_rank=initrow(24);
	public static final boolean [] fourth_rank=initrow(32);
	public static final boolean [] third_rank=initrow(40);
	public static final boolean [] second_rank=initrow(48);
	public static final boolean [] first_rank=initrow(56);
	public static final String[] algaebric_notation=initializealgaebricnotation();
	public static final Map<String,Integer> position_to_coordinate=initializepositiontocoordinatemap();
	private static final int start_tile_index=0;
	private boardutil()
	{
		throw new RuntimeException("You cannot instantiate me!");
	}
	private static Map<String, Integer> initializepositiontocoordinatemap() {
		final Map<String, Integer> positiontocoordinate=new HashMap<>();
		for(int i=start_tile_index;i<num_tiles;i++)
		{
			positiontocoordinate.put(algaebric_notation[i], i);
		}
		return ImmutableMap.copyOf(positiontocoordinate);
	}
	private static String[] initializealgaebricnotation() {
		return new String[] {"a8","b8","c8","d8","e8","f8","g8","h8",
														"a7","b7","c7","d7","e7","f7","g7","h7",
														"a6","b6","c6","d6","e6","f6","g6","h6",
														"a5","b5","c5","d5","e5","f5","g5","h5",
														"a4","b4","c4","d4","e4","f4","g4","h4",
														"a3","b3","c3","d3","e3","f3","g3","h3",
														"a2","b2","c2","d2","e2","f2","g2","h2",
														"a1","b1","c1","d1","e1","f1","g1","h1"};
		
	}
	private static boolean[] initcolumn(int columnnumber) {
		final boolean [] column=new boolean[num_tiles];
		do
		{
			column[columnnumber]=true;
			columnnumber+=num_tiles_per_row;
		}while(columnnumber < num_tiles);
		return column;
	}
	private static boolean[] initrow(int rownumber) {
		final boolean [] row=new boolean[num_tiles];
		do
		{
			row[rownumber]=true;
			rownumber++;
		}while(rownumber%num_tiles_per_row!=0);
		return row;
	}
	public final static boolean isvalidtilecoordinate(final int coordinate)
	{
		return coordinate>=0 && coordinate<64;
	}
	public static boolean eigth_rank(int tileid) {
		if(tileid>=0 && tileid<=7)
		{
			return true;
		}
		return false;
	
	}
	public static boolean seventh_rank(int tileid) {
		if(tileid>=8 && tileid<=15)
		{
			return true;
		}
		return false;
	}
	public static boolean sixth_rank(int tileid) {
		if(tileid>=16 && tileid<=23)
		{
			return true;
		}
		return false;
	}
	public static boolean fifth_rank(int tileid) {
		if(tileid>=24 && tileid<=31)
		{
			return true;
		}
		return false;
	}
	public static boolean fourth_rank(int tileid) {
		if(tileid>=32 && tileid<=39)
		{
			return true;
		}
		return false;
	}
	public static boolean third_rank(int tileid) {
		if(tileid>=40 && tileid<=47)
		{
			return true;
		}
		return false;
	}
	public static boolean second_rank(int tileid) {
		if(tileid>=48 && tileid<=55)
		{
			return true;
		}
		return false;
	}
	public static boolean first_rank(int tileid) {
		if(tileid>=56 && tileid<=63)
		{
			return true;
		}
		return false;
	}
	public static boolean eight_column(int pieceposition) {
		return false;
	}
	public static boolean first_column(int pieceposition) {
		return false;
	}
	public static int getcoordinateatposition(final String position)
	{
		return position_to_coordinate.get(position);
	}
	public static  String getpositionatcoordinate(final int coordinate) {
		return algaebric_notation[coordinate];
	}
}

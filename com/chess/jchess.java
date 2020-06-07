package com.chess;
import com.chess.engine.board.board;
import com.chess.gui.table;

public class jchess {
public static void main(String[] args) {
	board b= board.createstandardboard();
	table.get().show();
}
}
 
package com.chess.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import com.chess.engine.board.*;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.chess.engine.board.board;

public class gamehistorypanel extends JPanel 
{
	private final datamodel model;
	private final JScrollPane scrollpane;
	private static final Dimension history_panel_dimension=new Dimension(150,400);
	public gamehistorypanel() 
	{
		this.setLayout(new BorderLayout());
		this.model=new datamodel();
		final JTable table=new JTable(model);
		table.setRowHeight(15);
		this.scrollpane=new JScrollPane(table);
		scrollpane.setColumnHeaderView(table.getTableHeader());
		scrollpane.setPreferredSize(history_panel_dimension);
		this.add(scrollpane,BorderLayout.CENTER);
		this.setVisible(true);
	}
	void redo(final board b,final table.movelog ml)
	{
		int currentrow=0;
		this.model.clear();
		for(final move m:ml.getmoves())
		{
			final String movetext=m.toString();
			if(m.getmovedpiece().getpiecealliance().iswhite())
			{
				this.model.setValueAt(movetext, currentrow, 0);
				System.out.println(movetext);
			}
			else if(m.getmovedpiece().getpiecealliance().isblack())
			{
				this.model.setValueAt(movetext, currentrow, 1);
				System.out.println(movetext);
				currentrow++;
			}
		}
		if(ml.getmoves().size()>0)
		{
			final move lastmove=ml.getmoves().get(ml.size()-1);
			final String movetext=lastmove.toString();
			if(lastmove.getmovedpiece().getpiecealliance().iswhite())
			{
				this.model.setValueAt(movetext + calculatecheckandcheckmatehash(b),currentrow,0);
			}
			else if(lastmove.getmovedpiece().getpiecealliance().isblack())
			{
				this.model.setValueAt(movetext + calculatecheckandcheckmatehash(b), currentrow-1, 1);
			}
		}
		final JScrollBar vertical=scrollpane.getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum());
	}
	private String calculatecheckandcheckmatehash(board b) {
		if(b.currentplayer().isincheckmate())
		{
			return "0";
		}
		else if(b.currentplayer().isincheck())
		{
		return "+";
		}
		return "";
	}
	private static class datamodel extends DefaultTableModel
	{
		private final List<row> values;
		private static final String[] names= {"white","black"};
		datamodel()
		{
			this.values=new ArrayList<>();
		}
		public void clear()
		{
			this.values.clear();
			setRowCount(0);
		}
		@Override
		public int getRowCount()
		{
			if(this.values==null)
			{
				return 0;
			}
			return this.values.size();
		}		
		@Override
		public int getColumnCount()
		{
			return names.length;
		}
		@Override
		public Object getValueAt(final int row,final int column)
		{
			final row currentrow=this.values.get(row);
			if(column==0)
			{
				return currentrow.getwhitemove();
			}
			else if(column==1)
			{
				return currentrow.getblackmove();
			}
			return null;
		}
		@Override
		public void setValueAt(final Object avalue,final int Row,final int column )
		{
			final row currentrow;
			if(this.values.size()<=Row)
			{
				currentrow=new row();
				this.values.add(currentrow);
			}
			else
			{
				currentrow=this.values.get(Row);
			}
			if(column==0)
			{
				currentrow.setwhitemove((String)avalue);
				fireTableRowsInserted(Row, Row);
			}
			else if(column==1)
			{
				currentrow.setblackmove((String)avalue);
				fireTableCellUpdated(Row, column);
			}
		}
		@Override
		public Class<?> getColumnClass(final int column)
		{
			return com.chess.engine.board.move.class;
		}
		@Override
		public String getColumnName(final int column)
		{
			return names[column];
		}
	}
	private static class row
	{
		private String whitemove;
		private String blackmove;
		row()
		{
			
		}
		public String getwhitemove()
		{
			return this.whitemove;
		}
		public String getblackmove()
		{
			return this.blackmove;
		}
		private void setwhitemove(final String move)
		{
			this.whitemove=move;
		}
		private void setblackmove(final String move)
		{
			this.blackmove=move;
		}
	}
}

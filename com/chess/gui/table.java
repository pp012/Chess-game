package com.chess.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import com.chess.engine.board.board;
import com.chess.engine.board.boardutil;
import com.chess.engine.board.tile;
import com.chess.engine.board.move;
import com.chess.engine.pieces.piece;
import com.chess.engine.player.movetransition;
import com.chess.engine.player.ai.minimax;
import com.chess.engine.player.ai.movestrategy;
import com.google.common.collect.Lists;

public class table extends Observable
{
	private final JFrame gameframe;
	private final gamesetup gs;
	private final takenpiecespanel tpl;
	private final gamehistorypanel ghp;
	private final boardpanel bp;
	private final movelog mo;
	private board chessboard;
	public Color darktilecolor;
	public Color lighttilecolor;
	private tile sourcetile;
	private tile destinationtile;
	private piece humanmovedpiece;
	private boarddirection bd;
	private move computermove;
	private boolean highlightlegalmoves;
	private final static Dimension outer_frame_dimension=new Dimension(600,400);
	private final static Dimension board_panel_dimension=new Dimension(400, 350);
	private final static Dimension tile_panel_dimension=new Dimension(10, 10);
	private static String defaultpieceimagepath="art/pieces/plain/";
	private static final table instance=new table();
	private table()
	{
		this.gameframe=new JFrame("JCHESS");
		darktilecolor=new Color(125, 135, 150);
		lighttilecolor=new Color(232,235,239);
		this.gameframe.setLayout(new BorderLayout());
		final JMenuBar tablemenubar=createtablemenubar();
		this.gameframe.setJMenuBar(tablemenubar);
		this.gameframe.setSize(outer_frame_dimension);
		this.chessboard=board.createstandardboard();
		this.tpl=new takenpiecespanel();
		this.ghp=new gamehistorypanel();
		this.bp=new boardpanel();
		this.mo=new movelog();
		this.addObserver(new tablegameaiwatcher());
		this.gs=new gamesetup(this.gameframe,true);
		this.bd=bd.normal;
		this.highlightlegalmoves=false;
		this.gameframe.add(this.tpl,BorderLayout.WEST);
		this.gameframe.add(this.bp,BorderLayout.CENTER);
		this.gameframe.add(this.ghp,BorderLayout.EAST);
		this.gameframe.setVisible(true);
	}
	public void updategameboard(final board b)
	{
		this.chessboard=b;
	}
	public void updatecomputermove(final move m)
	{
		this.computermove=m;
	}
	private movelog getmovelog()
	{
		return this.mo;
	}
	private gamehistorypanel getgamehistorypanel()
	{
	return this.ghp;
	}
	private takenpiecespanel gettakenpiecespanel()
	{
		return this.tpl;
	}
	public static table get()
	{
		return instance;
	}
	public void show()
	{
		table.get().getmovelog().clear();
		table.get().getgamehistorypanel().redo(chessboard,table.get().getmovelog());
		table.get().gettakenpiecespanel().redo(table.get().getmovelog());
		table.get().getboardpanel().drawboard(table.get().getgameboard());
	}
	private gamesetup getgamesetup()
	{
		return this.gs;
	}
	private board getgameboard()
	{
		return this.chessboard;
	}
	private boardpanel getboardpanel()
	{
		return this.bp;
	}
	private JMenuBar createtablemenubar() {
		final JMenuBar tablemenubar=new JMenuBar();
		tablemenubar.add(createfilemenu());
		tablemenubar.add(createpreferencemenu());
		tablemenubar.add(createoptionsmenu());
		return tablemenubar;
		
	}
	private JMenu createfilemenu() {
		final JMenu filemenu=new JMenu("File");
		final JMenuItem openPGN=new JMenuItem("Load PGN File");
		openPGN.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Open than pgn file");
			}
		});
		filemenu.add(openPGN);
		final JMenuItem exitmenuitem=new JMenuItem("Exit");
		exitmenuitem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
				
			}
		});
		filemenu.add(exitmenuitem);
		return filemenu;
	}
	private JMenu createpreferencemenu()
	{
		final JMenu preferencemenu=new JMenu("Preferences");
		final JMenuItem flipboardmenuitem=new JMenuItem("Flip Board");
		flipboardmenuitem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				bd=bd.opposite();
				bp.drawboard(chessboard);
			}
		});
		preferencemenu.add(flipboardmenuitem);
		final JCheckBoxMenuItem legalmovehighlightercheckbox=new JCheckBoxMenuItem("Highlight legal moves", false);
		legalmovehighlightercheckbox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				highlightlegalmoves=legalmovehighlightercheckbox.isSelected();
				
			}
		});
		preferencemenu.add(legalmovehighlightercheckbox);
		return preferencemenu;
	}
	private JMenu createoptionsmenu()
	{
		final JMenu optionsmenu=new JMenu("Options");
		final JMenuItem setupgamemenuitem=new JMenuItem("Setup Game");
		setupgamemenuitem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				table.get().getgamesetup().promptUser();
				table.get().setupupdate(table.get().getgamesetup());
			}
		});
		optionsmenu.add(setupgamemenuitem);
		return optionsmenu;
	}
	private void setupupdate(final gamesetup gs)
	{
		setChanged();
		notifyObservers(gs);
	}
	private static class tablegameaiwatcher implements Observer
	{

		@Override
		public void update(final Observable o,final Object arg) {
			if(table.get().getgamesetup().isaiplayer(table.get().getgameboard().currentplayer()) && 
				!table.get().getgameboard().currentplayer().isincheckmate() && 
				!table.get().getgameboard().currentplayer().isinstalemate())
			{
				final aithinktank thinktank=new aithinktank();
				thinktank.execute();
			}
			if(table.get().getgameboard().currentplayer().isincheckmate())
			{
				JOptionPane.showMessageDialog(table.get().getboardpanel(),
                        "Game Over: Player " + table.get().getgameboard().currentplayer() + " is in checkmate!", "Game Over",
                        JOptionPane.INFORMATION_MESSAGE);
			}
			if(table.get().getgameboard().currentplayer().isinstalemate())
			{
				JOptionPane.showMessageDialog(table.get().getboardpanel(),
                        "Game Over: Player " + table.get().getgameboard().currentplayer() + " is in stalemate!", "Game Over",
                        JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
	enum playertype{
		HUMAN,
		COMPUTER
	}
	private void movemadeupdate(final playertype pt)
	{
		setChanged();
		notifyObservers(pt);
	}
	private static final class aithinktank extends SwingWorker<move, String>
	{
		private aithinktank()
		{  
			
		}
		@Override 
		protected move doInBackground() throws Exception
		{
			final movestrategy minimax=new minimax(4);
			final move bestmove=minimax.execute(table.get().getgameboard());
			return bestmove;
		}
		@Override
		public void done()
		{
			try {
				final move bestmove=get();
				table.get().updatecomputermove(bestmove);
				table.get().updategameboard(table.get().getgameboard().currentplayer().makemove(bestmove).gettransitionboard());
				table.get().getmovelog().addmove(bestmove);
				table.get().getgamehistorypanel().redo(table.get().getgameboard(),table.get().getmovelog());
				table.get().gettakenpiecespanel().redo(table.get().getmovelog());
				table.get().getboardpanel().drawboard(table.get().getgameboard());
				table.get().movemadeupdate(playertype.COMPUTER);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}
	private class boardpanel extends JPanel
	{
		final List<tilepanel> boardtiles;
		boardpanel()
		{
			super(new GridLayout(8,8));
			this.boardtiles=new ArrayList<>();
			for(int i=0;i<boardutil.num_tiles;i++)
			{
				final tilepanel tp=new tilepanel(this,i);
				this.boardtiles.add(tp);
				add(tp);
			}
			setPreferredSize(board_panel_dimension);
			validate(); 
		}
		public void drawboard(final board b) {
			removeAll();
			for(final tilepanel tp:bd.traverse(boardtiles))
			{
				tp.drawtile(b);
				add(tp);
			}
			validate();
			repaint();
		}
	}
	enum boarddirection
	{
		normal
		{

			@Override
			List<tilepanel> traverse(List <tilepanel> boardtiles) {
				return boardtiles;
			}

			@Override
			boarddirection opposite() {
				return flipped;
			}
		},
		flipped
		{
			@Override
			List<tilepanel> traverse(List <tilepanel> boardtiles) {
				return Lists.reverse(boardtiles);
			}

			@Override
			boarddirection opposite() {
				return normal;
			}
		};
	abstract List<tilepanel> traverse(final List<tilepanel>boardtiles);
	abstract boarddirection opposite();
	}
	public static class movelog
	{
		private final List<move> moves;
		movelog()
		{
			this.moves=new ArrayList<>();
		}
		public List<move> getmoves()
		{
			return this.moves;
		}
		public void addmove(final move m)
		{
			this.moves.add(m);
		}
		public int size()
		{
			return this.moves.size();
		}
		public void clear()
		{
			this.moves.clear();
		}
		public boolean removemove(final move m)
		{
			return this.moves.remove(m);
		}
		public move removemove(int index)
		
		{
			return this.moves.remove(index);
		}
	}
	private class tilepanel extends JPanel
	{
		private final int tileid;
		tilepanel(final boardpanel bp,final int tileid)
		{
			super(new GridBagLayout());
			this.tileid=tileid;
			setPreferredSize(tile_panel_dimension);
			assigntilecolor();
			assigntilepieceicon(chessboard);
			addMouseListener(new MouseListener() {
				public void mouseClicked(MouseEvent e) {
					if(javax.swing.SwingUtilities.isRightMouseButton(e))
					{
						sourcetile=null;
						destinationtile=null;
						humanmovedpiece=null;
					}
					else if(javax.swing.SwingUtilities.isLeftMouseButton(e))
					{
						if(sourcetile==null)
						{
							sourcetile=chessboard.gettile(tileid);
							humanmovedpiece=sourcetile.getpiece();
							if(humanmovedpiece==null)
							{
								sourcetile=null;
							}
						}
						else
						{
							destinationtile=chessboard.gettile(tileid);
							final move m=move.movefactory.createmove(chessboard,sourcetile.gettilecoordinate(), destinationtile.gettilecoordinate());
							final movetransition transition=chessboard.currentplayer().makemove(m);
							if(transition.getmovestatus().isdone())
							{
								chessboard=transition.gettransitionboard();
								mo.addmove(m);
							}
							sourcetile=null;
							humanmovedpiece=null;
						}
		
						javax.swing.SwingUtilities.invokeLater(new Runnable()
						{ 
							@Override
							public void run() {
								ghp.redo(chessboard, mo);
								tpl.redo(mo);
								//if(gamesetup.isaiplayer(chessboard.currentplayer()))
								//{
									table.get().movemadeupdate(playertype.HUMAN);
								//}
							bp.drawboard(chessboard);
						}
							
						});
					}
				}

				@Override
				public void mousePressed(MouseEvent e) {
					
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					
				}

				@Override
				public void mouseExited(MouseEvent e) {
					
				}
			});
			validate();
		}
		public  void drawtile(final board b) {
			assigntilecolor();
			assigntilepieceicon(b);
			highlightlegals(b);
			validate();
			repaint();
		}
		private void assigntilepieceicon(final board b)
		{
			this.removeAll();
			if(b.gettile(this.tileid).istilloccupied())
			{
				try {
					final BufferedImage image =ImageIO.read(new File(defaultpieceimagepath+ b.gettile(this.tileid).getpiece().getpiecealliance().toString().substring(0, 1).toLowerCase()+b.gettile(this.tileid).getpiece().toString().toLowerCase()+".gif"));
					add(new JLabel(new ImageIcon(image)));
				} catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
		private void assigntilecolor()
		{
			if(boardutil.eigth_rank(this.tileid) ||
			   boardutil.sixth_rank(this.tileid) ||
			   boardutil.fourth_rank(this.tileid) ||
			   boardutil.second_rank(this.tileid))
			{
				setBackground(this.tileid %2==0 ? lighttilecolor:darktilecolor);
			}
			if(boardutil.seventh_rank(this.tileid) ||
			   boardutil.fifth_rank(this.tileid) ||
			   boardutil.third_rank(this.tileid) ||
			   boardutil.first_rank(this.tileid))
					{
						setBackground(this.tileid %2 !=0 ? lighttilecolor:darktilecolor);
					}
		}
		private void highlightlegals(final board b)
		{
			if(highlightlegalmoves)
			{
				for(final move m:piecelegalmoves(b))
				{
					if(m.getdestinationcoordinate()==this.tileid)
					{
						try {
							add(new JLabel(new ImageIcon(ImageIO.read(new File("art/misc/green_dot.png")))));
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		private Collection<move> piecelegalmoves(final board b)
		{
			if(humanmovedpiece!=null && humanmovedpiece.getpiecealliance()==b.currentplayer().getalliance())
			{
				return humanmovedpiece.calculatelegalmoves(b);
			}
			return Collections.emptyList();
		}
	}
}
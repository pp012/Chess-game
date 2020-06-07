package com.chess.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import com.chess.engine.pieces.piece;
import com.google.common.primitives.Ints;

public class takenpiecespanel extends JPanel	
{
	private final JPanel northpanel;
	private final JPanel southpanel;
	private static final Color panel_color=Color.decode("0xFDFE6");
	private static final EtchedBorder panel_border =new EtchedBorder(EtchedBorder.RAISED);
	private static final Dimension taken_pieces_panel_dimension=new Dimension(40,80);
	public takenpiecespanel()
	{
		super(new BorderLayout());
		setBackground(panel_color);
		setBorder(panel_border);
		this.northpanel=new JPanel(new GridLayout(8, 2));
		this.southpanel=new JPanel(new GridLayout(8, 2));
		this.northpanel.setBackground(panel_color);
		this.southpanel.setBackground(panel_color);
		add(this.northpanel,BorderLayout.NORTH);
		add(this.southpanel,BorderLayout.SOUTH);
		setPreferredSize(taken_pieces_panel_dimension);
	}
	public void redo(final table.movelog mo)
	{
		this.southpanel.removeAll();
		this.northpanel.removeAll();
		final List<piece> whitetakenpieces=new ArrayList<>();
		final List<piece> blacktakenpieces=new ArrayList<>();
		for(final com.chess.engine.board.move m:mo.getmoves())
		{
			if(m.isattack())
			{
				final piece takenpiece=m.getattackedpiece();
				if(takenpiece.getpiecealliance().iswhite())
				{
					whitetakenpieces.add(takenpiece);
				}
				else if(takenpiece.getpiecealliance().isblack())
				{
					blacktakenpieces.add(takenpiece);
				}
				else
				{
					throw new RuntimeException("Should not reach here!");
				}
			}
		}
		Collections.sort(whitetakenpieces,new Comparator<piece>() {
			@Override
			public int compare(piece o1,piece o2)
			{
				return Ints.compare(o1.getpiecetype().getpiecevalue(),o2.getpiecetype().getpiecevalue());
			}
		});
		Collections.sort(blacktakenpieces,new Comparator<piece>() {
			@Override
			public int compare(piece o1,piece o2)
			{
				return Ints.compare(o1.getpiecetype().getpiecevalue(),o2.getpiecetype().getpiecevalue());
			}
		});
		for(final piece takenpiece:whitetakenpieces)
		{
			try {
				final BufferedImage image=ImageIO.read(new File("art/pieces/plain/"
						+ takenpiece.getpiecealliance().toString().substring(0, 1).toLowerCase()+takenpiece.toString().toLowerCase()+".gif"));
				final ImageIcon icon=new  ImageIcon(image);
				final JLabel imagelabel=new JLabel(new ImageIcon(icon.getImage().getScaledInstance(icon.getIconWidth()-15, icon.getIconWidth()-15, image.SCALE_SMOOTH)));
				this.southpanel.add(imagelabel);			
				}catch(final IOException e)
			{
					e.printStackTrace();
			}
		}
		for(final piece takenpiece:blacktakenpieces)
		{
			try {
				final BufferedImage image=ImageIO.read(new File("art/pieces/plain/"
						+ takenpiece.getpiecealliance().toString().substring(0, 1).toLowerCase()+takenpiece.toString().toLowerCase()+".gif"));
				final ImageIcon icon=new  ImageIcon(image);
				final JLabel imagelabel=new JLabel(new ImageIcon(icon.getImage().getScaledInstance(icon.getIconWidth()-15, icon.getIconWidth()-15, image.SCALE_SMOOTH)));
				this.southpanel.add(imagelabel);			
				}catch(final IOException e)
			{
					e.printStackTrace();
			}
		}
		validate();
	}
}

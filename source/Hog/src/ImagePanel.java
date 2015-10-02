


/** @name: IMAGEPANEL
	@description: background GUI component for Hog
	@copyright: 2010 by L.A, modded from Swing Hacks.
*/

import java.awt.*;
import javax.swing.*;

//-Imaged Background
		public class ImagePanel extends JPanel {			
			private static final long serialVersionUID = -986206900479234990L;
			private Image img;
			
			public ImagePanel(Image img){
				this.img = img;
				Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
				
				setSize(size);
				setMinimumSize(size);
				setMaximumSize(size);
				setLayout(null);		
			}
			public void paintComponent(Graphics g){
				g.drawImage(img,0,0,null);
			}
		}
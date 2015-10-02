

/** @name: IMAGEBUTTON
	@description: button GUI component for Hog
	@copyright: 2010 by L.A, modded from Swing Hacks.
*/

import java.awt.*;
import javax.swing.*;

	//-Imaged Button
		public class ImageButton extends JButton {
			/**
			 * Eclipse keeps asking for a serialVersionID, so here it is
			 */
			private static final long serialVersionUID = 420630738095833199L;

			public ImageButton(String imgFileName, int locX, int locY){
				ImageIcon icon = new ImageIcon("images/"+imgFileName+".png");			
				setSize(icon.getImage().getWidth(null),
						icon.getImage().getHeight(null));
				setIcon(icon);
				setMargin(new Insets(0,0,0,0));
				setIconTextGap(0);
				setOpaque(false);
				setBorderPainted(false);
				setBorderPainted(false);
				setBorder(null);
				setText(null);
				setLocation(locX,locY);
				
				//states
				super.setPressedIcon(new ImageIcon("images/"+imgFileName+"-pressed.png"));
				super.setRolloverIcon(new ImageIcon("images/"+imgFileName+"-hover.png"));				
			}
		}

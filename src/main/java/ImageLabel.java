


/** @name: IMAGELABEL
	@description: label GUI component for Hog
	@copyright: 2010 by L.A, modded from Swing Hacks.
*/

import java.awt.Color;

import javax.swing.*;

	//-Imaged Label
		public class ImageLabel extends JLabel{
			/**
			 * Eclipse keeps asking for a serialVersionID, so here it is
			 */
			private static final long serialVersionUID = 8696110685231330757L;
			public String text = "";
			
			public ImageLabel(ImageIcon icoIcon, int iLocX, int iLocY){
				setSize(icoIcon.getImage().getWidth(null),
						icoIcon.getImage().getHeight(null));
				setVerticalTextPosition(JLabel.CENTER);
				setHorizontalTextPosition(JLabel.CENTER);
				setHorizontalAlignment(JLabel.LEFT);
				setIcon(icoIcon);
				setIconTextGap(0);
				setBorder(null);
				setText(null);
				setOpaque(false);
				setForeground(Color.lightGray);
				setLocation(iLocX,iLocY);
			}	
			public void append(String strWhat){
				if (this.getText() != null){
					String buff = this.getText();
					buff += strWhat;								
					this.setText(buff);
				}							
				else {									
					this.setText(strWhat);
				}
			}
			
			public void backspc(){
				if (this.getText() != null){
					String buff = this.getText();
					String buff2 = "";
					for (int ctr = 0;ctr<(buff.length()-1);ctr++){
						buff2 += buff.charAt(ctr);
					}													
					this.setText(buff2);
					this.text = buff2;
				}											
			}
			
			public void refreshLabel(){
				this.setText("");
				this.text = "";
			}
		}
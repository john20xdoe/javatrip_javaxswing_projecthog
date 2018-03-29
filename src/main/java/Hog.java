package main.hog;

/** @name: HOG
	@description: a GUI driven MPCO machine simulator
				A Java project submitted by:
				* Lee Alexis Bermejo
				* Jan Fredrick Nietes
				* Mienard Vidas
				* Neil Valenzuela
	@copyright: 2010 by L.A.
*/

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;

	public class Hog extends JFrame implements ActionListener{		
		/**
		 * Eclipse keeps asking for a serialVersionID, so here it is 
		 */
		private static final long serialVersionUID = -8385334260114731164L;
		
		private static File file = new File("HogStatus.txt");		
		//declare all gui components outside Hog constructor so they can be used by Hog's methods, instead of only inside the Hog constructor
		ImageLabel lblDescDisplay;					
		ImageLabel lblNumDisplay;																			
		ImageLabel lblDispenser;				
		ImageButton btnAdd10P;
		ImageButton btnAdd5P;
		ImageButton btnAdd1P;
		ImageButton btnAdd25C;
		ImageButton btnAdd10C;
		ImageButton btnInquire;						
		ImageButton btnWithdraw;							
		ImageButton btnChangePIN;	
		ImageButton btnNum1;
		ImageButton btnNum2;
		ImageButton btnNum3;
		ImageButton btnNum4;
		ImageButton btnNum5;
		ImageButton btnNum6;
		ImageButton btnNum7;
		ImageButton btnNum8;
		ImageButton btnNum9;
		ImageButton btnNum0;
		ImageButton btnNumC;
		ImageButton btnNumD;
		ImageButton btnNumOK;
		
		//Flags		
			//flag for operations
				private int opMode = 0; //operation mode
				int next = 0;		//holds opmode temporarily while PIN entry
				int pinflag = 1;	//which pin is currently being processed
				boolean apr = false; //approval				
				String pin1 = "", pin2 ="";
				boolean cfrmPin = false;
				int pinChangeChoice = 0;
				
		//constants				
		private static final int LINE10P = 1;
		private static final int LINE5P = 2;
		private static final int LINE1P = 3;
		private static final int LINE25C = 4;
		private static final int LINE10C = 5;
		private static final int LINEPIN1 = 6;
		private static final int LINEPIN2 = 7;
		
		//array for coin denominations, used in calculations
		private static final float[] denomsNormal = {10F,5F,1F,.25F,.10F};
		private static final int[] denoms = {1000,500,100,25,10};
		private int[] tmpAmtPerCoin = {0,0,0,0,0};
		private int[] avblAmtPerCoin = {Integer.parseInt(readFileLine(LINE10P)),
				Integer.parseInt(readFileLine(LINE5P)),
				Integer.parseInt(readFileLine(LINE1P)),
				Integer.parseInt(readFileLine(LINE25C)),
				Integer.parseInt(readFileLine(LINE10C))};
		
		//constructor
		public Hog(){
			/**
			 * This is the Hog constructor. Simply instantiates an object of type Hog. No parameters required.
			 */
			 
			//BUILDING the Hog GUI
			//1. window
			super("Hog: a GUI-driven MPCO Simulator");
				//1.1 set the Hog window's icon
					BufferedImage imgImage = null;
					try {
						imgImage = ImageIO.read(getClass().getResource("images/appicon.png"));
					} catch (IOException e) {
						e.printStackTrace();
					}
					setIconImage(imgImage);			
		
				//1.2 what Hog does on close 
					setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			//2. background of Hog
			ImagePanel wrap = new ImagePanel(new ImageIcon("images/bg.png").getImage());
				//2.1 then add the background to the window	
				getContentPane().add(wrap);
				
			//3. build the components (buttons, labels)	
				//3.1 Labels
					ImageLabel lblLogo = new ImageLabel(new ImageIcon("images/lblLogo.png"),9,489);					
					lblDescDisplay = new ImageLabel(new ImageIcon("images/lblDescDisplay.png"),46,226);
					lblDescDisplay.setFont(new Font("Tahoma",Font.PLAIN,20));										
					lblNumDisplay = new ImageLabel(new ImageIcon("images/lblNumDisplay.png"),46,281);					
					lblDispenser = new ImageLabel(new ImageIcon("images/lblDispenser.png"),191,508);
					lblDescDisplay.setText("Welcome to Hog.");					
					lblNumDisplay.setText("<html>Add coins: use the openings on top of the Hog machine.<br><br>Start a transaction: click a menu button above.<br></html>");	
					wrap.add(lblLogo);
					wrap.add(lblDescDisplay);
					wrap.add(lblNumDisplay);			
					wrap.add(lblDispenser);
					
				//3.2 Buttons
					//five new openings for each coin type					
					String[] btnOpeningImages = {"opening10P","opening5P","opening1P","opening25C","opening10C"};
					btnAdd10P = new ImageButton(btnOpeningImages[0],81,21);
					btnAdd5P = new ImageButton(btnOpeningImages[1],226,21);
					btnAdd1P = new ImageButton(btnOpeningImages[2],356,21);
					btnAdd25C = new ImageButton(btnOpeningImages[3],464,21);
					btnAdd10C = new ImageButton(btnOpeningImages[4],544,21);
					btnAdd10P.addActionListener(this);
					btnAdd5P.addActionListener(this);
					btnAdd1P.addActionListener(this);
					btnAdd25C.addActionListener(this);
					btnAdd10C.addActionListener(this);
					wrap.add(btnAdd10P);
					wrap.add(btnAdd5P);
					wrap.add(btnAdd1P);
					wrap.add(btnAdd25C);
					wrap.add(btnAdd10C);					
					
					//menu items for each transaction
					btnInquire = new ImageButton("btnInquire",46,130);						
					btnWithdraw = new ImageButton("btnWithdraw",258,130);							
					btnChangePIN = new ImageButton("btnChangePin",470,130);	
					btnInquire.addActionListener(this);
					btnWithdraw.addActionListener(this);
					btnChangePIN.addActionListener(this);
					wrap.add(btnInquire);
					wrap.add(btnWithdraw);
					wrap.add(btnChangePIN);
					
					//number pad buttons
					String[] strNumImg = {"btnNum1","btnNum2","btnNum3","btnNum4","btnNum5","btnNum6","btnNum7","btnNum8","btnNum9","btnNum0","btnNumC","btnNumD","btnNumOK"};

					btnNum1 = new ImageButton(strNumImg[0],472,219);
					btnNum2 = new ImageButton(strNumImg[1],536,219);
					btnNum3 = new ImageButton(strNumImg[2],598,219);
					btnNum4 = new ImageButton(strNumImg[3],472,274);
					btnNum5 = new ImageButton(strNumImg[4],536,274);
					btnNum6 = new ImageButton(strNumImg[5],598,274);
					btnNum7 = new ImageButton(strNumImg[6],472,329);
					btnNum8 = new ImageButton(strNumImg[7],536,329);
					btnNum9 = new ImageButton(strNumImg[8],598,329);
					btnNum0 = new ImageButton(strNumImg[9],472,385);
					btnNumC = new ImageButton(strNumImg[10],598,385);
					btnNumD = new ImageButton(strNumImg[11],536,385);
					btnNumOK = new ImageButton(strNumImg[12],472,440);
					btnNum1.addActionListener(this);
					btnNum2.addActionListener(this);
					btnNum3.addActionListener(this);
					btnNum4.addActionListener(this);
					btnNum5.addActionListener(this);
					btnNum6.addActionListener(this);
					btnNum7.addActionListener(this);
					btnNum8.addActionListener(this);
					btnNum9.addActionListener(this);
					btnNum0.addActionListener(this);
					btnNumD.addActionListener(this);
					btnNumC.addActionListener(this);
					btnNumOK.addActionListener(this);									
					wrap.add(btnNum1);										
					wrap.add(btnNum2);			
					wrap.add(btnNum3);
					wrap.add(btnNum4);
					wrap.add(btnNum5);
					wrap.add(btnNum6);										
					wrap.add(btnNum7);			
					wrap.add(btnNum8);
					wrap.add(btnNum9);
					wrap.add(btnNum0);
					wrap.add(btnNumC);
					wrap.add(btnNumD);										
					wrap.add(btnNumOK);													
			//4. size then show window
			ImageIcon bgImg = new ImageIcon("images/bg.png");		
			Dimension dFrmSize = new Dimension(bgImg.getIconWidth(), (bgImg.getIconHeight()+14));			
			//follow the size of the background image of the panel
			setSize(dFrmSize);
			setResizable(false);
			setVisible(true);
			
		} //end Hog()		
				
		//methods
			//file input ops
				/** Remember: the iLineNo parameter variable has a value range that includes:
					1  (for 10-Peso coin amount)
					2  (for 5-Peso coin amount)
					3  (for 1-Peso coin amount)
					4  (for 25-cents coin amount)
					5  (for 10-cents coin amount)
					6  (for PIN1)
					7  (for PIN2)
					These numbers correspond to their line positions in the HogStatus.txt file and 
					their corresponding constant declarations can be found above.
				*/
				
				public static String readFileLine(int iLineNo){
					String strLine ="";					
					try {
						BufferedReader inStream = new BufferedReader(new FileReader(file.getPath()));		    
						strLine =  inStream.readLine();
			    
						if(strLine != null){
							for (int ctr = 1; ctr <= iLineNo; ctr++){
								strLine = inStream.readLine();
							}
						}
						inStream.close();			
					}catch(FileNotFoundException e){
						JOptionPane.showMessageDialog(null,"Vault file not found (\"HogStatus.txt\")", "Fatal Error: File missing",JOptionPane.ERROR_MESSAGE);
						e.printStackTrace();
					}
					catch(IOException e){
						JOptionPane.showMessageDialog(null, "IOERROR: " + e.getMessage() + "\n");
						e.printStackTrace();				
					}
					return strLine;
				}
		
				public static void writeFileLine(int iLineNo, String strData){
					try {														
						String[] strFileLines = {"","","","","","","",""};
						for(int ctr = 0;ctr <= 7;ctr++){
							strFileLines[ctr] = readFileLine(ctr);
						}
						//save file contents to an array
						strFileLines[iLineNo] = (strData);

						PrintStream outStream =  new PrintStream(file.getPath());
						for (int ctr=0;ctr<=7;ctr++){
							outStream.println(strFileLines[ctr]); 
						}
						outStream.close();
					} catch (IOException e) {
						JOptionPane.showMessageDialog(null,"IOERROR: " + e.getMessage() + "\n");
						e.printStackTrace();
					}
				}
		
				public float totalAmount(){	
					int coins[] = {Integer.parseInt(readFileLine(LINE10P)),
							Integer.parseInt(readFileLine(LINE5P)),
							Integer.parseInt(readFileLine(LINE1P)),
							Integer.parseInt(readFileLine(LINE25C)),
							Integer.parseInt(readFileLine(LINE10C))};
					float totalAmount = 0F;
					for (int i = 0 ; i <= 4; i++){
						totalAmount += (coins[i]* denomsNormal[i]);
					}	
					return totalAmount;
				}
				
				public String getPIN1(){					
					return readFileLine(LINEPIN1); 
				}
				
				public String getPIN2(){
					return readFileLine(LINEPIN2);
				}
				
				public void setPIN1(String strNewPIN1){				
					writeFileLine(LINEPIN1, strNewPIN1);
				}
				
				public void setPIN2(String strNewPIN2){				
					writeFileLine(LINEPIN2, strNewPIN2);
				}
				
				public void addCoin(int iWhatCoin){
					int iThisCoinAmount = Integer.parseInt(readFileLine(iWhatCoin));
					iThisCoinAmount++;
					writeFileLine(iWhatCoin,Integer.toString(iThisCoinAmount));
				}
				
				public float reFormat(float num) {										
					DecimalFormat d = new DecimalFormat("0.##");
					d.setRoundingMode(RoundingMode.HALF_UP);					
					return (Float.parseFloat(d.format(num)));
				}
				
				public String reFormatComma(float num) {
					DecimalFormat d = new DecimalFormat("#,##0.##");
					d.setRoundingMode(RoundingMode.HALF_UP);
					return (d.format(num));
				}
				
				private boolean uses25or10C(String amt){
					int iAmt = Integer.parseInt(amt);										
					if ((iAmt%10==5)||(iAmt%5==0)) return true;					
					else return false;
				}
				
				private boolean has1Dot(){
					String strInput = lblNumDisplay.getText();
					//check for more than 1 decimal point
					int dotCtr = 0;
					for (int ctr = 0;ctr < strInput.length();ctr++){
						if (strInput.charAt(ctr) == '.'){
							dotCtr++;
						}
					}								
					if (dotCtr > 0)return false;
					else return true;
				}
						
				private boolean requires25(int amt){
					if ((amt%10)==5) return true;
					else return false;
				}
				
				private boolean isValidAmt(String amt){
					boolean a = false,b = false;
					float c = Float.parseFloat(amt);
					c = ((c*100)/100); //an attempt to generate a 0.000006 precision error
					float d = (reFormat(Float.parseFloat(amt)));					
					int x = (int) ((d) * 100);
					
					//if amount is not zero or less than zero
					if (x > 0) a = true;
					
					//if no precision error occurred on variable c, then input must have 2 or less decimal points, similar to d
					if (c == d) b = true;
					
					if ((uses25or10C(Integer.toString(x)))&&(a)&&(b)) return true;
					else return false;
				}
				
				private void showError(String strTitle,String strMsg){
					lblDescDisplay.setText("ERROR: "+strTitle);
					lblNumDisplay.refreshLabel();
					lblNumDisplay.setText("<html>"+strMsg+"</html>");
					opMode = 0;
				}
				
				//for resetting Hog (e.g. after a transaction)
				public void refreshMachine(){
					//reset variables
						for (int c = 0;c!=5;c++) tmpAmtPerCoin[c] = 0;
						opMode = 0;
						apr = false;
						cfrmPin = false;
						pinflag = 1;
						pinChangeChoice = 0;
						for (int ctr = 1;ctr!=6;ctr++){
							avblAmtPerCoin[ctr-1] = Integer.parseInt(readFileLine(ctr));								
						}
					//reset GUI labels							
						lblDescDisplay.setText("Welcome to Hog.");
						lblNumDisplay.setText("<html>Add coins: use the openings on top of the Hog machine.<br><br>Start a transaction: click a menu button above.<br></html>");	
						lblNumDisplay.text="";
						lblDispenser.setIcon(new ImageIcon("images/lblDispenser.png"));
				}
				
				public void commitTransaction(){
					//subtract withdrawal amount from amounts saved in file
					for (int ctr = 1;ctr != 6;ctr++){						
						writeFileLine((ctr),Integer.toString(avblAmtPerCoin[ctr-1] - tmpAmtPerCoin[ctr-1]));
					}
					lblDispenser.setIcon(new ImageIcon("images/lblDispenser2.png"));
				}	
				
				public void breakDown(int amt,int whatDenom){																
					tmpAmtPerCoin[whatDenom] = (amt/denoms[whatDenom]);
					if (tmpAmtPerCoin[whatDenom] <= avblAmtPerCoin[whatDenom]){
						amt -= (tmpAmtPerCoin[whatDenom]*denoms[whatDenom]);					
					}
					else {
						tmpAmtPerCoin[whatDenom] = avblAmtPerCoin[whatDenom];
						amt -= (tmpAmtPerCoin[whatDenom]*denoms[whatDenom]);						
					}			
					whatDenom++;
					if (amt > 0 ){
						if (whatDenom > 4) {
							if (requires25(amt)){
								if (tmpAmtPerCoin[3] > 0){
									tmpAmtPerCoin[3]--;									
									amt += (25 + (tmpAmtPerCoin[4]*denoms[4]));
									tmpAmtPerCoin[4] = 0;
									breakDown((amt),4);									
								}
								else {
									showError("Not enough coins.","You do not have enough 25 coins stored in Hog. Please revise your withdrawal amount. <br><br>Press ENTER then WITHDRAW to try again.");
									return;
								}
							}
							else {
								showError("Not enough coins.","You may not have enough centavo coins stored. Please check your coin balances or try withdrawing an amount without centavos.<br><br> Press ENTER then WITHDRAW to try again.");
								return;
							}
						}
						else breakDown(amt,whatDenom);
					}
					else if (amt < 0){
						showError("Unknown fatal error.", "Something went wrong. Your transaction will not be continued.<br><br>Press ENTER to try another transaction.");
						return;
					}
					else commitTransaction();					
				}			
				
				//actions and events				
				private void PINentryThenSet() {
					next = opMode;
					opMode = 1;
					lblNumDisplay.refreshLabel();
					lblDescDisplay.setText("Enter PIN1:");								
				} 
				
				private void showChar(String cWhat){
					//numpad listeners for pin input
					if (opMode == 1){
						if (lblNumDisplay.getText().length() <= 4){
							lblNumDisplay.append("*");
							lblNumDisplay.text += cWhat;
						}													
					}
					//numpad listeners for inquiry
					else if (opMode == 2){
						if (cWhat == "1"){
							lblDescDisplay.setText("Total amount of coins:");
												
							lblNumDisplay.setText("<html>You have "+reFormatComma(totalAmount())+" pesos.<br><br>Press . to do another transaction.</html>");						
						}
						else if (cWhat == "2"){
							lblDescDisplay.setText("10-centavo coin info:");
							int coin = Integer.parseInt(readFileLine(LINE10C));
							int amt = coin*denoms[4];
							lblNumDisplay.setText("<html>You have "+coin+" 10-centavo coins amounting to<br>"+(amt)+" centavos. (does not include 25-cent coins)<br><br>Press . to do another transaction.</html>");														
						}
						else if (cWhat == "3"){
							lblDescDisplay.setText("25-centavo coin info:");
							int coin = Integer.parseInt(readFileLine(LINE25C));
							int amt = coin*denoms[3];
							lblNumDisplay.setText("<html>You have "+coin+" 25-centavo coins amounting to<br>"+(amt)+" centavos.(does not include 10-cent coins)<br><br>Press . to do another transaction.</html>");														
						}
						else if (cWhat == "4"){
							lblDescDisplay.setText("1-peso coin info:");
							int coin = Integer.parseInt(readFileLine(LINE1P));
							int amt = (int) (coin*denomsNormal[2]);
							lblNumDisplay.setText("<html>You have "+coin+" 1-peso coins amounting to<br>"+(amt)+" pesos.<br><br>Press . to do another transaction.</html>");														
						}
						else if (cWhat == "5"){
							lblDescDisplay.setText("5-peso coin info:");
							int coin = Integer.parseInt(readFileLine(LINE5P));
							int amt = (int) (coin*denomsNormal[1]);
							lblNumDisplay.setText("<html>You have "+coin+" 5-peso coins amounting to<br>"+(amt)+" pesos.<br><br>Press . to do another transaction.</html>");														
						}
						else if (cWhat == "6"){
							lblDescDisplay.setText("10-peso coin info:");
							int coin = Integer.parseInt(readFileLine(LINE10P));
							int amt = (int) (coin*denomsNormal[0]);
							lblNumDisplay.setText("<html>You have "+coin+" 10-peso coins amounting to<br>"+(amt)+" pesos.<br><br>Press . to do another transaction.</html>");														
						}
						else if (cWhat == ".")	refreshMachine();																			
					}
					//numpad listeners for withdrawal
					else if (opMode == 3){
						if ((cWhat == ".")&&(has1Dot()==false))	return;
						else lblNumDisplay.append(cWhat);
					}
					//numpad listeners for change pins
					else if (opMode == 4){
						if (pinChangeChoice == 0){
							if (cWhat == "1"){
								pinflag = 1;
								lblDescDisplay.setText("Please enter your new PIN1:");
								lblNumDisplay.refreshLabel();
								pinChangeChoice = 1;
							}
							else if (cWhat == "2") {
								pinflag = 2;
								lblDescDisplay.setText("Please enter your new PIN2:");
								lblNumDisplay.refreshLabel();
								pinChangeChoice = 1;
							}
						}
						else {
							if (lblNumDisplay.text.length()<=4){
								lblNumDisplay.append("*");
								lblNumDisplay.text += cWhat;
							}
						}					
					}
				}
				
				
				public void actionPerformed(ActionEvent e){
					Object src = e.getSource();
					//1. Openings
						//add coins
						if (src == btnAdd10P) addCoin(LINE10P); 
						else if (src == btnAdd5P)	addCoin(LINE5P); 
						else if (src == btnAdd1P) addCoin(LINE1P);
						else if (src == btnAdd25C) addCoin(LINE25C);
						else if (src == btnAdd10C) addCoin(LINE10C);
					
					//2. Menu	
						else if (src == btnInquire){
							opMode = 2;
							PINentryThenSet();																																		
						}
						else if (src == btnWithdraw){
							opMode = 3;
							PINentryThenSet();
						}
						else if (src == btnChangePIN){
							opMode = 4;
							PINentryThenSet();
						}
						
					//3. Numpad	
						else if (src == btnNum1) showChar("1");
						else if (src == btnNum2) showChar("2");
						else if (src == btnNum3) showChar("3");
						else if (src == btnNum4) showChar("4");
						else if (src == btnNum5) showChar("5");
						else if (src == btnNum6) showChar("6");
						else if (src == btnNum7) showChar("7");
						else if (src == btnNum8) showChar("8");
						else if (src == btnNum9) showChar("9");
						else if (src == btnNum0) showChar("0");
						else if (src == btnNumD) showChar(".");
						else if (src == btnNumC){ 
							if ((opMode == 1)||(opMode == 3)||(opMode == 4)){
								if (lblNumDisplay.getText() == "") refreshMachine();
								else lblNumDisplay.backspc();									
							}	
							else if (opMode == 0) return;
							else lblNumDisplay.backspc();
						}
						else if (src == btnNumOK){
							if (opMode == 1){
								if (pinflag == 1){									
									if (lblNumDisplay.text.equals(getPIN1())){
										lblNumDisplay.refreshLabel();
										lblDescDisplay.setText("Enter PIN2:");
										pinflag = 2;
									} //wrong PIN1
									else lblDescDisplay.setText("Invalid PIN1!");																	
								}
								else { //pinflag == 2
									if (lblNumDisplay.text.equals(getPIN2())){
										refreshMachine();
										opMode = next;
										apr = true;
										if (opMode == 2){
											lblDescDisplay.setText("Please select which coin amount to view:");
											lblNumDisplay.setText("<html>Press the appropriate key on the numpad. <br>" +
													"[1] &nbsp;&nbsp;&nbsp;Total amount <br>" +
													"[2] &nbsp;&nbsp;&nbsp;10-centavo coin info<br>" +
													"[3] &nbsp;&nbsp;&nbsp;25-centavo coin info<br>" +
													"[4] &nbsp;&nbsp;&nbsp;1-peso coin info<br>" +
													"[5] &nbsp;&nbsp;&nbsp;5-peso coin info<br>" +
													"[6] &nbsp;&nbsp;&nbsp;10-peso coin info<br>" +
													"[.] &nbsp;&nbsp;&nbsp;Do other transaction" +
													"</html>");	
										}
										else if (opMode == 3){
											lblDescDisplay.setText("Please enter amount to withdraw:");
											lblNumDisplay.refreshLabel();
										}
										else if (opMode ==  4){
											lblDescDisplay.setText("Select which PIN to change:");
											lblNumDisplay.refreshLabel();
											lblNumDisplay.setText("<html>[1] &nbsp;&nbsp;&nbsp;PIN1 <br>" +
																"[2] &nbsp;&nbsp;&nbsp;PIN2<br><br><br><br><br></html>");
										}
									} //wrong PIN2
									else lblDescDisplay.setText("Invalid PIN2!");
								}																										
							}
							//else if (opMode == 2){} no need for this
							else if (opMode == 3){
								String strInput = lblNumDisplay.getText();
								if (isValidAmt(strInput)){									
									float fAmt = reFormat(Float.parseFloat(strInput));
									int fAmtInCents = (int) (fAmt * 100);									
									strInput = Integer.toString(fAmtInCents);
									if (fAmt > totalAmount()){
										showError("You do not have enough money.","Press ENTER then Inquire to check your balance; or,<br>Press ENTER then WITHDRAW to try again.");										
									}
									else{									
										//proceed with breakdown
										if (uses25or10C(strInput)){	
											lblDescDisplay.setText("Processing");
											lblNumDisplay.setText("Please wait.");
											opMode = 2; //so that pressing ENTER will do nothing
											breakDown(fAmtInCents,0);		
											opMode = 3; //return to withdrawal mode
											lblDescDisplay.setText("You have withdrawn:");
											lblNumDisplay.setText("<html>PhP"+ reFormatComma(fAmt) + " <br><br>Press ENTER to claim the money.</html>");										
											opMode = 0;
										}
										else lblDescDisplay.setText("ERROR: Invalid cents! Please revise amount.");										
									}
								}
								else {
									showError("Invalid withdrawal amount","Please enter an amount that has centavo portion that ends in 0 or 5.<br>" +
											"Please enter an amount that only has 2 decimal places.<br>" +
											"Please enter an amount that is greater than zero" +
											"<br><br>Press ENTER then Withdraw to try again.");
									opMode = 0;
								}
							}
							else if (opMode == 4){
								String buff = lblNumDisplay.text;
								if (buff.length() == 5){
									if ((pinflag == 1)&&(!cfrmPin)){
										pin1 = buff;
										cfrmPin = true;
										lblDescDisplay.setText("Confirm new PIN1:");
										lblNumDisplay.refreshLabel();
									}
									else if ((pinflag == 1)&&(cfrmPin)){
										if (buff.equals(pin1)) {
											setPIN1(pin1);
											lblDescDisplay.setText("New PIN1 has been set.");
											lblNumDisplay.refreshLabel();
											lblNumDisplay.setText("Press ENTER to return to do another transaction.");
											opMode = 0;
										}
										else { 
											lblDescDisplay.setText("Confirmation for new PIN1 does not match.");																					
										}
									}
									else if ((pinflag == 2)&&(!cfrmPin)){
										pin2 = buff;
										cfrmPin = true;
										lblDescDisplay.setText("Confirm new PIN2:");
										lblNumDisplay.refreshLabel();
									}
									else if ((pinflag == 2)&&(cfrmPin)){
										if (buff.equals(pin2)){ 
											setPIN2(pin2);
											lblDescDisplay.setText("New PIN2 has been set.");
											lblNumDisplay.refreshLabel();
											lblNumDisplay.setText("Press ENTER to return to do another transaction.");
											opMode = 0;
										}
										else { 
											lblDescDisplay.setText("Confirmation for new PIN2 does not match.");											
										}
									}
								}
							}
							else refreshMachine();
						} 
					
					//end of Numpad actionPerformed
				}
				
				//main method
				public static void main(String[] args){		
					Hog MyHog = new Hog();
				}
		}
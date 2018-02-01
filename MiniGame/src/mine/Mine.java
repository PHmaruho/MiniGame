package mine;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Mine extends JFrame implements StaticLength, ActionListener {

	JPanel pGame, pMenu;
	JButton btnStart;
	JLabel lblTimer;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		new Mine();
	}

	/**
	 * Create the frame.
	 */
	public Mine() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 1000);
		// setResizable(false);
		getContentPane().setLayout(null);

		pGame = new JPanel();
		pGame.setBackground(new Color(123, 104, 238));
		pGame.setBounds(0, 0, 800, 800);
		getContentPane().add(pGame);

		pMenu = new JPanel();
		pMenu.setBackground(new Color(100, 149, 237));
		pMenu.setBounds(0, 800, 800, 178);
		getContentPane().add(pMenu);
		pMenu.setLayout(null);

		btnStart = new JButton("start");
		btnStart.setBounds(37, 17, 117, 29);
		pMenu.add(btnStart);

		lblTimer = new JLabel("05:00");
		lblTimer.setFont(new Font("Lucida Grande", Font.PLAIN, 40));
		lblTimer.setBounds(220, 6, 139, 48);
		pMenu.add(lblTimer);

		pGame.setLayout(null);

		// action
		btnStart.addActionListener(this);

		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == btnStart) {
			if (btnStart.getText().equals("start")) {
				btnStart.setText("stop");

				pGame.removeAll();

				Pmine pMine = new Pmine();
				pGame.add(pMine);
				pGame.revalidate();
				pGame.repaint();
			} else {
				btnStart.setText("start");
			}
		}
	}

	// mouseAction // 오른쪽 버튼 눌렀을 경우
	public class MouseAction extends MouseAdapter implements StaticLength {
		JButton[][] btnMine;
		int[][] arrMine;

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			for (int i = 0; i < WIDTH_LENGTH; i++) {
				for (int j = 0; j < HEIGHT_LENGTH; j++) {
					if ((e.getButton() == 3) && e.getSource() == btnMine[i][j]) {
						if (btnMine[i][j].getText().equals("▼")) {
							btnMine[i][j].setText("?");
							btnMine[i][j].setFont(new Font("Arial", Font.PLAIN, 40));
						} else if (btnMine[i][j].getText().equals("?")) {
							btnMine[i][j].setText("");
							btnMine[i][j].setFont(new Font("Arial", Font.PLAIN, 13));
						} else if (btnMine[i][j].getText().equals("")) {
							btnMine[i][j].setText("▼");
							btnMine[i][j].setFont(new Font("Arial", Font.PLAIN, 40));
						}
					}
				}
			}
		}

		public void getBtn(JButton[][] btnMine, int[][] arrMine) {
			this.btnMine = btnMine;
			this.arrMine = arrMine;
		}
	}

	// minePanel
	public class Pmine extends JPanel implements StaticLength {

		private JButton[][] btnMine = new JButton[WIDTH_LENGTH][HEIGHT_LENGTH];
		private int[][] arrMine = new int[WIDTH_LENGTH][HEIGHT_LENGTH];

		public Pmine() {
			setBounds(0, 0, 800, 800);
			setLayout(new GridLayout(WIDTH_LENGTH, HEIGHT_LENGTH));

			btnMineAction btnAction = new btnMineAction();
			MouseAction mouseAction = new MouseAction();
			btnAction.getArr(arrMine, btnMine);
			for (int i = 0; i < WIDTH_LENGTH; i++) {
				for (int j = 0; j < HEIGHT_LENGTH; j++) {
					btnMine[i][j] = new JButton("");
					btnMine[i][j].setToolTipText("");

					btnMine[i][j].addActionListener(btnAction);
					btnMine[i][j].addMouseListener(mouseAction);

					add(btnMine[i][j]);

					// mineSetting
					arrMine[i][j] = 0;
				}
			}

			SettingMine settingMine = new SettingMine(arrMine, btnMine);

			mouseAction.getBtn(btnMine, arrMine);

			setVisible(true);
		}
	}

	// MineSetting
	public class SettingMine implements StaticLength {

		private Boolean breaking = true;
		
		private int[][] arrMine;
		private JButton[][] btnMine;

		public int[][] getArrMine() {
			return arrMine;
		}

		public SettingMine(int[][] arrMine, JButton[][] btnMine) {
			this.arrMine = arrMine;
			this.btnMine = btnMine;

			int random = 0;
			int totalCounter = 0;
			int maxMine = (int) Math.round((WIDTH_LENGTH * HEIGHT_LENGTH) / 5.0);
			int selectRandMine = (int) (Math.random() * maxMine);

			while (breaking) {
				for (int i = 0; i < WIDTH_LENGTH; i++) {
					for (int j = 0; j < HEIGHT_LENGTH; j++) {
						if (totalCounter == maxMine) {
							breaking = false;
							break;
						} else {
							random = (int) (Math.random() * maxMine);
							if (btnMine[i][j].getToolTipText().equals("*")) {
								continue;
							} else {
								if (selectRandMine == random) {
									btnMine[i][j].setToolTipText("*");
//									arrMine[i][j] = 1;
									totalCounter++;
								}
							}
						}
					}
				}
			} // while end
			
			/*
			 * test
			 */
			for (int i = 0; i < WIDTH_LENGTH; i++) {
				for (int j = 0; j < HEIGHT_LENGTH; j++) {
					if(!btnMine[i][j].getToolTipText().equals("*")) {
						System.out.print("^");
					} else {
						System.out.print(btnMine[i][j].getToolTipText());
					}
				}
				System.out.println("");
			}
			
			System.out.println("");
			
			mineAroundNum();
			
			for (int i = 0; i < WIDTH_LENGTH; i++) {
				for (int j = 0; j < HEIGHT_LENGTH; j++) {
					if(!btnMine[i][j].getToolTipText().equals("*")) {
						btnMine[i][j].setText(""+arrMine[i][j]);
					} else {
						btnMine[i][j].setText("*");
						btnMine[i][j].setFont(new Font("Arial", Font.PLAIN, 40));
					}
				}
				System.out.println("");
			}
			/*
			 * test
			 */
		}

		// 지뢰 주변에 숫자표시해주는 logic
		public void mineAroundNum() {
			for (int i = 0; i < WIDTH_LENGTH; i++) {
				for (int j = 0; j < HEIGHT_LENGTH; j++) {
					if(btnMine[i][j].getToolTipText().equals("*")) {
						// i==0
						if (i == 0) {

							// [0,0]
							if (j == 0) {
								for (int x = 0; x < 2; x++) {
									for (int y = 0; y < 2; y++) {
										if (btnMine[i + x][j + y].getToolTipText().equals("*")) {
											continue;
										} else {
											arrMine[i + x][j + y]++;
										}
									}
								}
							}

							// [0,10]
							else if (j == HEIGHT_LENGTH - 1) {
								for (int x = 0; x < 2; x++) {
									for (int y = -1; y < 1; y++) {
										if (btnMine[i + x][j + y].getToolTipText().equals("*")) {
											continue;
										} else {
											arrMine[i + x][j + y]++;
										}
									}
								}
							}

							// [0, 1] ~ [0, 9]
							else {
								for (int x = 0; x < 2; x++) {
									for (int y = -1; y < 2; y++) {
										if (btnMine[i + x][j + y].getToolTipText().equals("*")) {
											continue;
										} else {
											arrMine[i + x][j + y]++;
										}
									}
								}
							} // j==0 or j=10 or else....
						} // if(i==0)

						// i == 10
						else if (i == WIDTH_LENGTH - 1) {
							// [10,0]
							if (j == 0) {

								for (int x = -1; x < 1; x++) {
									for (int y = 0; y < 2; y++) {
										if (btnMine[i + x][j + y].getToolTipText().equals("*")) {
											continue;
										} else {
											arrMine[i + x][j + y]++;
										}
									}
								}
							}

							// [10,10]
							else if (j == HEIGHT_LENGTH - 1) {
								for (int x = -1; x < 1; x++) {
									for (int y = -1; y < 1; y++) {
										if (btnMine[i + x][j + y].getToolTipText().equals("*")) {
											continue;
										} else {
											arrMine[i + x][j + y]++;
										}
									}
								}
							}

							// [10, 1] ~ [10, 9]
							else {
								for (int x = -1; x < 1; x++) {
									for (int y = -1; y < 2; y++) {
										if (btnMine[i + x][j + y].getToolTipText().equals("*")) {
											continue;
										} else {
											arrMine[i + x][j + y]++;
										}
									}
								}
							}
						} // end (i==0 || i==10)

						// j==0 이면서 중복을 없애기 위해서 i!=0 || i!=10의 조건에 맞는 부분만 찾는다
						else if (j == 0 && (i != 0 || i != WIDTH_LENGTH)) {
							for (int x = -1; x < 2; x++) {
								for (int y = 0; y < 2; y++) {
									if (btnMine[i + x][j + y].getToolTipText().equals("*")) {
										continue;
									} else {
										arrMine[i + x][j + y]++;
									}
								}
							}
						}

						// j==10 이면서 중복을 없애기 위해서 i!=0 || i!=10의 조건에 맞는 부분만 찾는다
						else if (j == HEIGHT_LENGTH - 1 && (i != 0 || i != WIDTH_LENGTH)) {
							for (int x = -1; x < 2; x++) {
								for (int y = -1; y < 1; y++) {
									if (btnMine[i + x][j + y].getToolTipText().equals("*")) {
										continue;
									} else {
										arrMine[i + x][j + y]++;
									}
								}
							}
						}

						/*
						 * [-,-,-,-,-]
						 * [-,#,#,#,-]
						 * [-,#,#,#,-]
						 * [-,#,#,#,-]
						 * [-,-,-,-,-]
						 * #(shape) 내부를 검사하기위한 로직
						 */
						else {
							for (int x = -1; x < 2; x++) {
								for (int y = -1; y < 2; y++) {
									if (btnMine[i + x][j + y].getToolTipText().equals("*")) {
										continue;
									} else {
										arrMine[i + x][j + y]++;
									}
								}
							}
						} // end if~else
					}
					
					

				} // for (int j ......end
			} // for (int i ......end
		}
	} // SettingMine Class ENd

	// btnAction
	public class btnMineAction implements ActionListener, StaticLength {
		private int[][] arrMine;
		private JButton[][] btnMine;

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			for (int i = 0; i < WIDTH_LENGTH; i++) {
				for (int j = 0; j < HEIGHT_LENGTH; j++) {
					if (e.getSource() == btnMine[i][j]) {
						if(btnMine[i][j].getToolTipText().equals("*")) {
							btnMine[i][j].setFont(new Font("Arial", Font.PLAIN, 40));
							btnMine[i][j].setText("★");
						} else if(btnMine[i][j].getToolTipText().length() == 0){
							
							if(arrMine[i][j] == 0) { // 0일 경우
								btnMine[i][j].setText("");
								btnMine[i][j].setEnabled(false);
								// 폭탄이 아닌 버튼을 눌렀을 때, 주변에 빈공간이 있으면 확산한다.
//								spreadNull(i, j, arrMine, btnMine);
								
							} else { // 숫자가 있는 경우
								btnMine[i][j].setText("" + arrMine[i][j]);
								btnMine[i][j].setEnabled(false);
							}
						}
					}
				}
			}
		}

		public void getArr(int[][] arrMine, JButton[][] btnMine) {
			this.arrMine = arrMine;
			this.btnMine = btnMine;
		}
	}
	
	// 확산하는 method
	public void spreadNull(int i, int j, int[][] arrMine, JButton[][] btnMine) {
		int tempI = i, tempJ = j;
		// i==0
		if(i==0) {
			
			// j==0
			if(j==0) {
				while(true) { // 오른쪽 아래 대각선 검사
					if((arrMine[tempI][tempJ+1] != 0) 
							&& (arrMine[tempI+1][tempJ] != 0)
							&& (arrMine[tempI+1][tempJ+1] != 0)) {
						btnMine[tempI][tempJ+1].setText(arrMine[tempI][tempJ+1]+"");
						btnMine[tempI+1][tempJ].setText(arrMine[tempI+1][tempJ]+"");
						btnMine[tempI+1][tempJ+1].setText(arrMine[tempI+1][tempJ+1]+"");
						btnMine[tempI][tempJ+1].setEnabled(false);
						btnMine[tempI+1][tempJ].setEnabled(false);
						btnMine[tempI+1][tempJ+1].setEnabled(false);
						break;
					} else {
						for (int x = 1; x < WIDTH_LENGTH+1; x++) {
							if(arrMine[tempI][tempJ+1] != 0 && arrMine[tempI+x][tempJ] == 0 ) {
								btnMine[tempI+x][tempJ].setText("");
								btnMine[tempI+x][tempJ].setEnabled(false);
							} else {
								btnMine[tempI+x][tempJ].setText(arrMine[tempI+x][tempJ] + "");
								btnMine[tempI+x][tempJ].setEnabled(false);
								break;
							}
						}
						for (int y = 0; y < HEIGHT_LENGTH+1; y++) {
							if(arrMine[tempI+1][tempJ] != 0 && arrMine[tempI][tempJ+y] == 0 ) {
								btnMine[tempI][tempJ+y].setText("");
								btnMine[tempI][tempJ+y].setEnabled(false);
							} else {
								btnMine[tempI][tempJ+y].setText(arrMine[tempI][tempJ+y] + "");
								btnMine[tempI][tempJ+y].setEnabled(false);
								break;
							}
						}
						if((arrMine[tempI][tempJ+1] != 0) && (arrMine[tempI+1][tempJ] != 0)) {
							btnMine[tempI][tempJ+1].setText(arrMine[tempI][tempJ+1] + "");
							btnMine[tempI][tempJ+1].setEnabled(false);
							btnMine[tempI+1][tempJ].setText(arrMine[tempI+1][tempJ] + "");
							btnMine[tempI+1][tempJ].setEnabled(false);
							break;
						}
					}
					
					tempI += 1;
					tempJ += 1;
				}
			}
		}
	}

	public class ThreadTimers implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub

		}
	}

}

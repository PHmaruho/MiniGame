package mine;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*
 * FIX
 * 만약에 10x10 이면
 * 12x12로 만든다
 */

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

		private JButton[][] btnMine = new JButton[SETTING_WIDTH_LENGTH][SETTING_HEIGHT_LENGTH];
		private int[][] arrMine = new int[SETTING_WIDTH_LENGTH][SETTING_HEIGHT_LENGTH];

		public Pmine() {
			setBounds(0, 0, 800, 800);
			setLayout(new GridLayout(WIDTH_LENGTH, HEIGHT_LENGTH));

			btnMineAction btnAction = new btnMineAction();
			MouseAction mouseAction = new MouseAction();
			btnAction.getArr(arrMine, btnMine);
			for (int i = 0; i < SETTING_WIDTH_LENGTH; i++) {
				for (int j = 0; j < SETTING_HEIGHT_LENGTH; j++) {
					btnMine[i][j] = new JButton("");
					btnMine[i][j].setToolTipText("");

					if (i == 0 || i == SETTING_WIDTH_LENGTH - 1 || j == 0 || j == SETTING_HEIGHT_LENGTH - 1) {
						continue;
					} else {
						btnMine[i][j].addActionListener(btnAction);
						btnMine[i][j].addMouseListener(mouseAction);
						add(btnMine[i][j]);
					}

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
			int selectRandMine = (int) (Math.random() * MAX_MINE);

			while (breaking) {
				for (int i = MINUS_WIDTH; i <= WIDTH_LENGTH; i++) {
					for (int j = MINUS_HEIGHT; j <= HEIGHT_LENGTH; j++) {
						if (totalCounter == MAX_MINE) {
							breaking = false;
							break;
						} else {
							random = (int) (Math.random() * MAX_MINE);
							if (btnMine[i][j].getToolTipText().equals("*")) {
								continue;
							} else {
								if (selectRandMine == random) {
									btnMine[i][j].setToolTipText("*");
									totalCounter++;
								}
							}
						}
					}
				}
			} // while end

			mineAroundNum();
		}

		// 지뢰 주변에 숫자표시해주는 logic
		public void mineAroundNum() {
			for (int i = 0; i < SETTING_WIDTH_LENGTH; i++) {
				for (int j = 0; j < SETTING_HEIGHT_LENGTH; j++) {
					if (btnMine[i][j].getToolTipText().equals("*")) {
						/*
						 * [-,-,-,-,-] [-,#,#,#,-] [-,#,#,#,-] [-,#,#,#,-] [-,-,-,-,-] #(shape) 내부를
						 * 검사하기위한 로직
						 */
						for (int x = -1; x < 2; x++) {
							for (int y = -1; y < 2; y++) {
								if (btnMine[i + x][j + y].getToolTipText().equals("*")) {
									continue;
								} else {
									arrMine[i + x][j + y]++;
								}
							}
						}
					}
				} // for(j) END
			} // for(i) END

			for (int i = 0; i < SETTING_WIDTH_LENGTH; i++) {
				for (int j = 0; j < SETTING_HEIGHT_LENGTH; j++) {
					if (i == 0 || i == SETTING_WIDTH_LENGTH - 1) {
						arrMine[i][j] = 11; // 지뢰 주변엔 9 이상이 찍히지 않는다.
					}
					if (j == 0 || j == SETTING_HEIGHT_LENGTH - 1) {
						arrMine[i][j] = 11;
					}
				}
			}

			for (int i = 0; i < SETTING_WIDTH_LENGTH; i++) {
				for (int j = 0; j < SETTING_HEIGHT_LENGTH; j++) {

					if (btnMine[i][j].getToolTipText().equals("*")) {
						System.out.printf("%3d", 99);
					} else {
						System.out.printf("%3d", arrMine[i][j]);
					}
				}
				System.out.println("");
			}
			for (int i = 1; i < SETTING_WIDTH_LENGTH - 1; i++) {
				for (int j = 1; j < SETTING_HEIGHT_LENGTH - 1; j++) {

					if (btnMine[i][j].getToolTipText().equals("*")) {
						System.out.print(" " + btnMine[i][j].getToolTipText());
					} else {
						System.out.print(" -");
					}
				}
				System.out.println("");
			}
		}
	} // SettingMine Class ENd

	// btnAction
	public class btnMineAction implements ActionListener, StaticLength {
		private int[][] arrMine;
		private JButton[][] btnMine;

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			for (int i = MINUS_WIDTH; i < SETTING_WIDTH_LENGTH - 1; i++) {
				for (int j = MINUS_HEIGHT; j < SETTING_HEIGHT_LENGTH - 1; j++) {
					if (e.getSource() == btnMine[i][j]) {
						if (btnMine[i][j].getToolTipText().equals("*")) {
							btnMine[i][j].setIcon(reSize("img/mine1.png"));
							btnMine[i][j].setEnabled(false);
						} else if (btnMine[i][j].getToolTipText().length() == 0) {

							if (arrMine[i][j] == 0) { // 0일 경우

								// 폭탄이 아닌 버튼을 눌렀을 때, 주변에 빈공간이 있으면 확산한다.
								SpreadNull spreadNull = new SpreadNull(i, j, arrMine, btnMine);

							} else { // 숫자가 있는 경우
								btnMine[i][j].setText("" + arrMine[i][j]);
								btnMine[i][j].setFont(new Font("Arial", Font.PLAIN, 20));
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

	// 확산하는 method - error.....
	public class SpreadNull implements StaticLength {
		private int tempI, tempJ;
		private int[][] arrMine;
		private JButton[][] btnMine;

		public SpreadNull(int i, int j, int[][] arrMine, JButton[][] btnMine) {
			this.tempI = i;
			this.tempJ = j;
			this.arrMine = arrMine;
			this.btnMine = btnMine;

			upCheck(tempI, tempJ);
			downCheck(tempI, tempJ);
		}

		public void upCheck(int x, int y) {
			if (x == 0) {
				return;
			}
			for (int tempX = 0; tempX <= x; tempX++) {
				System.out.println("up");
				if ((x - tempX) == 0) {
					break;
				} else if (arrMine[x - tempX][y] == 0) {
					btnMine[x - tempX][y].setText("");
					btnMine[x - tempX][y].setEnabled(false);
					leftCheck(x - tempX, y - 1);
					rightCheck(x - tempX, y + 1);
				} else if (arrMine[x - tempX][y] != 0) {
					btnMine[x - tempX][y].setText(arrMine[x - tempX][y] + "");
					btnMine[x - tempX][y].setEnabled(false);
					if (arrMine[x - tempX][y - 1] != 0) {
						btnMine[x - tempX][y - 1].setText(arrMine[x - tempX][y - 1] + "");
						btnMine[x - tempX][y - 1].setEnabled(false);
					}
					if (arrMine[x - tempX][y + 1] != 0) {
						btnMine[x - tempX][y + 1].setText(arrMine[x - tempX][y + 1] + "");
						btnMine[x - tempX][y + 1].setEnabled(false);
					}
					leftCheck(x - tempX, y - 1);
					rightCheck(x - tempX, y + 1);
					break;
				}
			}
		}

		public void downCheck(int x, int y) {
			if (x == WIDTH_LENGTH) {
				return;
			}
			System.out.println("down");
			for (int tempX = 0; tempX <= WIDTH_LENGTH; tempX++) {
				if ((x + tempX) == WIDTH_LENGTH) {
					break;
				} else if (arrMine[x + tempX][y] == 0) {
					btnMine[x + tempX][y].setText("");
					btnMine[x + tempX][y].setEnabled(false);
					leftCheck(x + tempX, y - 1);
					rightCheck(x + tempX, y + 1);
				} else if (arrMine[x + tempX][y] != 0) {
					btnMine[x + tempX][y].setText(arrMine[x + tempX][y] + "");
					btnMine[x + tempX][y].setEnabled(false);
					if (arrMine[x + tempX][y - 1] != 0) {
						btnMine[x + tempX][y - 1].setText(arrMine[x + tempX][y - 1] + "");
						btnMine[x + tempX][y - 1].setEnabled(false);
					}
					if (arrMine[x + tempX][y + 1] != 0) {
						btnMine[x + tempX][y + 1].setText(arrMine[x + tempX][y + 1] + "");
						btnMine[x + tempX][y + 1].setEnabled(false);
					}
					leftCheck(x + tempX, y - 1);
					rightCheck(x + tempX, y + 1);
					break;
				}
			}
		}

		public void leftCheck(int x, int y) {
			if (y == 0) {
				return;
			}
			System.out.println("left");
			for (int tempY = 0; tempY <= y; tempY++) {
				if ((y - tempY) == 0) {
					break;
				} else if (arrMine[x][y - tempY] == 0) {
					btnMine[x][y - tempY].setText("");
					btnMine[x][y - tempY].setEnabled(false);
					upCheck(x - 1, y - tempY);
					downCheck(x + 1, y - tempY);
				} else if (arrMine[x][y - tempY] != 0) {
					btnMine[x][y - tempY].setText(arrMine[x][y - tempY] + "");
					btnMine[x][y - tempY].setEnabled(false);
					if (arrMine[x - 1][y - tempY] != 0) {
						btnMine[x - 1][y - tempY].setText(arrMine[x - 1][y - tempY] + "");
						btnMine[x - 1][y - tempY].setEnabled(false);
					}
					if (arrMine[x + 1][y - tempY] != 0) {
						btnMine[x + 1][y - tempY].setText(arrMine[x + 1][y - tempY] + "");
						btnMine[x + 1][y - tempY].setEnabled(false);
					}
					upCheck(x - 1, y - tempY);
					downCheck(x + 1, y - tempY);
					break;
				}
			}
		}

		public void rightCheck(int x, int y) {
			if (y == HEIGHT_LENGTH) {
				return;
			}
			System.out.println("right");
			for (int tempY = 0; tempY <= HEIGHT_LENGTH; tempY++) {
				if ((y + tempY) == HEIGHT_LENGTH) {
					break;
				} else if (arrMine[x][y + tempY] == 0) {
					btnMine[x][y + tempY].setText("");
					btnMine[x][y + tempY].setEnabled(false);
					upCheck(x - 1, y + tempY);
					downCheck(x + 1, y + tempY);
				} else if (arrMine[x][y + tempY] != 0) {
					btnMine[x][y + tempY].setText(arrMine[x][y + tempY] + "");
					btnMine[x][y + tempY].setEnabled(false);
					if (arrMine[x - 1][y + tempY] != 0) {
						btnMine[x - 1][y + tempY].setText(arrMine[x - 1][y + tempY] + "");
						btnMine[x - 1][y + tempY].setEnabled(false);
					}
					if (arrMine[x + 1][y + tempY] != 0) {
						btnMine[x + 1][y + tempY].setText(arrMine[x + 1][y + tempY] + "");
						btnMine[x + 1][y + tempY].setEnabled(false);
					}
					upCheck(x - 1, y + tempY);
					downCheck(x + 1, y + tempY);
					break;
				}
			}
		}
	}
	//
	// public class ThreadTimers implements Runnable {
	//
	// @Override
	// public void run() {
	// // TODO Auto-generated method stub
	//
	// }
	// }

	// imageResize
	public ImageIcon reSize(String filePath) {
		ImageIcon imageIcon = new ImageIcon(filePath);
		Image image = imageIcon.getImage();
		Image reSize = image.getScaledInstance(32, 32, 0);

		return (new ImageIcon(reSize));
	}

}

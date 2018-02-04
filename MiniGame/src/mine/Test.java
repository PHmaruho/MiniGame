package mine;

public class Test {
	
	public static void main(String[] args) {
		Test test = new Test();
		test.test01();
	}
	
	public void test01() {
		int x = 1;
		int y = 7;
		for (int i = 0; i < y-x-1; i++) {
			System.out.println("test");
		}
	}
//	for (int tempX = 0; tempX < x; tempX++) {
//		if((x-tempX)==0) {
//			break;
//		}
//		if(upRightCheck(x-tempX, y, x, y+1)) { }
//		if(leftUpCheck(x, y-1, x-tempX, y)) { }
//		
//		if(checkUR != 0 && checkLU != 0) {
//			break;
//		}
//		if(arrMine[x-tempX][y] == 0) {
//			btnMine[x-tempX][y].setText("");
//			btnMine[x-tempX][y].setEnabled(false);
//			leftCheck(x-tempX, y-1);
//			rightCheck(x-tempX, y+1);
//		} else {
//			if(checkUR != 0){
//				leftCheck(x-tempX, y-1);
//			}
//			if(checkLU != 0) {
//				rightCheck(x-tempX, y+1);
//			}
//		}
//	}
}

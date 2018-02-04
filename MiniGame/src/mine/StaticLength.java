package mine;

public interface StaticLength {
	/*
	 * if 13x13이면
	 * 15x15로 셋팅한다
	 */
	final int SETTING_WIDTH_LENGTH = 9;
	final int SETTING_HEIGHT_LENGTH = 9;
	
	final int MINUS_WIDTH = 1;
	final int MINUS_HEIGHT = 1;
	
	final int WIDTH_LENGTH = SETTING_WIDTH_LENGTH - (MINUS_WIDTH*2);
	final int HEIGHT_LENGTH = SETTING_HEIGHT_LENGTH - (MINUS_HEIGHT*2);
	
	// MINESETTING
	final int MAX_MINE = (int) Math.round((WIDTH_LENGTH * HEIGHT_LENGTH) / 7.0);
	
	
}

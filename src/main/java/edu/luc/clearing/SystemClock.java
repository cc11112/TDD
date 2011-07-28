package edu.luc.clearing;

public class SystemClock implements Clock {
	
	private static final int TWENTY_FIVE_SECONDS = 25 * 1000;
	
	public long currentTime() {
		return System.currentTimeMillis();
	}
	
	public boolean IsOverTime(long startTime){
		return currentTime() - startTime > TWENTY_FIVE_SECONDS;
	}

}

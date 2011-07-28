package edu.luc.clearing;

public interface Clock {
	long currentTime();
	boolean IsOverTime(long startTime);
}

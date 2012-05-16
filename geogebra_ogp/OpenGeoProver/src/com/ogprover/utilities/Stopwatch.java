/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.utilities;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for stop watch used for measuring of spent time</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class Stopwatch {
	private long startTime = 0;			// start current time
	private long endTime = 0;			// end current time
	private double timeIntMillisec = 0; // time interval in milliseconds between start and end time
	private double timeIntSec = 0;		// time interval in seconds between start and end time
	
	public void startMeasureTime() {
		this.startTime = System.currentTimeMillis();
	}
	
	public void endMeasureTime() {
		this.endTime = System.currentTimeMillis();
		this.timeIntMillisec = this.endTime - this.startTime;
		this.timeIntSec = this.timeIntMillisec / 1000.0;
	}
	
	public void endMeasureTimeAndContinue() {
		this.endMeasureTime();
		this.startTime = this.endTime;
	}
	
	public double getTimeIntMillisec() {
		if (this.startTime == 0)
			return 0;
		return this.timeIntMillisec;
	}
	
	public double getTimeIntSec() {
		if (this.startTime == 0)
			return 0;
		return this.timeIntSec;
	}
}
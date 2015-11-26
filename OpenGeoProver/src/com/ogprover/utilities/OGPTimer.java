/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.utilities;

import java.util.Timer;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for limitation of time</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class OGPTimer extends Timer {
	private boolean timeIsUp = false;
	private OGPTimerTask timerTask;
	
	/**
	 * @param timeIsUp the timeIsUp to set
	 */
	public void setTimeIsUp(boolean timeIsUp) {
		this.timeIsUp = timeIsUp;
	}
	/**
	 * @return the timeIsUp
	 */
	public boolean isTimeIsUp() {
		return timeIsUp;
	}
	/**
	 * @param timerTask the timerTask to set
	 */
	public void setTimerTask(OGPTimerTask timerTask) {
		this.timerTask = timerTask;
	}
	/**
	 * @return the timerTask
	 */
	public OGPTimerTask getTimerTask() {
		return timerTask;
	}
	
	/**
	 * Constructor method
	 */
	public OGPTimer() {
		// Constructor from superclass always associates new thread to
		// object of Timer class. Therefore, when timer is not needed
		// anymore it is good to call cancel() method so thread could
		// terminate gracefully.
		this.timeIsUp = false;
		this.timerTask = new OGPTimerTask(this);
	}
	
	/**
	 * Method to set the timer.
	 */
	public void setTimer(long timeLimit) {
		this.timeIsUp = false;
		// schedule new timer task for execution after the specified delay in milliseconds
		this.schedule(this.timerTask, timeLimit);
	}
}
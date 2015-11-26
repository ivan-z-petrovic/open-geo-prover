/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.utilities;

import java.util.TimerTask;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for one timer task used for limitation of time</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class OGPTimerTask extends TimerTask {

	private OGPTimer timer;
	
	public OGPTimerTask(OGPTimer timer) {
		this.timer = timer;
	}
	
	public OGPTimer getTimer() {
		return timer;
	}
	
	public void setTimer(OGPTimer timer) {
		this.timer = timer;
	}
	
	@Override
	public void run() {
		this.timer.setTimeIsUp(true); // set the flag that timer has been expired
		this.cancel(); // prevent further execution of this task
	}
	
}
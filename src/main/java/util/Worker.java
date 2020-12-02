package util;

public class Worker implements Runnable {
private volatile boolean isCycleRunning = false;
private final Object isCycleRunningLock = new Object();
private volatile int timeSinceLastCycle = interval;

public void run() {
	synchronized (isCycleRunningLock) {
		while (timer != null) {
			while (!isCycleRunning) {
				try {
					isCycleRunningLock.wait();
				} catch (InterruptedException e) {    
					if (debug >= 1) {
						DebugUtils.printMessage(this, 1,
								"Caught InterruptedException");
					}    
					Thread.currentThread().interrupt();
				}
			}
			try {
				board.step(timeSinceLastCycle);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			synchronized (boardViews) {
				for (BoardView v : boardViews) {
					v.redraw();
				}
			}
			isCycleRunning = false;
		}
	}
}
	
private boolean tryStartCycle(int timeSinceLastCycle) {
	if (isCycleRunning) {
		return false;
	} else {
		synchronized (isCycleRunningLock) {
			this.timeSinceLastCycle = timeSinceLastCycle;
			isCycleRunning = true;
			isCycleRunningLock.notify();
		}
		return true;
	}
}
}
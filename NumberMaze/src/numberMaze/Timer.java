package numberMaze;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Timer {

	private int count;
	private boolean isPaused;
	private PropertyChangeSupport pcs;
	private boolean dead;
	
	public Timer(){
		count = 16;
		isPaused = false;
		dead = false;
		pcs = new PropertyChangeSupport(this);
		Thread t = new Thread(){
			public void run(){
				while(!dead){
					while (!dead && isPaused){
						try {
							sleep(1000);
							System.out.println("i'm paused!");
						} catch (InterruptedException e1) {
						}
					}
					pcs.firePropertyChange("count", count, count-1);
					System.out.println(count);
					count--;
					if (count <= 0){
						isPaused = true;
					}
					try {
						sleep(1000);
					} catch (InterruptedException e) {
					}
				}
			}
		};
		t.start();
	}
	
	public int getCount(){
		return count;
	}
	
	public PropertyChangeSupport getPCS(){
		return pcs;
	}
	
	public void resetCount(){
		pcs.firePropertyChange("count", count, 16);
		System.out.println("fired!");
		count = 16;
		isPaused = false;
	}
	
	public void pauseCount(){
		isPaused = true;
	}
	
	public void selfDestruct(){
		dead = true;
	}
}

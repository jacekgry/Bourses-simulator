package model;

public class Clock implements Runnable {

	@Override
	public void run() {

		while (true) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			World.setDate(World.getDate().plusMinutes(1));
		}
	}

}

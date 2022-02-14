class SafePoint extends Thread {
	private int x;
	private int y;
	
	
	public SafePoint(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	
	public SafePoint(SafePoint safePoint) {
		this(safePoint.x, safePoint.y);
	}
	
	// Public factory method (to be commented if needed)
	public SafePoint cloneSafePoint(SafePoint originalSafePoint) {
		int[] xy = originalSafePoint.getXY();
		return new SafePoint(xy[0], xy[1]);
	}
	
	
	public int[] getXY() {
		synchronized (this) {
			return new int[] {x, y};
		}
	}
	
	
	public void setXY(int x, int y) {
		synchronized (this) {
			this.x = x;
			
			try {
				System.out.println("Thread " + Thread.currentThread().getName() + " is sleeping!");
				Thread.sleep(1000);
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			this.y = y;
		}
	}


	@Override
	public String toString() {
		return "[x=" + x + ", y=" + y + "]";
	}
}


public class Test extends Thread {
	public static void main(String[] args) {
		final SafePoint originalSafePoint = new SafePoint(1, 1);
		
		
		// One thread is trying to change this SafePoint
		// Thread-1
		new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("Thread " + Thread.currentThread().getName() + " has just begun!");
				originalSafePoint.setXY(2, 2);
				System.out.println("Original: " + originalSafePoint);
				System.out.println("Thread " + Thread.currentThread().getName() + " has just ended!");
			}
		}).start();
		
		
		System.out.println("Main thread is between first and second thread!");
		// The other Thread is trying to create a copy of original safe point. The copy, depending on the JVM, must be either (1,1) or (2,2).
		// depending on which Thread starts first, but it can not be (1,2) or (2,1) for example.
		// Thread-2
		new Thread(new Runnable() {	
			@Override
			public void run() {
				System.out.println("Thread " + Thread.currentThread().getName() + " has just begun!");
//				SafePoint copySafePoint = new SafePoint(originalSafePoint);
				SafePoint copySafePoint = originalSafePoint.cloneSafePoint(originalSafePoint);
				System.out.println("Copy: " + copySafePoint);
				System.out.println("Thread " + Thread.currentThread().getName() + " has just ended!");
			}
		}).start();
	}
}

// Main thread is between first and second thread!
// Thread Thread-1 has just begun!
// Thread Thread-1 is sleeping!
// Thread Thread-2 has just begun!
// Original: [x=2, y=2]
// Copy: [x=2, y=2]
// Thread Thread-1 has just ended!
// Thread Thread-2 has just ended!

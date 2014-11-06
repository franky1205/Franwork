package test.org.franwork.core;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class MutiThreadTest {
	
	private static boolean ended = false;

	@Test
	public void testThreadsError() throws InterruptedException {
//		Thread first = new Thread(new Runnable() {
//			public void run() {
//				throw new RuntimeException("Thread One");
//			}
//		});
//		Thread second = new Thread(new Runnable() {
//			public void run() {
//				throw new RuntimeException("Thread Two");
//			}
//		});
//		first.start();
//		second.start();
		ThreadPoolExecutor es = new ThreadPoolExecutor(5, 10, 20, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(20));
		es.execute(new Runnable() {
			public void run() {
				throw new RuntimeException("Thread One");
			}
		});
		es.execute(new Runnable() {
			public void run() {
				throw new RuntimeException("Thread Two");
			}
		});
		es.execute(new Runnable() {
			public void run() {
				throw new RuntimeException("Thread Three");
			}
		});
		es.execute(new Runnable() {
			public void run() {
				throw new RuntimeException("Thread Four");
			}
		});
		es.execute(new Runnable() {
			public void run() {
				MutiThreadTest.ended = true;
			}
		});
		Thread.sleep(5000);
		System.out.println(ended);
	}
}

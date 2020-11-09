package demo;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 思考有多少种方式，在 main 函数启动一个新线程，运行一个方法，拿到这个方法的返回值后，退出主线程？
 * 
 * @author kevinhuangwl
 *
 */
public class ConcurrentDemo {

	static ExecutorService exec = Executors.newSingleThreadExecutor();

	public static void main(String[] args) throws Exception {
		spinWaiting();
		useWaitAndNotify();
		useCondition();
		useBlockingQueue();
		useCompletableFuture();
		useCountDownLatch();
		useCyclicBarrier();
		useFuture();
		useJoin();
		useSemaphore();
		exec.shutdown();
	}

	private static int fibo(int n) {
		if (n == 1)
			return 1;
		if (n == 2)
			return 2;
		return fibo(n - 1) + fibo(n - 2);
	}

	private static void spinWaiting() {
		final AtomicInteger i = new AtomicInteger(0);
		new Thread(() -> {
			i.set(fibo(30));
		}).start();

		while (i.get() == 0)
			;
		System.out.println("spinWaiting:" + i);
	}

	private static Object lock = new Object();

	private static void useWaitAndNotify() throws InterruptedException {
		final AtomicInteger i = new AtomicInteger(0);
		new Thread(() -> {
			synchronized (lock) {
				i.set(fibo(30));
				lock.notify();
			}

		}).start();
		synchronized (lock) {
			lock.wait();
		}
		System.out.println("useWaitAndNotify:" + i);
	}

	private static void useCondition() throws InterruptedException {
		Lock lock = new ReentrantLock();
		Condition isDone = lock.newCondition();
		final AtomicInteger i = new AtomicInteger(0);
		new Thread(() -> {
			lock.lock();
			try {
				i.set(fibo(30));
				isDone.signal();
			} finally {
				lock.unlock();
			}

		}).start();
		lock.lock();
		try {
			isDone.await();
		} finally {
			lock.unlock();
		}
		System.out.println("useCondition:" + i);
	}

	private static void useJoin() throws InterruptedException {
		final AtomicInteger i = new AtomicInteger(0);
		Thread t = new Thread(() -> {
			i.set(fibo(30));
		});
		t.start();
		t.join();
		System.out.println("useJoin:" + i);
	}

	private static void useBlockingQueue() throws InterruptedException {
		final ArrayBlockingQueue<Integer> q = new ArrayBlockingQueue<Integer>(10);
		new Thread(() -> {
			q.offer(fibo(30));
		}).start();

		System.out.println("useBlockingQueue:" + q.take());
	}

	private static void useFuture() throws InterruptedException, ExecutionException {
		Future<Integer> f = exec.submit(() -> {
			return fibo(29);
		});

		System.out.println("useFuture:" + f.get());
	}

	private static void useCompletableFuture() throws InterruptedException, ExecutionException {
		CompletableFuture<Integer> cf = CompletableFuture.supplyAsync(() -> {
			return fibo(30);
		});
		System.out.println("useCompletableFuture:" + cf.get());
	}

	private static void useSemaphore() throws InterruptedException {
		Semaphore s = new Semaphore(2);
		final AtomicInteger i = new AtomicInteger(0);
		s.acquire();
		new Thread(() -> {
			i.set(fibo(30));
			s.release();
		}).start();
		s.acquire(2);
		System.out.println("useSemaphore:" + i);
	}

	private static void useCountDownLatch() throws InterruptedException {
		CountDownLatch cdl = new CountDownLatch(1);
		final AtomicInteger i = new AtomicInteger(0);
		new Thread(() -> {
			i.set(fibo(30));
			cdl.countDown();
		}).start();
		cdl.await();
		System.out.println("useCountDownLatch:" + i);
	}

	private static void useCyclicBarrier() throws InterruptedException, BrokenBarrierException {
		final AtomicInteger i = new AtomicInteger(0);
		CyclicBarrier cb = new CyclicBarrier(2, () -> {
			System.out.println("useCyclicBarrier:" + i);
		});
		new Thread(() -> {
			i.set(fibo(30));
			try {
				cb.await();
			} catch (InterruptedException | BrokenBarrierException e) {
				e.printStackTrace();
			}
		}).start();
		cb.await();
	}
}

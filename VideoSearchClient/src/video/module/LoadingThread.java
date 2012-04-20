package video.module;

import java.util.LinkedList;
import java.util.Queue;

class LoadingThread {
	private static LoadingThread instance;
	
	public static void run(Runnable runnable){
		if(instance == null){
			createInstance();
		}
		instance.addTask(runnable);
	}
	
	private static void createInstance(){
		instance = new LoadingThread();
	}
	
	private MyThread thread;
	
	private LoadingThread(){
		thread = new MyThread();
		thread.start();
	}
	
	private class MyThread extends Thread {
		private Queue<Runnable> queue = new LinkedList<Runnable>();
		
		@Override
		public void run() {
			while(true){
				executeTasks();
				try {
					Thread.sleep(1000);
				} catch(InterruptedException e){
				}
			}
		}
		
		public void addTask(Runnable r){
			queue.offer(r);
		}
		
		private void executeTasks(){
			while(true){
				Runnable r = queue.poll();
				if(r == null){
					break;
				}
				r.run();
			}
		}
	}
	
	private void addTask(Runnable runnable){
		thread.addTask(runnable);
	}
}
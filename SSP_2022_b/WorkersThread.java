package com.lgcns.test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class WorkersThread implements Runnable {
	
	private int myProcessNum;
	private int processCount;
	private int myThreadNum;
	private int threadCount;
	private int totalQueueCount;
	private String[] inputURIs;
	// output queue per process
	private final BlockingQueue<Output> outputQueue;
	// workers of thread
	private Map<Integer, Worker> workerMap = new HashMap<Integer, Worker>();
	// input queue of thread
	private BlockingQueue<Input> inputQueue = new LinkedBlockingQueue<Input>();

	public WorkersThread(int myProcessNum, int processCount, int myThreadNum, int threadCount, int totalQueueCount
			, String[] inputURIs, BlockingQueue<Output> outputQueue) {
		this.myProcessNum = myProcessNum;
		this.processCount = processCount;
		this.myThreadNum = myThreadNum;
		this.threadCount = threadCount;
		this.totalQueueCount = totalQueueCount;
		this.inputURIs = inputURIs;
		this.outputQueue = outputQueue;
	}

	@Override
	public void run() {
		// queueNum별 input request runner 생성
		int queueNum = myProcessNum*threadCount + myThreadNum;
		while(queueNum < totalQueueCount) {
			// create request runner
			String inputURI = this.inputURIs[queueNum];
			InputRequestRunner workerRunner = new InputRequestRunner(queueNum, inputURI, inputQueue);
			
			Thread t = new Thread(workerRunner);
			t.start();
			
			// next queue number
			queueNum += processCount*threadCount;
		}

		// create worker
		queueNum = myProcessNum*threadCount + myThreadNum;
		while(queueNum < totalQueueCount) {
			Worker worker = new Worker(queueNum);
			workerMap.put(queueNum, worker);
			// next queue number
			queueNum += processCount*threadCount;
		}
		
		// input queue monitoring - input에 대해 worker 실행 및 output queue로 전달
		while(true) {
			if(!this.inputQueue.isEmpty()) {
				Input input = this.inputQueue.poll();
				Worker worker = workerMap.get(input.getQueueNum());
				// worker 실행
				String result = worker.run(input.getTimestamp(), input.getValue());
				// Output Queue에 출력
				if(result != null) {
					try {
						outputQueue.put(new Output(result));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

}

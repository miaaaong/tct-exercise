package programmers.level2.printer;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class Solution {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		int[] priorities = {2, 1, 3, 2};
//		int location = 2;
		int[] priorities = {1, 1, 9, 1, 1, 1};
		int location = 0;
		System.out.println(solution(priorities, location));
	}

    public static int solution(int[] priorities, int location) {
    	Queue<Integer> jobQueue = new ArrayBlockingQueue<Integer>(priorities.length);
    	Queue<Integer> idxQueue = new ArrayBlockingQueue<Integer>(priorities.length);
    	Queue<Integer> finishQueue = new ArrayBlockingQueue<Integer>(priorities.length);
    	for(int i=0; i<priorities.length; i++) {
    		jobQueue.add(priorities[i]);
    		idxQueue.add(i);
    	}
    	
    	while(!jobQueue.isEmpty()) {
    		// ù��° �۾� �켱����
    		int prior = jobQueue.element();
    		boolean flag = false;
    		Iterator<Integer> iterator = jobQueue.iterator();
    		while(iterator.hasNext()) {
    			Integer target = iterator.next();
    			if(target > prior) {
    				flag = true;
    				break;
    			}
    		}
    		Integer job = jobQueue.poll();
    		Integer idx = idxQueue.poll();
    		if(flag) {
    			// �켱������ ������ �ڷ� ����
    			jobQueue.add(job);
    			idxQueue.add(idx);
    		} else {
    			// �μ�
    			finishQueue.add(idx);
    		}
    	}
    	
        int answer = 1;
        while(!finishQueue.isEmpty()) {
        	int idx = finishQueue.poll();
        	if(idx == location) {
        		break;
        	} else {
        		answer++;
        	}
        }
        return answer;
    }

}

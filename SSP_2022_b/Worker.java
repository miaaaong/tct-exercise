package com.lgcns.test;

import java.util.List;

/* ----------------------------------------------------------------------------
 * 
 * Worker.java - removeExpiredStoreItems() ����, �� �� ���� ����
 * 
 * ----------------------------------------------------------------------------
 */
public class Worker extends AbstractWorker {
	
	/*
	 * �� Worker ����
	 * - <Queue ��ȣ>�� �Ķ���ͷ� �Ͽ� Worker �ν��Ͻ� ����
	 */
	public Worker(int queueNo) {
		super(queueNo);
	}

	/*
	 * �� ����� Store Item ����
	 * - �Էµ� Timestamp�� Store Item�� Timestamp���� ���̰� ����ð�(3000)�� �ʰ��ϸ� Store���� ����
	 */
	public void removeExpiredStoreItems(long timestamp, List<String> store) {
		for(int i=store.size()-1; i>=0; i--) {
			String[] split = store.get(i).split("#");
			long time = Long.parseLong(split[0]);
			long gap = timestamp - time;
			if(gap > 3000L) {
				store.remove(i);
			}
		}
	}
}

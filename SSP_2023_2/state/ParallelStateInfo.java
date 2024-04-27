package com.lgcns.test.state;

import java.util.ArrayList;
import java.util.List;

import com.lgcns.test.Workflow;

public class ParallelStateInfo extends StateInfo {

	private List<Workflow> branches = new ArrayList<Workflow>();

	public List<Workflow> getBranches() {
		return branches;
	}

	public void setBranches(List<Workflow> branches) {
		this.branches = branches;
	}

	@Override
	public String run() throws Exception {
		List<Thread> list = new ArrayList<Thread>();
		branches.forEach(workflow -> {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					workflow.run();
				}
			});
			t.start();
			list.add(t);			
		});
		
		list.forEach(t -> {
			try {
				t.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		return this.getNextStateName();
	}

}

package com.lgcns.test;

import java.util.ArrayList;
import java.util.List;

public class ServiceTrace {

	private String target;
	private String status;
	private List<ServiceTrace> services = new ArrayList<ServiceTrace>();
	
	public ServiceTrace() {
		
	}
	
	public ServiceTrace(String target, String status) {
		this.target = target;
		this.status = status;
	}
	
	public ServiceTrace(String target, String status, String child) {
		this.target = target;
		this.status = status;
		this.services.add(new ServiceTrace(child, status));
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<ServiceTrace> getServices() {
		return services;
	}

	public void setServices(List<ServiceTrace> services) {
		this.services = services;
	}

	public ServiceTrace findTarget(String from) {
		if(from.equals(target)) {
			return this;
		} else if(services.size() > 0){
			for(ServiceTrace trace : services) {
				ServiceTrace target = trace.findTarget(from);
				if(target != null) {
					return target;
				}
			}
			return null;
		} else {
			return null;
		}
	}
	
}

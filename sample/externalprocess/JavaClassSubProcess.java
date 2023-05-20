package com.lgcns.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JavaClassSubProcess {
	
	/* 참고 자료 */
	// https://lankydan.dev/running-a-java-class-as-a-subprocess
	// https://noritersand.github.io/java/java-program-arguments-jvm-arguments-system-properties/
	
	
	// subprocess로 java class (jar 아님) 실행하기
	public static void main(String[] args) {
		// java
		String javaHome = System.getProperty("java.home");
	    String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
	    String classpath = System.getProperty("java.class.path");
	    
	    System.out.println(javaHome);
	    System.out.println(javaBin);
	    System.out.println(classpath);
	    
	    // JVM arguments
		List<String> jvmArgs = new ArrayList<String>();
		jvmArgs.add("-Xms1024m");
		jvmArgs.add("-Xmx2048m");	    

	    // 실행할 java class
	    Class clazz = WorkersProcess.class;
	    
	    // class의 main 함수에서 받을 arguments
		List<String> arguments = new ArrayList<String>();
		arguments.add("0");
		arguments.add("2");
		arguments.add("input1");
		
	    List<String> command = new ArrayList<>();
	    command.add(javaBin);
	    command.addAll(jvmArgs);
	    command.add("-cp");
	    command.add(classpath);
	    command.add(clazz.getName());
	    command.addAll(arguments);

	    ProcessBuilder builder = new ProcessBuilder(command);
	    try {
	    	// subprocess 실행
	    	// inheritIO를 하면 console에서 subprocess의 stdout을 확인할 수 있음 
			Process process = builder.inheritIO().start();
//		    Process process = builder.start();
			
			// subprocess 종료 (종료 없는 thread 등을 실행하는 subprocess인 경우 호출하지 않는다)
			// subprocess 종료 대기
		    process.waitFor();
		    // subprocess의 종료 코드
		    System.out.println(process.exitValue());
			
		    // 동시에 많은 subprocess, thread를 생성을 반복해야 하는 경우  기동이 늦어지는 현상이 발생하면 sleep 추가
//		    Thread.sleep(500);
		    
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	    
	}
	
	

}

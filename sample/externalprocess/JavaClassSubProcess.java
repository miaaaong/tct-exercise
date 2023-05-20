package com.lgcns.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JavaClassSubProcess {
	
	/* ���� �ڷ� */
	// https://lankydan.dev/running-a-java-class-as-a-subprocess
	// https://noritersand.github.io/java/java-program-arguments-jvm-arguments-system-properties/
	
	
	// subprocess�� java class (jar �ƴ�) �����ϱ�
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

	    // ������ java class
	    Class clazz = WorkersProcess.class;
	    
	    // class�� main �Լ����� ���� arguments
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
	    	// subprocess ����
	    	// inheritIO�� �ϸ� console���� subprocess�� stdout�� Ȯ���� �� ���� 
			Process process = builder.inheritIO().start();
//		    Process process = builder.start();
			
			// subprocess ���� (���� ���� thread ���� �����ϴ� subprocess�� ��� ȣ������ �ʴ´�)
			// subprocess ���� ���
		    process.waitFor();
		    // subprocess�� ���� �ڵ�
		    System.out.println(process.exitValue());
			
		    // ���ÿ� ���� subprocess, thread�� ������ �ݺ��ؾ� �ϴ� ���  �⵿�� �ʾ����� ������ �߻��ϸ� sleep �߰�
//		    Thread.sleep(500);
		    
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	    
	}
	
	

}

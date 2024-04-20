package sample.classloader;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class ClassLoader {

	// URLClassLoader, Reflection
	// 참고 - https://stackoverflow.com/questions/6219829/method-to-dynamically-load-java-class-files
	public void loadAndInvoke() {
		// class file이 위치한 루트 디렉토리 또는 jar 파일 경로
		File file = new File("c:\\myclasses\\");
		// File file = new File("./LIB/" + computingLib + ".jar");

		try {
			// Convert File to a URL
			URL url = file.toURI().toURL(); // file:/c:/myclasses/
			URL[] urls = new URL[] { url };

			// Create a new class loader with the directory
			URLClassLoader cl = new URLClassLoader(urls);

			// Load in the class; MyClass.class should be located in
			// the directory file:/c:/myclasses/com/mycompany
			Class myclass = cl.loadClass("com.mycompany.MyClass");
			
			// constructor
			Constructor[] constructors = myclass.getConstructors();
			Constructor constructor = myclass.getConstructor(String.class, int.class);
			
			// interfaces
			Class[] interfaces = myclass.getInterfaces();
			
			// super class
			Class superclass = myclass.getSuperclass();
			
			// invoke method 
			Object instance = myclass.newInstance();
			Method method = myclass.getMethod("example", int.class);
			method.invoke(instance, 10);
			
			// static method 
			Method m = myclass.getMethod("main", String[].class);
			String[] args = new String[0];
			m.invoke(null, args);  // invoke the method
			
		} catch (MalformedURLException e) {
		} catch (ClassNotFoundException e) {
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

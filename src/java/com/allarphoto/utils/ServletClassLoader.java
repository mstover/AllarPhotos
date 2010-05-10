/*
 * @(#)ClassLoader.java 1.125 99/04/22 Copyright 1994-1999 by Sun Microsystems, Inc., 901 San Antonio Road, Palo Alto,
 * California, 94303, U.S.A. All rights reserved. This software is the confidential and proprietary information of Sun
 * Microsystems, Inc. ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with Sun.
 */

package com.allarphoto.utils;

// import sun.misc.Launcher;

/**
 * The class <code>ClassLoader</code> is an abstract class. A class loader is
 * an object that is responsible for loading classes. Given the name of a class,
 * it should attempt to locate or generate data that constitutes a definition
 * for the class. A typical strategy is to transform the name into a file name
 * and then read a "class file" of that name from a file system.
 * <p>
 * Every <code>Class</code> object contains a
 * {@link Class#getClassLoader() reference} to the <code>ClassLoader</code>
 * that defined it.
 * <p>
 * Class objects for array classes are not created by class loaders, but are
 * created automatically as required by the Java runtime. The class loader for
 * an array class, as returned by {@link Class#getClassLoader()} is the same as
 * the class loader for its element type; if the element type is a primitive
 * type, then the array class has no class loader.
 * <p>
 * Applications implement subclasses of <code>ClassLoader</code> in order to
 * extend the manner in which the Java virtual machine dynamically loads
 * classes.
 * <p>
 * Class loaders may typically be used by security managers to indicate security
 * domains.
 * <p>
 * The <code>ClassLoader</code> class uses a delegation model to search for
 * classes and resources. Each instance of <code>ClassLoader</code> has an
 * associated parent class loader. When called upon to find a class or resource,
 * a <code>ClassLoader</code> instance will delegate the search for the class
 * or resource to its parent class loader before attempting to find the class or
 * resource itself. The virtual machine's built-in class loader, called the
 * bootstrap class loader, does not itself have a parent but may serve as the
 * parent of a <code>ClassLoader</code> instance.
 * <p>
 * Normally, the Java virtual machine loads classes from the local file system
 * in a platform-dependent manner. For example, on UNIX systems, the virtual
 * machine loads classes from the directory defined by the
 * <code>CLASSPATH</code> environment variable.
 * <p>
 * However, some classes may not originate from a file; they may originate from
 * other sources, such as the network, or they could be constructed by an
 * application. The method <code>defineClass</code> converts an array of bytes
 * into an instance of class <code>Class</code>. Instances of this newly
 * defined class can be created using the <code>newInstance</code> method in
 * class <code>Class</code>.
 * <p>
 * The methods and constructors of objects created by a class loader may
 * reference other classes. To determine the class(es) referred to, the Java
 * virtual machine calls the <code>loadClass</code> method of the class loader
 * that originally created the class.
 * <p>
 * For example, an application could create a network class loader to download
 * class files from a server. Sample code might look like: <blockquote>
 * 
 * <pre>
 *    ClassLoader loader = new NetworkClassLoader(host, port);
 *    Object main = loader.loadClass(&quot;Main&quot;, true).newInstance();
 * 	  . . .
 * </pre>
 * 
 * </blockquote>
 * <p>
 * The network class loader subclass must define the methods
 * <code>findClass</code> and <code>loadClassData</code> to load a class
 * from the network. Once it has downloaded the bytes that make up the class, it
 * should use the method <code>defineClass</code> to create a class instance.
 * A sample implementation is:
 * <p>
 * <hr>
 * <blockquote>
 * 
 * <pre>
 * class NetworkClassLoader extends ClassLoader {
 * 
 * 	String host;
 * 
 * 	int port;
 * 
 * 	public Class findClass(String name) {
 * 		byte[] b = loadClassData(name);
 * 		return defineClass(name, b, 0, b.length);
 * 	}
 * 
 * 	private byte[] loadClassData(String name) {
 *              // load the class data from the connection
 *               . . .
 *          }
 * }
 * </pre>
 * 
 * </blockquote>
 * <hr>
 * 
 * @version 1.125, 04/22/99
 * @see java.lang.Class
 * @see java.lang.Class#newInstance()
 * @see java.lang.ClassLoader#defineClass(byte[], int, int)
 * @see java.lang.ClassLoader#loadClass(java.lang.String, boolean)
 * @see java.lang.ClassLoader#resolveClass(java.lang.Class)
 * @since JDK1.0
 */
public class ServletClassLoader extends ClassLoader {

	/**
	 * Finds the specified class. This method should be overridden by class
	 * loader implementations that follow the new delegation model for loading
	 * classes, and will be called by the <code>loadClass</code> method after
	 * checking the parent class loader for the requested class. The default
	 * implementation throws <code>ClassNotFoundException</code>.
	 * 
	 * @param name
	 *            the name of the class
	 * @return the resulting <code>Class</code> object
	 * @exception ClassNotFoundException
	 *                if the class could not be found
	 * @since JDK1.2
	 */
	protected Class findClass(String name) throws ClassNotFoundException {
		String cp = System.getProperty("java.class.path");
		String[] paths = Functions.split(cp, ";");
		String ps = System.getProperty("file.separator");
		String className = new String(name);
		name = name.replace('.', ps.charAt(0)) + ".class";
		Functions.javaLog("ServletClassLoader: className = " + className
				+ " name = " + name);
		byte[] b = new byte[0];
		for (int x = 0; x < paths.length; x++) {
			paths[x] = paths[x].replace('/', ps.charAt(0));
			if (!paths[x].endsWith(ps))
				paths[x] = paths[x] + ps;
			Functions.javaLog("ServletClassLoader: file = " + paths[x] + name);
			try {
				b = Filer.readFileData(paths[x] + name);
				break;
			} catch (Exception e) {
				Functions.javaLog("ServletClassLoader: exception = "
						+ e.toString());
			}
		}
		if (b.length > 0)
			return defineClass(className, b, 0, b.length);
		else
			throw new ClassNotFoundException();
	}

	/**
	 * Loads the class with the specified name. This method searches for classes
	 * in the same manner as the {@link #loadClass(String, boolean)} method. It
	 * is called by the Java virtual machine to resolve class references.
	 * Calling this method is equivalent to calling
	 * <code>loadClass(name, false)</code>.
	 * 
	 * @param name
	 *            the name of the class
	 * @return the resulting <code>Class</code> object
	 * @exception ClassNotFoundException
	 *                if the class was not found
	 */
	public Class loadClass(String name) throws ClassNotFoundException {
		// First, check if the class has already been loaded
		if (!name.startsWith("com.allarphoto"))
			return super.loadClass(name);
		Class c = findClass(name);
		resolveClass(c);
		return c;
	}

}

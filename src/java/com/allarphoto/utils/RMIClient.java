package com.allarphoto.utils;

import java.io.File;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;

import com.allarphoto.rmi.PackerInterface;

public class RMIClient {

	public RMIClient() {
	}

	public static void main(String[] args) {
		try {
			if (System.getSecurityManager() == null) {
				System.setSecurityManager(new RMISecurityManager());
			} // */
			PackerInterface pi = (PackerInterface) Naming
					.lookup("//archimedes:1099/com.allarphoto.rmi.Packer");

			String ds = args[0];
			String outputFile = args[1];
			String[] filenames = Functions.split(args[2], ",");

			File test;
			File[] files = new File[filenames.length];
			int counter = 0;
			for (int i = 0; i < filenames.length; i++) {
				test = new File(filenames[i]);
				if (test.exists()) {
					files[counter++] = test;
				}
			}
			boolean success = false;
			if (pi.zipForMacWithPacker(files, outputFile, ds)) {
				System.out.println("Yay!");
			} else {
				System.out.println("D'oh!");
			}

		} catch (Exception e) {
			System.out.println("Error accessing remote object.");
			System.out.println(e.toString());
		}
	}
}
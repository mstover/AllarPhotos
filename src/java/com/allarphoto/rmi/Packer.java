package com.lazerinc.rmi;

import java.io.File;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;

import com.lazerinc.utils.Filer;

public class Packer extends UnicastRemoteObject implements PackerInterface {
	private static final long serialVersionUID = 1;

	public Packer() throws RemoteException {
		super();
	}

	/***************************************************************************
	 * This method takes a list of files and zips them up for Macs by using
	 * MediaBanks Childp process (yuck), saving the zip file where instructed.
	 * 
	 * @param files
	 *            array of File objects to be zipped up
	 * @param zipFileName
	 *            name of file to save zip file to
	 * @param childp
	 *            executable file to package files for Macs.
	 * @return boolean true if successful at zipping everything, false otherwise
	 **************************************************************************/
	public boolean zipForMacWithPacker(File[] files, String zipFileName,
			String childp) {
		Properties sysProps = System.getProperties();
		StringBuffer instructions = new StringBuffer("999\tFLATTEN\r\n"
				+ zipFileName + "\r\nLIST\tFILES\r\n");
		int count = -1;
		while (++count < files.length)
			instructions.append(files[count].getPath() + "\r\n");
		instructions.append("ENDLIST\r\n");
		int rnd = (int) (Math.random() * 10000.0000);
		String inbox = sysProps.getProperty("user.dir")
				+ "\\in"
				+ rnd
				+ zipFileName.substring(zipFileName.lastIndexOf("\\") + 1,
						zipFileName.lastIndexOf(".")) + ".txt";
		String outbox = sysProps.getProperty("user.dir")
				+ "\\out"
				+ rnd
				+ zipFileName.substring(zipFileName.lastIndexOf("\\") + 1,
						zipFileName.lastIndexOf(".")) + ".txt";
		String command = childp + " " + outbox + " " + inbox;
		Filer.writeFile(outbox, instructions.toString());
		Runtime sending = Runtime.getRuntime();
		Process sendingProcess;
		int exit = -1;
		try {
			sendingProcess = sending.exec(command);
			exit = sendingProcess.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Filer.deleteFile(outbox);
		Filer.deleteFile(inbox);
		if (exit == 0)
			return true;
		else
			return false;
	} // End Method

	public String zipWithPacker(String originalString) throws RemoteException {
		int length = originalString.length();

		StringBuffer temp = new StringBuffer(length);

		for (int i = length; i > 0; i--) {
			temp.append(originalString.substring(i - 1, i));
		}

		return temp.toString();
	}

	public static void main(String[] args) {
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new RMISecurityManager());
		}

		String name = "//192.9.200.154/com.lazerinc.rmi.Packer";

		try {
			Packer p = new Packer();
			Naming.rebind(name, p);
			System.out.println("Packer object bound");
		} catch (Exception e) {
			System.out.println("Error while binding Packer object");
			System.out.println(e.toString());
		}
	}
}
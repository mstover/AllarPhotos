package com.lazerinc.rmi;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PackerInterface extends Remote {

	String zipWithPacker(String originalString) throws RemoteException;

	public boolean zipForMacWithPacker(File[] files, String zipFileName,
			String childp);
}

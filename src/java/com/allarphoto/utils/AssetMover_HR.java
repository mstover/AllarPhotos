/*
 * AssetMover_HR.java
 *
 * Created on March 25, 2003
 */

package com.lazerinc.utils;

import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;

/*******************************************************************************
 * @author Tom Cousins - The initial function of this class is to assist in the
 *         renaming of files for the Welch Allyn library. This one needs the
 *         flexibility to actually find the proper extension for the HIRES file
 *         itself. NOTE: The suffix given in the CSV file will be insignifigant!
 *         We will want to use the proper file extension.
 ******************************************************************************/
public class AssetMover_HR {
	// FIELDS *********************************************
	private final String TAB = "\t";

	public final String DELIMETER = ",";

	public final String NL = System.getProperty("line.separator");

	private static final String logFile = "c:/AssetMover_HRLog.txt";

	// The Welch Allyn drive will be setup on drive W:
	private final String primeKey = "File Name";

	private final String OLDFILE = primeKey;

	private final String OLDPATH = "Original Path";

	private final String NEWNAME = "Renamed File";

	private final String NEWPATH = "Moved Path";

	public String[] moveHeaders = { primeKey, NEWNAME, OLDPATH, NEWPATH };

	public String sourceFile = "c:/csv/move_files.csv";

	private static Data moveData;

	// CONSTRUCTORS *********************************************
	/** Creates a new instance of AssetMover */
	public AssetMover_HR() {
		new AssetMover_HR(sourceFile);
	}

	public AssetMover_HR(String srcFileName) {
		// need to instantiate the data object here.
		init(sourceFile);
		// maybe create a log file here
	}

	// METHODS *********************************************
	/***************************************************************************
	 * Instantiates the moveData object
	 * 
	 * @param srcFileName -
	 *            csv file for data object instantiation
	 * @return void
	 **************************************************************************/
	public void init(String srcFileName) {
		moveData = new Data();
		if (Filer.exist(srcFileName) && Filer.canEdit(srcFileName)) {
			try { // Get the text info from the CSV file.
				// System.out.println("We do have data to deal with! - " +
				// sourceFile);
				String[] myFileContents = Filer.readFile(srcFileName);
				moveData.setData(myFileContents, DELIMETER);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Can't read file. - " + srcFileName);
		}
		// displayData();
	}

	/***************************************************************************
	 * Renames the file to a designated path. This method should only be used
	 * with BINARY files at this point.
	 * 
	 * @param oldFile -
	 *            Full path to file including the filename.
	 * @param newName -
	 *            New filename.
	 * @param newPath -
	 *            Path where new file should be written.
	 * @return boolean - Was the action successful?
	 **************************************************************************/
	public boolean renameFile(String oldFile, String oldPath, String newName,
			String newPath) {
		// We'll want to loop through here
		boolean retVal = false;
		String oldIn = oldPath + oldFile;
		String newOut = newPath + newName;
		String statusMessage = "success";
		traversePath(newPath); // Make sure that the directory exists.
		if (Filer.exist(oldIn) && Filer.canEdit(oldIn)) {
			try {
				File myFile = new File(oldIn);
				File myNewFile = new File(newOut);
				retVal = myFile.renameTo(myNewFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			retVal = false;

			System.out
					.println("I'm sorry, your action failed. Please make sure the file ("
							+ oldFile + ") exists.");
		}
		return retVal;
	}

	/***************************************************************************
	 * Renames all of the files in the moveData object.
	 * 
	 * @param void
	 * @return boolean - were all files processed?
	 **************************************************************************/
	public boolean renameAllFiles() {
		boolean retVal = true;
		moveData.reset();
		// File myLog = new File(logFile);
		GregorianCalendar gc = new GregorianCalendar();
		String rnMin = "" + gc.get(Calendar.MINUTE);
		if (rnMin.length() < 2)
			rnMin = "0" + rnMin;
		String rightNow = gc.get(Calendar.MONTH) + " "
				+ gc.get(Calendar.DAY_OF_MONTH) + ", " + gc.get(Calendar.YEAR)
				+ " : " + gc.get(Calendar.HOUR_OF_DAY) + ":" + rnMin;
		StringBuffer myText = new StringBuffer(
				"************** STATUS OF HR RENAME SCRIPT **************" + NL
						+ rightNow + NL);
		myText.append("" + NL + OLDFILE + TAB + "Status" + TAB + "Path From"
				+ TAB + "Path To" + TAB + "Renamed" + TAB
				+ "File Extension of HR" + NL);

		while (moveData.next()) {
			String oldFile = (String) moveData.getColumnValue(OLDFILE);
			String oldPath = (String) moveData.getColumnValue(OLDPATH);
			String newName = (String) moveData.getColumnValue(NEWNAME);
			String newPath = (String) moveData.getColumnValue(NEWPATH);

			// This may be a good place to break to check for the HIRES file
			// extension.
			String fileExt = "none found";
			String[] possibles = { ".eps", ".tif", ".tiff", ".psd", ".pdf",
					".qxd", ".ppt", ".pct", ".bmp", ".doc", ".xls", ".png",
					".csv", ".txt", ".jpg", ".jpeg", ".gif" };
			boolean indRetVal = false;
			for (int i = 0; i < possibles.length; i++) {
				String myFile = oldFile.substring(0, oldFile.lastIndexOf("."))
						+ possibles[i];
				boolean myFileExists = Filer.exist(oldPath, myFile);
				// System.out.println("Checking to see if " + oldPath + myFile +
				// " exists: " + myFileExists);
				if (myFileExists) {
					fileExt = possibles[i];
					String myNewName = newName.substring(0, newName
							.lastIndexOf("."))
							+ fileExt;
					indRetVal = renameFile(myFile, oldPath, myNewName, newPath);
					i = possibles.length + 1;
				}
			}

			retVal = indRetVal && retVal;

			// Let's log our results here!
			myText.append("" + oldFile + TAB + indRetVal + TAB + oldPath + TAB
					+ newPath + TAB + newName + TAB + fileExt + NL);
		}
		myText.append("" + NL + NL);

		System.out.println("Writing log file to: " + logFile);
		Filer.appendToFile(logFile, myText.toString());

		return retVal;
	}

	/***************************************************************************
	 * Traverse the path that you wish to write to and make sure all folders
	 * exist. If they don't create them.
	 * 
	 * @param newPath
	 * @return void
	 **************************************************************************/
	public void traversePath(String newPath) {
		String[] myFolders = Functions.split(newPath, "/");
		String tempPath = "";
		for (int i = 0; i < myFolders.length; i++) {
			tempPath = tempPath + "/" + myFolders[i];
			if (!Filer.exist(tempPath)) {
				System.out.println("Creating folder: " + tempPath);
				Filer.mkdir(tempPath);
			} else {
				// Do nothing at this point.
				// System.out.println(tempPath + " seems ok and exists.");
			}
		}
	}

	/***************************************************************************
	 * This should printout all of the info in the moveData object. Should be
	 * used for command line testing purposes only. TOTAL KITCHEN-SINK!
	 * 
	 * @param myData -
	 *            Data object that you wish to display.
	 * @return void
	 **************************************************************************/
	public void displayData(Data myData) {
		if (null != myData) {
			String[] myHdrs = myData.getHeaders();
			for (int i = 0; i < myHdrs.length; i++) {
				System.out.print(myHdrs[i] + TAB);
			}
			System.out.println();
			myData.reset();
			while (myData.next()) {
				for (int i = 0; i < myHdrs.length; i++) {
					System.out.print((String) myData.getColumnValue(i) + TAB);
				}
				System.out.println();
			}
		} else {
			System.out.println("I'm sorry, but I didn't get any data!");
		}
	}

	/***************************************************************************
	 * This should printout all of the info in the data object for logging the
	 * actions taken by this class.
	 * 
	 * @param void
	 * @return void
	 **************************************************************************/
	public void writeLogData(String logPath) {
		try {
			/*
			 * File myLog = new File(logPath); FileWriter fOut = new
			 * FileWriter(myLog); StringBuffer fc = new StringBuffer("");
			 * String[] myHeaders = logData.getHeaders(); for(int i=0; i<myHeaders.length;
			 * i++) { fc.append(myHeaders[i] + TAB); } fc.append(NL);
			 * 
			 * logData.reset(); while(logData.next()) { for(int ct=0; ct<myHeaders.length;
			 * ct++) { fc.append((String)logData.getColumnValue(myHeaders[ct]) +
			 * TAB); } fc.append(NL); }
			 * 
			 * System.out.println("Writing log file to: " + logPath);
			 * fOut.write(fc.toString());
			 */
		} catch (Exception e) {
			// Do nothing at this point.
		}
	}

	/***************************************************************************
	 * The actual setup and work is done here.
	 * 
	 * @param args -
	 *            arguments supplied to the main method.
	 * @return void
	 **************************************************************************/
	public static void main(String[] args) {
		AssetMover_HR assMover = new AssetMover_HR();

		// Alert user to location of log file.
		StringBuffer greet = new StringBuffer("");
		greet
				.append("*************** MOVING & RENAMING HIRES FILES ***************"
						+ assMover.NL);
		greet
				.append("This class is designed to be run locally from the command line."
						+ assMover.NL);
		greet
				.append("Please make sure that you have everything setup prior to moving forward ... "
						+ assMover.NL);
		greet.append("* There should be a CSV file: " + assMover.sourceFile
				+ assMover.NL);
		greet
				.append("* The CSV file should have the following columns properly formatted."
						+ assMover.NL);
		for (int ct = 0; ct < assMover.moveHeaders.length; ct++) {
			greet.append("   - " + assMover.moveHeaders[ct] + assMover.NL);
		}
		greet
				.append("* Please double-check all paths before proceeding. This action could have very detrimental effects if not used properly."
						+ assMover.NL);
		greet.append("LOGFILE: There will be a logfile at: " + logFile
				+ assMover.NL);
		System.out.println(greet.toString());

		// assMover.displayData(moveData);
		boolean allDone = assMover.renameAllFiles();
		System.out.println("All assets were renamed as requested: " + allDone);
		assMover.writeLogData(logFile);
	}

}

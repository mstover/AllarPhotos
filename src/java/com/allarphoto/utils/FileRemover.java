/*
 * FileRemover.java
 * Created on March 4, 2003, 9:45 AM
 *
 * - This is intended to allow a user to supply a csv file of file names 
 * to the tool_box which will duplicate the appropriate file type and create 
 * a dummy file to be placed in the Remove_from_online_library folder to be 
 * catalogged and purged. Rather than searching one by one, the files can 
 * be group purged. The catalogger does the work to allow user to move on 
 * to another task.
 * - Tom
 */

package com.allarphoto.utils;

/**
 * 
 * @author Tom Cousins - The initial function of this class is to assist in the
 *         removal of files from the Welch Allyn library (Hires folder, Low res
 *         folder, and handling the files for MediaBank to recatalog files in a
 *         Remove_From_Online_Library folder.
 */
public class FileRemover {
	private final String tab = "\t";

	private final String nl = System.getProperty("line.separator");

	// The Welch Allyn drive will be setup on drive W:
	private final String sourceFile = "c:/csv/remove_data.csv";

	private final String dummyMasters = "c:/csv" + "/dummy/";

	private final String LR_BASE = "c:/tool_box/dummy_lr/";

	private final String remFrmOnline = "c:/001/Remove_from_online_library/";

	public final String delimeter = ",";

	private final String primeKey = "File Name";

	private final String extCol = "Extension";

	private final String dummyCol = "Dummy File Path";

	private final String dummyRem = "Dummy Remove Path";

	private Data remData;

	/** Creates a new instance of FileRemover */
	public FileRemover() {
		init(sourceFile);
	}

	public FileRemover(String fileName) {
		init(fileName);
	}

	// METHODS - ****************************************************
	public void init(String srcFileName) {
		if (Filer.exist(srcFileName) && Filer.canEdit(srcFileName)) {
			try {
				remData = new Data();
				System.out.println("Getting data from file: " + srcFileName);
				String[] myFileContents = Filer.readFile(srcFileName);
				remData.setData(myFileContents, delimeter);
				/*
				 * Test Segment direct read of file. for(int ct=0; ct<myFileContents.length;
				 * ct++) { System.out.println(myFileContents[ct]); }
				 */
				// /*
				String tempExt;
				String tempDummy;
				String tempDummyRem;
				remData.addHeader(extCol);
				remData.addHeader(dummyCol);
				remData.addHeader(dummyRem);

				/*
				 * // more testing stuff String[] tryThis =
				 * remData.getHeaders(); for(int i=0; i<tryThis.length; i++) {
				 * System.out.print(tryThis[i] + tab); } System.out.println(""); //
				 */

				remData.reset();
				while (remData.next()) {
					// Cycle through the file names and extract the file
					// extensions placing the extension in the Extension column.
					tempExt = getFileExt((String) remData
							.getColumnValue(primeKey));
					if (tempExt.equals("tiff")) {
						tempExt = "tif";
					}
					tempDummy = dummyMasters + "dummy_" + tempExt + "."
							+ tempExt;
					tempDummyRem = remFrmOnline
							+ (String) remData.getColumnValue(primeKey);
					remData.addColumnValue(extCol, tempExt);
					remData.addColumnValue(dummyCol, tempDummy);
					remData.addColumnValue(dummyRem, tempDummyRem);
					// System.out.println("My file: " +
					// (String)remData.getColumnValue(primeKey) + " has ext: " +
					// (String)remData.getColumnValue(extCol));
				}
				// */
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Can't read file. - CRAZY! " + srcFileName);
		}
	}

	public String[] myHeaders() {
		if (null != remData) {
			return remData.getHeaders();
		} else {
			return null;
		}
	}

	public String[][] pourableData() {
		// System.out.println("remData !null = " + (null != remData) + "
		// remData.size(): " + remData.size());
		if (null != remData) {
			String[] myHeaders = remData.getHeaders();
			int width = myHeaders.length;
			int height = remData.getColumn(myHeaders[0]).length; // remData.size();
			String[][] retVal = new String[width][height];
			for (int i = 0; i < height; i++) {
				for (int ct = 0; ct < width; ct++) {
					String[] tempCol = remData.getColumn(myHeaders[ct]);
					if ((null != tempCol) && (tempCol.length >= i)
							&& (null != tempCol[i])) {
						retVal[ct][i] = tempCol[i];
					} else {
						retVal[ct][i] = "N/A";
					}
				}
			}
			return retVal;
		} else {
			return null;
		}
	}

	public String getFileExt(String fileName) {
		// Lets determine the file extension.
		String myExt;
		int extIndex = fileName.lastIndexOf(".");
		if (extIndex > 0 && extIndex < fileName.length()) {
			myExt = fileName.substring(fileName.lastIndexOf(".") + 1);
		} else {
			myExt = "unknown";
		}
		return myExt;
	}

	public void processDummyFiles() {
		// ************ REALLY NEED TO SETUP A LOG HERE ************ !!!
		StringBuffer logMessage = new StringBuffer("");
		remData.addHeader("Success of Remove");
		String[] logHeaders = myHeaders();

		Filer curDummy = new Filer();
		Filer procLog = new Filer();
		byte[] tempData;
		remData.reset();
		while (remData.next()) {
			try {
				tempData = curDummy.readFileData((String) remData
						.getColumnValue(dummyCol));
				// Make the file or it won't get written!
				curDummy.mkdir((String) remData.getColumnValue(dummyRem));
				boolean success = curDummy.writeFile((String) remData
						.getColumnValue(dummyRem), tempData);
			} catch (Exception fio) {
				System.out.println("File IO issue. @: "
						+ (String) remData.getColumnValue(dummyCol));
			}
		}
	}

	// Simple test segment.
	public static void main(String[] args) {
		FileRemover fr = new FileRemover();
		String[][] pd = fr.pourableData();
		String[] hdrs = fr.myHeaders();
		if (null != pd) {
			for (int i = 0; i < hdrs.length; i++) {
				System.out.print("" + hdrs[i] + "\t");
			}
			System.out.println("");
			for (int y = 0; y < pd[0].length; y++) {
				for (int x = 0; x < pd.length; x++) {
					System.out.print("" + pd[x][y] + "\t");
				}
				System.out.println("");
			}
		}
		fr.processDummyFiles();
		// System.out.println(fr.pourableData().toString());

	}
}

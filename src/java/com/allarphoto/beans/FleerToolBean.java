/*
 * FleerToolBean.java Created on June 26, 2003, 2:53 PM
 */

package com.lazerinc.beans;

/**
 * @author Tom Cousins
 */

import java.io.Serializable;

import com.lazerinc.utils.Data;
import com.lazerinc.utils.Filer;

public class FleerToolBean implements Serializable {
	private static final long serialVersionUID = 1;

	// FIELDS *********************************************
	// NOTE: The X; drive is to be mapped to the Fleer volume of the Descartes
	// server.
	public final String BOXROOT = "x:/toms_toolbox/"; // BASE_ROOT for
														// everything here.

	public final String CSV = BOXROOT + "csv/"; // Text file setups (input).

	public final String MASTERS = BOXROOT + "masters/"; // Master File(s) root
														// (input).

	public final String DUPEOUT = BOXROOT + "output/"; // Target (output) for
														// duplications.

	public final String XMLOUT = BOXROOT + "output/"; // Target (output) for
														// XML formatted files.

	public final String LOGS = BOXROOT + "logs/"; // ERROR and Output logs
													// target folder.

	public final String DEFAULT_SRC = CSV + "0-dupe_masters.txt";

	public final String DEFAULT_MASTER = MASTERS + "0-DefaultMaster.psd";

	// COLUMN HEADERS FOR THE TOOLDATA OBJECT
	public final String FIRSTN = "First";

	public final String LASTN = "Last";

	public final String PNAME = "Name";

	public final String TEAMN = "Team";

	public final String POSITION = "Pos";

	public final String CARD = "Card";

	public final String PSD = "PSD";

	public final String PROPER_FILE = "Proper File";

	private String user;

	private String myTask;

	private String sport;

	private String masterFile;

	private String srcFile;

	private String workingFolder;

	private String workingFilePrefix;

	private String xmlOptions;

	private Data toolData = new Data();

	// CONSTRUCTORS *********************************************
	/** Creates a new instance of FleerToolBean */
	public FleerToolBean() {
		setUser("guest");
		setSrcFile(DEFAULT_SRC);
		setMasterFile(DEFAULT_MASTER);
	}

	// METHODS *********************************************
	// METHODS FOR - user
	public void setUser(String newUser) {
		this.user = newUser;
	}

	public String getUser() {
		return this.user;
	}

	// METHODS FOR - myTask
	public void setMyTask(String newTask) {
		this.myTask = newTask;
	}

	public String getMyTask() {
		return this.myTask;
	}

	// METHODS FOR - sport
	public void setSport(String newSport) {
		this.sport = newSport;
	}

	public String getSport() {
		return this.sport;
	}

	// METHODS FOR - masterFile
	public void setMasterFile(String newMasterFile) {
		this.masterFile = newMasterFile;
	}

	public String getMasterFile() {
		return this.masterFile;
	}

	// METHODS FOR - srcFile
	public boolean setSrcFile(String newSrcFile) {
		if (Filer.exist(newSrcFile)) {
			this.srcFile = newSrcFile;
			return true;
		} else {
			return false;
		}
	}

	public String getSrcFile() {
		return this.srcFile;
	}

	// METHODS FOR - workingFolder
	public void setWorkingFolder(String newWorkFolder) {
		this.workingFolder = "" + this.DUPEOUT + newWorkFolder;
		Filer.mkdir("" + this.DUPEOUT + newWorkFolder);
	}

	public String getWorkingFolder() {
		return this.workingFolder;
	}

	// METHODS FOR - workingFilePrefix
	public void setWorkingFilePrefix(String newWorkFilePrefix) {
		this.workingFilePrefix = "" + this.DUPEOUT + newWorkFilePrefix;
	}

	public String getWorkingFilePrefix() {
		return this.workingFilePrefix;
	}

	// METHODS FOR - toolData
	public void setToolData(String mySrcFile) {
		this.toolData = new Data();
		if (Filer.exist(mySrcFile) && Filer.canEdit(mySrcFile)) {
			try { // Get the text info from the CSV file.
				String[] myFileContents = Filer.readFile(mySrcFile);
				this.toolData.setData(myFileContents, "\t");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Can't read file. - " + mySrcFile);
		}
	}

	public void setToolData(Data newData) {
		this.toolData = newData;
	}

	/**
	 * Given that the toolData has already been set properly, with columns for
	 * FIRST and LAST, this method should now create another column of properly
	 * formatted filenames (first 3 characters of FIRST | first 4 characters of
	 * LAST | setSuffix | file suffix). NOTE: This method will also need to
	 * handle exceptions, such as "Ichiro Suzuki" & "Nene" must only be labeled
	 * as "Ichiro" NOTE: FAILED ATTEMPT - DO NOT USE THIS METHOD UNTIL FIXED !!!
	 */
	public String[] formulateDupeNames(String setSuffix) {
		String fileSuffix = this.getMasterFile().substring(
				(this.getMasterFile()).lastIndexOf("."))
				+ setSuffix;
		toolData.addHeader("DupeName");
		toolData.reset();
		while (toolData.next()) {
			String firstN = (String) toolData.getColumnValue(FIRSTN);
			String lastN = (String) toolData.getColumnValue(LASTN);
			boolean hasException = false;
			if (!hasException) {
				String dupeName = firstN + lastN;
				dupeName = dupeName.replaceAll(" ", "") + fileSuffix;
				// toolData.setColumnValue("DupeName", dupeName);
			}
		}
		return toolData.getColumn("DupeName");
	}

	public void formulateProperFiles() {
		if (null != toolData && null != toolData.getColumn(FIRSTN)) {
			if (null == toolData.getColumn(PROPER_FILE))
				toolData.addHeader(PROPER_FILE);
			// Some exception handlers need to be incorporated here.
			toolData.reset();
			while (toolData.next()) {

			}
		}
	}

	public Data getToolData() {
		return this.toolData;
	}
}

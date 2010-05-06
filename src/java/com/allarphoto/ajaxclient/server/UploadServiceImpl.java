package com.lazerinc.ajaxclient.server;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import strategiclibrary.util.Files;

import com.lazerinc.ajaxclient.client.UploadService;
import com.lazerinc.client.beans.UserBean;
import com.lazerinc.client.exceptions.BadFilenameCharacter;

public class UploadServiceImpl extends AbstractGwtServlet implements
		UploadService {
	private static final long serialVersionUID = 1;

	public UploadServiceImpl() {
		super();
	}

	public String[][] getMetaCategories() {
		String[][] props = (String[][]) getThreadLocalHandlerData()
				.getUserBean("properties_to_be_set");
		return props;
	}

	public String[] getPrimaryNames() {
		String[] names = ((Collection<String>) getThreadLocalHandlerData()
				.getUserBean("uploaded_file_list")).toArray(new String[0]);
		return names;
	}

	public boolean isUploadDone() {
		String upload = (String) getThreadLocalHandlerData().getUserBean(
				"uploaded_file");
		if (upload != null) {
			return true;
		}
		return false;
	}

	public boolean uploadMeta(String[][] metadata) {
		ZipOutputStream out = null;
		try {
			String delim = "\",\"";
			StringBuffer metaCvs = new StringBuffer(delim);
			metaCvs.append("\n").append("Primary");
			String[][] props = (String[][]) getThreadLocalHandlerData()
					.getUserBean("properties_to_be_set");
			for (String cat : props[0])
				metaCvs.append(delim).append(cat);
			for (String cat : props[1])
				metaCvs.append(delim).append(cat);
			metaCvs.append(delim).append("Submitter");
			for (String[] values : metadata) {
				metaCvs.append("\n");
				int count = 0;
				for (String value : values) {
					metaCvs.append(value);
					count++;
					metaCvs.append(delim);
				}
			}
			UserBean user = (UserBean) getThreadLocalHandlerData().getUserBean(
					"user");
			metaCvs.append(user.getLastName() + ", " + user.getFirstName());
			File upload = new File((String) getThreadLocalHandlerData()
					.getUserBean("uploaded_file"));
			out = new ZipOutputStream(new BufferedOutputStream(
					new FileOutputStream(new File(upload.getParentFile()
							.getParentFile(), upload.getName()))));
			out.putNextEntry(new ZipEntry("additional.meta"));
			out.write(metaCvs.toString().getBytes("utf-8"));
			out.closeEntry();
			transferZipEntries(new ZipInputStream(new FileInputStream(upload)),
					out);
			upload.delete();
			return true;
		} catch (Exception e) {
			getLog().warn("Failure to update zip upload", e);
			return false;
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					getLog().warn("Failure to update zip upload", e);
				}
			}
		}
	}

	private void transferZipEntries(ZipInputStream inZip, ZipOutputStream outZip)
			throws IOException, BadFilenameCharacter {
		try {
			int filecount = 1;
			ZipEntry entry = inZip.getNextEntry();
			while (entry != null) {
				writeStreamToZip(Files.getNameOnly(entry.getName()), outZip,
						inZip);
				entry = inZip.getNextEntry();
			}
		} finally {
			if (inZip != null)
				inZip.close();
		}
	}

	private void writeStreamToZip(String filename, ZipOutputStream out,
			InputStream in) throws IOException, BadFilenameCharacter {
		filename = filename.replaceAll(" ", "_");
		if (filename.indexOf("/") > -1 || filename.indexOf("\\") > -1
				|| filename.indexOf(";") > -1 || filename.indexOf(":") > -1
				|| filename.indexOf("'") > -1 || filename.indexOf("!") > -1
				|| filename.indexOf("`") > -1 || filename.indexOf("&") > -1
				|| filename.indexOf("*") > -1 || filename.indexOf("\"") > -1)
			throw new BadFilenameCharacter(
					"Filename contains illegal characters(/\\\":;'`!&*): "
							+ filename);
		out.putNextEntry(new ZipEntry(filename));
		Files.copy(in, out);
		out.closeEntry();
	}

}

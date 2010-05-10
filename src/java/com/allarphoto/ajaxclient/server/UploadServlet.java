package com.allarphoto.ajaxclient.server;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.coinjema.context.CoinjemaDependency;

import strategiclibrary.service.notification.NotificationService;
import strategiclibrary.service.webaction.HandlerData;
import strategiclibrary.util.Converter;
import strategiclibrary.util.Files;

import com.allarphoto.beans.UploadItem;
import com.allarphoto.beans.User;
import com.allarphoto.category.ProductField;
import com.allarphoto.client.beans.UserBean;
import com.allarphoto.client.exceptions.BadFilenameCharacter;
import com.allarphoto.ecommerce.DatabaseUtilities;
import com.allarphoto.ecommerce.ProductFamily;
import com.allarphoto.server.UserService;
import com.allarphoto.servlet.AbstractContextServlet;
import com.allarphoto.utils.DatabaseLogger;
import com.allarphoto.utils.Resource;

public class UploadServlet extends AbstractContextServlet {
	private static final long serialVersionUID = 1;

	DatabaseUtilities dbutil;

	DatabaseLogger dblogger;

	NotificationService emailer;

	String[] uploadRecipients;
	
	UserService ugd;

	String uploadDir = "";

	String altUploadDir = "";

	volatile int count = 0;

	public UploadServlet() {
		super();
	}

	@Override
	protected void doPost(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		HandlerData info = getThreadLocalHandlerData();
		getLog().info("Uploading " + info.getFileName("file-to-upload"));
		if (info.isFileUpload()) {
			info.removeUserBean("uploaded_file");
			info.removeUserBean("uploaded_file_list");
			info.removeUserBean("properties_to_be_set");
			ProductFamily family = getProductFamily(info);
			if (family == null)
				family = dbutil.getProductFamily(info.getParameter("library"));
			if (family == null) {
				arg1.getWriter().write("Upload failed: No Library Chosen");
				info.setUserBean("uploaded_file", "Bad File");
				getLog().warn("Upload failed, no library chosen");
				return;
			}
			UserBean user = (UserBean) info.getBean("user");
			getLog().info("user is " + user);
			if (!user.getPermissions().getPermission(family.getTableName(),
					Resource.DATATABLE, "upload")) {
				arg1.getWriter().write(
						"You do not have upload permissions for this library ("
								+ family.getDescriptiveName() + ")");
				return;
			}
			List<ProductField> fields = UploadFieldFilter.resortFields(family
					.getFields());
			Properties imageMeta = new Properties();
			setProperties(imageMeta, info);
			imageMeta.setProperty("family", family.getTableName());
			String[] values = info.getParameter("categories", "").split(",");
			for (int i = 1; i < values.length; i++)
				imageMeta.setProperty(fields.get(i - 1).getName().replaceAll(
						" ", "_"), values[i]);
			imageMeta.setProperty("Submitter", user.getUser().getLastName()
					+ ", " + user.getUser().getFirstName());
			String filename = info.getFileName("file-to-upload");
			try {
				UploadItem logit = new UploadItem();
				logit.setUser(user.getUser());
				logProperties(logit, imageMeta);

				writeZipFile(info, filename, imageMeta, logit, family);
				arg1.getWriter().write(
						"Uploaded file: \n" + filename + "</b> with values \n"
								+ imageMeta.toString());
				// dblogger.addLogItem(logit);
				if (info.getParameter("has_new_cat", "false").equals("true")) {
					arg1
							.getWriter()
							.write(
									"\nBecause these files are being uploaded to a newly created folder, \nthey "
											+ "need to be approved before importing into the library.  \nThey will appear in searches within"
											+ " one business day.");
					emailer.sendMessage(family.isRemoteManaged() ? getAdmins(family) : uploadRecipients,
							"upload file needs inspection", "text/plain",
							"There's a new upload file with ad hoc fields");
				}
			} catch (Exception e) {
				arg1.getWriter().write("Upload failed: " + e.getMessage());
				info.setUserBean("uploaded_file", "Bad File");
				getLog().warn("Upload failed", e);
			}
		} else {

		}
	}
	
	private String[] getAdmins(ProductFamily family)
	{
		List<String> emails = new ArrayList<String>();
		for(User u : ugd.getAdmins(new Resource(family.getTableName(),Resource.DATATABLE)))
		{
			emails.add(u.getEmailAddress());			
		}
		return emails.toArray(new String[emails.size()]);
	}

	private void setProperties(Properties props, HandlerData info) {
		Collection<String> meta = info.getParamNames("lb_");
		for (String m : meta) {
			props.setProperty(m.substring(3), info.getParameter(m));
		}
	}

	private void logProperties(UploadItem logit, Properties meta) {
		for (Object o : meta.keySet()) {
			logit.setValue((String) o, meta.getProperty((String) o));
		}
	}

	private synchronized void writeZipFile(HandlerData info, String filename,
			Properties meta, UploadItem logit, ProductFamily pf)
			throws Exception {
		boolean temp = pf.getRequiredAndOptionalUploadProperties().length > 0;
		getLog().info(
				"temp = " + temp + " "
						+ pf.getRequiredAndOptionalUploadProperties().length);
		String uploadDirectory = info.getParameter("has_new_cat", "false")
				.equals("false") ? uploadDir : altUploadDir;
		File f = new File(uploadDirectory, "upload_"
				+ Converter.formatCalendar(new GregorianCalendar(), "yyMMdd")
				+ ".zip");
		while (f.exists()) {
			f = new File(uploadDirectory, "upload_"
					+ (count++)
					+ Converter.formatCalendar(new GregorianCalendar(),
							"yyMMdd") + ".zip");
		}
		if (temp) {
			f = new File(new File(f.getParentFile(), "temp"), f.getName());
			f.getParentFile().mkdirs();
		}
		ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
				new FileOutputStream(f)));
		ArrayList<String> uploadedImages = new ArrayList<String>();
		try {
			out.putNextEntry(new ZipEntry("imagedata.properties"));
			meta.store(out, null);
			out.closeEntry();
			if (!filename.endsWith(".zip")) {
				InputStream in = info.getFileData("file-to-upload");
				logit.setValue("file", filename);
				long filesize = 0;
				try {
					filesize = writeStreamToZip(filename, out, in);
					uploadedImages.add(filename);
					logit.setValue("filesize", String.valueOf(filesize));
					dblogger.addLogItem(logit);
				} finally {
					if (in != null)
						in.close();
				}
			} else {
				transferZipEntries(new ZipInputStream(info
						.getFileData("file-to-upload")), out, logit,
						uploadedImages);
			}
			if (temp) {
				info.setUserBean("uploaded_file_list", uploadedImages);
				info.setUserBean("properties_to_be_set", pf
						.getRequiredAndOptionalUploadProperties());
			}
			info.setUserBean("uploaded_file", f.getAbsolutePath());
		} finally {
			if (out != null)
				out.close();
		}
	}

	private void transferZipEntries(ZipInputStream inZip,
			ZipOutputStream outZip, UploadItem logit,
			List<String> uploadedImages) throws IOException,
			BadFilenameCharacter {
		try {
			int filecount = 1;
			ZipEntry entry = inZip.getNextEntry();
			while (entry != null) {
				if (entry.getName().indexOf(".") > -1) {
					UploadItem newLogit = (UploadItem) logit
							.copy(new UploadItem());
					newLogit.setValue("file_" + (filecount++), Files
							.getNameOnly(entry.getName()));
					newLogit.setValue("filesize", String
							.valueOf(writeStreamToZip(Files.getNameOnly(entry
									.getName()), outZip, inZip)));
					uploadedImages.add(Files.getNameOnly(entry.getName()));
					dblogger.addLogItem(newLogit);
				}
				entry = inZip.getNextEntry();
			}
		} finally {
			if (inZip != null)
				inZip.close();
		}
	}

	private long writeStreamToZip(String filename, ZipOutputStream out,
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
		long numBytes = Files.copy(in, out);
		if (numBytes == 0)
			throw new IOException("No File uploaded");
		out.closeEntry();
		return numBytes;
	}

	private ProductFamily getProductFamily(HandlerData info) {
		return dbutil.getProductFamilyFromDescription(info.getParameter(
				"categories", "").split(",")[0]);
	}

	@CoinjemaDependency(alias = "uploadDir", hasDefault = true)
	public void setUploadDir(String ud) {
		uploadDir = ud;
	}

	@CoinjemaDependency(alias = "altUploadDir", hasDefault = true)
	public void setAltUploadDir(String aud) {
		altUploadDir = aud;
	}

	@CoinjemaDependency(type = "dbutil")
	public void setDbutil(DatabaseUtilities dbutil) {
		this.dbutil = dbutil;
	}

	@CoinjemaDependency(type = "databaseLogger")
	public void setDblogger(DatabaseLogger dblogger) {
		this.dblogger = dblogger;
	}

	@CoinjemaDependency(type = "emailService")
	public void setEmailer(NotificationService emailer) {
		this.emailer = emailer;
	}

	@CoinjemaDependency(method = "recipients")
	public void setUploadRecipients(String[] rec) {
		uploadRecipients = rec;
	}

	@CoinjemaDependency(type = "userService")
	public void setUserService(UserService u) {
		ugd = u;
	}

}

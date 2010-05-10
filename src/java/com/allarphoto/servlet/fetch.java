package com.allarphoto.servlet;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaDynamic;
import org.coinjema.context.CoinjemaObject;

import com.allarphoto.application.Product;
import com.allarphoto.client.beans.UserBean;
import com.allarphoto.ecommerce.DatabaseUtilities;
import com.sun.media.jai.codec.FileSeekableStream;

/*******************************************************************************
 * @param
 * @return
 ******************************************************************************/
@CoinjemaObject
public class fetch extends HttpServlet {
	private static final long serialVersionUID = 1;

	private static Logger log = fetch.getLogger();

	@CoinjemaDynamic(alias = "log4j")
	private static Logger getLogger() {
		return null;
	}

	private static final String fs = "/"; // System.getProperty("file.separator");

	String defaultFile;

	String defaultMime;

	String pathPrefix;

	DatabaseUtilities dbUtil;

	/***************************************************************************
	 * @param
	 * @return
	 **************************************************************************/
	public void init(ServletConfig cfg) throws ServletException {
		super.init(cfg);
		defaultFile = cfg.getServletContext().getRealPath(
				cfg.getInitParameter("default_file"));
		defaultMime = cfg.getInitParameter("default_mime");
	} // End Method

	/***************************************************************************
	 * @param
	 * @return
	 **************************************************************************/
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		if (null != req.getParameter("default_file")) {
			defaultFile = req.getParameter("default_file");
		}
		if (null == defaultFile) {
			throw new ServletException("default_file was not set");
		} else if (!(new File(defaultFile)).exists()) {
			throw new ServletException("default_file does not exist");
		}
		String mime = req.getParameter("mimetype");

		res.setContentType(mime);
		Product p = dbUtil.getProductFamily(req.getParameter("product_family"))
				.getProduct(
						req.getParameter("product_id"),
						((UserBean) req.getSession().getAttribute("user"))
								.getPermissions());
		String isThumb = req.getParameter("thumb");
		if ("true".equals(isThumb))
			isThumb = "_thumb";
		else
			isThumb = "_web";
		String fileName = pathPrefix + "/" + p.getPathName()
				+ p.getValue(isThumb);
		log.debug("Begin fetching " + fileName);
		if (fileName == null) {
			log.warn("fetch: using the default file, filename not set");
			fileName = defaultFile;
		} else { // try to use pv file, if available
			int brk = fileName.lastIndexOf(fs);
			String pv = fileName.substring(0, brk + 1) + "pv"
					+ fileName.substring(brk);
			File testFile = new File(pv);
			if (testFile.exists())
				fileName = pv;
		}
		File file = new File(fileName);
		if (!file.exists() || file.length() <= 0) {
			log.warn("fetch: Using default file, file: " + fileName
					+ " not found.");
			fileName = defaultFile;
			file = new File(fileName);
			mime = defaultMime;
		}
		if (mime == null) {
			log.warn("fetch: Using default file, mimetype: " + mime
					+ " not found.");
			mime = defaultMime;
			fileName = defaultFile;
			file = new File(fileName);
		}
		// BufferedInputStream fileIn;
		FileSeekableStream fileIn;
		try {

			log.debug("Trying to get " + fileName);
			// fileIn=new BufferedInputStream(new FileInputStream(file)); <--
			// for fetching with no scaling
			fileIn = new FileSeekableStream(file);
		} catch (IOException e) {
			log.warn("fetch: Unable to read data from file: " + fileName);
			mime = defaultMime;
			fileName = defaultFile;
			file = new File(fileName);
			// fileIn=new BufferedInputStream(new FileInputStream(file)); <--
			// for fetching with no scaling
			fileIn = new FileSeekableStream(file);
		}
		res.setContentLength((int) file.length());
		res.setContentType(mime);
		if (fileName.indexOf(fs) > -1) {
			res.setHeader("Content-Location", fileName.substring(fileName
					.lastIndexOf(fs)));
		} else {
			log
					.error("fetch: Bad Filename, missing correct file separator in: "
							+ fileName);
		}

		int height = 0, width = 0, maxDim = 0;
		if (null != req.getParameter("maxDim"))
			maxDim = Integer.valueOf(req.getParameter("maxDim")).intValue();
		res.addHeader("Cache-Control", "no-cache");
		res.addHeader("Pragma", "no-cache");
		res.addHeader("Expires", "0");

		ServletOutputStream out = res.getOutputStream();
		/*
		 * // Added for "on-the-fly" scaling of thumbnails // Create an operator
		 * to decode the image file. RenderedOp image1 = JAI.create("stream",
		 * fileIn); // Calculate scale factor float scale = 1.0F; if(maxDim > 0) {
		 * height = image1.getHeight(); width = image1.getWidth(); if (width >
		 * height && width > 0) scale = (float)maxDim /(float)width; else
		 * if(height > 0) scale = (float)maxDim /(float)height; } // set the
		 * encoder for the output stream, works for jpeg, gif and uncompressed
		 * tiff. JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out); //
		 * only scale images down, do not enlarge if(scale < 1.0F){ //Scaling
		 * interpolation for param block Interpolation interp =
		 * Interpolation.getInstance( Interpolation.INTERP_NEAREST); // Set up
		 * the param block ParameterBlock params = new ParameterBlock();
		 * params.addSource(image1); params.add(scale); // x scale factor
		 * params.add(scale); // y scale factor params.add(0.0F); // x translate
		 * params.add(0.0F); // y translate params.add(interp); // interpolation
		 * method log.debug("About to scale "+fileName); // Create an operator
		 * to scale image. RenderedOp image2 = JAI.create("scale", params);
		 * log.debug("Finished scaling "+fileName);
		 * encoder.encode(image2.getAsBufferedImage()); }else{
		 * encoder.encode(image1.getAsBufferedImage()); } //end new code for
		 * scaling on the fly log.debug("Finished ecoding "+fileName);
		 */
		// OLD code for fetching without scaling
		byte[] buffer = new byte[10000];
		int x = 0;
		x = fileIn.read(buffer);
		while (x > 0) {
			out.write(buffer, 0, x);
			x = fileIn.read(buffer);
		}

		fileIn.close();
		out.flush();
		out.close();

		log.debug("End fetching " + fileName);
	} // End Method

	@CoinjemaDependency(type = "dbutil")
	public void setDbUtil(DatabaseUtilities service) {
		dbUtil = service;
	}

	@CoinjemaDependency(alias = "physicalAsset.path")
	public void setPathPrefix(String pathPrefix) {
		this.pathPrefix = pathPrefix;
	}
} // End Class


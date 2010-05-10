package com.allarphoto.servlet.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author Administrator To change this generated comment edit the template
 *         variable "typecomment": Window>Preferences>Java>Templates.
 */
public class DownloadSpeed extends TagSupport {
	private static final long serialVersionUID = 1;

	float speed;

	long size;

	/**
	 * Set the total number of bytes to be downloaded
	 */
	public void setSize(String size) {
		try {
			System.out.println("Size = " + size);
			this.size = Long.parseLong(size);
		} catch (RuntimeException e) {
			this.size = 0L;
		}
	}

	/**
	 * Set the total number of bytes to be downloaded public void setSize(long
	 * size) { this.size = size; }
	 */
	/**
	 * Set the speed, in kbps, of the download connection
	 */
	public void setSpeed(String speed) {
		try {
			this.speed = Float.parseFloat(speed);
		} catch (RuntimeException e) {
			this.speed = 14.4F;
		}
	}

	/**
	 * Set the speed, in kbps, of the download connection
	 */
	public void setSpeed(float speed) {
		this.speed = speed;
	}

	/**
	 * Calculate and print the display string to the JSP
	 */
	public int doEndTag() throws JspException {
		try {
			this.pageContext.getOut().write(getDisplayString());
		} catch (IOException e) {
		}
		return super.doEndTag();
	}

	private String getDisplayString() {
		StringBuffer response = new StringBuffer();
		double dlHours = ((size / 1024) * 8 * 1.1 / speed) / 3600;
		int dlIntHours = (int) Math.floor(dlHours);
		double dlMins = (dlHours - (double) dlIntHours) * 60;
		int dlIntMins = (int) Math.floor(dlMins);
		int dlIntSecs = (int) Math.round((dlMins - (double) dlIntMins) * 60);
		if (dlHours >= 2) {
			response.append(dlIntHours + " hours");
		} else if (dlHours >= 1) {
			response.append(dlIntHours + " hour ");
		}
		if (dlMins >= 2 && dlHours < 15) {
			response.append(dlIntMins + "mins ");
		} else if (dlMins >= 1 && dlHours < 15) {
			response.append(dlIntMins + "min ");
		}
		if (dlHours < 1 && dlIntSecs > 2) {
			response.append(dlIntSecs + " secs");
		} else if (dlHours < 1 && dlIntSecs <= 2) {
			response.append("2 secs");
		}
		return response.toString();
	}
}

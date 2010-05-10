package com.allarphoto.kodak;

import org.apache.log4j.Logger;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaObject;

import strategiclibrary.util.Converter;

import com.allarphoto.application.Controller;
import com.allarphoto.application.Product;
import com.allarphoto.application.SecurityModel;
import com.allarphoto.client.beans.Cart;
import com.allarphoto.utils.Right;

public class KodakShoppingCartBean extends KProShoppingCartBean {

	public float calcCost(Cart cartObj) {
		int pixelsPerInch = Converter.getInt(controller
				.getConfigValue("pixes_per_inch"), 327);
		log.info("pixels per inch = " + pixelsPerInch);
		double bytes = ((Number) cartObj.prod.getValue("Width")).doubleValue()
				* ((Number) cartObj.prod.getValue("Height")).doubleValue();
		log.info("bytes = " + bytes);
		bytes = bytes * pixelsPerInch * pixelsPerInch;
		log.info("bytes = " + bytes);
		bytes = bytes / 2.255E20;
		log.info("bytes = " + bytes);
		return (float) (bytes * .39);
	}

	public boolean canDownloadHires(Cart cartObj, SecurityModel perms) {
		return canDownload(cartObj, perms) && isGar(cartObj);
	}

	public boolean isPDF(Cart cartObj) {
		return cartObj.prod.getPrimary().endsWith(".pdf");
	}

	public boolean isSampleImage(Cart cartObj) {
		return "Sample Pictures".equals(cartObj.prod.getValue("Image Type"));
	}

	public boolean isFtpOrder() {
		return containsProductInstruction("order", "ftp");
	}

	public boolean isCdOrder() {
		return containsProductInstruction("order", "cd");
	}

	public boolean canOrder(Cart cartObj, SecurityModel perms) {
		log.info("expiration tester = "
				+ cartObj.getProduct().getProductFamily()
						.getProductExpirationTester());
		return cartObj.getProduct().getProductFamily()
				.getProductExpirationTester().hasPermission(cartObj.prod,
						Right.ORDER, perms);
	}

	public boolean canDownload(Cart cartObj, SecurityModel perms) {
		return !isPrintSample(cartObj)
				&& cartObj.getProduct().getProductFamily()
						.getProductExpirationTester().hasPermission(
								cartObj.prod, Right.DOWNLOAD, perms);
	}

	public boolean isMovie(Cart cartObj) {
		return cartObj.prod.getPrimary().endsWith(".mov")
				|| cartObj.prod.getPrimary().endsWith(".mpg")
				|| cartObj.prod.getPrimary().endsWith(".avi");
	}

	public String getTagLine(String pre, Cart cartObj, String post,
			SecurityModel perms) {
		if (!cartObj.getProduct().getProductFamily()
				.getProductExpirationTester().hasPermission(cartObj.prod,
						Right.ORDER, perms))
			return pre + "To order or download this file, please<br>"
					+ "<A HREF='requestexpired.jsp?request_product_family="
					+ cartObj.prod.getProductFamilyName()
					+ "&request_product_id=" + cartObj.prod.getId() + "'>"
					+ "request renegotiation of usage rights.</A><br>" + post;
		if (isGar(cartObj))
			return pre
					+ "This image is available for<br>GAR usage only.<br>A high resolution version of this<br>image may be downloaded:"
					+ post;
		else if (isLogo(cartObj))
			return pre
					+ "A high resolution version of this<br>image may be downloaded:"
					+ post;
		else if (isPrintSample(cartObj))
			return pre
					+ "To order this image, please contact<br>Kim McAllister (585) 781-4257."
					+ post;
		else if (isReview(cartObj))
			return pre
					+ "To reach this model, please contact<br>Cindi Rohnke (585) 781-9407."
					+ post;
		else
			return null;
	}

	private boolean isGar(Cart cartObj) {
		return "GAR Photoshoot August 2002".equals(cartObj.prod
				.getValue("Business Category"));
	}

	public boolean isLogo(Cart cartObj) {
		getLog().info(
				"Business Category = "
						+ cartObj.prod.getValue("Business Category")
						+ " primary = " + cartObj.prod.getPrimary());
		return "Logos".equals(cartObj.prod.getValue("Business Category"))
				&& cartObj.prod.getPrimary().endsWith(".jpg");
	}

	private boolean isPrintSample(Cart cartObj) {
		return "Print Samples".equals(cartObj.prod.getValue("Folder 2"));
	}

	public boolean isReview(Cart cartObj) {
		return ("Kodak Kids Review".equals(cartObj.prod.getValue("Library")) || "Kodak Review Site"
				.equals(cartObj.prod.getValue("Library")))
				&& !isUnavailable(cartObj);
	}

	private boolean isUnavailable(Cart cartObj) {
		return !"Digital Cameras".equals(cartObj.prod.getValue("Image Type"));
	}

}

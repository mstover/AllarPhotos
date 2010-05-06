package com.lazerinc.servlet.actionhandler.admin.keyword;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.LinkedList;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.lazerinc.application.Product;
import com.lazerinc.client.exceptions.LazerwebException;
import com.lazerinc.client.util.ProductFileParser;
import com.lazerinc.servlet.actionhandler.ActionHandlerBase;

public class FileImport extends ActionHandlerBase {

	public String getName() {
		return "keyword_file_import";
	}

	public void performAction(HandlerData info) throws ActionException {
		try {
			BufferedReader dataReader = new BufferedReader(
					new InputStreamReader(info.getFileData("keyword_file"),
							info.getParameter("charSet", "iso-8859-1",
									emptyStrings)));
			Collection<String> lines = new LinkedList<String>();
			String line = null;
			try {
				while ((line = dataReader.readLine()) != null)
					lines.add(line);
			} catch (IOException e) {
				getLog().error("Couldn't read uploaded keyword file");
				throw new ActionException("BadFileUploadException");
			} finally {
				try {
					dataReader.close();
				} catch (IOException e) {
					getLog().error("Couldn't close uploaded keyword file");
					throw new ActionException("BadFileUploadException");
				}
			}
			ProductFileParser parser = new ProductFileParser(info
					.getParameter("product_family"), "\t",
					getCurrentUserPerms(info));
			Collection<Product> updatedProducts = parser
					.parseFileForProducts(lines
							.toArray(new String[lines.size()]));
			int countNonNulls = 0;
			for (Product p : updatedProducts) {
				if (p != null) {
					if (getLog().isDebugEnabled())
						getLog().debug("Updating product " + p.getName());
					dbUtil
							.getProductFamily(
									info.getParameter("product_family"))
							.updateProduct(p);
					addMessage("Product " + p.getName()
							+ " successfully updated", p, info);
					countNonNulls++;
				}
			}
			addMessage(countNonNulls + " Products Successfully updated out of "
					+ updatedProducts.size(), updatedProducts, info);
			addMessage("Bad Image Names in keyword file: "
					+ parser.getBadProducts(), null, info);
			addMessage(
					"Bad Columns in keyword file: " + parser.getBadColumns(),
					null, info);
		} catch (UnsupportedEncodingException e) {
			throw new LazerwebException("UnsupportedEncodingException", e);
		}
	}

}

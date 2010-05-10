package com.allarphoto.ajaxclient.client;

import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.allarphoto.ajaxclient.client.beans.AjaxCart;

public interface LibraryInfoAsync {

	public void getLibraryName(AsyncCallback callback);

	public void getUploadableFolders(String[] previousChioces,
			String familyDescriptiveName, AsyncCallback callback);

	public void getSearchFolders(String[][] previousChioces,
			String familyDescriptiveName, AsyncCallback callback);

	public void getProducts(String[][] categoryChoices,
			String familyDescriptiveName, int chunkSize, int chunkSet,
			AsyncCallback callback);

	public void getProductCount(String[][] categoryChoices,
			String familyDescription, AsyncCallback callback);

	public void getProductFamily(String family, AsyncCallback callback);

	public void getLibraries(String right, AsyncCallback callback);

	public void updateField(String name, String fam, String typ, int dor,
			int so, AsyncCallback callback);

	public void refreshProductFamily(String family, AsyncCallback acb);

	public void getDownloadStats(String family, AsyncCallback acb);

	public void getDownloadStats(String family, String fromDate,
			AsyncCallback acb);

	public void getDownloadStats(String family, String fromDate, String toDate,
			AsyncCallback acb);

	public void addField(String family, String fieldName, AsyncCallback acb);

	public void getPendingUploads(AsyncCallback acb);

	public void approveUploadPackage(Map uploadParams, AsyncCallback acb);

	public void getCategoryValues(String family, String fieldname,
			AsyncCallback acb);

	public void getLibrarySize(String family, AsyncCallback acb);

	public void getDisplayableFields(String family, AsyncCallback acb);

	public void getEditableFields(String family, AsyncCallback acb);

	public void updateProduct(int productId, String familyName, Map values,
			AsyncCallback acb);

	public void moveAsset(int productId, String familyName, String newDir,
			String category, AsyncCallback acb);

	public void setFamilyRemotedManaged(String family, boolean rm,
			AsyncCallback acb);

	public void setProductSorter(String sorterName, AsyncCallback acb);
	
	public void deleteField(String fieldName,String familyName, AsyncCallback acb);
}

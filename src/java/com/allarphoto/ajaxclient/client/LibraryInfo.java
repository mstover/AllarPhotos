package com.allarphoto.ajaxclient.client;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.allarphoto.ajaxclient.client.beans.AjaxProduct;
import com.allarphoto.ajaxclient.client.beans.AjaxProductFamily;
import com.allarphoto.ajaxclient.client.beans.DownloadStats;

public interface LibraryInfo extends RemoteService {

	public String getLibraryName();

	public String[] getUploadableFolders(String[] previousChoices,
			String familyDescriptiveName);

	public String[] getSearchFolders(String[][] previousChoices,
			String familyDescriptiveName);

	public AjaxProduct[] getProducts(String[][] categoryChoices,
			String familyDescriptiveName, int chunkSize, int chunkSet);

	public int getProductCount(String[][] categoryChoices,
			String familyDescription);

	public AjaxProductFamily getProductFamily(String family);

	public AjaxProductFamily[] getLibraries(String right);

	public boolean updateField(String name, String fam, String typ, int dor,
			int so);

	public boolean refreshProductFamily(String family);

	public DownloadStats getDownloadStats(String family);

	public DownloadStats getDownloadStats(String family, String fromDate);

	public DownloadStats getDownloadStats(String family, String fromDate,
			String toDate);

	public boolean addField(String family, String fieldName);

	public boolean setFamilyRemotedManaged(String family, boolean rm);

	public boolean setProductSorter(String sorterName);

	/**
	 * @gwt.typeArgs <java.lang.String>
	 * @param family
	 * @param fieldname
	 * @return
	 */
	public Collection getCategoryValues(String family, String fieldname);

	/**
	 * @gwt.typeArgs <java.lang.String>
	 * @param family
	 * @return
	 */
	public List getDisplayableFields(String family);

	/**
	 * @gwt.typeArgs <com.allarphoto.ajaxclient.client.beans.AjaxProductField>
	 * @param family
	 * @return
	 */
	public List getEditableFields(String family);

	/**
	 * @gwt.typeArgs <java.util.Map>
	 * @return
	 */
	public List getPendingUploads();

	/**
	 * @gwt.typeArgs uploadParams <java.lang.String,java.lang.String>
	 * @param uploadParams
	 * @return
	 */
	public boolean approveUploadPackage(Map uploadParams);

	public String getLibrarySize(String family);

	/**
	 * @gwt.typeArgs values <java.lang.String,java.lang.String>
	 * @param p
	 * @param values
	 * @return
	 */
	public boolean updateProduct(int productId, String familyName, Map values);

	public boolean moveAsset(int productId, String familyName, String newDir,
			String category);
	
	public boolean deleteField(String fieldName,String familyName);
}

package com.lazerinc.ajaxclient.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.lazerinc.ajaxclient.client.beans.AjaxAccessRequest;
import com.lazerinc.ajaxclient.client.beans.AjaxCart;
import com.lazerinc.ajaxclient.client.beans.AjaxGroup;
import com.lazerinc.ajaxclient.client.beans.AjaxPermissions;
import com.lazerinc.ajaxclient.client.beans.AjaxUser;

public interface UserInformation extends RemoteService {

	public String getFullName();

	public String getCallingUrl();

	public AjaxGroup[] getGroups(boolean canAdmin);

	public AjaxUser getUser(String username);

	public AjaxUser getCurrentUser();

	public AjaxUser login(String username, String password, boolean setCookie);

	public boolean addUser(String firstName, String lastName, String email,
			String middleInitial, String username, String password,
			String phone, String company, String address1, String address2,
			String city, String zip, String state, String country,
			String expDate, String[] groups);

	public boolean updateUser(String firstName, String lastName, String email,
			String middleInitial, String username, String password,
			String phone, String company, String address1, String address2,
			String city, String zip, String state, String country,
			String expDate, String[] groups);

	public boolean addGroup(String groupName, String[] adminGroups);

	public AjaxUser[] getUsers(String group);

	/**
	 * @gwt.typeArgs <com.lazerinc.ajaxclient.client.beans.AjaxUser>
	 * @param canAdmin
	 * @return
	 */
	public List getUsers(boolean canAdmin);

	public AjaxGroup getGroup(String groupName);

	public boolean updateRight(String groupName, String resName, int resType,
			String right, boolean hasRight);

	public String[] getResourceNames(int resType);

	public boolean updateGroupName(String groupName, String newGroupName);

	public boolean deleteGroup(String groupName);

	public AjaxUser[] searchUsers(String searchText, boolean canAdmin);

	public boolean addUserToGroup(String username, String groupName);

	public boolean removeUserFromGroup(String username, String groupName);

	public boolean toggleUserToGroup(String username, String groupName);

	public boolean isAdmin();

	public AjaxAccessRequest[] getAccessRequests(boolean canAdmin);

	public boolean rejectAccessRequest(int requestId, String feedback);

	public boolean grantAccessRequest(int requestId, String group,
			String feedback);

	public boolean sendPassword(String username);

	public boolean sendPasswordFromEmail(String email);

	public boolean deleteUser(String username);

	public boolean updateGroupDescription(String groupName,
			String newDescription);

	public String getSessionVariable(String var);

	public boolean clearSesseionVariable(String var);

	public AjaxPermissions getPermissions();

	/**
	 * @gwt.typeArgs libraries <java.lang.String>
	 * @param username
	 * @param libraries
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param company
	 * @param phone
	 * @param reason
	 * @return
	 */
	public boolean requestAccess(String username, List libraries,
			String firstName, String lastName, String email, String company,
			String phone, String reason);

	public String getBrowser();

	public boolean askQuestion(String name, String email, String question);

}

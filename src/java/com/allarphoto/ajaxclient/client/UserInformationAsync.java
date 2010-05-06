package com.lazerinc.ajaxclient.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.lazerinc.ajaxclient.client.beans.AjaxAccessRequest;
import com.lazerinc.ajaxclient.client.beans.AjaxCart;
import com.lazerinc.ajaxclient.client.beans.AjaxGroup;
import com.lazerinc.ajaxclient.client.beans.AjaxPermissions;
import com.lazerinc.ajaxclient.client.beans.AjaxResource;
import com.lazerinc.ajaxclient.client.beans.AjaxUser;

public interface UserInformationAsync {

	public void getFullName(AsyncCallback acb);

	public void getCallingUrl(AsyncCallback acb);

	public void getGroups(boolean ignorePerms, AsyncCallback acb);

	public void getUser(String username, AsyncCallback acb);

	public void getCurrentUser(AsyncCallback acb);

	public void login(String username, String password, boolean setCookie,
			AsyncCallback acb);

	public void addUser(String firstName, String lastName, String email,
			String middleInitial, String username, String password,
			String phone, String company, String address1, String address2,
			String city, String zip, String state, String country,
			String expDate, String[] groups, AsyncCallback acb);

	public void updateUser(String firstName, String lastName, String email,
			String middleInitial, String username, String password,
			String phone, String company, String address1, String address2,
			String city, String zip, String state, String country,
			String expDate, String[] groups, AsyncCallback acb);

	public void addGroup(String groupName, String[] adminGroups,
			AsyncCallback acb);

	public void getUsers(String group, AsyncCallback acb);

	public void getUsers(boolean canAdmin, AsyncCallback acb);

	public void getGroup(String groupName, AsyncCallback acb);

	public void updateRight(String groupName, String resName, int resType,
			String right, boolean hasRight, AsyncCallback acb);

	public void getResourceNames(int resType, AsyncCallback acb);

	public void updateGroupName(String groupName, String newGroupName,
			AsyncCallback acb);

	public void deleteGroup(String groupName, AsyncCallback acb);

	public void searchUsers(String searchText, boolean canAdmin,
			AsyncCallback acb);

	public void addUserToGroup(String username, String groupName,
			AsyncCallback acb);

	public void removeUserFromGroup(String username, String groupName,
			AsyncCallback acb);

	public void toggleUserToGroup(String username, String groupName,
			AsyncCallback acb);

	public void isAdmin(AsyncCallback acb);

	public void getAccessRequests(boolean canAdmin, AsyncCallback acb);

	public void rejectAccessRequest(int requestId, String feedback,
			AsyncCallback acb);

	public void grantAccessRequest(int requestId, String group,
			String feedback, AsyncCallback acb);

	public void sendPassword(String username, AsyncCallback acb);

	public void sendPasswordFromEmail(String email, AsyncCallback acb);

	public void deleteUser(String username, AsyncCallback acb);

	public void updateGroupDescription(String groupName, String newDescription,
			AsyncCallback acb);

	public void getSessionVariable(String var, AsyncCallback acb);

	public void clearSesseionVariable(String var, AsyncCallback acb);

	public void getPermissions(AsyncCallback acb);

	public void requestAccess(String username, List libraries,
			String firstName, String lastName, String email, String company,
			String phone, String reason, AsyncCallback acb);

	public void getBrowser(AsyncCallback acb);

	public void askQuestion(String name, String email, String question,
			AsyncCallback acb);
}

package com.lazerinc.client.beans;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.coinjema.collections.HashTree;

import com.lazerinc.application.SecurityModel;
import com.lazerinc.beans.City;
import com.lazerinc.beans.Company;
import com.lazerinc.beans.Country;
import com.lazerinc.beans.Referrer;
import com.lazerinc.beans.State;
import com.lazerinc.ecommerce.CommerceUser;
import com.lazerinc.ecommerce.UserGroup;
import com.lazerinc.utils.Functions;
import com.lazerinc.utils.Resource;
import com.lazerinc.utils.Right;

public class UserBean implements Serializable {
	private static final long serialVersionUID = 1;

	SecurityModel permissions;

	CommerceUser user;

	private String clientType;

	public UserBean() {
	}

	public SecurityModel getPermissions() {
		return permissions;
	}

	public void clear() {
		setUser(null);
		setPermissions(null);
	}

	public void setPermissions(SecurityModel perm) {
		permissions = perm;
	}

	public void setUser(CommerceUser user) {
		this.user = user;
	}

	public String getPassword() {
		return getUser().getPassword();
	}

	public String getUsername() {
		if (getUser() == null) {
			return null;
		}
		return getUser().getUsername();
	}

	public CommerceUser getUser() {
		return user;
	}

	public String getFirstName() {
		return getUser().getFirstName();
	}

	public String getEmailAddress() {
		return getUser().getEmailAddress();
	}

	public String getShipAddress1() {
		return getUser().getShipAddress1();
	}

	public String getBillAddress1() {
		return getUser().getBillAddress1();
	}

	public String getBillAddress2() {
		return getUser().getBillAddress2();
	}

	public State getBillState() {
		return getUser().getBillState();
	}

	public String getBillZip() {
		return getUser().getBillZip();
	}

	public City getBillCity() {
		return getUser().getBillCity();
	}

	public Country getBillCountry() {
		return getUser().getBillCountry();
	}

	public String getShipAddress2() {
		return getUser().getShipAddress2();
	}

	public Referrer getReferrer() {
		return getUser().getReferrer();
	}

	public String getFax() {
		return getUser().getFax();
	}

	public String getPhone() {
		return getUser().getPhone();
	}

	public Company getCompany() {
		return getUser().getCompany();
	}

	public Country getShipCountry() {
		return getUser().getShipCountry();
	}

	public String getMiddleInitial() {
		return getUser().getMiddleInitial();
	}

	public String getLastName() {
		return getUser().getLastName();
	}

	public State getShipState() {
		return getUser().getShipState();
	}

	public String getShipZip() {
		return getUser().getShipZip();
	}

	public City getShipCity() {
		return getUser().getShipCity();
	}

	public String[] listFamiliesCanAdmin() {
		Collection set = getPermissions().getAvailableResourceList(
				Resource.DATATABLE, Right.ADMIN);
		String[] retVal = new String[set.size()];
		int x = 0;
		Iterator it = set.iterator();
		while (it.hasNext())
			retVal[x++] = ((Resource) it.next()).getName();
		return retVal;
	}

	public String[] listFamiliesCanRead() {
		Collection set = getPermissions().getAvailableResourceList(
				Resource.DATATABLE, Right.READ);
		String[] retVal = new String[set.size()];
		int x = 0;
		Iterator it = set.iterator();
		while (it.hasNext())
			retVal[x++] = ((Resource) it.next()).getName();
		return retVal;
	}

	public boolean getCanReadFamily(String family) {
		boolean retVal = false;
		Collection set = getPermissions().getAvailableResourceList(
				Resource.DATATABLE, Right.READ);
		int x = 0;
		Iterator it = set.iterator();
		while (it.hasNext())
			if ((((Resource) it.next()).getName()).equals(family)) {
				retVal = true;
				break;
			}
		return retVal;
	}

	public String[] listGroupsCanAdmin() {
		Collection set = getPermissions().getAvailableResourceList(
				Resource.GROUP, Right.ADMIN);
		String[] retVal = new String[set.size()];
		int x = 0;
		Iterator it = set.iterator();
		while (it.hasNext())
			retVal[x++] = ((Resource) it.next()).getName();
		return retVal;
	}

	public String[] listMerchantsCanAdmin() {
		Collection list = getPermissions().getAvailableResourceList(
				Resource.MERCHANT, Right.ADMIN);
		String[] retVal = new String[list.size()];
		int x = 0;
		Iterator it = list.iterator();
		while (it.hasNext())
			retVal[x++] = ((Resource) it.next()).getName();
		return retVal;
	}

	public String[] listUsersCanAdmin() {
		Collection set = getPermissions().getAvailableResourceList(
				Resource.USER, Right.ADMIN);
		String[] retVal = new String[set.size()];
		int x = 0;
		Iterator it = set.iterator();
		while (it.hasNext())
			retVal[x++] = ((Resource) it.next()).getName();
		return retVal;
	}

	public HashTree mapFields() {
		Collection tables = getPermissions().getAvailableResourceList(
				Resource.DATATABLE, Right.ADMIN);
		Collection fields = getPermissions().getAvailableResourceList(
				Resource.PROTECTED_FIELD);
		Set names = new HashSet();
		HashTree list = new HashTree();
		for (Iterator x = tables.iterator(); x.hasNext();)
			names.add(((Resource) x.next()).getName());
		for (Iterator x = fields.iterator(); x.hasNext();) {
			String resName = ((Resource) x.next()).getName();
			String[] splitField = Functions.split(resName, ".");
			if (splitField.length == 3) {
				if (names.contains(splitField[0]))
					list.add(new String[] { splitField[0], splitField[1] },
							new String[] { splitField[2] });
			}
		}
		return list;
	}

	public boolean isGroupAdmin(String group) {
		boolean retVal = false;
		String[] groups = listGroupsCanAdmin();
		for (int i = 0; i < groups.length; i++) {
			if (groups[i].equals(group)) {
				retVal = true;
			}
		}
		return retVal;
	}

	public boolean isUserInfoComplete() {
		boolean retVal = false;

		return retVal;
	}

	public boolean isShippingInfoComplete() {
		boolean retVal = false;

		return retVal;
	}

	public boolean isBillingInfoComplete() {
		boolean retVal = false;

		return retVal;

	}

	public void setBillingEqualsShippingTrue() {
	}

	public void setBillingEqualsShippingFalse() {
	}

	/**
	 * Returns the clientType.
	 * 
	 * @return String
	 */
	public String getClientType() {
		return clientType;
	}

	/**
	 * Sets the clientType.
	 * 
	 * @param clientType
	 *            The clientType to set
	 */
	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

	public Collection<UserGroup> getGroups() {
		return user.getGroups();
	}

}
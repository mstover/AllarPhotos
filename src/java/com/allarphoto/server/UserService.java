package com.allarphoto.server;

import java.io.StringWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.coinjema.context.CoinjemaDependency;
import org.coinjema.context.CoinjemaDynamic;
import org.coinjema.context.CoinjemaObject;

import strategiclibrary.service.cache.CacheService;
import strategiclibrary.service.notification.NotificationService;
import strategiclibrary.service.sql.ObjectMappingService;
import strategiclibrary.service.template.TemplateService;
import strategiclibrary.service.webaction.HandlerData;

import com.allarphoto.application.SecurityModel;
import com.allarphoto.beans.User;
import com.allarphoto.client.exceptions.InformationalException;
import com.allarphoto.client.exceptions.LazerwebException;
import com.allarphoto.dbtools.DBConnect;
import com.allarphoto.ecommerce.CommerceUser;
import com.allarphoto.ecommerce.DatabaseUtilities;
import com.allarphoto.ecommerce.UserGroup;
import com.allarphoto.security.VirtualGroupBasedSecurity;
import com.allarphoto.utils.Resource;
import com.allarphoto.utils.Right;
import com.allarphoto.utils.Rights;

@CoinjemaObject(type = "userService")
public class UserService {
	static char[] digits = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
			'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
			'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
			'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
			'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	private Logger log;

	DBConnect database;

	DatabaseUtilities dbUtil;

	// Table Names
	static final String priceBreakTable = "price_break";

	// static final String productTable = "product_tables";

	static final String priceKeyTable = "price_keys";

	static final String descriptionTable = "description";

	static final String categoryTable = "category";

	static final String statTable = "stats";

	static final String keywordTable = "_key";

	static final String priceTable = "price";

	static final String fieldTable = "fields";

	static final String referrerTable = "referrers";

	static final String companyTable = "companies";

	static final String industryTable = "industries";

	static final String groupTable = "groups";

	static final String userTable = "users";

	static final String stateTable = "states";

	static final String cityTable = "cities";

	static final String countryTable = "countries";

	static final String merchantTable = "merchants";

	static final String merchantProductTable = "merchant_product_tables";

	static final String userGroupTable = "user_group";

	static final String transactionTable = "transactions";

	static final String securityTable = "security";

	static final String resourcesTable = "resources";

	static final String rightsTable = "rights";

	static final String merchantTaxedStatesTable = "merchant_tax_states";

	static final String creditCardTable = "credit_cards";

	static final String descriptionKeyTable = "_descript";

	static final String orderTable = "orders";

	// Column Names
	static final String attributeIDColumn = "att_id";

	static final String valueIDColumn = "value_id";

	static final String longValueIDColumn = "long_value_id";

	static final String longValueColumn = "long_value";

	static final String attributeTypeColumn = "att_type";

	static final String expirationDateColumn = "exp_date";

	static final String quantityColumn = "quantity";

	static final String descriptionIDColumn = "description_id";

	static final String valueColumn = "val_col";

	static final String ccIDColumn = "credit_id";

	static final String ccTypeColumn = "cc_type";

	static final String ccNumberColumn = "cc_number";

	static final String expDateColumn = "exp_date";

	static final String taxRateColumn = "sales_tax";

	static final String fieldIDColumn = "field_id";

	static final String fieldNameColumn = "name";

	static final String fieldTypeColumn = "field_type";

	static final String searchOrderColumn = "search_order";

	static final String displayOrderColumn = "display_order";

	static final String productIDColumn = "product_id";

	static final String primaryLabelColumn = "primary_label";

	static final String nameColumn = "name";

	static final String priceColumn = "price";

	static final String keywordIDColumn = "keyword_id";

	static final String keywordColumn = "keyword";

	static final String productTableIDColumn = "product_table_id";

	static final String breakPointColumn = "break_point";

	static final String tableNameColumn = "table_name";

	static final String descriptiveNameColumn = "descriptive_name";

	static final String priceKeyIDColumn = "price_key_id";

	static final String priceKeyColumn = "price_key";

	static final String descriptionColumn = "description";

	static final String inventoryColumn = "inventory";

	static final String dateCatalogedColumn = "date_cataloged";

	static final String productTypeColumn = "product_type";

	// static final String typeColumn = "type";
	static final String merchantAdministrationTable = "merchant_admin";

	static final String groupAdministrationTable = "group_admin";

	static final String groupAdministerIDColumn = "administers_id";

	static final String browseRightsColumn = "browse_rights";

	static final String adminRightsColumn = "admin_rights";

	static final String orderRightsColumn = "order_rights";

	static final String downloadRightsColumn = "download_rights";

	static final String companyIDColumn = "company_id";

	static final String industryIDColumn = "industry_id";

	static final String referrerIDColumn = "referrer_id";

	static final String groupIDColumn = "group_id";

	static final String firstNameColumn = "first_name";

	static final String lastNameColumn = "last_name";

	static final String middleInitialColumn = "middle_initial";

	static final String usernameColumn = "username";

	static final String passwordColumn = "passwd";

	static final String emailColumn = "email";

	static final String billPhoneColumn = "bill_phone";

	static final String billAddress1Column = "bill_address1";

	static final String billAddress2Column = "bill_address2";

	static final String billCityIDColumn = "bill_city_id";

	static final String billStateIDColumn = "bill_state_id";

	static final String billZipColumn = "bill_zip";

	static final String billCountryIDColumn = "bill_country_id";

	static final String faxColumn = "fax";

	static final String shipPhoneColumn = "ship_phone";

	static final String shipAddress1Column = "ship_address1";

	static final String shipAddress2Column = "ship_address2";

	static final String shipCityIDColumn = "ship_city_id";

	static final String shipStateIDColumn = "ship_state_id";

	static final String shipZipColumn = "ship_zip";

	static final String shipCountryIDColumn = "ship_country_id";

	static final String userIDColumn = "user_id";

	static final String stateIDColumn = "state_id";

	static final String stateAbbrColumn = "abbr";

	static final String cityIDColumn = "city_id";

	static final String countryIDColumn = "country_id";

	static final String countryCodeColumn = "country_code";

	static final String orderingEmailColumn = "ordering_email";

	static final String phoneColumn = "phone";

	static final String address1Column = "address1";

	static final String address2Column = "address2";

	static final String creditCardsAcceptedColumn = "credit_cards";

	static final String fulfillmentEmailColumn = "fulfillment_email";

	static final String orderProcessingColumn = "order_processing";

	static final String zipColumn = "zip";

	static final String merchantModelColumn = "order_model";

	static final String merchantIDColumn = "merchant_id";

	static final String modelClassColumn = "order_model";

	static final String resourceIDColumn = "resource_id";

	static final String resourceTypeColumn = "resource_type";

	static final String rightIDColumn = "right_id";

	static final String transactionIDColumn = "transaction_id";

	static final String dateEnteredColumn = "date_entered";

	static final String dateFulfilledColumn = "date_fulfilled";

	static final String authCodeColumn = "auth_code";

	static final String billCodeColumn = "bill_code";

	static final String referenceNumColumn = "reference_num";

	static final String requestNumColumn = "request_num";

	static final String authMsgColumn = "auth_msg";

	static final String billMsgColumn = "bill_msg";

	static final String authAmountColumn = "auth_amount";

	static final String billAmountColumn = "bill_amount";

	static final String timeBilledColumn = "time_billed";

	static final String currencyColumn = "currency";

	CacheService userCache;

	ObjectMappingService objectMapper;

	public UserService() {
	}

	Logger getLog() {
		return log;
	}

	public SecurityModel getSecurity(CommerceUser user) {
		SecurityModel permissions = new VirtualGroupBasedSecurity(user, user
				.getGroups());
		return permissions;
	}

	public Collection<UserGroup> findGroups(Map<String, Object> searchTerms) {
		return objectMapper.getObjects("findGroups.sql", searchTerms);
	}

	public Collection<UserGroup> getGroups(CommerceUser u) {
		return userCache.getCache(UserGroup.class).getCachedObjects("user", u);
	}

	public UserGroup getGroup(String groupName) {
		return (UserGroup) userCache.getCache(UserGroup.class).getCachedObject(
				"name", groupName);
	}

	public UserGroup getGroup(int id) {
		return (UserGroup) userCache.getCache(UserGroup.class).getCachedObject(
				"id", id);
	}

	@CoinjemaDependency(method = "objectMapper")
	public void setObjectMapper(ObjectMappingService oms) {
		objectMapper = oms;
	}

	public Collection<CommerceUser> findUsers(Map<String, Object> searchTerms) {
		return objectMapper.getObjects("findUsers.sql", searchTerms);
	}

	public Collection<CommerceUser> searchUsers(String searchTerm) {
		return userCache.getCache(CommerceUser.class).getCachedObjects("NULL",
				searchTerm);
	}

	public CommerceUser checkUser(String username, String password)
			throws LazerwebException {
		if (username == null)
			username = dbUtil.DEFAULT;
		CommerceUser user = getUser(username);
		if (password == null)
			password = dbUtil.DEFAULT;
		if (user == null
				|| !password.equals(user.getPassword())
				|| (user.getExpDate() != null && new GregorianCalendar()
						.after(user.getExpDate()))) {
			getLog().debug(
					"User " + username + " failed to log in with password '"
							+ password + "'");
			if (user != null)
				getLog().debug(
						"actual password = '" + user.getPassword()
								+ "' equals("
								+ password.equals(user.getPassword())
								+ ") and expiration date = "
								+ user.getExpDate());
			throw new InformationalException(LazerwebException.INVALID_LOG_IN);
		}
		return user;
	}

	public CommerceUser checkUserWithEncPasswd(String username, String encPasswd)
			throws LazerwebException {
		if (username == null)
			username = dbUtil.DEFAULT;
		CommerceUser user = getUser(username);
		if (encPasswd == null)
			encPasswd = CommerceUser.encrypt(dbUtil.DEFAULT);
		if (user == null
				|| !encPasswd.equals(user.getEncryptedPassword())
				|| (user.getExpDate() != null && new GregorianCalendar()
						.after(user.getExpDate()))) {
			getLog().debug(
					"User " + username + " failed to log in with password '"
							+ encPasswd + "'");
			if (user != null)
				getLog().debug(
						"actual password = '" + user.getEncryptedPassword()
								+ "' equals("
								+ encPasswd.equals(user.getPassword())
								+ ") and expiration date = "
								+ user.getExpDate());
			throw new InformationalException(LazerwebException.INVALID_LOG_IN);
		}
		return user;
	}

	public void saveUserProperties(CommerceUser user) {
		((DatabaseUtilities) dbUtil).saveUserProperties(user);
	}

	@CoinjemaDependency(alias = "log4j")
	public void setLog(Logger l) {
		log = l;
	}

	@CoinjemaDependency(method = "userCache")
	public void setUserCache(CacheService c) {
		System.out.println("Setting usercache");
		userCache = c;
	}

	public void addGroup(String groupName, String administratingGroup,
			SecurityModel security) {
		if (security.getPermission(administratingGroup, Resource.GROUP,
				Right.ADMIN)) {
			UserGroup group = new UserGroup();
			group.setName(groupName);
			group.setId((int) dbUtil.getNewId("groups"));
			userCache.getCache(UserGroup.class).addItem(group);
			dbUtil.addResource(groupName, Resource.GROUP);
			updateGroupRights(administratingGroup, resService.getResource(
					groupName, Resource.GROUP), Right.ADMIN, true, null,
					security);
		} else {
			getLog().warn(
					"User does not have admin privileges over group: "
							+ administratingGroup);
		}
	}

	/***************************************************************************
	 * Sets a permission value for a group. In order to do this, the current set
	 * of groups must have admin permission for the given groupName, and must
	 * have admin permission for the given resource.
	 * 
	 * @param groupName
	 *            Name of group for which to set permission.
	 * @param resource
	 *            name of resource setting permission for.
	 * @param type
	 *            Resouce type.
	 * @param right
	 *            Name of right to set.
	 * @param value
	 *            True if group has the right, false otherwise.
	 **************************************************************************/
	public void updateGroupRights(String groupName, Resource resource,
			Right right, boolean value, GregorianCalendar cal,
			SecurityModel security) {
		updateGroupRights(getGroup(groupName), resource, right, value, cal,
				security);
	}

	public void updateGroupRights(UserGroup group, Resource res, Right right,
			boolean value, GregorianCalendar cal, SecurityModel security) {
		if (security
				.getPermission(group.getName(), Resource.GROUP, Right.ADMIN) && security.getPermission(res,Right.ADMIN)) {
			updateGroupRights(group, res, right.value(), value, cal, security);
			group.updateRight(res, right, value);
		} else
			throw new RuntimeException(LazerwebException.INVALID_PERMISSION);
	}

	private void updateGroupRights(UserGroup group, Resource resource,
			String right, boolean value, GregorianCalendar cal,
			SecurityModel security) {
		if (security
				.getPermission(group.getName(), Resource.GROUP, Right.ADMIN)) {
			String[] cols, vals;
			int rightID = dbUtil.getRightID(right);
			if (cal == null) {
				cols = new String[] { groupIDColumn, resourceIDColumn,
						rightIDColumn, expDateColumn };
				vals = new String[] { "" + group.getId(),
						"" + resource.getId(), "" + rightID, "NULL" };
			} else {
				cols = new String[] { groupIDColumn, resourceIDColumn,
						rightIDColumn, expDateColumn };
				vals = new String[] { "" + group.getId(),
						"" + resource.getId(), "" + rightID,
						"'" + database.getDate(cal) + "'" };
			}
			String where = database.where(groupIDColumn, "" + group.getId(),
					DBConnect.EQ, DBConnect.NUMBER)
					+ " AND "
					+ database.where(resourceIDColumn, "" + resource.getId(),
							DBConnect.EQ, DBConnect.NUMBER)
					+ " AND "
					+ database.where(rightIDColumn, "" + rightID, DBConnect.EQ,
							DBConnect.NUMBER);
			database.delete(securityTable, where);
			if (value) {
				database.insert(securityTable, cols, vals);
			}
		} else
			throw new RuntimeException(LazerwebException.INVALID_PERMISSION);
	}
	
	public Collection<UserGroup> getGroupsWithPrivs(Resource res,Right rght) {

		Set<UserGroup> admins = new HashSet<UserGroup>();
		for (UserGroup g : userCache.getCache(UserGroup.class).getCachedList()) {
			if (g.getPermission(res, rght)
					&& !g.getPermission("all", Resource.DATABASE, Right.ADMIN)) {
					admins.add(g);
			}
		}
		if (admins.size() == 0) {
			for (UserGroup g : userCache.getCache(UserGroup.class)
					.getCachedList()) {
				if (g.getPermission("all", Resource.DATABASE, Right.ADMIN)) {
						admins.add(g);
				}
			}
		}
		return admins;
	}

	public Collection<User> getAdmins(Resource res) {
		Set<User> admins = new HashSet<User>();
		for (UserGroup g : userCache.getCache(UserGroup.class).getCachedList()) {
			if (g.getPermission(res, Right.ADMIN)
					&& !g.getPermission("all", Resource.DATABASE, Right.ADMIN)) {
				for (User u : g.getUsers()) {
					admins.add(u);
				}
			}
		}
		return admins;
	}

	/***************************************************************************
	 * Updates a user in the database.
	 * 
	 * @return true if successful, false otherwise.
	 **************************************************************************/
	public boolean updateUser(CommerceUser user, SecurityModel security) {
		return updateUser(user, security, false);
	}

	/***************************************************************************
	 * Updates a user in the database.
	 * 
	 * @return true if successful, false otherwise.
	 **************************************************************************/
	public boolean updateUser(CommerceUser user, SecurityModel security,
			boolean self) {
		boolean retVal;
		if ((security == null && self)
				|| security.getPermission(user.getUsername(), Rights.USER,
						Right.ADMIN)) {
			userCache.getCache(CommerceUser.class).updateItem(user);
			retVal = true;
		} else
			retVal = false;
		return retVal;
	}

	public SecurityModel getGroupRights(String groupName, SecurityModel security) {
		UserGroup g = getGroup(groupName);
		return g;
	}

	public CommerceUser getUser(String username) {
		return userCache.getCache(CommerceUser.class).getCachedObject(
				"username", username);
	}

	public CommerceUser getUserByEmail(String email) {
		return userCache.getCache(CommerceUser.class).getCachedObject("email",
				email);
	}

	public CommerceUser getUser(int id) {
		return userCache.getCache(CommerceUser.class).getCachedObject("id", id);
	}

	public Collection<CommerceUser> getUsers(Collection<UserGroup> groups) {
		Collection<CommerceUser> users = new TreeSet<CommerceUser>();
		for (UserGroup group : groups) {
			users.addAll(userCache.getCache(CommerceUser.class)
					.getCachedObjects("group", group));
		}
		return users;
	}

	public Collection<CommerceUser> getUsers(SecurityModel security) {
		Collection<CommerceUser> users = new TreeSet<CommerceUser>();
		for (Resource res : (Collection<Resource>) security
				.getAvailableResourceList(Resource.GROUP, Right.ADMIN)) {
			users.addAll(userCache.getCache(CommerceUser.class)
					.getCachedObjects("group", getGroup(res.getName())));
		}
		return users;
	}

	public Collection<CommerceUser> listUsers(String groupName,
			SecurityModel security) {
		if (security.getPermission(groupName, Resource.GROUP, Right.ADMIN)) {
			return userCache.getCache(CommerceUser.class).getCachedObjects(
					"group", getGroup(groupName));
		}
		return Collections.EMPTY_LIST;
	}

	/***************************************************************************
	 * Sets a permission value for a group. In order to do this, the current set
	 * of groups must have admin permission for the given groupName, and must
	 * have admin permission for the given resource.
	 * 
	 * @param groupName
	 *            Name of group for which to set permission.
	 * @param right
	 *            Rights object.
	 **************************************************************************/
	public void updateGroupRights(String groupName, Rights right,
			SecurityModel security) {
		if (security.getPermission(groupName, Resource.GROUP, Right.ADMIN)) {
			String where = database.where(groupIDColumn, dbUtil
					.getGroupID(groupName), DBConnect.EQ, DBConnect.NUMBER)
					+ " AND "
					+ database.where(resourceIDColumn, right.getResource()
							.getId(), DBConnect.EQ, DBConnect.NUMBER);
			database.delete(securityTable, where);
			String[] rghts = right.getRightNames();
			Resource resource = right.getResource();
			for (int x = 0; x < rghts.length; x++)
				updateGroupRights(groupName, resource,
						Right.getRight(rghts[x]), right.getRight(rghts[x]),
						null, security);
		} else
			throw new RuntimeException(LazerwebException.INVALID_PERMISSION);
	}

	/***************************************************************************
	 * Updates a group in the database - effectively, changes the name of the
	 * group.
	 * 
	 * @param oldName
	 *            Current name of group.
	 * @param newName
	 *            Name to change to.
	 **************************************************************************/
	public void updateGroup(String oldName, String newName,
			SecurityModel security) // changes database, should throw event
	{
		if (security.getPermission(oldName, Rights.GROUP, Right.ADMIN)) {
			UserGroup grp = userCache.getCache(UserGroup.class)
					.getCachedObject("name", oldName);
			grp.setName(newName);
			int groupID = dbUtil.getGroupID(oldName);
			String where = database.where(groupIDColumn, groupID, DBConnect.EQ,
					DBConnect.NUMBER);
			String[] cols = { nameColumn };
			String[] vals = { database.cleanString(newName) };
			database.update(groupTable, cols, vals, where);
			Resource res = resService.getResource(oldName, Resource.GROUP);
			res.setName(newName);
			res.update();
			userCache.getCache(UserGroup.class).updateItem(grp);
		} else
			throw new RuntimeException(LazerwebException.INVALID_PERMISSION);
	}

	public void updateGroupDescription(String name, String newDescription,
			SecurityModel security) {
		if (security.getPermission(name, Rights.GROUP, Right.ADMIN)) {
			UserGroup grp = userCache.getCache(UserGroup.class)
					.getCachedObject("name", name);
			grp.setDescription(newDescription);
			int groupID = grp.getId();
			String where = database.where(groupIDColumn, groupID, DBConnect.EQ,
					DBConnect.NUMBER);
			String[] cols = { descriptionColumn };
			String[] vals = { database.cleanString(newDescription) };
			database.update(groupTable, cols, vals, where);
			userCache.getCache(UserGroup.class).updateItem(grp);
		} else
			throw new RuntimeException(LazerwebException.INVALID_PERMISSION);
	}

	public void removeUserFromGroup(String username, String groupName,
			SecurityModel security) {
		if (security.getPermission(username, Resource.USER, Right.ADMIN)
				&& security.getPermission(groupName, Resource.GROUP,
						Right.ADMIN)) {
			int userID = dbUtil.getUserID(username);
			int groupID = dbUtil.getGroupID(groupName);
			String table = userGroupTable;
			String where = database.where(userIDColumn, "" + userID,
					DBConnect.EQ, DBConnect.NUMBER)
					+ " AND "
					+ database.where(groupIDColumn, "" + groupID, DBConnect.EQ,
							DBConnect.NUMBER);
			database.delete(table, where);
			userCache.getCache(CommerceUser.class).updateItem(getUser(userID));
			userCache.getCache(UserGroup.class).updateItem(getGroup(groupName));
		} else
			throw new RuntimeException(LazerwebException.INVALID_PERMISSION);
	}

	/***************************************************************************
	 * Removes a user from the database.
	 * 
	 * @param cu
	 *            Username of User to be removed from database.
	 * @return true if successful, false otherwise.
	 **************************************************************************/
	public boolean deleteUser(CommerceUser user, SecurityModel security) {
		boolean retVal;
		if (security
				.getPermission(user.getUsername(), Rights.USER, Right.ADMIN)) {
			userCache.getCache(CommerceUser.class).deleteObject(user);
			retVal = true;
		} else
			retVal = false;
		return retVal;
	}

	public boolean deleteUser(String username, SecurityModel security) {
		return deleteUser(getUser(username), security);
	}

	public void deleteGroup(String groupName, SecurityModel security) {
		if (security.getPermission(groupName, Resource.GROUP, Right.ADMIN)) {
			UserGroup group = userCache.getCache(UserGroup.class)
					.getCachedObject("name", groupName);
			userCache.getCache(UserGroup.class).deleteObject(group);
			dbUtil.removeResource(groupName, Resource.GROUP);
		} else
			throw new RuntimeException(LazerwebException.INVALID_PERMISSION);
	}

	public void assignFamilyToMerchant(String tableName, String merchantName,
			SecurityModel security) {
		if (security.getPermission(tableName, Resource.DATATABLE, Right.ADMIN)
				&& security.getPermission(merchantName, Resource.MERCHANT,
						Right.ADMIN)) {
			String[] cols = { merchantIDColumn, productTableIDColumn };
			int productTableID = dbUtil.getProductTableID(tableName);
			String merchantID = dbUtil.getMerchant(merchantName)
					.getMerchantID();
			String[] vals = { database.cleanString(merchantID),
					"" + productTableID };
			String where = database.where(productTableIDColumn, productTableID,
					DBConnect.EQ, DBConnect.NUMBER);
			database.delete(merchantProductTable, where);
			database.insert(merchantProductTable, cols, vals);
		} else
			throw new RuntimeException(LazerwebException.INVALID_PERMISSION);
	}

	public void addUserToGroup(String username, String group,
			SecurityModel security) {
		UserGroup grp = getGroup(group);
		CommerceUser user = getUser(username);
		if (!grp.getUsers().contains(user)) {
			if (/* security.getPermission(username,Resource.USER,Rights.ADMIN) && */
			security.getPermission(group, Resource.GROUP, Right.ADMIN)) {
				String[] cols = { userIDColumn, groupIDColumn };
				String[] vals = { "" + user.getUserID(),
						"" + dbUtil.getGroupID(group) };
				database.insert(userGroupTable, cols, vals);
				userCache.getCache(CommerceUser.class).updateItem(user);
				userCache.getCache(UserGroup.class).updateItem(grp);
			}
		}
	}

	public boolean addUser(CommerceUser user, Collection<UserGroup> groups,
			SecurityModel security) {
		boolean retVal = true;
		if (groups == null || user == null)
			retVal = false;
		if (/* security.getPermission(user.getUsername(),Resource.USER,Rights.ADMIN) */true) {
			boolean flag = true;
			for (UserGroup group : groups)
				if (!security.getPermission(group.getName(), Resource.GROUP,
						Right.ADMIN))
					flag = false;
			if (flag) {
				userCache.getCache(CommerceUser.class).addItem(user);
				for (UserGroup group : groups) {
					addUserToGroup(user.getUsername(), group.getName(),
							security);
				}
			} else
				retVal = false;
		}

		return retVal;
	}

	public void emailNewUser(CommerceUser user, HandlerData info) {
		Map<String, Object> values;
		StringWriter msg = new StringWriter();
		values = new HashMap<String, Object>();
		values.put("user", user);
		values.put("url", "http://" + info.getBean("host"));
		List<String> libraries = new LinkedList<String>();
		for (Resource r : getSecurity(user).getAvailableResourceList(
				Resource.DATATABLE)) {
			libraries.add(dbUtil.getProductFamily(r.getName())
					.getDescriptiveName());
		}
		values.put("libraries", libraries);
		getTemplateService().mergeTemplate("add_user.vtl", values, msg);
		notifier.sendMessage(new String[] { user.getEmailAddress() },
				"webmaster@lazerinc.com",
				"Your requested account has been created", "text/plain", msg
						.toString());
	}

	public Collection<UserGroup> getGroups(SecurityModel perms) {
		try {
			Collection<Resource> resources = perms.getAvailableResourceList(
					Resource.GROUP, Right.ADMIN);
			SortedSet<UserGroup> groups = new TreeSet<UserGroup>();
			for (Resource r : resources) {
				UserGroup g = userCache.getCache(UserGroup.class)
						.getCachedObject("name", r.getName());
				if (g != null)
					groups.add(g);
			}
			return groups;
		} catch (NullPointerException e) {
			log.error("Failed to getGroups: ", e);
			throw e;
		}
	}

	public String createRandomPassword() {
		Random r = new Random();
		StringBuffer pass = new StringBuffer();
		for (int i = 0; i < 8; i++) {
			pass.append(digits[r.nextInt(digits.length)]);
		}
		return pass.toString();
	}

	@CoinjemaDependency(type = "dbconnect", method = "dbconnect")
	public void setDatabase(DBConnect database) {
		this.database = database;
	}

	@CoinjemaDependency(type = "dbutil", method = "dbutil")
	public void setDbUtil(DatabaseUtilities dbUtil) {
		this.dbUtil = dbUtil;
	}

	@CoinjemaDynamic(type = "velocityService", method = "emailTemplateService")
	public TemplateService getTemplateService() {
		return null;
	}

	protected ResourceService resService;

	@CoinjemaDependency(type = "resourceService")
	public void setResourceService(ResourceService rs) {
		resService = rs;
	}

	NotificationService notifier;

	@CoinjemaDependency(type = "emailService")
	public void setNotifier(NotificationService s) {
		notifier = s;
	}
}
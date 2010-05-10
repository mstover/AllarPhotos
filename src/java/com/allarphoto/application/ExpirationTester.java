package com.allarphoto.application;

import com.allarphoto.utils.Right;

public interface ExpirationTester {

	/**
	 * Indicate whether the product in question is expired.
	 * 
	 * @param p
	 * @return
	 */
	public boolean isExpired(Product p);

	/**
	 * Indicate whether the product in question will expire at some point in the
	 * future.
	 * 
	 * @param p
	 * @return
	 */
	public boolean willExpire(Product p);

	/**
	 * Determine whether the given permissions object has a given right over the
	 * given product, taking expiration issues into account.
	 * 
	 * @param p
	 * @param right
	 * @param perms
	 * @return
	 */
	public boolean hasPermission(Product p, Right right, SecurityModel perms);

	/**
	 * Determine whether the given permissions object has a given right over the
	 * given product, taking expiration issues into account.
	 * 
	 * @param p
	 * @param right
	 * @param perms
	 * @return
	 */
	public boolean hasPermission(Product p, String right, SecurityModel perms);

	/**
	 * Determine whether the given permissions object as a given right over the
	 * given product from strictly an expiration point of view (ie, rights are
	 * assumed over the datatable in question).
	 * 
	 * @param p
	 * @param right
	 * @param perms
	 * @return
	 */
	public boolean hasExpiredPermission(Product p, Right right,
			SecurityModel perms);

	/**
	 * Determine whether the given permissions object as a given right over the
	 * given product from strictly an expiration point of view (ie, rights are
	 * assumed over the datatable in question).
	 * 
	 * @param p
	 * @param right
	 * @param perms
	 * @return
	 */
	public boolean hasExpiredPermission(Product p, String right,
			SecurityModel perms);

}

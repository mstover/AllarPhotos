package com.lazerinc.application;

import java.util.Collection;

/**
 * @author Administrator To change this generated comment edit the template
 *         variable "typecomment": Window>Preferences>Java>Templates.
 */
public interface LazerwebDataSource {

	/**
	 * Get a list of all the states in the database.
	 */
	public Collection getStates();

	/**
	 * Get a list of all the countries in the database.
	 */
	public Collection getCountries();

	/**
	 * Get a list of all the cities in the database.
	 */
	public Collection getCities();

	/**
	 * Get a list of all the referrers in the database.
	 */
	public Collection getReferrers();

	/**
	 * Get a list of all the industries in the database.
	 */
	public Collection getIndustries();
}

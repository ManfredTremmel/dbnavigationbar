/**
 * This file is part of DBNavigationBar.
 *
 * RiPhone is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * 
 * RiPhone is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RiPhone.  If not, see <http://www.gnu.org/licenses/>
 * 
 * 
 * Copyright (c) 2011 Manfred Tremmel
 *
 * --
 *	Name		Date		Change
 */
package de.knightsoft.DBNavigationBar.client.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * The <code>DomainUser</code> class is a exchange structure
 * between the login and user mask on the client and the servlet
 * on the server, it's abstract and has to be fully implemented
 * in depending classes
 * 
 * @author Manfred Tremmel
 * @version 1.0.0, 2011-02-05
 */
public abstract class DomainUser extends DomainHeadPosDataBase implements Serializable {

	/**
	 * Serial version id
	 */
	private static final long serialVersionUID = -2694311776378786793L;


	private Boolean useLDAP;

	private int mandator;
	private String user;
	private String password;
	private int logonErrorCount;
	private Date logonErrorDate;
	private Date logonDate;

	/**
	 * Constructor, setup a empty entry
	 */
	public DomainUser() {
		super();
	}

	/**
	 * Copy Constructor, creates a new user with the same
	 * entries as the one who's given as parameter
	 * 
	 * @param copyUser entry to copy
	 */
	public DomainUser(DomainUser copyUser) {
		super(copyUser);
		if( copyUser == null ) {
			this.setUpDefaultUser(1, true, "batch", null);
		} else {
			this.useLDAP				=	copyUser.useLDAP;

			this.mandator				=	copyUser.mandator;
			this.user					=	copyUser.user;
			this.password				=	copyUser.password;
			this.logonErrorCount		=	copyUser.logonErrorCount;
			this.logonErrorDate			=	copyUser.logonErrorDate;
			this.logonDate				=	copyUser.logonDate;
		}
	}


	@Override
	public String getKeyNew() {
		return user;
	}

	/**
	 * get useLDAP
	 * 
     * @return useLDAP
	 */
	public Boolean getUseLDAP() {
		return useLDAP;
	}

	/**
	 * set useLDAP
	 * 
     * @param useLDAP
	 */
	public void setUseLDAP(boolean useLDAP) {
		this.useLDAP = useLDAP;
	}

	/**
	 * set useLDAP
	 * 
     * @param useLDAP
	 */
	public void setUseLDAP(Boolean useLDAP) {
		this.useLDAP = useLDAP;
	}

	/**
	 * get mandator
	 * 
     * @return mandator
	 */
	public int getMandator() {
		return mandator;
	}

	/**
	 * set mandator
	 * 
     * @param mandator
	 */
	public void setMandator(int mandator) {
		this.mandator = mandator;
	}

	/**
	 * get user
	 * 
     * @return user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * set user
	 * 
     * @param user
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * get password
	 * 
     * @return password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * set password
	 * 
     * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * get logonErrorCount
	 * 
     * @return logonErrorCount
	 */
	public int getLogonErrorCount() {
		return logonErrorCount;
	}

	/**
	 * set logonErrorCount
	 * 
     * @param logonErrorCount
	 */
	public void setLogonErrorCount(int logonErrorCount) {
		this.logonErrorCount = logonErrorCount;
	}

	/**
	 * get logonErrorDate
	 * 
     * @return logonErrorDate
	 */
	public Date getLogonErrorDate() {
		return logonErrorDate;
	}

	/**
	 * set logonErrorDate
	 * 
     * @param logonErrorDate
	 */
	public void setLogonErrorDate(Date logonErrorDate) {
		this.logonErrorDate = logonErrorDate;
	}

	/**
	 * get logonDate
	 * 
     * @return logonDate
	 */
	public Date getlogon_date() {
		return logonDate;
	}

	/**
	 * set logonDate
	 * 
     * @param logonDate
	 */
	public void setLogonDate(Date logonDate) {
		this.logonDate = logonDate;
	}


	/**
	 * equalsUser compares the user part of two users
	 * 
	 * @param vglUser user to compare with user of this class
	 * @return true if both contain the same entries, otherwise false
	 */
	public boolean equalsUser(DomainUser vglUser) {
		boolean isequal		=	true;
		if( vglUser == null ) {
			isequal			=	false;
		} else {
			isequal	&=	(this.mandator == vglUser.mandator);
			isequal	&=	stringEquals(this.user, vglUser.user);
			isequal	&=	stringEquals(this.password, vglUser.password);
		}

		return isequal;
	}


	@Override
	public boolean equalsEntry(DomainHeadDataBase vglEntry) {
		return this.equalsUser((DomainUser)vglEntry);
	}


	/**
	 * set up a user with default entries
	 * 
	 * @param mandator to set
	 * @param useLDAP use ldap (true/false)
	 * @param user name of the user
	 * @param password of the user
	 */
	public void setUpDefaultUser(int mandator,
			boolean useLDAP,
			String user,
			String password) {
		super.setUpDefaultEntry();

		this.useLDAP			=	useLDAP;

		this.mandator			=	mandator;
		this.user				=	user;
		this.password			=	password;
		this.logonErrorCount	=	0;
		this.logonErrorDate		=	null;
		this.logonDate			=	null;
	}

	/**
	 * set up a initial user
	 * 
	 * @param mandator to set
	 * @param useLDAP use ldap (true/false)
	 * @param user name of the user
	 * @param password of the user
	 */
	public void setUpInitUser(int mandator,
			boolean useLDAP,
			String user,
			String password) {
		this.setUpDefaultUser(
				mandator,
				useLDAP,
				user,
				password);
	}

	/**
	 * set up a default entries
	 * 
	 */
	public void setUpDefaultEntry() {
		setUpDefaultUser(0, false, null,null);
	}

	/**
	 * calculate a hashCode
	 *
	 * @return hash code
	 */
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result
				+ ((user == null) ? 0 : (Integer.toString(mandator) + user).hashCode());
		return result;
	}
}

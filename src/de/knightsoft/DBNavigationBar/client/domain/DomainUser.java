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


	private Boolean UseLDAP;

	private int Mandator;
	private String User;
	private String Password;
	private int logon_error_count;
	private Date logon_error_date;
	private Date logon_date;

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
	 * @param CopyUser entry to copy
	 */
	public DomainUser(DomainUser CopyUser) {
		super(CopyUser);
		if( CopyUser == null ) {
			this.setUpDefaultUser(1, true, "batch", null);
		} else {
			this.UseLDAP				=	CopyUser.UseLDAP;

			this.Mandator				=	CopyUser.Mandator;
			this.User					=	CopyUser.User;
			this.Password				=	CopyUser.Password;
			this.logon_error_count		=	CopyUser.logon_error_count;
			this.logon_error_date		=	CopyUser.logon_error_date;
			this.logon_date				=	CopyUser.logon_date;
		}
	}


	@Override
	public String getKeyNew() {
		return User;
	}

	/**
	 * get UseLDAP
	 * 
     * @return UseLDAP
	 */
	public Boolean getUseLDAP() {
		return UseLDAP;
	}

	/**
	 * set UseLDAP
	 * 
     * @param UseLDAP
	 */
	public void setUseLDAP(boolean UseLDAP) {
		this.UseLDAP = UseLDAP;
	}

	/**
	 * set UseLDAP
	 * 
     * @param UseLDAP
	 */
	public void setUseLDAP(Boolean UseLDAP) {
		this.UseLDAP = UseLDAP;
	}

	/**
	 * get Mandator
	 * 
     * @return Mandator
	 */
	public int getMandator() {
		return Mandator;
	}

	/**
	 * set Mandator
	 * 
     * @param Mandator
	 */
	public void setMandator(int Mandator) {
		this.Mandator = Mandator;
	}

	/**
	 * get User
	 * 
     * @return User
	 */
	public String getUser() {
		return User;
	}

	/**
	 * set User
	 * 
     * @param User
	 */
	public void setUser(String User) {
		this.User = User;
	}

	/**
	 * get Password
	 * 
     * @return Password
	 */
	public String getPassword() {
		return Password;
	}

	/**
	 * set Password
	 * 
     * @param Password
	 */
	public void setPassword(String Password) {
		this.Password = Password;
	}

	/**
	 * get logon_error_count
	 * 
     * @return logon_error_count
	 */
	public int getlogon_error_count() {
		return logon_error_count;
	}

	/**
	 * set logon_error_count
	 * 
     * @param logon_error_count
	 */
	public void setlogon_error_count(int logon_error_count) {
		this.logon_error_count = logon_error_count;
	}

	/**
	 * get logon_error_date
	 * 
     * @return logon_error_date
	 */
	public Date getlogon_error_date() {
		return logon_error_date;
	}

	/**
	 * set logon_error_date
	 * 
     * @param logon_error_date
	 */
	public void setlogon_error_date(Date logon_error_date) {
		this.logon_error_date = logon_error_date;
	}

	/**
	 * get logon_date
	 * 
     * @return logon_date
	 */
	public Date getlogon_date() {
		return logon_date;
	}

	/**
	 * set logon_date
	 * 
     * @param logon_date
	 */
	public void setlogon_date(Date logon_date) {
		this.logon_date = logon_date;
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
			isequal	&=	(this.Mandator == vglUser.Mandator);
			isequal	&=	StringEquals(this.User, vglUser.User);
			isequal	&=	StringEquals(this.Password, vglUser.Password);
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
	 * @param Mandator to set
	 * @param use_ldap use ldap (true/false)
	 * @param User name of the user
	 * @param Password of the user
	 */
	public void setUpDefaultUser(int Mandator,
			boolean use_ldap,
			String User,
			String Password) {
		super.setUpDefaultEntry();

		this.UseLDAP			=	use_ldap;

		this.Mandator			=	Mandator;
		this.User				=	User;
		this.Password			=	Password;
		this.logon_error_count	=	0;;
		this.logon_error_date	=	null;
		this.logon_date			=	null;
	}

	/**
	 * set up a initial user
	 * 
	 * @param Mandator to set
	 * @param use_ldap use ldap (true/false)
	 * @param User name of the user
	 * @param Password of the user
	 */
	public void setUpInitUser(int Mandator,
			boolean use_ldap,
			String User,
			String Password) {
		this.setUpDefaultUser(
				Mandator,
				use_ldap,
				User,
				Password);
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
				+ ((User == null) ? 0 : (Integer.toString(Mandator) + User).hashCode());
		return result;
	}
}

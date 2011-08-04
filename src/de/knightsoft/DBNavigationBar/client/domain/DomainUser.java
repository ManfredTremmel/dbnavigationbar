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
 *    Name        Date        Change
 */
package de.knightsoft.DBNavigationBar.client.domain;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * The <code>DomainUser</code> class is a exchange structure
 * between the login and user mask on the client and the servlet
 * on the server, it's abstract and has to be fully implemented
 * in depending classes.
 *
 * @author Manfred Tremmel
 * @version 1.0.0, 2011-02-05
 */
public abstract class DomainUser extends DomainHeadPosDataBase
       implements Serializable {

    /**
     * Serial version id.
     */
    private static final long serialVersionUID = -2694311776378786793L;


    /**
     * use ldap for authentication.
     */
    private Boolean useLDAP;

    /**
     * mandator.
     */
    private int mandator;

    /**
     * user name.
     */
    private String user;

    /**
     * password.
     */
    private String password;

    /**
     * number wrong input tries.
     */
    private int logonErrorCount;

    /**
     * date of the wrong login try.
     */
    private Date logonErrorDate;

    /**
     * date of the last successfully login.
     */
    private Date logonDate;

    /**
     * Constructor, setup a empty entry.
     */
    public DomainUser() {
        super();
    }

    /**
     * Copy Constructor, creates a new user with the same
     * entries as the one who's given as parameter.
     *
     * @param copyUser entry to copy
     */
    public DomainUser(final DomainUser copyUser) {
        super(copyUser);
        if (copyUser == null) {
            this.setUpDefaultUser(1, true, "batch", null);
        } else {
            this.useLDAP             = copyUser.useLDAP;

            this.mandator            = copyUser.mandator;
            this.user                = copyUser.user;
            this.password            = copyUser.password;
            this.logonErrorCount     = copyUser.logonErrorCount;
            this.logonErrorDate      = copyUser.logonErrorDate;
            this.logonDate           = copyUser.logonDate;
        }
    }


    @Override
    public final String getKeyNew() {
        return this.user;
    }

    /**
     * get useLDAP.
     *
     * @return useLDAP
     */
    public final Boolean getUseLDAP() {
        return this.useLDAP;
    }

    /**
     * set useLDAP.
     *
     * @param newUseLDAP use ldap true/false
     */
    public final void setUseLDAP(final boolean newUseLDAP) {
        this.useLDAP = newUseLDAP;
    }

    /**
     * set useLDAP.
     *
     * @param newUseLDAP use ldap true/false
     */
    public final void setUseLDAP(final Boolean newUseLDAP) {
        this.useLDAP = newUseLDAP;
    }

    /**
     * get mandator.
     *
     * @return mandator
     */
    public final int getMandator() {
        return this.mandator;
    }

    /**
     * set mandator.
     *
     * @param newMandator to set
     */
    public final void setMandator(final int newMandator) {
        this.mandator = newMandator;
    }

    /**
     * get user.
     *
     * @return user
     */
    public final String getUser() {
        return this.user;
    }

    /**
     * set user.
     *
     * @param newUser to set
     */
    public final void setUser(final String newUser) {
        this.user = newUser;
    }

    /**
     * get password.
     *
     * @return password
     */
    public final String getPassword() {
        return this.password;
    }

    /**
     * set password.
     *
     * @param newPassword to set
     */
    public final void setPassword(final String newPassword) {
        this.password = newPassword;
    }

    /**
     * get logonErrorCount.
     *
     * @return logonErrorCount
     */
    public final int getLogonErrorCount() {
        return this.logonErrorCount;
    }

    /**
     * set logonErrorCount.
     *
     * @param newLogonErrorCount to set
     */
    public final void setLogonErrorCount(final int newLogonErrorCount) {
        this.logonErrorCount = newLogonErrorCount;
    }

    /**
     * get logonErrorDate.
     *
     * @return logonErrorDate
     */
    public final Date getLogonErrorDate() {
        return this.logonErrorDate;
    }

    /**
     * set logonErrorDate.
     *
     * @param newLogonErrorDate to set
     */
    public final void setLogonErrorDate(final Date newLogonErrorDate) {
        this.logonErrorDate = newLogonErrorDate;
    }

    /**
     * get logonDate.
     *
     * @return logonDate
     */
    public final Date getlogonDate() {
        return this.logonDate;
    }

    /**
     * set logonDate.
     *
     * @param newLogonDate to set
     */
    public final void setLogonDate(final Date newLogonDate) {
        this.logonDate = newLogonDate;
    }


    /**
     * equalsHeadUser compares the user part of two users.
     *
     * @param vglUser user to compare with user of this class
     * @return true if both contain the same entries, otherwise false
     */
    public final boolean equalsHeadUser(final DomainUser vglUser) {
        boolean isequal        =    true;
        if (vglUser == null) {
            isequal            =    false;
        } else {
            isequal    &=    (this.mandator == vglUser.mandator);
            isequal    &=    stringEquals(this.user, vglUser.user);
            isequal    &=    stringEquals(this.password, vglUser.password);
        }

        return isequal;
    }

    /**
     * equalsUser compares the user part of two users.
     *
     * @param vglUser user to compare with user of this class
     * @return true if both contain the same entries, otherwise false
     */
    public abstract boolean equalsUser(final DomainUser vglUser);


    @Override
    public final boolean equalsEntry(final DomainDataBaseInterface vglEntry) {
        return this.equalsUser((DomainUser) vglEntry);
    }


    /**
     * set up a user with default entries.
     *
     * @param defaultMandator to set
     * @param defaultUseLDAP use ldap (true/false)
     * @param defaultUser name of the user
     * @param defaultPassword of the user
     */
    public final void setUpHeadDefaultUser(
            final int defaultMandator,
            final boolean defaultUseLDAP,
            final String defaultUser,
            final String defaultPassword) {
        super.setUpDefaultEntryKey();

        this.useLDAP         =    defaultUseLDAP;

        this.mandator        =    defaultMandator;
        this.user            =    defaultUser;
        this.password        =    defaultPassword;
        this.logonErrorCount =    0;
        this.logonErrorDate  =    null;
        this.logonDate       =    null;
    }

    /**
     * set up a default user.
     *
     * @param initMandator to set
     * @param initUseLDAP use ldap (true/false)
     * @param initUser name of the user
     * @param initPassword of the user
     */
    public abstract void setUpDefaultUser(
            final int initMandator,
            final boolean initUseLDAP,
            final String initUser,
            final String initPassword);

    /**
     * set up a initial user.
     *
     * @param initMandator to set
     * @param initUseLDAP use ldap (true/false)
     * @param initUser name of the user
     * @param initPassword of the user
     */
    public abstract void setUpInitUser(
            final int initMandator,
            final boolean initUseLDAP,
            final String initUser,
            final String initPassword);

    /**
     * set up a default entries.
     *
     */
    @Override
    public final void setUpDefaultEntry() {
        setUpDefaultUser(0, false, null, null);
    }
}

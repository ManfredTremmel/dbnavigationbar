/**
 * This file is part of DBNavigation.
 *
 * RiPhone is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * RiPhone is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with RiPhone. If not, see <a
 * href="http://www.gnu.org/licenses>http://www.gnu.org/licenses</a>
 *
 *
 * Copyright (c) 2011-2015 Manfred Tremmel
 *
 */

package de.knightsoft.dbnavigationbar.client.domain;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * The <code>AbstractDomainUser</code> class is a exchange structure between the login and user mask on the client and the
 * servlet on the server, it's abstract and has to be fully implemented in depending classes.
 *
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public abstract class AbstractDomainUser extends AbstractDomainHeadPosDB implements Serializable {

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
  public AbstractDomainUser() {
    super();
  }

  /**
   * Copy Constructor, creates a new user with the same entries as the one who's given as parameter.
   *
   * @param pUser entry to copy
   */
  public AbstractDomainUser(final AbstractDomainUser pUser) {
    super(pUser);
    if (pUser == null) {
      this.setUpDefaultUser(1, true, "batch", null);
    } else {
      this.useLDAP = pUser.useLDAP;

      this.mandator = pUser.mandator;
      this.user = pUser.user;
      this.password = pUser.password;
      this.logonErrorCount = pUser.logonErrorCount;
      this.logonErrorDate = pUser.logonErrorDate;
      this.logonDate = pUser.logonDate;
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
   * @param pUseLDAP use ldap true/false
   */
  public final void setUseLDAP(final boolean pUseLDAP) {
    this.useLDAP = pUseLDAP;
  }

  /**
   * set useLDAP.
   *
   * @param pUseLDAP use ldap true/false
   */
  public final void setUseLDAP(final Boolean pUseLDAP) {
    this.useLDAP = pUseLDAP;
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
   * @param pMandator to set
   */
  public final void setMandator(final int pMandator) {
    this.mandator = pMandator;
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
   * @param pUser to set
   */
  public final void setUser(final String pUser) {
    this.user = pUser;
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
   * @param pPassword to set
   */
  public final void setPassword(final String pPassword) {
    this.password = pPassword;
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
   * @param pLogonErrorCount to set
   */
  public final void setLogonErrorCount(final int pLogonErrorCount) {
    this.logonErrorCount = pLogonErrorCount;
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
   * @param pLogonErrorDate to set
   */
  public final void setLogonErrorDate(final Date pLogonErrorDate) {
    this.logonErrorDate = pLogonErrorDate;
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
   * @param pLogonDate to set
   */
  public final void setLogonDate(final Date pLogonDate) {
    this.logonDate = pLogonDate;
  }

  /**
   * equalsHeadUser compares the user part of two users.
   *
   * @param pCompareUser user to compare with user of this class
   * @return true if both contain the same entries, otherwise false
   */
  public final boolean equalsHeadUser(final AbstractDomainUser pCompareUser) {
    boolean isequal = true;
    if (pCompareUser == null) {
      isequal = false;
    } else {
      isequal &= this.mandator == pCompareUser.mandator;
      isequal &= StringUtils.equals(this.user, pCompareUser.user);
      isequal &= StringUtils.equals(this.password, pCompareUser.password);
    }

    return isequal;
  }

  /**
   * equalsUser compares the user part of two users.
   *
   * @param pCompareUser user to compare with user of this class
   * @return true if both contain the same entries, otherwise false
   */
  public abstract boolean equalsUser(final AbstractDomainUser pCompareUser);

  @Override
  public final boolean equalsEntry(final DomainDataBaseInterface pCompareEntry) {
    return this.equalsUser((AbstractDomainUser) pCompareEntry);
  }

  /**
   * set up a user with default entries.
   *
   * @param pMandator to set
   * @param pUseLDAP use ldap (true/false)
   * @param pUser name of the user
   * @param pPassword of the user
   */
  public final void setUpHeadDefaultUser(final int pMandator, final boolean pUseLDAP, final String pUser, //
      final String pPassword) {
    super.setUpDefaultEntryKey();

    this.useLDAP = pUseLDAP;

    this.mandator = pMandator;
    this.user = pUser;
    this.password = pPassword;
    this.logonErrorCount = 0;
    this.logonErrorDate = null;
    this.logonDate = null;
  }

  /**
   * set up a default user.
   *
   * @param pMandator to set
   * @param pUseLDAP use ldap (true/false)
   * @param pUser name of the user
   * @param pPassword of the user
   */
  public abstract void setUpDefaultUser(final int pMandator, final boolean pUseLDAP, final String pUser, //
      final String pPassword);

  /**
   * set up a initial user.
   *
   * @param pMandator to set
   * @param pUseLDAP use ldap (true/false)
   * @param pUser name of the user
   * @param pPassword of the user
   */
  public abstract void setUpInitUser(final int pMandator, final boolean pUseLDAP, final String pUser, final String pPassword);

  /**
   * set up a default entries.
   *
   */
  @Override
  public final void setUpDefaultEntry() {
    this.setUpDefaultUser(0, false, null, null);
  }
}

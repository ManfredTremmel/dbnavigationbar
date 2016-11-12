/**
 * This file is part of DBNavigationBar.
 *
 * DBNavigationBar is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * DBNavigationBar is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with DBNavigationBar. If
 * not, see <a href="http://www.gnu.org/licenses>http://www.gnu.org/licenses</a>
 *
 *
 * Copyright (c) 2012-2015 Manfred Tremmel
 *
 */

package de.knightsoft.dbnavigationbar.shared.exceptions;

import java.io.Serializable;

/**
 *
 * The <code>EntryIsLockedException</code> is thrown, when a entry which should be updated or
 * deleted.
 *
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public class EntryIsLockedException extends ServerErrorException implements Serializable {
  /**
   * serial version uid.
   */
  private static final long serialVersionUID = 8124314407802142028L;

  /**
   * key that should be changed or deleted.
   */
  private String key;

  /**
   * user that has locked the entry.
   */
  private String user;

  /**
   * default constructor.
   */
  public EntryIsLockedException() {
    super();
  }

  /**
   * constructor with initial data.
   *
   * @param pKey initial key to set
   * @param pUser initial user set
   */
  public EntryIsLockedException(final String pKey, final String pUser) {
    super();
    this.key = pKey;
    this.user = pUser;
  }

  public final String getKey() {
    return this.key;
  }

  public final void setKey(final String pKey) {
    this.key = pKey;
  }

  public final String getUser() {
    return this.user;
  }

  public final void setUser(final String pUser) {
    this.user = pUser;
  }
}

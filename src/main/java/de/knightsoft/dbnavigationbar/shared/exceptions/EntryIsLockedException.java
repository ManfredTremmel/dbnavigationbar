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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with RiPhone. If not, see <http://www.gnu.org/licenses/>
 * 
 * 
 * Copyright (c) 2012 Manfred Tremmel
 * 
 */
package de.knightsoft.dbnavigationbar.shared.exceptions;

import java.io.Serializable;

/**
 * 
 * The <code>EntryIsLockedException</code> is thrown, when a entry which should
 * be updated or deleted.
 * 
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public class EntryIsLockedException extends ServerErrorException
    implements Serializable
{
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
  public EntryIsLockedException()
  {
    super();
  }

  /**
   * constructor with initial data.
   * 
   * @param keyInit
   *        initial key to set
   * @param userInit
   *        initial user set
   */
  public EntryIsLockedException(
      final String keyInit,
      final String userInit)
  {
    super();
    this.key = keyInit;
    this.user = userInit;
  }

  /**
   * @return the key
   */
  public final String getKey()
  {
    return this.key;
  }

  /**
   * @param keySet
   *        the key to set
   */
  public final void setKey(final String keySet)
  {
    this.key = keySet;
  }

  /**
   * @return the user
   */
  public final String getUser()
  {
    return this.user;
  }

  /**
   * @param userSet
   *        the user to set
   */
  public final void setUser(final String userSet)
  {
    this.user = userSet;
  }
}

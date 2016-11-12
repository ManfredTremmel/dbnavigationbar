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
 * The <code>EntryNotFoundException</code> is thrown, when a entry which should be read from
 * database could not be found.
 *
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public class EntryNotFoundException extends ServerErrorException implements Serializable {
  /**
   * serial version uid.
   */
  private static final long serialVersionUID = -6326451380001601358L;

  /**
   *
   * The <code>ReadTyp</code> defines which way the read was done.
   *
   * @author Manfred Tremmel
   * @version $Rev$, $Date$
   */
  public enum ReadTyp {
    /**
     * read the exact key.
     */
    READ_EQUAL,
    /**
     * read the next key.
     */
    READ_NEXT,
    /**
     * read the previous key.
     */
    READ_PREVIOUS,
    /**
     * search was not successfully.
     */
    SEARCH,
    /**
     * search from given key on was not successfully.
     */
    SEARCH_NEXT,
    /**
     * search before given key on was not successfully.
     */
    SEARCH_PREVIOUS
  }

  /**
   * key that could not be found or was base for search.
   */
  private String key;

  /**
   * defines which way the read was done.
   */
  private ReadTyp readTyp;

  /**
   * default constructor.
   */
  public EntryNotFoundException() {
    super();
  }

  /**
   * constructor with initial data.
   *
   * @param pKey initial key to set
   * @param pReadTyp initial read type to set
   */
  public EntryNotFoundException(final String pKey, final ReadTyp pReadTyp) {
    super();
    this.key = pKey;
    this.readTyp = pReadTyp;
  }

  public final String getKey() {
    return this.key;
  }

  public final void setKey(final String pKey) {
    this.key = pKey;
  }

  public final ReadTyp getReadTyp() {
    return this.readTyp;
  }

  public final void setReadTyp(final ReadTyp pReadTyp) {
    this.readTyp = pReadTyp;
  }
}

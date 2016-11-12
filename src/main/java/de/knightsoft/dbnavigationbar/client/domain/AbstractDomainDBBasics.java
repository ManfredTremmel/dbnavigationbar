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
 * Copyright (c) 2011-2015 Manfred Tremmel
 *
 */

package de.knightsoft.dbnavigationbar.client.domain;

import java.io.Serializable;

/**
 *
 * The <code>AbstractDomainDBBasics</code> class is a exchange structure between the client and the
 * servlet on the server with database functions. This is a abstract basic class to setup on.
 *
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public abstract class AbstractDomainDBBasics implements DomainDataBaseInterface, Serializable {

  /**
   * Serial version id.
   */
  private static final long serialVersionUID = 6396714435198200480L;

  /**
   * readOnly, true if only read is allowed, false, if it's writable.
   */
  private Boolean readOnly;

  /**
   * keyMin, the lowest key in the database.
   */
  private String keyMin;

  /**
   * keyMax, the highest key in the database.
   */
  private String keyMax;

  /**
   * keyCur, the key of the current entry, in new entries it's null.
   */
  private String keyCur;

  /**
   * Constructor, setup a empty entry.
   */
  public AbstractDomainDBBasics() {
    this.setUpDefaultEntry();
    this.keyMin = null;
    this.keyMax = null;
  }

  /**
   * Copy Constructor, creates a new user with the same entries as the one who's given as parameter.
   *
   * @param pEntry entry to copy
   */
  public AbstractDomainDBBasics(final AbstractDomainDBBasics pEntry) {
    if (pEntry == null) {
      this.setUpDefaultEntry();
      this.keyMin = null;
      this.keyMax = null;
    } else {
      this.readOnly = pEntry.readOnly;
      this.keyMin = pEntry.keyMin;
      this.keyMax = pEntry.keyMax;
      this.keyCur = pEntry.keyCur;
    }
  }

  /**
   * get readOnly.
   *
   * @return readOnly
   */
  @Override
  public final boolean isReadOnly() {
    return this.readOnly;
  }

  /**
   * set readOnly.
   *
   * @param pReadOnly new readOnly
   */
  @Override
  public final void setIsReadOnly(final boolean pReadOnly) {
    this.readOnly = pReadOnly;
  }

  /**
   * set readOnly.
   *
   * @param pReadOnly new readOnly
   */
  @Override
  public final void setIsReadOnly(final Boolean pReadOnly) {
    this.readOnly = pReadOnly;
  }

  /**
   * get keyMin.
   *
   * @return keyMin
   */
  @Override
  public final String getKeyMin() {
    return this.keyMin;
  }

  /**
   * set keyMin.
   *
   * @param pKeyMin new keyMin
   */
  @Override
  public final void setKeyMin(final String pKeyMin) {
    this.keyMin = pKeyMin;
  }

  /**
   * get keyMax.
   *
   * @return keyMax
   */
  @Override
  public final String getKeyMax() {
    return this.keyMax;
  }

  /**
   * set keyMax.
   *
   * @param pKeyMax new keyMax
   */
  @Override
  public final void setKeyMax(final String pKeyMax) {
    this.keyMax = pKeyMax;
  }

  /**
   * get keyCur.
   *
   * @return keyCur
   */
  @Override
  public final String getKeyCur() {
    return this.keyCur;
  }

  /**
   * set keyCur.
   *
   * @param pKeyCur key to set
   */
  @Override
  public final void setKeyCur(final String pKeyCur) {
    this.keyCur = pKeyCur;
  }

  /**
   * set up a default key and read only entries.
   *
   */
  public final void setUpDefaultEntryKey() {
    this.readOnly = false;
    this.keyCur = null;
  }
}

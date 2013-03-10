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
 * Copyright (c) 2011-2012 Manfred Tremmel
 * 
 */
package de.knightsoft.dbnavigationbar.client.domain;

/**
 * 
 * The <code>DomainDataBaseInterface</code> interface defines the base
 * functions for a data base domain.
 * 
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public interface DomainDataBaseInterface
{
  /**
   * get isReadOnly.
   * 
   * @return isReadOnly
   */
  boolean isReadOnly();

  /**
   * set isReadOnly.
   * 
   * @param newIsReadOnly
   *        new isReadOnly
   */
  void setIsReadOnly(final boolean newIsReadOnly);

  /**
   * set isReadOnly.
   * 
   * @param newIsReadOnly
   *        new isReadOnly
   */
  void setIsReadOnly(final Boolean newIsReadOnly);

  /**
   * get keyMin.
   * 
   * @return keyMin
   */
  String getKeyMin();

  /**
   * set keyMin.
   * 
   * @param newKeyMin
   *        new keyMin
   */
  void setKeyMin(final String newKeyMin);

  /**
   * get keyMax.
   * 
   * @return keyMax
   */
  String getKeyMax();

  /**
   * set keyMax.
   * 
   * @param newKeyMax
   *        new keyMax
   */
  void setKeyMax(final String newKeyMax);

  /**
   * get keyCur.
   * 
   * @return keyCur
   */
  String getKeyCur();

  /**
   * set keyCur.
   * 
   * @param newKeyCur
   *        key to set
   */
  void setKeyCur(final String newKeyCur);

  /**
   * get KeyNew.
   * 
   * @return Key of new Entry
   */
  String getKeyNew();

  /**
   * equals compares two entries.
   * 
   * @param obj
   *        entry to compare with entry of this class
   * @return true if both contain the same entries, otherwise false
   */
  boolean equals(final Object obj);

  /**
   * hashCode implementation.
   * 
   * @return hash code of the key
   */
  int hashCode();

  /**
   * set up a default entries.
   * 
   */
  void setUpDefaultEntry();

  /**
   * equalsEntry compares the head part of two entries.
   * 
   * @param vglEntry
   *        entry to compare with entry of this class
   * @return true if both contain the same entries, otherwise false
   */
  boolean equalsEntry(final DomainDataBaseInterface vglEntry);
}

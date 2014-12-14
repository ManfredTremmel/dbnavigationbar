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
package de.knightsoft.dbnavigationbar.client.domain;

import java.util.HashMap;

import de.knightsoft.dbnavigationbar.shared.fields.FieldInterface;

/**
 *
 * The <code>InterfaceDataBase</code> interface defines the base
 * functions for a data base domain.
 *
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public interface InterfaceDataBase
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
   * @param pIsReadOnly
   *        new isReadOnly
   */
  void setIsReadOnly(final boolean pIsReadOnly);

  /**
   * set isReadOnly.
   *
   * @param pIsReadOnly
   *        new isReadOnly
   */
  void setIsReadOnly(final Boolean pIsReadOnly);

  /**
   * get keyMin.
   *
   * @return keyMin
   */
  String getKeyMin();

  /**
   * set keyMin.
   *
   * @param pKeyMin
   *        new keyMin
   */
  void setKeyMin(final String pKeyMin);

  /**
   * get keyMax.
   *
   * @return keyMax
   */
  String getKeyMax();

  /**
   * set keyMax.
   *
   * @param pKeyMax
   *        new keyMax
   */
  void setKeyMax(final String pKeyMax);

  /**
   * get keyCur.
   *
   * @return keyCur
   */
  String getKeyCur();

  /**
   * set keyCur.
   *
   * @param pKeyCur
   *        key to set
   */
  void setKeyCur(final String pKeyCur);

  /**
   * get KeyNew.
   *
   * @return Key of new Entry
   */
  String getKeyNew();

  /**
   * set new field map.
   *
   * @param pFieldMap
   *        FieldMap to set
   */
  void setFildMap(final HashMap<String, FieldInterface<?>> pFieldMap);

  /**
   * get field map.
   *
   * @return FieldMap
   */
  HashMap<String, FieldInterface<?>> getFieldMap();

  /**
   * add or update a entry in field map.
   *
   * @param pFieldName
   *        name of the field
   * @param pEntry
   *        entry to add or update
   */
  void addUpdateFieldEntry(final String pFieldName, final FieldInterface<?> pEntry);

  /**
   * get field entry.
   *
   * @param pFieldName
   *        name of the field to read
   * @return field
   */
  FieldInterface<?> getFieldEntry(final String pFieldName);

  /**
   * test the fields.
   *
   * @return true if all fields are ok
   */
  boolean isFieldSetOkSimple();

  /**
   * test the fields.
   *
   * @return true if all fields are ok
   */
  boolean isFieldSetOk();

  /**
   * equals compares two entries.
   *
   * @param pObj
   *        entry to compare with entry of this class
   * @return true if both contain the same entries, otherwise false
   */
  @Override
  boolean equals(final Object pObj);

  /**
   * hashCode implementation.
   *
   * @return hash code of the key
   */
  @Override
  int hashCode();

  /**
   * set up a default entries.
   *
   */
  void setUpDefaultEntry();

  /**
   * equalsEntry compares the head part of two entries.
   *
   * @param pCompareEntry
   *        entry to compare with entry of this class
   * @return true if both contain the same entries, otherwise false
   */
  boolean equalsEntry(final InterfaceDataBase pCompareEntry);
}

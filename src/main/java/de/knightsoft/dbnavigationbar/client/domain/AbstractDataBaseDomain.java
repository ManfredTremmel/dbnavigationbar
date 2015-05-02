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
 * Copyright (c) 2012-2015 Manfred Tremmel
 *
 */

package de.knightsoft.dbnavigationbar.client.domain;

import de.knightsoft.dbnavigationbar.shared.fields.FieldInterface;

import java.util.HashMap;
import java.util.Objects;

/**
 *
 * The <code>AbstractDataBaseDomain</code> is a abstract implementation of data base domain.
 *
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public abstract class AbstractDataBaseDomain implements InterfaceDataBase {

  /**
   * read only entry.
   */
  private boolean readOnly;

  /**
   * lowest key in database.
   */
  private String keyMin;

  /**
   * highest key in database.
   */
  private String keyMax;

  /**
   * current key of the entry.
   */
  private String keyCur;

  /**
   * map of fields.
   */
  private HashMap<String, FieldInterface<?>> fieldMap;

  /**
   * default constructor.
   */
  public AbstractDataBaseDomain() {
    super();
    this.readOnly = false;
    this.keyMin = null;
    this.keyMax = null;
    this.keyCur = null;
    this.fieldMap = new HashMap<String, FieldInterface<?>>();
  }

  @Override
  public final boolean isReadOnly() {
    return this.readOnly;
  }

  @Override
  public final void setIsReadOnly(final boolean pIsReadOnly) {
    this.readOnly = pIsReadOnly;
  }

  @Override
  public final void setIsReadOnly(final Boolean pReadOnly) {
    if (pReadOnly == null) {
      this.readOnly = false;
    } else {
      this.readOnly = pReadOnly.booleanValue();
    }
  }

  @Override
  public final String getKeyMin() {
    return this.keyMin;
  }

  @Override
  public final void setKeyMin(final String pKeyMin) {
    this.keyMin = pKeyMin;
  }

  @Override
  public final String getKeyMax() {
    return this.keyMax;
  }

  @Override
  public final void setKeyMax(final String pKeyMax) {
    this.keyMax = pKeyMax;
  }

  @Override
  public final String getKeyCur() {
    return this.keyCur;
  }

  @Override
  public final void setKeyCur(final String pKeyCur) {
    this.keyCur = pKeyCur;
  }

  @Override
  public final String getKeyNew() {
    if (this.keyCur == null) {
      return null;
    } else {
      return this.keyCur;
    }
  }

  @Override
  public final void setFildMap(final HashMap<String, FieldInterface<?>> pFieldMap) {
    this.fieldMap = pFieldMap;
  }

  @Override
  public final HashMap<String, FieldInterface<?>> getFieldMap() {
    return this.fieldMap;
  }

  @Override
  public final void addUpdateFieldEntry(final String pFieldName, final FieldInterface<?> pEntry) {
    this.fieldMap.put(pFieldName, pEntry);
  }

  @Override
  public final FieldInterface<?> getFieldEntry(final String pFieldName) {
    return this.fieldMap.get(pFieldName);
  }

  @Override
  public final boolean isFieldSetOkSimple() {
    boolean ok = true;
    for (final FieldInterface<?> testEntry : this.fieldMap.values()) {
      if (!testEntry.isOK()) {
        ok = false;
        break;
      }
    }
    return ok;
  }

  @Override
  public final void setUpDefaultEntry() {
    for (final FieldInterface<?> entry : this.fieldMap.values()) {
      entry.setValueObject(entry.getDefaultValue());
    }
  }

  @Override
  public final boolean equalsEntry(final InterfaceDataBase pCompareEntry) {
    boolean entriesAreEqual =
        (this.readOnly == pCompareEntry.isReadOnly()) && Objects.equals(this.keyCur, pCompareEntry.getKeyCur())
            && (this.fieldMap.size() == pCompareEntry.getFieldMap().size());
    if (entriesAreEqual) {
      for (final String key : this.fieldMap.keySet()) {
        entriesAreEqual &= Objects.equals(this.getFieldEntry(key), pCompareEntry.getFieldEntry(key));
      }
    }
    return entriesAreEqual;
  }
}

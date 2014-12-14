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
import java.util.Objects;

import de.knightsoft.dbnavigationbar.shared.fields.FieldInterface;

/**
 *
 * The <code>AbstractDataBaseDomain</code> is a abstract implementation
 * of data base domain.
 *
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public abstract class AbstractDataBaseDomain implements InterfaceDataBase
{

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
  public AbstractDataBaseDomain()
  {
    super();
    this.readOnly = false;
    this.keyMin = null;
    this.keyMax = null;
    this.keyCur = null;
    this.fieldMap = new HashMap<String, FieldInterface<?>>();
  }

  /*
   * (non-Javadoc)
   *
   * @see de.knightsoft.dbnavigationbar.client.domain
   * .InterfaceDataBase#getIsReadOnly()
   */
  @Override
  public final boolean isReadOnly()
  {
    return this.readOnly;
  }

  /*
   * (non-Javadoc)
   *
   * @see de.knightsoft.dbnavigationbar.client.domain
   * .InterfaceDataBase#setIsReadOnly(boolean)
   */
  @Override
  public final void setIsReadOnly(final boolean pIsReadOnly)
  {
    this.readOnly = pIsReadOnly;
  }

  /*
   * (non-Javadoc)
   *
   * @see de.knightsoft.dbnavigationbar.client.domain
   * .InterfaceDataBase#setIsReadOnly(java.lang.Boolean)
   */
  @Override
  public final void setIsReadOnly(final Boolean pIsReadOnly)
  {
    if (pIsReadOnly == null)
    {
      this.readOnly = false;
    }
    else
    {
      this.readOnly = pIsReadOnly.booleanValue();
    }
  }

  /*
   * (non-Javadoc)
   *
   * @see de.knightsoft.dbnavigationbar.client.domain
   * .InterfaceDataBase#getKeyMin()
   */
  @Override
  public final String getKeyMin()
  {
    return this.keyMin;
  }

  /*
   * (non-Javadoc)
   *
   * @see de.knightsoft.dbnavigationbar.client.domain
   * .InterfaceDataBase#setKeyMin(de.knightsoft.dbnavigationbar.shared
   * .fields.FieldInterface)
   */
  @Override
  public final void setKeyMin(final String pKeyMin)
  {
    this.keyMin = pKeyMin;
  }

  /*
   * (non-Javadoc)
   *
   * @see de.knightsoft.dbnavigationbar.client.domain
   * .InterfaceDataBase#getKeyMax()
   */
  @Override
  public final String getKeyMax()
  {
    return this.keyMax;
  }

  /*
   * (non-Javadoc)
   *
   * @see de.knightsoft.dbnavigationbar.client.domain
   * .InterfaceDataBase#setKeyMax(de.knightsoft.dbnavigationbar.shared
   * .fields.FieldInterface)
   */
  @Override
  public final void setKeyMax(final String pKeyMax)
  {
    this.keyMax = pKeyMax;
  }

  /*
   * (non-Javadoc)
   *
   * @see de.knightsoft.dbnavigationbar.client.domain
   * .InterfaceDataBase#getKeyCur()
   */
  @Override
  public final String getKeyCur()
  {
    return this.keyCur;
  }

  /*
   * (non-Javadoc)
   *
   * @see de.knightsoft.dbnavigationbar.client.domain
   * .InterfaceDataBase#setKeyCur(de.knightsoft.dbnavigationbar.shared
   * .fields.FieldInterface)
   */
  @Override
  public final void setKeyCur(final String pKeyCur)
  {
    this.keyCur = pKeyCur;
  }

  /*
   * (non-Javadoc)
   *
   * @see de.knightsoft.dbnavigationbar.client.domain
   * .InterfaceDataBase#getKeyNew()
   */
  @Override
  public final String getKeyNew()
  {
    if (this.keyCur == null)
    {
      return null;
    }
    else
    {
      return this.keyCur;
    }
  }

  /*
   * (non-Javadoc)
   *
   * @see de.knightsoft.dbnavigationbar.client.domain
   * .InterfaceDataBase#setFildMap(java.util.HashMap)
   */
  @Override
  public final void setFildMap(final HashMap<String, FieldInterface<?>> pFieldMap)
  {
    this.fieldMap = pFieldMap;
  }

  /*
   * (non-Javadoc)
   *
   * @see de.knightsoft.dbnavigationbar.client.domain
   * .InterfaceDataBase#getFieldMap()
   */
  @Override
  public final HashMap<String, FieldInterface<?>> getFieldMap()
  {
    return this.fieldMap;
  }

  /*
   * (non-Javadoc)
   *
   * @see de.knightsoft.dbnavigationbar.client.domain
   * .InterfaceDataBase#addUpdateFieldEntry(java.lang.String,
   * de.knightsoft.dbnavigationbar.shared.fields.FieldInterface)
   */
  @Override
  public final void addUpdateFieldEntry(final String pFieldName,
      final FieldInterface<?> pEntry)
  {
    this.fieldMap.put(pFieldName, pEntry);
  }

  /*
   * (non-Javadoc)
   *
   * @see de.knightsoft.dbnavigationbar.client.domain
   * .InterfaceDataBase#getFieldEntry(java.lang.String)
   */
  @Override
  public final FieldInterface<?> getFieldEntry(final String pFieldName)
  {
    return this.fieldMap.get(pFieldName);
  }

  /*
   * (non-Javadoc)
   *
   * @see de.knightsoft.dbnavigationbar.client.domain
   * .InterfaceDataBase#isFieldSetOkSimple()
   */
  @Override
  public final boolean isFieldSetOkSimple()
  {
    boolean ok = true;
    for (final FieldInterface<?> testEntry : this.fieldMap.values())
    {
      if (!testEntry.isOK())
      {
        ok = false;
        break;
      }
    }
    return ok;
  }

  /*
   * (non-Javadoc)
   *
   * @see de.knightsoft.dbnavigationbar.client.domain
   * .InterfaceDataBase#setUpDefaultEntry()
   */
  @Override
  public final void setUpDefaultEntry()
  {
    for (final FieldInterface<?> entry : this.fieldMap.values())
    {
      entry.setValueObject(entry.getDefaultValue());
    }
  }

  /*
   * (non-Javadoc)
   *
   * @see de.knightsoft.dbnavigationbar.client.domain
   * .InterfaceDataBase#equalsEntry(de.knightsoft.dbnavigationbar.client
   * .domain.InterfaceDataBase)
   */
  @Override
  public final boolean equalsEntry(final InterfaceDataBase vglEntry)
  {
    boolean entriesAreEqual = (this.readOnly == vglEntry.isReadOnly())
        && Objects.equals(this.keyCur, vglEntry.getKeyCur())
        && (this.fieldMap.size() == vglEntry.getFieldMap().size());
    if (entriesAreEqual)
    {
      for (final String key : this.fieldMap.keySet())
      {
        entriesAreEqual &= Objects.equals(this.getFieldEntry(key), vglEntry.getFieldEntry(key));
      }
    }
    return entriesAreEqual;
  }
}

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
 * The <code>AbstractDataBaseDomain</code> is a abstract implementation
 * of data base domain.
 * 
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public abstract class AbstractDataBaseDomain
    implements InterfaceDataBase
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
  public final void setIsReadOnly(final boolean newIsReadOnly)
  {
    this.readOnly = newIsReadOnly;
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.knightsoft.dbnavigationbar.client.domain
   * .InterfaceDataBase#setIsReadOnly(java.lang.Boolean)
   */
  @Override
  public final void setIsReadOnly(final Boolean newIsReadOnly)
  {
    if (newIsReadOnly == null)
    {
      this.readOnly = false;
    }
    else
    {
      this.readOnly = newIsReadOnly.booleanValue();
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
  public final void setKeyMin(final String newKeyMin)
  {
    this.keyMin = newKeyMin;
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
  public final void setKeyMax(final String newKeyMax)
  {
    this.keyMax = newKeyMax;
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
  public final void setKeyCur(final String newKeyCur)
  {
    this.keyCur = newKeyCur;
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
  public final void setFildMap(
      final HashMap<String, FieldInterface<?>> newFieldMap)
  {
    this.fieldMap = newFieldMap;
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
  public final void addUpdateFieldEntry(final String fieldName,
      final FieldInterface<?> entry)
  {
    this.fieldMap.put(fieldName, entry);
  }

  /*
   * (non-Javadoc)
   * 
   * @see de.knightsoft.dbnavigationbar.client.domain
   * .InterfaceDataBase#getFieldEntry(java.lang.String)
   */
  @Override
  public final FieldInterface<?> getFieldEntry(final String fieldName)
  {
    return this.fieldMap.get(fieldName);
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
    boolean entriesAreEqual =
        (this.readOnly == vglEntry.isReadOnly())
            && this.objectEquals(this.keyCur,
                vglEntry.getKeyCur())
            && (this.fieldMap.size() == vglEntry.getFieldMap().size());
    if (entriesAreEqual)
    {
      for (final String key : this.fieldMap.keySet())
      {
        entriesAreEqual &= this.objectEquals(this.getFieldEntry(key),
            vglEntry.getFieldEntry(key));
      }
    }
    return entriesAreEqual;
  }

  /**
   * objectEquals compares if two objects are equal.
   * 
   * @param thisObject
   *        a string to compare with vglString
   * @param compareObject
   *        entry to compare with thisString
   * @return true if both contain the same entries, otherwise false
   */
  protected final boolean objectEquals(final Object thisObject,
      final Object compareObject)
  {
    if (thisObject == compareObject) // NOPMD we really want same
    {
      return true;
    }
    if (thisObject == null || compareObject == null)
    {
      return false;
    }
    return thisObject.equals(compareObject);
  }
}

/**
 * This file is part of knightsoft db navigation.
 * 
 * DBNavigationBar is free software: you can redistribute it and/or modify
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
 * Copyright (c) 2012 RI Solutions GmbH
 * 
 */
package de.knightsoft.dbnavigationbar.shared.fields;

import java.io.Serializable;
import java.text.ParseException;

/**
 * 
 * <code>IntegerField</code> is a class to define a Integer field.
 * 
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public class IntegerField
    extends AbstractField<Integer>
    implements Serializable, FieldInterface<Integer>
{

  /**
   * Serial version id.
   */
  private static final long serialVersionUID = -7745497749409953830L;

  /**
   * minimum value.
   */
  private final int minEntry;

  /**
   * maximum value.
   */
  private final int maxEntry;

  /**
   * constructor.
   * 
   * @param setCanBeNull
   *        true if value allowed to be null
   * @param setPrimaryKey
   *        is primary key
   * @param setMinEntry
   *        maximum length of the value
   * @param setMaxEntry
   *        maximum length of the value
   * @param setDefaultValue
   *        default value
   */
  public IntegerField(final boolean setCanBeNull,
      final boolean setPrimaryKey,
      final int setMinEntry,
      final int setMaxEntry,
      final Integer setDefaultValue)
  {
    super(setCanBeNull, setPrimaryKey,
        Integer.toString(setMaxEntry).length(),
        setDefaultValue);
    this.minEntry = setMinEntry;
    this.maxEntry = setMaxEntry;
  }

  @Override
  public final String getString()
  {
    if (super.getValue() == null)
    {
      return null;
    }
    else
    {
      return super.getValue().toString();
    }
  }

  @Override
  public final void setString(final String sString) throws ParseException
  {
    if (sString == null || "".equals(sString))
    {
      super.setValue(null);
    }
    else
    {
      int pos = 0;
      for (final char c : sString.toCharArray())
      {
        if (!Character.isDigit(c))
        {
          throw new ParseException("not numeric", pos);
        }
        pos++;
      }
      try
      {
        super.setValue(Integer.valueOf(Integer.parseInt(sString)));
      }
      catch (final NumberFormatException e)
      {
        throw new ParseException(e.getMessage(), -1); // NOPMD
      }
    }
  }

  @Override
  public final boolean isOK()
  {
    boolean checkOk = !super.checkNullError();
    if (checkOk && (super.getValue() != null))
    {
      if (super.getValue().intValue() < this.minEntry)
      {
        // entry to small
        checkOk = false;
      }
      else if (super.getValue().intValue() > this.maxEntry)
      {
        // entry to big
        checkOk = false;
      }
    }
    return checkOk;
  }

  /**
   * @return the minEntry
   */
  public final int getMinEntry()
  {
    return this.minEntry;
  }

  /**
   * @return the maxEntry
   */
  public final int getMaxEntry()
  {
    return this.maxEntry;
  }
}

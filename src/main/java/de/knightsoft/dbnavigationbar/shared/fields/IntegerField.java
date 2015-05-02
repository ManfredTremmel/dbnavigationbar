/**
 * This file is part of DBNavigationBar.
 *
 * DBNavigationBar is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * DBNavigationBar is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with DBNavigationBar. If not, see <a
 * href="http://www.gnu.org/licenses>http://www.gnu.org/licenses</a>
 *
 *
 * Copyright (c) 2012-2015 Manfred Tremmel
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
public class IntegerField extends AbstractField<Integer> implements Serializable, FieldInterface<Integer> {

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
   * @param pCanBeNull true if value allowed to be null
   * @param pPrimaryKey is primary key
   * @param pMinEntry maximum length of the value
   * @param pMaxEntry maximum length of the value
   * @param pDefaultValue default value
   */
  public IntegerField(final boolean pCanBeNull, final boolean pPrimaryKey, final int pMinEntry, final int pMaxEntry,
      final Integer pDefaultValue) {
    super(pCanBeNull, pPrimaryKey, Integer.toString(pMaxEntry).length(), pDefaultValue);
    this.minEntry = pMinEntry;
    this.maxEntry = pMaxEntry;
  }

  @Override
  public final String getString() {
    if (super.getValue() == null) {
      return null;
    } else {
      return super.getValue().toString();
    }
  }

  @Override
  public final void setString(final String pString) throws ParseException {
    if (pString == null || "".equals(pString)) {
      super.setValue(null);
    } else {
      int pos = 0;
      for (final char c : pString.toCharArray()) {
        if (!Character.isDigit(c)) {
          throw new ParseException("not numeric", pos);
        }
        pos++;
      }
      try {
        super.setValue(Integer.valueOf(Integer.parseInt(pString)));
      } catch (final NumberFormatException e) {
        throw new ParseException(e.getMessage(), -1); // NOPMD
      }
    }
  }

  @Override
  public final boolean isOK() {
    boolean checkOk = !super.checkNullError();
    if (checkOk && (super.getValue() != null)) {
      if (super.getValue().intValue() < this.minEntry) {
        // entry to small
        checkOk = false;
      } else if (super.getValue().intValue() > this.maxEntry) {
        // entry to big
        checkOk = false;
      }
    }
    return checkOk;
  }

  public final int getMinEntry() {
    return this.minEntry;
  }

  public final int getMaxEntry() {
    return this.maxEntry;
  }
}

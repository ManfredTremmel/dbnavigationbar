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

package de.knightsoft.dbnavigationbar.shared.fields;

import java.io.Serializable;
import java.text.ParseException;

/**
 *
 * <code>StringField</code> is a class to define a String field.
 *
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public class StringField extends AbstractField<String> implements Serializable, FieldInterface<String> {

  /**
   * Serial version id.
   */
  private static final long serialVersionUID = -7745497749409953830L;

  /**
   * regular expression to check the entry.
   */
  private final String regExCheck;

  /**
   * constructor.
   *
   * @param pCanBeNull true if value allowed to be null
   * @param pPrimaryKey is primary key
   * @param pMaxLength maximum length of the value
   * @param pDefaultValue default value
   */
  public StringField(final boolean pCanBeNull, final boolean pPrimaryKey, final int pMaxLength, final String pDefaultValue) {
    super(pCanBeNull, pPrimaryKey, pMaxLength, pDefaultValue);
    this.regExCheck = null;
  }

  /**
   * constructor.
   *
   * @param pCanBeNull true if value allowed to be null
   * @param pPrimaryKey is primary key
   * @param pMaxLength maximum length of the value
   * @param pDefaultValue default value
   * @param pRegExCheck regular expression to check file
   */
  public StringField(final boolean pCanBeNull, final boolean pPrimaryKey, final int pMaxLength, final String pDefaultValue,
      final String pRegExCheck) {
    super(pCanBeNull, pPrimaryKey, pMaxLength, pDefaultValue);
    this.regExCheck = pRegExCheck;
  }

  @Override
  public final String getString() {
    return super.getValue();
  }

  @Override
  public final void setString(final String pString) throws ParseException {
    super.setValue(pString);
  }

  @Override
  public final boolean isOK() {
    boolean checkOk = !super.checkNullError();
    if (checkOk) {
      if ((super.getValue() != null) && (super.getValue().length() > this.getMaxLength())) {
        checkOk = false;
      }
      if (checkOk && this.regExCheck != null && this.getValue() != null) {
        checkOk = this.getValue().matches(this.regExCheck);
      }
    }
    return checkOk;
  }
}

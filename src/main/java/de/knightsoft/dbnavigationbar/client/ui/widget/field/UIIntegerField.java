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

package de.knightsoft.dbnavigationbar.client.ui.widget.field;

import de.knightsoft.dbnavigationbar.shared.fields.IntegerField;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.text.ParseException;

/**
 *
 * <code>UIIntegerField</code> is a class to define a Integer UI field.
 *
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public class UIIntegerField implements UIFieldInterface<Integer, IntegerField> {

  /**
   * field with unchanged database entry.
   */
  private final IntegerField dbField;

  /**
   * field with work entry.
   */
  private final IntegerField workField;

  /**
   * label to display.
   */
  private final Label inputLabel;

  /**
   * ui input box.
   */
  private final TextBox inputField;

  /**
   * localized messages.
   */
  private final UIIntegerFieldConstants localeMessages;

  /**
   * is input ok.
   */
  private boolean isOK;

  /**
   * error text.
   */
  private String errorText;

  /**
   * constructor.
   *
   * @param pInitField field to get data from
   * @param pTabNumber tabulator number of the field
   */
  public UIIntegerField(final IntegerField pInitField, final int pTabNumber) {
    this.dbField = pInitField;
    this.workField = pInitField;
    this.localeMessages = (UIIntegerFieldConstants) GWT.create(UIIntegerFieldConstants.class);
    this.inputLabel = new Label();
    this.inputField = new TextBox();

    // different label style for fields that can be empty
    this.inputLabel.setStylePrimaryName("gwt-Label");
    if (pInitField.isCanBeNull()) {
      this.inputLabel.setStyleName("optional", true);
    } else {
      this.inputLabel.setStyleName("mustBeFilled", true);
    }

    // set up input field
    this.inputField.setStylePrimaryName("gwt-TextBox");
    this.inputField.setTabIndex(pTabNumber);
    this.inputField.setMaxLength(pInitField.getMaxLength());
    // check input on every change and change style on error
    this.inputField.addKeyUpHandler(new KeyUpHandler() {
      @Override
      public final void onKeyUp(final KeyUpEvent pEvent) {
        final String entry = ((TextBox) pEvent.getSource()).getText();
        UIIntegerField.this.setText(entry);
      }
    });
  }

  /**
   * set value as text.
   *
   * @param pEntry to set
   */
  protected final void setText(final String pEntry) {
    try {
      if (pEntry == null || "".equals(pEntry)) {
        this.workField.setValue(null);
      } else {
        this.workField.setString(pEntry);
      }
      this.setValueField(this.workField);
    } catch (final ParseException e) {
      this.isOK = false;
      this.errorText = this.localeMessages.notNumeric(this.inputLabel.getText());
    }
  }

  @Override
  public final IntegerField getField() {
    return this.workField;
  }

  @Override
  public final void setBaseValue(final Integer pValue) {
    this.dbField.setValue(pValue);
    this.setValue(pValue);
  }

  @Override
  public final void setBaseValueField(final IntegerField pValueField) {
    this.dbField.setValue(pValueField.getValue());
    this.setValue(pValueField.getValue());
  }

  @Override
  public final void setValue(final Integer pValue) {
    this.workField.setValue(pValue);
    if ((this.inputField.getText() == null && pValue != null) || (this.inputField.getText() != null && pValue == null)
        || (!this.inputField.getText().equals(pValue.toString()))) {
      if (pValue == null) {
        this.inputField.setText(null);
      } else {
        this.inputField.setText(pValue.toString());
      }
    }
    this.isOK = this.workField.isOK();
    if (!this.isOK) {
      if (this.workField.getValue() == null) {
        this.errorText = this.localeMessages.emptyField(this.inputLabel.getText());
      } else {
        this.errorText =
            this.localeMessages.outOfRange(this.inputLabel.getText(), this.workField.getMinEntry(),
                this.workField.getMaxEntry());
      }
    }
    this.inputField.setStyleName("error", !this.isOK);
    this.inputField.setTitle(this.getErrorMessage());
  }

  @Override
  public final void setValueField(final IntegerField pValueField) {
    this.setValue(pValueField.getValue());
  }

  @Override
  public final boolean valueHasChanged() {
    return (this.dbField.getValue() == null && this.workField.getValue() != null)
        || !(this.dbField.getValue().equals(this.workField.getValue()));
  }

  @Override
  public final boolean valueIsOk() {
    return this.isOK;
  }

  @Override
  public final String getErrorMessage() {
    return this.errorText;
  }

  @Override
  public final void setLabelEntry(final String pLabelText) {
    this.inputLabel.setText(pLabelText);
  }

  @Override
  public final Label getLabel() {
    return this.inputLabel;
  }

  @Override
  public final Widget getWidget() {
    return this.inputField;
  }
}

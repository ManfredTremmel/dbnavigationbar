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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RiPhone.  If not, see <http://www.gnu.org/licenses/>
 *
 *
 * Copyright (c) 2012 Manfred Tremmel
 *
 * --
 *    Name        Date        Change
 */
package de.knightsoft.DBNavigationBar.client.ui.widget.field;

import java.text.ParseException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import de.knightsoft.DBNavigationBar.shared.fields.IntegerField;

/**
*
* <code>UIIntegerField</code> is a class to define a Integer UI field.
*
* @author Manfred Tremmel
* @version 1.0.0, 2012-05-17
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
     * is input ok?
     */
    private boolean isOK;

    /**
     * error text.
     */
    private String errorText;

    /**
     * constructor.
     *
     * @param initField
     *          field to get data from
     * @param tabNumber
     *          tabulator number of the field
     */
    public UIIntegerField(final IntegerField initField,
            final int tabNumber) {
        this.dbField = initField;
        this.workField = initField;
        this.localeMessages = (UIIntegerFieldConstants)
                GWT.create(UIIntegerFieldConstants.class);
        this.inputLabel = new Label();
        this.inputField = new TextBox();

        // different label style for fields that can be empty
        this.inputLabel.setStylePrimaryName("gwt-Label");
        if (initField.isCanBeNull()) {
            this.inputLabel.setStyleName("optional", true);
        } else {
            this.inputLabel.setStyleName("mustBeFilled", true);
        }

        // set up input field
        this.inputField.setStylePrimaryName("gwt-TextBox");
        this.inputField.setTabIndex(tabNumber);
        this.inputField.setMaxLength(initField.getMaxLength());
        // check input on every change and change style on error
        this.inputField.addKeyUpHandler(new KeyUpHandler() {
            @Override
            public final void onKeyUp(final KeyUpEvent event) {
                final String entry = ((TextBox) event.getSource()).getText();
                UIIntegerField.this.setText(entry);
            }
        });
    }

    /**
     * set value as text.
     *
     * @param entry to set
     */
    protected final void setText(final String entry) {
        try {
            if (entry == null || "".equals(entry)) {
                this.workField.setValue(null);
            } else {
                this.workField.setString(entry);
            }
            this.setValueField(this.workField);
        } catch (final ParseException e) {
            this.isOK = false;
            this.errorText = this.localeMessages.notNumeric(
                    this.inputLabel.getText());
        }
    }

    @Override
    public final IntegerField getField() {
        return this.workField;
    }

    @Override
    public final void setBaseValue(final Integer newValue) {
        this.dbField.setValue(newValue);
        this.setValue(newValue);
    }

    @Override
    public final void setBaseValueField(final IntegerField newValueField) {
        this.dbField.setValue(newValueField.getValue());
        this.setValue(newValueField.getValue());
    }

    @Override
    public final void setValue(final Integer newValue) {
        this.workField.setValue(newValue);
        if ((this.inputField.getText() == null && newValue != null)
         || (this.inputField.getText() != null && newValue == null)
         || (!this.inputField.getText().equals(newValue.toString()))) {
            if (newValue == null) {
                this.inputField.setText(null);
            } else {
                this.inputField.setText(newValue.toString());
            }
        }
        this.isOK = this.workField.isOK();
        if (!this.isOK) {
            if (this.workField.getValue() == null) {
                this.errorText = this.localeMessages.emptyField(
                        this.inputLabel.getText());
            } else {
                this.errorText = this.localeMessages.outOfRange(
                        this.inputLabel.getText(), this.workField.getMinEntry(),
                        this.workField.getMaxEntry());
            }
        }
        this.inputField.setStyleName("error", !this.isOK);
        this.inputField.setTitle(this.getErrorMessage());
    }

    @Override
    public final void setValueField(final IntegerField newValueField) {
        this.setValue(newValueField.getValue());
    }

    @Override
    public final boolean valueHasChanged() {
        return (this.dbField.getValue() == null
                && this.workField.getValue() != null)
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
    public final void setLabelEntry(final String labelText) {
        this.inputLabel.setText(labelText);
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

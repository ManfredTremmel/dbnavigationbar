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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import de.knightsoft.DBNavigationBar.shared.fields.StringField;

/**
*
* <code>UIStringField</code> is a class to define a String UI field.
*
* @author Manfred Tremmel
* @version 1.0.0, 2012-05-17
*/
public class UIStringField implements UIFieldInterface<String, StringField> {

    /**
     * field with unchanged database entry.
     */
    private final StringField dbField;

    /**
     * field with work entry.
     */
    private final StringField workField;

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
    private final UIStringFieldConstants localeMessages;

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
    public UIStringField(final StringField initField,
            final int tabNumber) {
        this.dbField = initField;
        this.workField = initField;
        this.localeMessages = (UIStringFieldConstants)
                GWT.create(UIStringFieldConstants.class);
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
                UIStringField.this.setValue(entry);
            }
        });
    }

    @Override
    public final StringField getField() {
        return this.workField;
    }

    @Override
    public final void setBaseValue(final String newValue) {
        this.dbField.setValue(newValue);
        this.setValue(newValue);
    }

    @Override
    public final void setBaseValueField(final StringField newValueField) {
        this.dbField.setValue(newValueField.getValue());
        this.setValue(newValueField.getValue());
    }

    @Override
    public final void setValue(final String newValue) {
        final String workValue;
        if ("".equals(newValue) && this.workField.isCanBeNull()) {
            workValue = null;
        } else {
            workValue = newValue;
        }
        this.workField.setValue(workValue);
        if ((this.inputField.getText() == null && workValue != null)
         || (!this.inputField.getText().equals(workValue))) {
            this.inputField.setText(workValue);
        }
        final boolean ok = this.valueIsOk();
        this.inputField.setStyleName("error", !ok);
        this.inputField.setTitle(this.getErrorMessage());
    }

    @Override
    public final void setValueField(final StringField newValueField) {
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
        final boolean isOK = this.workField.isOK();
        if (!isOK) {
            if (this.workField.getValue() == null) {
                this.errorText = this.localeMessages.emptyField(
                        this.inputLabel.getText());
            } else {
                this.errorText = this.localeMessages.formatError(
                        this.inputLabel.getText());
            }
        }
        return isOK;
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

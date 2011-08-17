/**
 * This file is part of DBNavigation.
 *
 * RiPhone is free software: you can redistribute it and/or modify
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
 * Copyright (c) 2011 Manfred Tremmel
 *
 * --
 *    Name        Date        Change
 */
package de.knightsoft.DBNavigationBar.client.ui;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;

import de.knightsoft.DBNavigationBar.client.Parent;
import de.knightsoft.DBNavigationBar.client.domain.DomainUser;

/**
 * The <code>BasicTemplateUI</code> class is a template for all
 * input mask.
 *
 * @param <F> parent widget
 *
 * @author Manfred Tremmel
 * @version 1.0.0, 2011-02-19
 */
public abstract class BasicTemplateUI<F extends Parent> extends Composite
    implements BasicTemplateUIInterface<F> {

    /**
     * parent widget.
     */
    private final F parentwidget;

    /**
     * a decimal point, localized.
     */
    private final char point;

    /**
     * key press handler for numeric input fields
     * (only numeric signs).
     */
    private final KeyPressHandler numericKeyPressHandler;

    /**
     * key press handler for numeric input fields
     * (numeric signs and decimal point).
     */
    private final KeyPressHandler numericPointKeyPressHandler;


    /**
     * Constructor.
     *
     * @param setParentwidget
     *         the parent widget, where this frame is displayed
     */
    public BasicTemplateUI(
            final F setParentwidget) {

        final double decPointTest = 1.1;

        this.parentwidget  =    setParentwidget;
        char tmpPoint      =    ',';
        try {
            tmpPoint = NumberFormat.getFormat("0.00")
                    .format(decPointTest).replaceAll("1", "")
                    .replaceAll("0", "").trim().charAt(0);
        } catch (Exception e) {
            tmpPoint = ',';
        }
        this.point    =    tmpPoint;

        this.numericKeyPressHandler = new KeyPressHandler() {
            @Override
            public void onKeyPress(final KeyPressEvent event) {
                int keyCode = 0;
                if (event.getNativeEvent() != null) {
                   keyCode = event.getNativeEvent().getKeyCode();
                }
                char charCode    =    event.getCharCode();

                switch(keyCode) {
                    case KeyCodes.KEY_BACKSPACE:
                    case KeyCodes.KEY_DELETE:
                    case KeyCodes.KEY_LEFT:
                    case KeyCodes.KEY_RIGHT:
                    case KeyCodes.KEY_SHIFT:
                    case KeyCodes.KEY_TAB:
                    case KeyCodes.KEY_ENTER:
                    case KeyCodes.KEY_HOME:
                    case KeyCodes.KEY_END:
                    case KeyCodes.KEY_UP:
                    case KeyCodes.KEY_DOWN:
                        break;
                    default:
                        // Copy, Cut or Paste or numeric input
                        if (!(event.isControlKeyDown()
                              && (charCode == 'c'
                               || charCode == 'x'
                               || charCode == 'v'))
                          && !Character.isDigit(charCode)) {
                            ((TextBox) event.getSource()).cancelKey();
                        }
                        break;
                }
            }
        };

        this.numericPointKeyPressHandler = new KeyPressHandler() {
            @Override
            public void onKeyPress(final KeyPressEvent event) {
                int keyCode = 0;
                if (event.getNativeEvent() != null) {
                   keyCode = event.getNativeEvent().getKeyCode();
                }
                char charCode    =    event.getCharCode();

                switch(keyCode) {
                    case KeyCodes.KEY_BACKSPACE:
                    case KeyCodes.KEY_DELETE:
                    case KeyCodes.KEY_LEFT:
                    case KeyCodes.KEY_RIGHT:
                    case KeyCodes.KEY_SHIFT:
                    case KeyCodes.KEY_TAB:
                    case KeyCodes.KEY_ENTER:
                    case KeyCodes.KEY_HOME:
                    case KeyCodes.KEY_END:
                    case KeyCodes.KEY_UP:
                    case KeyCodes.KEY_DOWN:
                        break;
                    default:
                        // Copy, Cut or Paste or numeric input or point
                        if (!(event.isControlKeyDown()
                              && (charCode == 'c'
                               || charCode == 'x'
                               || charCode == 'v'))
                          && !Character.isDigit(charCode)
                          && (BasicTemplateUI.this.point
                              != charCode)) {
                            ((TextBox) event.getSource()).cancelKey();
                        }
                        break;
                }
            }
        };
    }

    /**
     * <code>setFocusOnFirstWidget</code> set focus to first enabled
     * input field.
     *
     */
    protected abstract void setFocusOnFirstWidget();


    /**
     * The Method <code>matchesMenuSimple</code> looks if this UI is selected
     * and makes the necessary changes.
     *
     * @param itemtext
     *            selected menu item
     * @param user
     *            user information about the currently logged in user
     * @return true if it is allowed for this user
     */
    public final boolean matchesMenuSimple(
            final String itemtext,
            final DomainUser user) {
        boolean matches = false;
        if (itemtext != null
         && itemtext.indexOf(this.getMenuText()) >= 0) {
            //this.myNavigationBar.enableAllButtons();
            this.parentwidget.getMainPanel().clear();
            this.parentwidget.getMainPanel().add(this);
            this.setFocusOnFirstWidget();
            matches    =    true;
            History.newItem("page=" + this.getMenuText());
        }
        return matches;
    }

    /**
     * return parent widget.
     * @return the parent widget
     */
    @Override
    public final F getParentwidget() {
        return parentwidget;
    }

    /**
     * return decimal point.
     * @return the point
     */
    public final char getPoint() {
        return point;
    }

    /**
     * return numeric key press handler.
     * @return the numericKeyPressHandler
     */
    public final KeyPressHandler getNumericKeyPressHandler() {
        return numericKeyPressHandler;
    }

    /**
     * return numeric point key press handler.
     * @return the numericPointKeyPressHandler
     */
    public final KeyPressHandler getNumericPointKeyPressHandler() {
        return numericPointKeyPressHandler;
    }

}

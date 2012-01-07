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
 * Copyright (c) 2011-2012 Manfred Tremmel
 *
 * --
 *    Name        Date        Change
 */
package de.knightsoft.DBNavigationBar.client.ui;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;

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
     * Constructor.
     *
     * @param setParentwidget
     *         the parent widget, where this frame is displayed
     */
    public BasicTemplateUI(
            final F setParentwidget) {

        this.parentwidget  =    setParentwidget;
    }

    /**
     * <code>setFocusOnFirstWidget</code> set focus to first enabled
     * input field.
     *
     */
    protected abstract void setFocusOnFirstWidget();

    /**
     * set up the mask.
     * @param user
     *            user information about the currently logged in user
     */
    protected abstract void setUpMask(DomainUser user);


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
        if (this.allowedToSee(user)
         && itemtext != null
         && itemtext.equals(this.getMenuText())) {
            //this.myNavigationBar.enableAllButtons();
            this.parentwidget.getMainPanel().clear();
            this.setUpMask(user);
            this.parentwidget.getMainPanel().add(this);
            matches    =    true;
            History.newItem("page=" + this.getMenuText());
        }
        return matches;
    }

    @Override
    public final void onLoad() {
        super.onLoad();
        this.setFocusOnFirstWidget();
    }
    /**
     * return parent widget.
     * @return the parent widget
     */
    @Override
    public final F getParentwidget() {
        return parentwidget;
    }
}

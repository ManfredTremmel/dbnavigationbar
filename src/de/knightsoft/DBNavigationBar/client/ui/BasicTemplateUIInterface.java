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
 */
package de.knightsoft.DBNavigationBar.client.ui;

import com.google.gwt.safehtml.shared.SafeHtml;

import de.knightsoft.DBNavigationBar.client.AbstractParent;
import de.knightsoft.DBNavigationBar.client.domain.AbstractDomainUser;

/**
 * The <code>BasicTemplateUIInterface</code> defines the basic
 * method every mask has to implement.
 *
 * @param <F> parent widget
 *
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public interface BasicTemplateUIInterface<F extends AbstractParent> {

    /**
     * The Method <code>getHeaderTitle</code> gets title text.
     *
     * @return menu text
     */
    String getHeaderTitle();

    /**
     * The Method <code>getMenuText</code> gets menu text.
     *
     * @return menu text
     */
    String getMenuText();

    /**
     * The Method <code>getMenuEntry</code> gets completely menu entry.
     *
     * @return menu entry
     */
    SafeHtml getMenuEntry();

    /**
     * returns the parent widget.
     * @return the parent widget
     */
    F getParentwidget();

    /**
     * The Method <code>matchesMenu</code> looks if this UI is selected and
     * makes the necessary changes.
     *
     * @param itemtext
     *            selected menu item
     * @param user
     *            user information about the currently logged in user
     * @return true if it is allowed for this user
     */
    boolean matchesMenu(final String itemtext, final AbstractDomainUser user);

    /**
     * The Method <code>allowedToSee</code> tells you if the currently logged
     * in user is allowed to access this application.
     *
     * @param user
     *            information about logged in user
     * @return true if it is allowed for this user
     */
    boolean allowedToSee(AbstractDomainUser user);

    /**
     * The Method <code>allowedToChange</code> tells you if the currently
     * logged in user is allowed to change data in this application.
     *
     * @param user
     *            information about logged in user
     * @return true if it is allowed for this user
     */
    boolean allowedToChange(AbstractDomainUser user);
}

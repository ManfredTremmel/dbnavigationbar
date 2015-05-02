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
 * Copyright (c) 2011-2015 Manfred Tremmel
 *
 */

package de.knightsoft.dbnavigationbar.client.ui;

import de.knightsoft.dbnavigationbar.client.AbstractParent;
import de.knightsoft.dbnavigationbar.client.domain.AbstractDomainUser;

import com.google.gwt.safehtml.shared.SafeHtml;

/**
 * The <code>BasicTemplateUIInterface</code> defines the basic method every mask has to implement.
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
   *
   * @return the parent widget
   */
  F getParentwidget();

  /**
   * The Method <code>matchesMenu</code> looks if this UI is selected and makes the necessary changes.
   *
   * @param pItemtext selected menu item
   * @param pUser user information about the currently logged in user
   * @return true if it is allowed for this user
   */
  boolean matchesMenu(final String pItemtext, final AbstractDomainUser pUser);

  /**
   * The Method <code>allowedToSee</code> tells you if the currently logged in user is allowed to access this application.
   *
   * @param pUser information about logged in user
   * @return true if it is allowed for this user
   */
  boolean allowedToSee(AbstractDomainUser pUser);

  /**
   * The Method <code>allowedToChange</code> tells you if the currently logged in user is allowed to change data in this
   * application.
   *
   * @param pUser information about logged in user
   * @return true if it is allowed for this user
   */
  boolean allowedToChange(AbstractDomainUser pUser);
}

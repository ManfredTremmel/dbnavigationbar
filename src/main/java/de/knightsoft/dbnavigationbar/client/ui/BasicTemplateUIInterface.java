/**
 * This file is part of DBNavigationBar.
 *
 * DBNavigationBar is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * DBNavigationBar is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with DBNavigationBar. If
 * not, see <a href="http://www.gnu.org/licenses>http://www.gnu.org/licenses</a>
 *
 *
 * Copyright (c) 2011-2015 Manfred Tremmel
 *
 */

package de.knightsoft.dbnavigationbar.client.ui;

import de.knightsoft.dbnavigationbar.client.domain.AbstractDomainUser;

/**
 * The <code>BasicTemplateUIInterface</code> defines the basic method every mask has to implement.
 *
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public interface BasicTemplateUIInterface {

  /**
   * The Method <code>getHeaderTitle</code> gets title text.
   *
   * @return menu text
   */
  String getHeaderTitle();

  /**
   * The Method <code>allowedToSee</code> tells you if the currently logged in user is allowed to
   * access this application.
   *
   * @param pUser information about logged in user
   * @return true if it is allowed for this user
   */
  boolean allowedToSee(AbstractDomainUser pUser);

  /**
   * The Method <code>allowedToChange</code> tells you if the currently logged in user is allowed to
   * change data in this application.
   *
   * @param pUser information about logged in user
   * @return true if it is allowed for this user
   */
  boolean allowedToChange(AbstractDomainUser pUser);
}

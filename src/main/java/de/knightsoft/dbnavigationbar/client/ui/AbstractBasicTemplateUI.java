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

import com.gwtplatform.mvp.client.ViewImpl;

/**
 * The <code>AbstractBasicTemplateUI</code> class is a template for all input mask.
 *
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public abstract class AbstractBasicTemplateUI extends ViewImpl implements BasicTemplateUIInterface {

  /**
   * <code>setFocusOnFirstWidget</code> set focus to first enabled input field.
   *
   */
  protected abstract void setFocusOnFirstWidget();

  /**
   * set up the mask.
   *
   * @param pUser user information about the currently logged in user
   */
  protected abstract void setUpMask(AbstractDomainUser pUser);

  public void onReveal(final AbstractDomainUser pSessionUser) {
    this.setUpMask(pSessionUser);
    this.setFocusOnFirstWidget();
  }
}

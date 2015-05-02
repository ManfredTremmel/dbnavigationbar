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
import de.knightsoft.dbnavigationbar.client.domain.AbstractDomainHeadDB;

import com.google.gwt.user.client.ui.Widget;

/**
 *
 * The <code>AbstractDBHeadTemplateUI</code> class is a template for database input mask.
 *
 * @param <E> data structure
 * @param <F> parent widget
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public abstract class AbstractDBHeadTemplateUI<E extends AbstractDomainHeadDB, F extends AbstractParent> extends
    AbstractBasicDBTemplateUI<E, F> {

  /**
   * Constructor.
   *
   * @param pParentwidget the parent widget, where this frame is displayed
   * @param pThisWidgetlist list of widgets to display
   * @param pUserdefinedfunction special function to include into the navigation bar
   */
  public AbstractDBHeadTemplateUI(final F pParentwidget, final Widget[] pThisWidgetlist, final String pUserdefinedfunction) {

    super(pParentwidget, pThisWidgetlist, pUserdefinedfunction);
  }
}

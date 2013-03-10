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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with RiPhone. If not, see <http://www.gnu.org/licenses/>
 * 
 * 
 * Copyright (c) 2011-2012 Manfred Tremmel
 * 
 */
package de.knightsoft.dbnavigationbar.client.ui;

import com.google.gwt.user.client.ui.Widget;

import de.knightsoft.dbnavigationbar.client.AbstractParent;
import de.knightsoft.dbnavigationbar.client.domain.AbstractDomainHeadDB;

/**
 * 
 * The <code>AbstractDBHeadTemplateUI</code> class is a template for database
 * input mask.
 * 
 * @param <E>
 *        data structure
 * @param <F>
 *        parent widget
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public abstract class AbstractDBHeadTemplateUI<E extends AbstractDomainHeadDB, F extends AbstractParent>
    extends AbstractBasicDBTemplateUI<E, F>
{

  /**
   * Constructor.
   * 
   * @param parentwidget
   *        the parent widget, where this frame is displayed
   * @param thisWidgetlist
   *        list of widgets to display
   * @param userdefinedfunction
   *        special function to include into the navigation bar
   */
  public AbstractDBHeadTemplateUI(
      final F parentwidget,
      final Widget[] thisWidgetlist,
      final String userdefinedfunction)
  {

    super(parentwidget, thisWidgetlist, userdefinedfunction);
  }
}

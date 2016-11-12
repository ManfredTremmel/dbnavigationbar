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

import com.google.gwt.i18n.client.Constants;

/**
 *
 * The <code>DBHeadTemplateUIConstants</code> class contains the messages for head template.
 *
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public interface BasicDBTemplateUIConstants extends Constants {

  /**
   * title of the page.
   *
   * @return title
   */
  String title();

  /**
   * menuEntry of the page.
   *
   * @return menuEntry
   */
  String menuEntry();

  /**
   * message to display if database entry is not found.
   *
   * @return searchErrorMessage
   */
  String searchErrorMessage();

  /**
   * message to display if input format is wrong.
   *
   * @return formatError
   */
  String formatError();

  /**
   * message to display when field is empty.
   *
   * @return empty
   */
  String empty();

  /**
   * message to display if entry can't be found in database.
   *
   * @return notFoundInDB
   */
  String notFoundInDB();

  /**
   * message to display if saving was successfully.
   *
   * @return savedSuccessfully
   */
  String savedSuccessfully();
}

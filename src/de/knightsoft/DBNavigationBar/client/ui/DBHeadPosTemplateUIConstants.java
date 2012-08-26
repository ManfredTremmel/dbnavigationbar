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

import com.google.gwt.i18n.client.Constants;

/**
 *
 * The <code>DBHeadPosTemplateUIConstants</code> class contains the
 * messages for position template.
 *
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public interface DBHeadPosTemplateUIConstants extends Constants {
    /**
     * the text "function".
     * @return function
     */
    String function();
    /**
     * text of the new entry button.
     * @return addPositionButton
     */
    String addPositionButton();
    /**
     * message to display if two entries with the same key are inserted.
     * @return doubleEntryError
     */
    String doubleEntryError();
}

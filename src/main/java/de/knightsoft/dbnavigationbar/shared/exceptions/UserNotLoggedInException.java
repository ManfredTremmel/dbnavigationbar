/**
 * This file is part of DBNavigationBar.
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
 * Copyright (c) 2012 Manfred Tremmel
 * 
 */
package de.knightsoft.dbnavigationbar.shared.exceptions;

import java.io.Serializable;

/**
 * 
 * The <code>UserNotAllowedException</code> is thrown, when a user is not
 * allowed to do, what he want's to do.
 * 
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public class UserNotLoggedInException extends UserNotAllowedException
    implements Serializable
{

  /**
   * serial version uid.
   */
  private static final long serialVersionUID = 1116544479101222616L;
}

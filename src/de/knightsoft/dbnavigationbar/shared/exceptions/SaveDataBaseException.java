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
 * The <code>SaveDataBaseException</code> is thrown, when save of an entry
 * in data base fails.
 * 
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public class SaveDataBaseException extends ServerErrorException
    implements Serializable
{

  /**
   * serial version uid.
   */
  private static final long serialVersionUID = -3793818666585452517L;

  /**
   * default constructor.
   */
  public SaveDataBaseException()
  {
    super();
  }

  /**
   * constructor with given message.
   * 
   * @param messageInit
   *        a message to set.
   */
  public SaveDataBaseException(final String messageInit)
  {
    super(messageInit);
  }

  /**
   * constructor with given message.
   * 
   * @param exception
   *        a message to set.
   */
  public SaveDataBaseException(final Exception exception)
  {
    super(exception);
  }
}

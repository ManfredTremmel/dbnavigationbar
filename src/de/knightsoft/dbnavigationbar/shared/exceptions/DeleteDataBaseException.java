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
 * The <code>DeleteDataBaseException</code> is thrown, when deleting
 * of a database entry fails.
 * 
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public class DeleteDataBaseException extends ServerErrorException
    implements Serializable
{

  /**
   * serial version uid.
   */
  private static final long serialVersionUID = 6454631106948597750L;

  /**
   * default constructor.
   */
  public DeleteDataBaseException()
  {
    super();
  }

  /**
   * constructor with given message.
   * 
   * @param messageInit
   *        a message to set.
   */
  public DeleteDataBaseException(final String messageInit)
  {
    super(messageInit);
  }

  /**
   * constructor with given message.
   * 
   * @param exception
   *        a message to set.
   */
  public DeleteDataBaseException(final Exception exception)
  {
    super(exception);
  }
}

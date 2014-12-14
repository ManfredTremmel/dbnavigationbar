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
 * Copyright (c) 2011-2012 Manfred Tremmel
 *
 */
package de.knightsoft.dbnavigationbar.client.domain;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * The <code>RiPhoneDomaneHeadDateBase</code> class is a exchange structure
 * between the client and the servlet on the server.
 *
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public abstract class AbstractDomainHeadDB extends AbstractDomainDBBasics
    implements DomainHeadDataBaseInterface, Serializable
{

  /**
   * Serial version id.
   */
  private static final long serialVersionUID = -2178996035568385436L;

  /**
   * Constructor, setup a empty entry.
   */
  public AbstractDomainHeadDB()
  {
    super();
  }

  /**
   * Copy Constructor, creates a new user with the same
   * entries as the one who's given as parameter.
   *
   * @param pEntry
   *        entry to copy
   */
  public AbstractDomainHeadDB(final AbstractDomainHeadDB pEntry)
  {
    super(pEntry);
  }

  /**
   * equals compares two entries.
   *
   * @param pObj
   *        entry to compare with entry of this class
   * @return true if both contain the same entries, otherwise false
   */
  @Override
  public final boolean equals(final Object pObj)
  {
    if (this == pObj)
    {
      return true;
    }
    if (pObj == null)
    {
      return false;
    }
    if (!pObj.getClass().equals(this.getClass()))
    {
      return false;
    }
    return this.equalsEntry((AbstractDomainDBBasics) pObj);
  }

  /**
   * hashCode implementation.
   *
   * @return hash code of the key
   */
  @Override
  public final int hashCode()
  {
    return Objects.hashCode(this.getKeyCur());
  }
}

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

package de.knightsoft.dbnavigationbar.client.domain;

import java.io.Serializable;
import java.util.Objects;

/**
 * The <code>RiPhoneDomaneHeadPosDataBase</code> class is a exchange structure between the client
 * and the servlet on the server.
 *
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public abstract class AbstractDomainHead2PosDB extends AbstractDomainDBBasics implements
    DomainHeadDataBaseInterface, DomainHeadPosDataBaseInt, DomainHead2PosDataBaseInt, Serializable {

  /**
   * Serial version id.
   */
  private static final long serialVersionUID = -7326572024556553144L;

  /**
   * Constructor, setup a empty entry.
   */
  public AbstractDomainHead2PosDB() {
    super();
  }

  /**
   * Copy Constructor, creates a new user with the same entries as the one who's given as parameter.
   *
   * @param pEntry entry to copy
   */
  public AbstractDomainHead2PosDB(final AbstractDomainHead2PosDB pEntry) {
    super(pEntry);
  }

  /**
   * equals compares two entries.
   *
   * @param pObj entry to compare with entry of this class
   * @return true if both contain the same entries, otherwise false
   */
  @Override
  public final boolean equals(final Object pObj) {
    if (this == pObj) {
      return true;
    }
    if (pObj == null) {
      return false;
    }
    if (!pObj.getClass().equals(this.getClass())) {
      return false;
    }

    boolean isequal = this.equalsEntry((AbstractDomainDBBasics) pObj);
    if (isequal) {
      String[] posKeys = this.getKeyPos();
      String[] vglPosKeys = ((AbstractDomainHead2PosDB) pObj).getKeyPos();
      if (posKeys == null) {
        posKeys = new String[0];
      }
      if (vglPosKeys == null) {
        vglPosKeys = new String[0];
      }
      if (posKeys.length == vglPosKeys.length) {
        for (int pos = 0; (pos < posKeys.length) && isequal; pos++) {
          isequal &= this.equalsPosition((AbstractDomainHead2PosDB) pObj, pos, pos);
        }
      } else {
        isequal = false;
      }
    }
    if (isequal) {
      String[] pos2Keys = this.getKeyPos2();
      String[] vglPos2Keys = ((AbstractDomainHead2PosDB) pObj).getKeyPos2();
      if (pos2Keys == null) {
        pos2Keys = new String[0];
      }
      if (vglPos2Keys == null) {
        vglPos2Keys = new String[0];
      }
      if (pos2Keys.length == vglPos2Keys.length) {
        for (int pos2 = 0; (pos2 < pos2Keys.length) && isequal; pos2++) {
          isequal &= this.equalsPosition2((AbstractDomainHead2PosDB) pObj, pos2, pos2);
        }
      } else {
        isequal = false;
      }
    }
    return isequal;
  }

  /**
   * compare to positions.
   *
   * @param pCompare comparison position
   * @param pPosThis position of this structure
   * @param pPosCompare position of the comparison structure
   * @return true if equal otherwise false
   */
  @Override
  public abstract boolean equalsPosition(DomainHeadPosDataBaseInt pCompare, int pPosThis,
      int pPosCompare);

  /**
   * compare two positions.
   *
   * @param pCompare comparison position
   * @param pPosThis position of this structure
   * @param pPosCompare position of the comparison structure
   * @return true if equal otherwise false
   */
  @Override
  public abstract boolean equalsPosition2(DomainHead2PosDataBaseInt pCompare, int pPosThis,
      int pPosCompare);

  /**
   * hashCode implementation.
   *
   * @return hash code of the key
   */
  @Override
  public final int hashCode() {
    return Objects.hashCode(this.getKeyCur());
  }
}

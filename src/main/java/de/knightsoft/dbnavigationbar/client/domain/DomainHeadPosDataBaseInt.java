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

/**
 *
 * The <code>DomainHeadDataBaseInterface</code> interface defines the base functions for a data base
 * domain.
 *
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public interface DomainHeadPosDataBaseInt extends DomainHeadDataBaseInterface {

  /**
   * get KeyPos.
   *
   * @return Key of Positions
   */
  String[] getKeyPos();

  /**
   * compare to positions.
   *
   * @param pCompare comparison position
   * @param pPosThis position of this structure
   * @param pPosCompare position of the comparison structure
   * @return true if equal otherwise false
   */
  boolean equalsPosition(DomainHeadPosDataBaseInt pCompare, int pPosThis, int pPosCompare);

}

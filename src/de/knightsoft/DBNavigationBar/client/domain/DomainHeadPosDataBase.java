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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RiPhone.  If not, see <http://www.gnu.org/licenses/>
 *
 *
 * Copyright (c) 2011 Manfred Tremmel
 *
 * --
 *    Name        Date        Change
 */
package de.knightsoft.DBNavigationBar.client.domain;

import java.io.Serializable;



/**
 *
 * The <code>RiPhoneDomaneHeadPosDataBase</code> class is a exchange structure
 * between the client and the servlet on the server.
 *
 * @author Manfred Tremmel
 * @version 1.0.0, 2011-02-05
 */
public abstract class DomainHeadPosDataBase
    extends DomainDataBaseBasics
    implements DomainHeadDataBaseInterface, DomainHeadPosDataBaseInt,
    Serializable {

    /**
     * Serial version id.
     */
    private static final long serialVersionUID = -6242668640945495634L;

    /**
     * Constructor, setup a empty entry.
     */
    public DomainHeadPosDataBase() {
        super();
    }

    /**
     * Copy Constructor, creates a new user with the same
     * entries as the one who's given as parameter.
     *
     * @param copyEntry entry to copy
     */
    public DomainHeadPosDataBase(final DomainHeadPosDataBase copyEntry) {
        super(copyEntry);
    }

    /**
     * equals compares two entries.
     *
     * @param obj entry to compare with entry of this class
     * @return true if both contain the same entries, otherwise false
     */
    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof DomainHeadPosDataBase)) {
            return false;
        }

        boolean isequal         =  this.equalsEntry((DomainDataBaseBasics) obj);
        if (isequal) {
            String[] posKeys    = this.getKeyPos();
            String[] vglPosKeys = ((DomainHeadPosDataBase) obj).getKeyPos();
            if (posKeys == null) {
                posKeys = new String[0];
            }
            if (vglPosKeys == null) {
                vglPosKeys = new String[0];
            }
            if (posKeys.length == vglPosKeys.length) {
                for (int pos = 0;
                     (pos < posKeys.length)
                     && isequal; pos++) {
                    isequal &= this.equalsPosition(
                            ((DomainHeadPosDataBase) obj), pos, pos);
                }
            } else {
                isequal    =    false;
            }
        }
        return isequal;
    }

    /**
     * hashCode implementation.
     * @return hash code of the key
     */
    @Override
    public final int hashCode() {
        return this.getKeyCur().hashCode();
    }
}

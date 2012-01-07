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
 * Copyright (c) 2011-2012 Manfred Tremmel
 *
 * --
 *    Name        Date        Change
 */
package de.knightsoft.DBNavigationBar.client.domain;

import java.io.Serializable;

/**
 * The <code>RiPhoneDomaneHeadPosDataBase</code> class is a exchange structure
 * between the client and the servlet on the server.
 *
 * @author Manfred Tremmel
 * @version 1.0.0, 2011-02-05
 */
public abstract class DomainHead2PosDataBase extends DomainDataBaseBasics
       implements DomainHeadDataBaseInterface, DomainHeadPosDataBaseInt,
       DomainHead2PosDataBaseInt, Serializable {

    /**
     * Serial version id.
     */
    private static final long serialVersionUID = -7326572024556553144L;

    /**
     * Constructor, setup a empty entry.
     */
    public DomainHead2PosDataBase() {
        super();
    }

    /**
     * Copy Constructor, creates a new user with the same
     * entries as the one who's given as parameter.
     *
     * @param copyEntry entry to copy
     */
    public DomainHead2PosDataBase(final DomainHead2PosDataBase copyEntry) {
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
        if (!(obj instanceof DomainHead2PosDataBase)) {
            return false;
        }

        boolean isequal         =  this.equalsEntry((DomainDataBaseBasics) obj);
        if (isequal) {
            String[] posKeys    = this.getKeyPos();
            String[] vglPosKeys = ((DomainHead2PosDataBase) obj).getKeyPos();
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
                            ((DomainHead2PosDataBase) obj), pos, pos);
                }
            } else {
                isequal    =    false;
            }
        }
        if (isequal) {
            String[] pos2Keys    = this.getKeyPos2();
            String[] vglPos2Keys = ((DomainHead2PosDataBase) obj).getKeyPos2();
            if (pos2Keys == null) {
                pos2Keys = new String[0];
            }
            if (vglPos2Keys == null) {
                vglPos2Keys = new String[0];
            }
            if (pos2Keys.length == vglPos2Keys.length) {
                for (int pos2 = 0;
                     (pos2 < pos2Keys.length)
                     && isequal; pos2++) {
                    isequal &= this.equalsPosition2(
                            ((DomainHead2PosDataBase) obj), pos2, pos2);
                }
            } else {
                isequal    =    false;
            }
        }
        return isequal;
    }

    /**
     * compare to positions.
     * @param vgl
     *      comparison position
     * @param posthis
     *      position of this structure
     * @param posvgl
     *      position of the comparison structure
     * @return true if equal otherwise false
     */
    public abstract boolean equalsPosition(DomainHeadPosDataBaseInt vgl,
            int posthis, int posvgl);

    /**
     * compare two positions.
     * @param vgl
     *      comparison position
     * @param posthis
     *      position of this structure
     * @param posvgl
     *      position of the comparison structure
     * @return true if equal otherwise false
     */
    public abstract boolean equalsPosition2(DomainHead2PosDataBaseInt vgl,
            int posthis, int posvgl);

    /**
     * hashCode implementation.
     * @return hash code of the key
     */
    @Override
    public final int hashCode() {
        return this.getKeyCur().hashCode();
    }
}

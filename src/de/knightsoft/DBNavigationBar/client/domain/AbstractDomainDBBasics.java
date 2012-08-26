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
 */
package de.knightsoft.DBNavigationBar.client.domain;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * The <code>AbstractDomainDBBasics</code> class is a exchange structure
 * between the client and the servlet on the server with database
 * functions. This is a abstract basic class to setup on.
 *
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public abstract class AbstractDomainDBBasics
    implements DomainDataBaseInterface, Serializable {

    /**
     * Serial version id.
     */
    private static final long serialVersionUID = 6396714435198200480L;

    /**
     * readOnly, true if only read is allowed, false, if it's writable.
     */
    private Boolean readOnly;

    /**
     * keyMin, the lowest key in the database.
     */
    private String keyMin;

    /**
     * keyMax, the highest key in the database.
     */
    private String keyMax;

    /**
     * keyCur, the key of the current entry, in new entries it's null.
     */
    private String keyCur;

    /**
     * Constructor, setup a empty entry.
     */
    public AbstractDomainDBBasics() {
        this.setUpDefaultEntry();
        this.keyMin = null;
        this.keyMax = null;
    }

    /**
     * Copy Constructor, creates a new user with the same
     * entries as the one who's given as parameter.
     *
     * @param copyEntry entry to copy
     */
    public AbstractDomainDBBasics(
            final AbstractDomainDBBasics copyEntry) {
        if (copyEntry == null) {
            this.setUpDefaultEntry();
            this.keyMin = null;
            this.keyMax = null;
        } else {
            this.readOnly = copyEntry.readOnly;
            this.keyMin = copyEntry.keyMin;
            this.keyMax = copyEntry.keyMax;
            this.keyCur = copyEntry.keyCur;
        }
    }

    /**
     * get readOnly.
     *
     * @return readOnly
     */
    @Override
    public final boolean isReadOnly() {
        return this.readOnly;
    }

    /**
     * set readOnly.
     *
     * @param newIsReadOnly new readOnly
     */
    @Override
    public final void setIsReadOnly(final boolean newIsReadOnly) {
        this.readOnly = newIsReadOnly;
    }

    /**
     * set readOnly.
     *
     * @param newIsReadOnly new readOnly
     */
    @Override
    public final void setIsReadOnly(final Boolean newIsReadOnly) {
        this.readOnly = newIsReadOnly;
    }

    /**
     * get keyMin.
     *
     * @return keyMin
     */
    @Override
    public final String getKeyMin() {
        return this.keyMin;
    }

    /**
     * set keyMin.
     *
     * @param newKeyMin new keyMin
     */
    @Override
    public final void setKeyMin(final String newKeyMin) {
        this.keyMin = newKeyMin;
    }

    /**
     * get keyMax.
     *
     * @return keyMax
     */
    @Override
    public final String getKeyMax() {
        return this.keyMax;
    }

    /**
     * set keyMax.
     *
     * @param newKeyMax new keyMax
     */
    @Override
    public final void setKeyMax(final String newKeyMax) {
        this.keyMax = newKeyMax;
    }

    /**
     * get keyCur.
     *
     * @return keyCur
     */
    @Override
    public final String getKeyCur() {
        return this.keyCur;
    }

    /**
     * set keyCur.
     *
     * @param newKeyCur key to set
     */
    @Override
    public final void setKeyCur(final String newKeyCur) {
        this.keyCur = newKeyCur;
    }


    /**
     * stringEquals compares two String entries.
     *
     * @param thisString a string to compare with vglString
     * @param vglString entry to compare with thisString
     * @return true if both contain the same entries, otherwise false
     */
    protected final boolean stringEquals(final String thisString,
          final String vglString) {
        String compareThisString = thisString;
        String compareVglString = vglString;
        if (compareThisString == null) {
            compareThisString = "";
        }
        if (compareVglString == null) {
            compareVglString = "";
        }
        return compareThisString.equals(compareVglString);
    }

    /**
     * dateEquals compares two Date entries.
     *
     * @param thisEntry entry to compare with vglEntry
     * @param vglEntry entry to compare with thisEntry
     * @return true if both contain the same entries, otherwise false
     */
    protected final boolean dateEquals(final Date thisEntry,
            final Date vglEntry) {
        Date compareThisEntry = thisEntry;
        Date compareVglEntry = vglEntry;
        if (compareThisEntry == null) {
            compareThisEntry = new Date(0);
        }
        if (compareVglEntry == null) {
            compareVglEntry = new Date(0);
        }
        return compareThisEntry.equals(compareVglEntry);
    }

    /**
     * set up a default key and read only entries.
     *
     */
    public final void setUpDefaultEntryKey() {
        this.readOnly                    =    false;
        this.keyCur                        =    null;
    }

}

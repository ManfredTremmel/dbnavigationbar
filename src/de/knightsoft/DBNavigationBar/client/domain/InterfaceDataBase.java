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
 * Copyright (c) 2012 Manfred Tremmel
 *
 */
package de.knightsoft.DBNavigationBar.client.domain;

import java.util.HashMap;

import de.knightsoft.DBNavigationBar.shared.fields.FieldInterface;

/**
 *
 * The <code>InterfaceDataBase</code> interface defines the base
 * functions for a data base domain.
 *
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 * @param <E> Type of the keyField
 */
public interface InterfaceDataBase<E> {
    /**
     * get isReadOnly.
     *
     * @return isReadOnly
     */
    boolean isReadOnly();

    /**
     * set isReadOnly.
     *
     * @param newIsReadOnly new isReadOnly
     */
    void setIsReadOnly(final boolean newIsReadOnly);

    /**
     * set isReadOnly.
     *
     * @param newIsReadOnly new isReadOnly
     */
    void setIsReadOnly(final Boolean newIsReadOnly);

    /**
     * get keyMin.
     *
     * @return keyMin
     */
    FieldInterface<E> getKeyMin();

    /**
     * set keyMin.
     *
     * @param newKeyMin new keyMin
     */
    void setKeyMin(final FieldInterface<E> newKeyMin);

    /**
     * get keyMax.
     *
     * @return keyMax
     */
    FieldInterface<E> getKeyMax();

    /**
     * set keyMax.
     *
     * @param newKeyMax new keyMax
     */
    void setKeyMax(final FieldInterface<E> newKeyMax);

    /**
     * get keyCur.
     *
     * @return keyCur
     */
    FieldInterface<E> getKeyCur();

    /**
     * set keyCur.
     *
     * @param newKeyCur key to set
     */
    void setKeyCur(final FieldInterface<E> newKeyCur);

    /**
     * get KeyNew.
     * @return Key of new Entry
     */
    E getKeyNew();

    /**
     * set new field map.
     *
     * @param newFieldMap FieldMap to set
     */
    void setFildMap(final HashMap<String,
            FieldInterface<?>> newFieldMap);

    /**
     * get field map.
     *
     * @return FieldMap
     */
    HashMap<String, FieldInterface<?>> getFieldMap();

    /**
     * add or update a entry in field map.
     *
     * @param fieldName name of the field
     * @param entry entry to add or update
     */
    void addUpdateFieldEntry(final String fieldName,
            final FieldInterface<?> entry);

    /**
     * get field entry.
     *
     * @param fieldName name of the field to read
     * @return field
     */
    FieldInterface<?> getFieldEntry(final String fieldName);

    /**
     * test the fields.
     *
     * @return true if all fields are ok
     */
    boolean isFieldSetOkSimple();

    /**
     * test the fields.
     *
     * @return true if all fields are ok
     */
    boolean isFieldSetOk();

    /**
     * equals compares two entries.
     *
     * @param obj entry to compare with entry of this class
     * @return true if both contain the same entries, otherwise false
     */
    boolean equals(final Object obj);

    /**
     * hashCode implementation.
     * @return hash code of the key
     */
    int hashCode();

    /**
     * set up a default entries.
     *
     */
    void setUpDefaultEntry();

    /**
     * equalsEntry compares the head part of two entries.
     *
     * @param vglEntry entry to compare with entry of this class
     * @return true if both contain the same entries, otherwise false
     */
    boolean equalsEntry(final InterfaceDataBase<E> vglEntry);
}

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
 *	Name		Date		Change
 */
package de.knightsoft.DBNavigationBar.client.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * The <code>RiPhoneDomaneHeadDateBase</code> class is a exchange structure
 * between the client and the servlet on the server
 * 
 * @author Manfred Tremmel
 * @version 1.0.0, 2011-02-05
 */
public abstract class DomainHeadDataBase implements Serializable {

	/**
	 * Serial version id
	 */
	private static final long serialVersionUID = -2178996035568385436L;

	private Boolean isReadOnly;
	private String keyMin;
	private String keyMax;
	private String keyCur;

	/**
	 * Constructor, setup a empty entry
	 */
	public DomainHeadDataBase() {
		this.setUpDefaultEntry();
		this.keyMin				=	null;
		this.keyMax				=	null;
	}

	/**
	 * Copy Constructor, creates a new user with the same
	 * entries as the one who's given as parameter
	 * 
	 * @param copyEntry entry to copy
	 */
	public DomainHeadDataBase(DomainHeadDataBase copyEntry) {
		if( copyEntry == null ) {
			this.setUpDefaultEntry();
			this.keyMin					=	null;
			this.keyMax					=	null;
		} else {
			this.isReadOnly				=	copyEntry.isReadOnly;
			this.keyMin					=	copyEntry.keyMin;
			this.keyMax					=	copyEntry.keyMax;
			this.keyCur					=	copyEntry.keyCur;
		}
	}
	
	/**
	 * get isReadOnly
	 * 
     * @return isReadOnly
	 */
	public boolean getIsReadOnly() {
		return this.isReadOnly;
	}

	/**
	 * set isReadOnly
	 * 
     * @param isReadOnly
	 */
	public void setIsReadOnly(boolean isReadOnly) {
		this.isReadOnly = isReadOnly;
	}

	/**
	 * set isReadOnly
	 * 
     * @param isReadOnly
	 */
	public void setIsReadOnly(Boolean isReadOnly) {
		this.isReadOnly = isReadOnly;
	}

	/**
	 * get keyMin
	 * 
     * @return keyMin
	 */
	public String getKeyMin() {
		return this.keyMin;
	}

	/**
	 * set keyMin
	 * 
     * @param keyMin
	 */
	public void setKeyMin(String keyMin) {
		this.keyMin = keyMin;
	}

	/**
	 * get keyMax
	 * 
     * @return keyMax
	 */
	public String getKeyMax() {
		return this.keyMax;
	}

	/**
	 * set keyMax
	 * 
     * @param keyMax
	 */
	public void setKeyMax(String keyMax) {
		this.keyMax = keyMax;
	}

	/**
	 * get keyCur
	 * 
     * @return keyCur
	 */
	public String getKeyCur() {
		return this.keyCur;
	}

	/**
	 * set keyCur
	 * 
     * @param keyCur
	 */
	public void setKeyCur(String keyCur) {
		this.keyCur = keyCur;
	}

	/**
	 * get KeyNew
	 * @return Key of new Entry
	 */
	public abstract String getKeyNew();


	/**
	 * stringEquals compares two String entries
	 * 
	 * @param thisString a string to compare with vglString
	 * @param vglString entry to compare with thisString
	 * @return true if both contain the same entries, otherwise false
	 */
	protected boolean stringEquals(String thisString, String vglString) {
		return (thisString == null ? "" : thisString).equals((vglString == null ? "" : vglString));
	}

	/**
	 * dateEquals compares two Date entries
	 * 
	 * @param thisEntry entry to compare with vglEntry
	 * @param vglEntry entry to compare with thisEntry
	 * @return true if both contain the same entries, otherwise false
	 */
	protected boolean dateEquals(Date thisEntry, Date vglEntry) {
		return (thisEntry == null ? "" : thisEntry).equals((vglEntry == null ? "" : vglEntry));
	}

	/**
	 * equals compares two entries
	 * 
	 * @param vglEntry entry to compare with entry of this class
	 * @return true if both contain the same entries, otherwise false
	 */
	public boolean equals(DomainHeadDataBase vglEntry) {
		boolean isequal		=	true;
		isequal	&=	equalsEntry(vglEntry);
		return isequal;
	}

	/**
	 * equals compares two entries
	 * 
	 * @param obj entry to compare with entry of this class
	 * @return true if both contain the same entries, otherwise false
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!this.getClass().getName().equals(obj.getClass().getName())) {
			return false;
		}
		final DomainHeadDataBase other = (DomainHeadDataBase) obj;
		return this.equals(other);
	}

	/**
	 * equalsEntry compares the head part of two entries
	 * 
	 * @param vglEntry entry to compare with entry of this class
	 * @return true if both contain the same entries, otherwise false
	 */
	public abstract boolean equalsEntry(DomainHeadDataBase vglEntry);

	/**
	 * set up a default entries
	 * 
	 */
	public void setUpDefaultEntry() {
		this.isReadOnly					=	false;
		this.keyCur						=	null;
	}
}

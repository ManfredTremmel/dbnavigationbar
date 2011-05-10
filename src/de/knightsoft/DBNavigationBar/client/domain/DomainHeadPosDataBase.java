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
import java.util.Arrays;


/**
 * 
 * The <code>RiPhoneDomaneHeadPosDataBase</code> class is a exchange structure
 * between the client and the servlet on the server
 * 
 * @author Manfred Tremmel
 * @version 1.0.0, 2011-02-05
 */
public abstract class DomainHeadPosDataBase extends DomainHeadDataBase implements Serializable {

	/**
	 * Serial version id
	 */
	private static final long serialVersionUID = -6242668640945495634L;

	/**
	 * Constructor, setup a empty entry
	 */
	public DomainHeadPosDataBase() {
		super();
	}

	/**
	 * Copy Constructor, creates a new user with the same
	 * entries as the one who's given as parameter
	 * 
	 * @param CopyEntry entry to copy
	 */
	public DomainHeadPosDataBase(DomainHeadPosDataBase CopyEntry) {
		super(CopyEntry);
	}
	/**
	 * get KeyPos
	 * @return Key of Positions
	 */
	public abstract String[] getKeyPos();


	/**
	 * equals compares two entries
	 * 
	 * @param vglEntry entry to compare with entry of this class
	 * @return true if both contain the same entries, otherwise false
	 */
	public boolean equals(DomainHeadPosDataBase vglEntry) {
		boolean isequal		=	super.equals(vglEntry);
		isequal	&=	Arrays.equals(this.getKeyPos(), (vglEntry == null ? null : vglEntry.getKeyPos()));
		return isequal;
	}

	/**
	 * equals compares two entries
	 * 
	 * @param vglEntry entry to compare with entry of this class
	 * @return true if both contain the same entries, otherwise false
	 */
	@Override
	public boolean equals(DomainHeadDataBase vglEntry) {
		return equals((DomainHeadPosDataBase)vglEntry);
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
		final DomainHeadPosDataBase other = (DomainHeadPosDataBase) obj;
		return this.equals(other);
	}

	/**
	 * compare to positions 
	 * @param vgl
	 * @param posthis
	 * @param posvgl
	 * @return true if equal otherwise false
	 */
	public abstract boolean equalsPosition(DomainHeadPosDataBase vgl, int posthis, int posvgl);
}

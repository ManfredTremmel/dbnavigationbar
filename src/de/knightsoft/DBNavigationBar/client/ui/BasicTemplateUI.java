/**
 * This file is part of DBNavigation.
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
package de.knightsoft.DBNavigationBar.client.ui;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;

import de.knightsoft.DBNavigationBar.client.Parent;
import de.knightsoft.DBNavigationBar.client.domain.DomainUser;

/**
 * 
 * The <code>BasicTemplateUI</code> class is a template for all
 * input mask
 * 
 * @author Manfred Tremmel
 * @version 1.0.0, 2011-02-19
 */
public abstract class BasicTemplateUI<F extends Parent> extends Composite {

	protected final F parentwidget;

	protected final char point;
	protected final KeyPressHandler numericKeyPressHandler;
	protected final KeyPressHandler numericPointKeyPressHandler;

	
	/**
	 * Constructor
	 * 
	 * @param parentwidget
	 * 		the parent widget, where this frame is displayed
	 */
	public BasicTemplateUI(
			F parentwidget) {

		this.parentwidget	=	parentwidget;
		char tmpPoint	=	',';
		try {
			tmpPoint = NumberFormat.getFormat("0.00").format(1.1).replaceAll("1", "").replaceAll("0", "").trim().charAt(0);
		} catch( Exception e ) {
			tmpPoint = ',';
		}
		this.point	=	tmpPoint;

	    this.numericKeyPressHandler = new KeyPressHandler() {
	    	@Override
			public void onKeyPress(KeyPressEvent event) {
				int keyCode = (event.getNativeEvent() == null ? 0 : event.getNativeEvent().getKeyCode());
				char charCode	=	event.getCharCode();
				
				switch(keyCode) {
					case KeyCodes.KEY_BACKSPACE:
					case KeyCodes.KEY_DELETE:
					case KeyCodes.KEY_LEFT:
					case KeyCodes.KEY_RIGHT:
					case KeyCodes.KEY_SHIFT:
					case KeyCodes.KEY_TAB:
					case KeyCodes.KEY_ENTER:
					case KeyCodes.KEY_HOME:
					case KeyCodes.KEY_END:
					case KeyCodes.KEY_UP:
					case KeyCodes.KEY_DOWN:
						break;
					default:
						if( event.isControlKeyDown() &&
							(charCode == 'c' || charCode == 'x') ) {
							// Copy or Cut
						} else if( event.isControlKeyDown() &&
							charCode == 'v' ) {
							// Paste, how to verify copied text?
						} else if( !Character.isDigit(charCode) ) {
							((TextBox) event.getSource()).cancelKey();
						}
						break;
				}
			}
		};

	    this.numericPointKeyPressHandler = new KeyPressHandler() {
	    	@Override
			public void onKeyPress(KeyPressEvent event) {
				int keyCode = (event.getNativeEvent() == null ? 0 : event.getNativeEvent().getKeyCode());
				char charCode	=	event.getCharCode();
				
				switch(keyCode) {
					case KeyCodes.KEY_BACKSPACE:
					case KeyCodes.KEY_DELETE:
					case KeyCodes.KEY_LEFT:
					case KeyCodes.KEY_RIGHT:
					case KeyCodes.KEY_SHIFT:
					case KeyCodes.KEY_TAB:
					case KeyCodes.KEY_ENTER:
					case KeyCodes.KEY_HOME:
					case KeyCodes.KEY_END:
					case KeyCodes.KEY_UP:
					case KeyCodes.KEY_DOWN:
						break;
					default:
						if( event.isControlKeyDown() &&
							(charCode == 'c' || charCode == 'x') ) {
							// Copy or Cut
						} else if( event.isControlKeyDown() &&
							charCode == 'v' ) {
							// Paste, how to verify copied text?
						} else if( !Character.isDigit(event.getCharCode()) &&
								BasicTemplateUI.this.point != event.getCharCode()) {
							((TextBox) event.getSource()).cancelKey();
						}
						break;
				}
			}
		};
	}

	/**
	 * <code>setFocusOnFirstWidget</code> set focus to first enabled
	 * input field
	 * 
	 */
	protected abstract void setFocusOnFirstWidget();


    /**
     * The Method <code>getHeaderTitle</code> gets title text
     * 
     * @return menu text
     */
	public abstract String getHeaderTitle();

    /**
     * The Method <code>getMenuText</code> gets menu text
     * 
     * @return menu text
     */
	public abstract String getMenuText();

    /**
     * The Method <code>getMenuEntry</code> gets completely menu entry
     * 
     * @return menu entry
     */
	public abstract String getMenuEntry();

    /**
     * The Method <code>matchesMenu</code> looks if this UI is selected and
     * makes the necessary changes.
     * 
     * @param itemtext
     *            selected menu item
     * @param user
     *            user information about the currently logged in user
     * @return true if it is allowed for this user
     */
	public boolean matchesMenu(
			String itemtext,
			DomainUser user) {
		boolean matches = false;
		if(	itemtext != null &&
			itemtext.indexOf(this.getMenuText()) >= 0 ) {
			//this.myNavigationBar.enableAllButtons();
			this.parentwidget.mainPanel.clear();
			this.parentwidget.mainPanel.add(this);
			this.setFocusOnFirstWidget();
			matches	=	true;
			History.newItem("page=" + this.getMenuText());
		}
		return matches;
	}

    /**
     * The Method <code>allowedToSee</code> tells you if the currently logged
     * in user is allowed to access this application.
     * 
     * @param user
     *            information about logged in user
     * @return true if it is allowed for this user
     */
	public abstract boolean allowedToSee(
			DomainUser user);

    /**
     * The Method <code>allowedToChange</code> tells you if the currently
     * logged in user is allowed to change data in this application.
     * 
     * @param user
     *            information about logged in user
     * @return true if it is allowed for this user
     */
    public abstract boolean allowedToChange(
    		DomainUser user);
}

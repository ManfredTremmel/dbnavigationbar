/**
 * This file is part of DBNavigation.
 *
 * RiPhone is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * RiPhone is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with RiPhone. If not, see <a
 * href="http://www.gnu.org/licenses>http://www.gnu.org/licenses</a>
 *
 *
 * Copyright (c) 2011-2015 Manfred Tremmel
 *
 */

package de.knightsoft.dbnavigationbar.client.ui;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.TextBox;

/**
 * The <code>PhoneKeyPressHandler</code> class is KeyPress Handler Class to limit input to characters allowed in a phone number.
 *
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public class PhoneKeyPressHandler implements KeyPressHandler {

  @Override
  public final void onKeyPress(final KeyPressEvent pEvent) {
    int keyCode = 0;
    if (pEvent.getNativeEvent() != null) {
      keyCode = pEvent.getNativeEvent().getKeyCode();
    }
    final char charCode = pEvent.getCharCode();

    switch (keyCode) {
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
        // Copy, Cut or Paste or numeric input or point
        if (!(pEvent.isControlKeyDown() && (charCode == 'c' || charCode == 'x' || charCode == 'v'))
            && !Character.isDigit(charCode) && ('-' != charCode)) {
          ((TextBox) pEvent.getSource()).cancelKey();
        }
        break;
    }
  }
}

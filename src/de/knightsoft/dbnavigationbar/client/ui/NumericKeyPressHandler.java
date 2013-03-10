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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with RiPhone. If not, see <http://www.gnu.org/licenses/>
 * 
 * 
 * Copyright (c) 2011-2012 Manfred Tremmel
 * 
 */
package de.knightsoft.dbnavigationbar.client.ui;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.TextBox;

/**
 * The <code>NumericKeyPressHandler</code> class is KeyPress Handler
 * Class to limit input to numeric fields.
 * 
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public class NumericKeyPressHandler implements KeyPressHandler
{

  @Override
  public final void onKeyPress(final KeyPressEvent event)
  {
    int keyCode = 0;
    if (event.getNativeEvent() != null)
    {
      keyCode = event.getNativeEvent().getKeyCode();
    }
    final char charCode = event.getCharCode();

    switch (keyCode)
    {
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
        // Copy, Cut or Paste or numeric input
        if (!(event.isControlKeyDown()
            && (charCode == 'c'
            || charCode == 'x'
            || charCode == 'v'))
            && !Character.isDigit(charCode))
        {
          ((TextBox) event.getSource()).cancelKey();
        }
        break;
    }
  }
}

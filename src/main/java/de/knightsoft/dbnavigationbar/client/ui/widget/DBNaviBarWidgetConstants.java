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
package de.knightsoft.dbnavigationbar.client.ui.widget;

import com.google.gwt.i18n.client.Constants;

/**
 * 
 * The <code>DBNavigationBarWidgetConstants</code> class contains the
 * messages for navigation bar widget.
 * 
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public interface DBNaviBarWidgetConstants extends Constants
{

  /**
   * minimum label text.
   * 
   * @return labelMin
   */
  String labelMin();

  /**
   * maximum label text.
   * 
   * @return labelMax
   */
  String labelMax();

  /**
   * current label text.
   * 
   * @return labelCur
   */
  String labelCur();

  /**
   * current access key.
   * 
   * @return currentAccessKey
   */
  String currentAccessKey();

  /**
   * current text.
   * 
   * @return currentMessage
   */
  String currentMessage();

  /**
   * new text.
   * 
   * @return buttonNewMessage
   */
  String buttonNewMessage();

  /**
   * new access key.
   * 
   * @return buttonNewAccessKey
   */
  String buttonNewAccessKey();

  /**
   * save text.
   * 
   * @return buttonSaveMessage
   */
  String buttonSaveMessage();

  /**
   * save access key.
   * 
   * @return buttonSaveAccessKey
   */
  String buttonSaveAccessKey();

  /**
   * delete text.
   * 
   * @return buttonDeleteMessage
   */
  String buttonDeleteMessage();

  /**
   * delete access key.
   * 
   * @return buttonDeleteAccessKey
   */
  String buttonDeleteAccessKey();

  /**
   * find message.
   * 
   * @return buttonFindMessage
   */
  String buttonFindMessage();

  /**
   * find access key.
   * 
   * @return buttonFindAccessKey
   */
  String buttonFindAccessKey();

  /**
   * stop text.
   * 
   * @return buttonStopMessage
   */
  String buttonStopMessage();

  /**
   * stop access key.
   * 
   * @return buttonStopAccessKey
   */
  String buttonStopAccessKey();

  /**
   * fast back text.
   * 
   * @return buttonFBackMessage
   */
  String buttonFBackMessage();

  /**
   * fast back text in find mode.
   * 
   * @return buttonFBackMessageFind
   */
  String buttonFBackMessageFind();

  /**
   * fast back access key.
   * 
   * @return buttonFBackAccessKey
   */
  String buttonFBackAccessKey();

  /**
   * back text.
   * 
   * @return buttonBackMessage
   */
  String buttonBackMessage();

  /**
   * back text in find mode.
   * 
   * @return buttonBackMessageFind
   */
  String buttonBackMessageFind();

  /**
   * back access key.
   * 
   * @return buttonBackAccessKey
   */
  String buttonBackAccessKey();

  /**
   * ok text.
   * 
   * @return buttonOkMessage
   */
  String buttonOkMessage();

  /**
   * ok access key.
   * 
   * @return buttonOkAccessKey
   */
  String buttonOkAccessKey();

  /**
   * forward text.
   * 
   * @return buttonForwardMessage
   */
  String buttonForwardMessage();

  /**
   * forward text in find mode.
   * 
   * @return buttonForwardMessageFind
   */
  String buttonForwardMessageFind();

  /**
   * forward access key.
   * 
   * @return buttonForwardAccessKey
   */
  String buttonForwardAccessKey();

  /**
   * fast forward text.
   * 
   * @return buttonFForwardMessage
   */
  String buttonFForwardMessage();

  /**
   * fast forward text in find mode.
   * 
   * @return buttonFForwardMessageFind
   */
  String buttonFForwardMessageFind();

  /**
   * fast forward access key.
   * 
   * @return buttonFForwardAccessKey
   */
  String buttonFForwardAccessKey();

  /**
   * key for user defined function.
   * 
   * @return buttonUserDefinedAccessKey
   */
  String buttonUserDefinedAccessKey();

  /**
   * header line of the delete dialog box.
   * 
   * @return deleteDialogHeader
   */
  String deleteDialogHeader();

  /**
   * text of the delete dialog box.
   * 
   * @return deleteDialogText
   */
  String deleteDialogText();

  /**
   * text of the yes button.
   * 
   * @return yes
   */
  String yes();

  /**
   * key of the yes button.
   * 
   * @return yesKey
   */
  String yesKey();

  /**
   * text of the no button.
   * 
   * @return no
   */
  String no();

  /**
   * key for the no button.
   * 
   * @return noKey
   */
  String noKey();

  /**
   * find field label text.
   * 
   * @return fieldNameLabel
   */
  String fieldNameLabel();

  /**
   * find field key.
   * 
   * @return findFieldKey
   */
  String findFieldKey();

  /**
   * text of the find entry label.
   * 
   * @return fieldEntryLabel
   */
  String fieldEntryLabel();

  /**
   * find entry key.
   * 
   * @return findEntryKey
   */
  String findEntryKey();

  /**
   * text of find type label.
   * 
   * @return findTypeLabel
   */
  String findTypeLabel();

  /**
   * key to activate find type.
   * 
   * @return findTypeKey
   */
  String findTypeKey();

  /**
   * "equals" text.
   * 
   * @return findEquals
   */
  String findEquals();

  /**
   * "greater" text.
   * 
   * @return findGreater
   */
  String findGreater();

  /**
   * "greater equals" text.
   * 
   * @return findGreaterEquals
   */
  String findGreaterEquals();

  /**
   * "lower equals" text.
   * 
   * @return findLowerEquals
   */
  String findLowerEquals();

  /**
   * "lower" text.
   * 
   * @return findLower
   */
  String findLower();

  /**
   * "contains" text.
   * 
   * @return findContains
   */
  String findContains();
}

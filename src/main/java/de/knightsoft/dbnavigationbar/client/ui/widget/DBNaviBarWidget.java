/**
 * This file is part of DBNavigationBar.
 *
 * DBNavigationBar is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * DBNavigationBar is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with DBNavigationBar. If
 * not, see <a href="http://www.gnu.org/licenses>http://www.gnu.org/licenses</a>
 *
 *
 * Copyright (c) 2011-2015 Manfred Tremmel
 *
 */

package de.knightsoft.dbnavigationbar.client.ui.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.VerticalPanel;

import org.apache.commons.lang3.StringUtils;

/**
 * Database Navigation.
 *
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public class DBNaviBarWidget extends Composite implements HasClickHandlers {

  /**
   * pictures.
   */
  private static final DBNavigationBarImages IMAGES =
      (DBNavigationBarImages) GWT.create(DBNavigationBarImages.class);

  /**
   * search methods small.
   */
  private static final String[] SEARCH_METHODS_SMALL = {"=", ">", ">=", "<=", "<", "like"};
  /**
   * search methods display.
   */
  private static final String[] SEARCH_METHODS = {"=", ">", ">=", "<=", "<", "like"};

  /**
   * spacing.
   */
  private static final int SPACING = 10;
  /**
   * panel style.
   */
  private static final String PANELSTYLE = "PANELSTYLE";

  /**
   * Create a handler for the sendButton and nameField.
   *
   * @author Manfred Tremmel
   */
  class MyHandler implements ClickHandler {

    @Override
    public void onClick(final ClickEvent pEvent) {
      PushButton eventPushButton = null;
      eventPushButton = (PushButton) pEvent.getSource();

      DBNaviBarWidget.this.hintText.setText(StringUtils.EMPTY);

      int fieldSelectedIndex = DBNaviBarWidget.this.fieldSelect.getSelectedIndex();
      if (fieldSelectedIndex < 0) {
        fieldSelectedIndex = 0;
      }
      int searchMethodeSelectedIndex = DBNaviBarWidget.this.searchMethodSelect.getSelectedIndex();
      if (searchMethodeSelectedIndex < 0) {
        searchMethodeSelectedIndex = 0;
      }

      if (DBNaviBarWidget.this.newPushButton.equals(eventPushButton)) {
        DBNaviBarWidget.this.buttonState = ButtonState.NEW;
        DBNaviBarWidget.this.fireEvent(pEvent);
      } else if (DBNaviBarWidget.this.savePushButton.equals(eventPushButton)) {
        DBNaviBarWidget.this.buttonState = ButtonState.SAVE;
        DBNaviBarWidget.this.fireEvent(pEvent);
      } else if (DBNaviBarWidget.this.deletePushButton.equals(eventPushButton)) {
        DBNaviBarWidget.this.dialogYesNoBox.center();
        DBNaviBarWidget.this.dialogYesNoBox.show();
      } else if (DBNaviBarWidget.this.stopPushButton.equals(eventPushButton)) {
        DBNaviBarWidget.this.buttonState = ButtonState.STOP;
        DBNaviBarWidget.this.fireEvent(pEvent);
      } else if (DBNaviBarWidget.this.fbackPushButton.equals(eventPushButton)) {
        DBNaviBarWidget.this.buttonState = ButtonState.FAST_BACK;
        if (DBNaviBarWidget.this.findToggleButton.isDown()) {
          DBNaviBarWidget.this.searchFieldString =
              DBNaviBarWidget.this.searchFieldsRemember[fieldSelectedIndex];
          DBNaviBarWidget.this.searchFieldMethod =
              DBNaviBarWidget.SEARCH_METHODS_SMALL[searchMethodeSelectedIndex];
          DBNaviBarWidget.this.searchFieldEntry = DBNaviBarWidget.this.fieldEntry.getText();
          if (StringUtils.isNotEmpty(DBNaviBarWidget.this.searchFieldEntry)) {
            DBNaviBarWidget.this.buttonState = ButtonState.FAST_BACK_FIND;
          }
        }
        DBNaviBarWidget.this.fireEvent(pEvent);
      } else if (DBNaviBarWidget.this.backPushButton.equals(eventPushButton)) {
        DBNaviBarWidget.this.buttonState = ButtonState.BACK;
        if (DBNaviBarWidget.this.findToggleButton.isDown()) {
          DBNaviBarWidget.this.searchFieldString =
              DBNaviBarWidget.this.searchFieldsRemember[fieldSelectedIndex];
          DBNaviBarWidget.this.searchFieldMethod =
              DBNaviBarWidget.SEARCH_METHODS_SMALL[searchMethodeSelectedIndex];
          DBNaviBarWidget.this.searchFieldEntry = DBNaviBarWidget.this.fieldEntry.getText();
          if (StringUtils.isNotEmpty(DBNaviBarWidget.this.searchFieldEntry)) {
            DBNaviBarWidget.this.buttonState = ButtonState.BACK_FIND;
          }
        }
        DBNaviBarWidget.this.fireEvent(pEvent);
      } else if (DBNaviBarWidget.this.okPushButton.equals(eventPushButton)) {
        DBNaviBarWidget.this.buttonState = ButtonState.CHANGE;
        DBNaviBarWidget.this.currentDBNumber = DBNaviBarWidget.this.currentEntry.getText();
        DBNaviBarWidget.this.fireEvent(pEvent);
      } else if (DBNaviBarWidget.this.forwardPushButton.equals(eventPushButton)) {
        DBNaviBarWidget.this.buttonState = ButtonState.FORWARD;
        if (DBNaviBarWidget.this.findToggleButton.isDown()) {
          DBNaviBarWidget.this.searchFieldString =
              DBNaviBarWidget.this.searchFieldsRemember[fieldSelectedIndex];
          DBNaviBarWidget.this.searchFieldMethod =
              DBNaviBarWidget.SEARCH_METHODS_SMALL[searchMethodeSelectedIndex];
          DBNaviBarWidget.this.searchFieldEntry = DBNaviBarWidget.this.fieldEntry.getText();
          if (StringUtils.isNotEmpty(DBNaviBarWidget.this.searchFieldEntry)) {
            DBNaviBarWidget.this.buttonState = ButtonState.FORWARD_FIND;
          }
        }
        DBNaviBarWidget.this.fireEvent(pEvent);
      } else if (DBNaviBarWidget.this.fforwardPushButton.equals(eventPushButton)) {
        DBNaviBarWidget.this.buttonState = ButtonState.FAST_FORWARD;
        if (DBNaviBarWidget.this.findToggleButton.isDown()) {
          DBNaviBarWidget.this.searchFieldString =
              DBNaviBarWidget.this.searchFieldsRemember[fieldSelectedIndex];
          DBNaviBarWidget.this.searchFieldMethod =
              DBNaviBarWidget.SEARCH_METHODS_SMALL[searchMethodeSelectedIndex];
          DBNaviBarWidget.this.searchFieldEntry = DBNaviBarWidget.this.fieldEntry.getText();
          if (StringUtils.isNotEmpty(DBNaviBarWidget.this.searchFieldEntry)) {
            DBNaviBarWidget.this.buttonState = ButtonState.FAST_FORWARD_FIND;
          }
        }
        DBNaviBarWidget.this.fireEvent(pEvent);
      } else if (DBNaviBarWidget.this.userdefinedPushButton.equals(eventPushButton)) {
        DBNaviBarWidget.this.buttonState = ButtonState.USER_DEFINED;
        DBNaviBarWidget.this.fireEvent(pEvent);
      }
    }
  }

  /**
   * Key up handler.
   *
   * @author Manfred Tremmel
   */
  class MyEntryHandler implements KeyUpHandler {
    @Override
    public void onKeyUp(final KeyUpEvent pEvent) {
      if (pEvent.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
        /**
         * Click Handler.
         *
         * @author Manfred Tremmel
         */
        class OurClickEvent extends ClickEvent {
        }

        DBNaviBarWidget.this.okPushButton.fireEvent(new OurClickEvent());
      }
    }
  }

  /**
   * Search handler.
   *
   * @author Manfred Tremmel
   */
  class MySearchHandler implements KeyUpHandler {
    @Override
    public void onKeyUp(final KeyUpEvent pEvent) {
      if (pEvent.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
        /**
         * Click event.
         *
         * @author Manfred Tremmel
         */
        class OurClickEvent extends ClickEvent {
        }

        if (DBNaviBarWidget.this.fbackPushButton.isEnabled()
            && DBNaviBarWidget.this.fbackPushButton.isVisible()) {
          DBNaviBarWidget.this.fbackPushButton.fireEvent(new OurClickEvent());
        } else {
          DBNaviBarWidget.this.forwardPushButton.fireEvent(new OurClickEvent());
        }
      }
    }
  }

  /**
   * Create a handler for the find toggle button.
   *
   * @author Manfred Tremmel
   */
  private final ClickHandler myFindToggleClick = pEvent -> {
    if (((ToggleButton) pEvent.getSource()).isDown()) {
      DBNaviBarWidget.this.searchpanel.setVisible(true);
      DBNaviBarWidget.this.fbackPushButton
          .setTitle(DBNaviBarWidget.this.constants.buttonFBackMessageFind());
      DBNaviBarWidget.this.backPushButton
          .setTitle(DBNaviBarWidget.this.constants.buttonBackMessageFind());
      DBNaviBarWidget.this.forwardPushButton
          .setTitle(DBNaviBarWidget.this.constants.buttonForwardMessageFind());
      DBNaviBarWidget.this.fforwardPushButton
          .setTitle(DBNaviBarWidget.this.constants.buttonFForwardMessageFind());
    } else {
      DBNaviBarWidget.this.searchpanel.setVisible(false);
      DBNaviBarWidget.this.fbackPushButton
          .setTitle(DBNaviBarWidget.this.constants.buttonFBackMessage());
      DBNaviBarWidget.this.backPushButton
          .setTitle(DBNaviBarWidget.this.constants.buttonBackMessage());
      DBNaviBarWidget.this.forwardPushButton
          .setTitle(DBNaviBarWidget.this.constants.buttonForwardMessage());
      DBNaviBarWidget.this.fforwardPushButton
          .setTitle(DBNaviBarWidget.this.constants.buttonFForwardMessage());
    }
  };

  /**
   * Different button states.
   *
   * @author Manfred Tremmel
   */
  public enum ButtonState {
    /**
     * New.
     */
    NEW,
    /**
     * Save.
     */
    SAVE,
    /**
     * Delete.
     */
    DELETE,
    /**
     * Stop.
     */
    STOP,
    /**
     * Fast back.
     */
    FAST_BACK,
    /**
     * Fast back find.
     */
    FAST_BACK_FIND,
    /**
     * Back.
     */
    BACK,
    /**
     * Back find.
     */
    BACK_FIND,
    /**
     * Change.
     */
    CHANGE,
    /**
     * Forward.
     */
    FORWARD,
    /**
     * Forward find.
     */
    FORWARD_FIND,
    /**
     * Fast forward.
     */
    FAST_FORWARD,
    /**
     * Fast forward find.
     */
    FAST_FORWARD_FIND,
    /**
     * User defined.
     */
    USER_DEFINED;
  }

  /**
   * pictures.
   *
   * @author Manfred Tremmel
   */
  public interface DBNavigationBarImages extends ClientBundle {
    /**
     * new button picture.
     *
     * @return image
     */
    @Source("filenew.png")
    ImageResource filenew();

    /**
     * save button picture.
     *
     * @return image
     */
    @Source("save.png")
    ImageResource save();

    /**
     * delete button picture.
     *
     * @return image
     */
    @Source("delete.png")
    ImageResource delete();

    /**
     * find button picture.
     *
     * @return image
     */
    @Source("find.png")
    ImageResource find();

    /**
     * stop button picture.
     *
     * @return image
     */
    @Source("stop.png")
    ImageResource stop();

    /**
     * fast back button picture.
     *
     * @return image
     */
    @Source("fback.png")
    ImageResource fback();

    /**
     * back button picture.
     *
     * @return image
     */
    @Source("back.png")
    ImageResource back();

    /**
     * ok button picture.
     *
     * @return image
     */
    @Source("ok.png")
    ImageResource ok(); // NOPMD

    /**
     * forward button picture.
     *
     * @return image
     */
    @Source("forward.png")
    ImageResource forward();

    /**
     * fast forward button picture.
     *
     * @return image
     */
    @Source("fforward.png")
    ImageResource fforward();

    /**
     * user defined button picture.
     *
     * @return image
     */
    @Source("userdefined.png")
    ImageResource userdefined();
  }

  /**
   * yes/no dialog box.
   */
  private final DialogBox dialogYesNoBox;

  /**
   * button state.
   */
  private ButtonState buttonState;
  /**
   * search field string.
   */
  private String searchFieldString;
  /**
   * search field entry.
   */
  private String searchFieldEntry;
  /**
   * search field method.
   */
  private String searchFieldMethod;

  /**
   * current database number.
   */
  private String currentDBNumber;
  /**
   * old database number.
   */
  private String oldDBNumber;
  /**
   * lowest database number.
   */
  private String minDBNumber;
  /**
   * highest database number.
   */
  private String maxDBNumber;

  /**
   * big panel.
   */
  private final VerticalPanel bigpanel = new VerticalPanel();
  /**
   * panel.
   */
  private final HorizontalPanel panel = new HorizontalPanel();
  /**
   * search panel.
   */
  private final HorizontalPanel searchpanel = new HorizontalPanel();
  /**
   * search grid panel.
   */
  private final Grid searchgridpanel = new Grid(2, 3);

  /**
   * new button.
   */
  private final PushButton newPushButton =
      new PushButton(new Image(DBNaviBarWidget.IMAGES.filenew()));
  /**
   * save button.
   */
  private final PushButton savePushButton =
      new PushButton(new Image(DBNaviBarWidget.IMAGES.save()));
  /**
   * delete button.
   */
  private final PushButton deletePushButton =
      new PushButton(new Image(DBNaviBarWidget.IMAGES.delete()));
  /**
   * find button.
   */
  private final ToggleButton findToggleButton =
      new ToggleButton(new Image(DBNaviBarWidget.IMAGES.find()));
  /**
   * stop button.
   */
  private final PushButton stopPushButton =
      new PushButton(new Image(DBNaviBarWidget.IMAGES.stop()));
  /**
   * fast back button.
   */
  private final PushButton fbackPushButton =
      new PushButton(new Image(DBNaviBarWidget.IMAGES.fback()));
  /**
   * back button.
   */
  private final PushButton backPushButton =
      new PushButton(new Image(DBNaviBarWidget.IMAGES.back()));
  /**
   * min/max grid.
   */
  private final Grid minmaxcurpanel = new Grid(3, 2);

  /**
   * min label.
   */
  private final Label minLabel = new Label();
  /**
   * min entry.
   */
  private final Label minEntry = new Label();
  /**
   * max label.
   */
  private final Label maxLabel = new Label();
  /**
   * max entry.
   */
  private final Label maxEntry = new Label();
  /**
   * current database number label.
   */
  private final Label currentLabel = new Label();
  /**
   * current database number.
   */
  private final TextBox currentEntry = new TextBox();
  /**
   * ok button.
   */
  private final PushButton okPushButton = new PushButton(new Image(DBNaviBarWidget.IMAGES.ok()));
  /**
   * forward button.
   */
  private final PushButton forwardPushButton =
      new PushButton(new Image(DBNaviBarWidget.IMAGES.forward()));
  /**
   * fast forward button.
   */
  private final PushButton fforwardPushButton =
      new PushButton(new Image(DBNaviBarWidget.IMAGES.fforward()));
  /**
   * user defined button.
   */
  private final PushButton userdefinedPushButton =
      new PushButton(new Image(DBNaviBarWidget.IMAGES.userdefined()));
  /**
   * hint text.
   */
  private final Label hintText = new Label();

  /**
   * search field select label.
   */
  private final Label fieldSelectLabel = new Label();
  /**
   * search field select.
   */
  private final ListBox fieldSelect = new ListBox();
  /**
   * search entry label.
   */
  private final Label fieldEntryLabel = new Label();
  /**
   * search entry.
   */
  private final TextBox fieldEntry = new TextBox();
  /**
   * search method.
   */
  private final ListBox searchMethodSelect = new ListBox();
  /**
   * search methods label.
   */
  private final Label searchMethodsLabel = new Label();

  /**
   * new button is enabled.
   */
  private boolean newPushButtonEnabled = true;
  /**
   * save button is enabled.
   */
  private boolean savePushButtonEnabled = true;
  /**
   * delete button is enabled.
   */
  private boolean deletePushButtonEnabled = true;
  /**
   * find button is enabled.
   */
  private boolean findPushButtonEnabled = true;
  /**
   * stop button is enabled.
   */
  private boolean stopPushButtonEnabled = true;
  /**
   * fast back button is enabled.
   */
  private boolean fbackPushButtonEnabled = true;
  /**
   * back button is enabled.
   */
  private boolean backPushButtonEnabled = true;
  /**
   * current entry is enabled.
   */
  private boolean currentEntryEnabled = true;
  /**
   * ok button is enabled.
   */
  private boolean okPushButtonEnabled = true;
  /**
   * forward button is enabled.
   */
  private boolean forwardPushButtonEnabled = true;
  /**
   * fast forward button is enabled.
   */
  private boolean fforwardPushButtonEnabled = true;
  /**
   * user defined button is enabled.
   */
  private boolean userdefinedPushButtonEnabled = true;
  /**
   * min/max is enabled.
   */
  private boolean minMaxEnabled = true;

  /**
   * search fields.
   */
  private final String[] searchFieldsRemember;

  /**
   * constants.
   */
  private final DBNaviBarWidgetConstants constants = GWT.create(DBNaviBarWidgetConstants.class);

  /**
   * Constructor.
   *
   * @param pSearchfields fields to search for
   * @param pSearchFieldsDisplay fields to search for display names
   * @param pUserdefinedText user defined functions (null if not wanted)
   */
  public DBNaviBarWidget(final String[] pSearchfields, final String[] pSearchFieldsDisplay,
      final String pUserdefinedText) {
    super();

    this.dialogYesNoBox = this.createYesNoDialogBox(this.constants);
    this.dialogYesNoBox.hide();

    this.searchFieldsRemember = pSearchfields;

    this.initWidget(this.bigpanel);
    this.bigpanel.setBorderWidth(1);
    this.bigpanel.setStyleName(DBNaviBarWidget.PANELSTYLE);
    this.panel.setSpacing(DBNaviBarWidget.SPACING);
    this.minLabel.setText(this.constants.labelMin());
    this.minEntry.setText(StringUtils.EMPTY);
    this.maxLabel.setText(this.constants.labelMax());
    this.maxEntry.setText(StringUtils.EMPTY);
    this.currentLabel.setText(this.constants.labelCur());
    this.currentEntry.setText(StringUtils.EMPTY);
    this.hintText.setText(StringUtils.EMPTY);

    DBNaviBarWidget.SEARCH_METHODS[0] = this.constants.findEquals();
    DBNaviBarWidget.SEARCH_METHODS[1] = this.constants.findGreater();
    DBNaviBarWidget.SEARCH_METHODS[2] = this.constants.findGreaterEquals();
    DBNaviBarWidget.SEARCH_METHODS[3] = this.constants.findLowerEquals();
    DBNaviBarWidget.SEARCH_METHODS[4] = this.constants.findLower();
    DBNaviBarWidget.SEARCH_METHODS[5] = this.constants.findContains();

    this.newPushButton.setAccessKey(this.constants.buttonNewAccessKey().trim().charAt(0));
    this.newPushButton.setTitle(this.constants.buttonNewMessage());

    this.savePushButton.setAccessKey(this.constants.buttonSaveAccessKey().trim().charAt(0));
    this.savePushButton.setTitle(this.constants.buttonSaveMessage());

    this.deletePushButton.setAccessKey(this.constants.buttonDeleteAccessKey().trim().charAt(0));
    this.deletePushButton.setTitle(this.constants.buttonDeleteMessage());

    this.findToggleButton.setAccessKey(this.constants.buttonFindAccessKey().trim().charAt(0));
    this.findToggleButton.setTitle(this.constants.buttonFindMessage());
    this.findToggleButton.setDown(false);

    this.stopPushButton.setAccessKey(this.constants.buttonStopAccessKey().trim().charAt(0));
    this.stopPushButton.setTitle(this.constants.buttonStopMessage());

    this.fbackPushButton.setAccessKey(this.constants.buttonFBackAccessKey().trim().charAt(0));
    this.fbackPushButton.setTitle(this.constants.buttonFBackMessage());

    this.backPushButton.setAccessKey(this.constants.buttonBackAccessKey().trim().charAt(0));
    this.backPushButton.setTitle(this.constants.buttonBackMessage());

    this.currentEntry.setAccessKey(this.constants.currentAccessKey().trim().charAt(0));
    this.currentEntry.setTitle(this.constants.currentMessage());

    this.okPushButton.setAccessKey(this.constants.buttonOkAccessKey().trim().charAt(0));
    this.okPushButton.setTitle(this.constants.buttonOkMessage());

    this.forwardPushButton.setAccessKey(this.constants.buttonForwardAccessKey().trim().charAt(0));
    this.forwardPushButton.setTitle(this.constants.buttonForwardMessage());

    this.fforwardPushButton.setAccessKey(this.constants.buttonFForwardAccessKey().trim().charAt(0));
    this.fforwardPushButton.setTitle(this.constants.buttonFForwardMessage());

    this.userdefinedPushButton
        .setAccessKey(this.constants.buttonUserDefinedAccessKey().trim().charAt(0));

    this.fieldSelectLabel.setText(this.constants.fieldNameLabel());
    this.fieldEntryLabel.setText(this.constants.fieldEntryLabel());
    if (pSearchfields != null && pSearchFieldsDisplay != null) {
      for (int i = 0; i < pSearchfields.length && i < pSearchFieldsDisplay.length; i++) {
        this.fieldSelect.addItem(pSearchFieldsDisplay[i], pSearchfields[i]);
      }
    }
    this.searchMethodsLabel.setText(this.constants.findTypeLabel());
    for (final String element2 : DBNaviBarWidget.SEARCH_METHODS) {
      this.searchMethodSelect.addItem(element2);
    }
    this.fieldSelect.setAccessKey(this.constants.findFieldKey().trim().charAt(0));
    this.searchMethodSelect.setAccessKey(this.constants.findTypeKey().trim().charAt(0));
    this.fieldEntry.setAccessKey(this.constants.findEntryKey().trim().charAt(0));

    // this.searchgridpanel.setHeight("2.5em");
    this.searchpanel.setVisible(this.findToggleButton.isDown());

    if (pUserdefinedText == null) {
      this.userdefinedPushButton.setEnabled(false);
      this.userdefinedPushButtonEnabled = false;
    } else {
      this.userdefinedPushButton.setTitle(pUserdefinedText);
    }
    this.setDBMinMaxCurNumber(null, null, null);

    final MyHandler handler = new MyHandler();
    final MyEntryHandler entryhandler = new MyEntryHandler();
    final MySearchHandler searchhandler = new MySearchHandler();
    this.newPushButton.addClickHandler(handler);
    this.savePushButton.addClickHandler(handler);
    this.deletePushButton.addClickHandler(handler);
    this.findToggleButton.addClickHandler(this.myFindToggleClick);
    this.stopPushButton.addClickHandler(handler);
    this.fbackPushButton.addClickHandler(handler);
    this.backPushButton.addClickHandler(handler);
    this.currentEntry.addKeyUpHandler(entryhandler);
    this.okPushButton.addClickHandler(handler);
    this.forwardPushButton.addClickHandler(handler);
    this.fforwardPushButton.addClickHandler(handler);
    this.userdefinedPushButton.addClickHandler(handler);
    this.fieldEntry.addKeyUpHandler(searchhandler);
    this.layoutComponents();
  }

  /**
   * Constructor.
   *
   * @param pSearchfields fields to search for
   * @param pUserdefinedText user defined text
   */
  public DBNaviBarWidget(final String[] pSearchfields, final String pUserdefinedText) {
    this(pSearchfields, pSearchfields, pUserdefinedText);
  }

  /**
   * Constructor.
   *
   * @param pSearchfields fields to search for
   * @param pSearchFieldsDisplay fields to search for display names
   */
  public DBNaviBarWidget(final String[] pSearchfields, final String[] pSearchFieldsDisplay) {
    this(pSearchfields, pSearchFieldsDisplay, null);
  }

  /**
   * Constructor.
   *
   * @param pSearchfields fields to search for
   */
  public DBNaviBarWidget(final String[] pSearchfields) {
    this(pSearchfields, pSearchfields, null);
  }

  /**
   * layout components.
   */
  private void layoutComponents() {
    this.panel.add(this.newPushButton);
    this.panel.add(this.savePushButton);
    this.panel.add(this.deletePushButton);
    this.panel.add(this.findToggleButton);
    this.panel.add(this.stopPushButton);
    this.panel.add(this.fbackPushButton);
    this.panel.add(this.backPushButton);
    this.minmaxcurpanel.setWidget(0, 0, this.minLabel);
    this.minmaxcurpanel.setWidget(0, 1, this.minEntry);
    this.minmaxcurpanel.setWidget(1, 0, this.maxLabel);
    this.minmaxcurpanel.setWidget(1, 1, this.maxEntry);
    this.minmaxcurpanel.setWidget(2, 0, this.currentLabel);
    this.minmaxcurpanel.setWidget(2, 1, this.currentEntry);
    // this.minpanel.add(this.minLabel);
    // this.minpanel.add(this.minEntry);
    // this.maxpanel.add(this.maxLabel);
    // this.maxpanel.add(this.maxEntry);
    // this.curpanel.add(this.currentLabel);
    // this.curpanel.add(this.currentEntry);
    // this.minmaxcurpanel.add(this.minpanel);
    // this.minmaxcurpanel.add(this.maxpanel);
    // this.minmaxcurpanel.add(this.curpanel);
    this.panel.add(this.minmaxcurpanel);
    this.panel.add(this.okPushButton);
    this.panel.add(this.forwardPushButton);
    this.panel.add(this.fforwardPushButton);
    this.panel.add(this.userdefinedPushButton);
    this.searchgridpanel.setWidget(0, 0, this.fieldSelectLabel);
    this.searchgridpanel.setWidget(0, 1, this.searchMethodsLabel);
    this.searchgridpanel.setWidget(0, 2, this.fieldEntryLabel);
    this.searchgridpanel.setWidget(1, 0, this.fieldSelect);
    this.searchgridpanel.setWidget(1, 1, this.searchMethodSelect);
    this.searchgridpanel.setWidget(1, 2, this.fieldEntry);
    this.searchpanel.add(this.searchgridpanel);
    /*
     * VerticalPanel Searchfield = new VerticalPanel(); Searchfield.add(this.fieldSelectLabel);
     * Searchfield.add(this.fieldSelect); VerticalPanel Searchmethodes = new VerticalPanel();
     * Searchmethodes.add(this.SearchMethodesLabel); Searchmethodes.add(this.searchMethodeSelect);
     * VerticalPanel Searchentry = new VerticalPanel(); Searchentry.add(this.fieldEntryLabel);
     * Searchentry.add(this.fieldEntry); this.searchpanel.add(Searchfield);
     * this.searchpanel.add(Searchmethodes); this.searchpanel.add(Searchentry);
     */
    this.bigpanel.add(this.panel);
    this.bigpanel.add(this.searchpanel);
    this.bigpanel.add(this.hintText);
  }

  /**
   * get the constants.
   *
   * @return constants
   */
  public final DBNaviBarWidgetConstants getConstants() {
    return this.constants;
  }

  /**
   * get button state.
   *
   * @return buttonState
   */
  public final ButtonState getButtonState() {
    return this.buttonState;
  }

  /**
   * get current database number.
   *
   * @return currentDBNumber
   */
  public final String getCurrentDBNumber() {
    return this.currentDBNumber;
  }

  /**
   * get old database number.
   *
   * @return oldDBNumber
   */
  public final String getOldDBNumber() {
    return this.oldDBNumber;
  }

  /**
   * get search field.
   *
   * @return searchFieldString
   */
  public final String getSearchFieldField() {
    return this.searchFieldString;
  }

  /**
   * get search field entry.
   *
   * @return searchFieldEntry
   */
  public final String getSearchFieldEntry() {
    return this.searchFieldEntry;
  }

  /**
   * get search field method.
   *
   * @return searchFieldMethod
   */
  public final String getSearchFieldMethode() {
    return this.searchFieldMethod;
  }

  /**
   * display hint.
   *
   * @param pHint text to display
   */
  public final void displayHint(final String pHint) {
    this.hintText.setText(pHint);
  }

  /**
   * enable all buttons.
   */
  public final void enableAllButtons() {
    this.newPushButton.setEnabled(true);
    this.savePushButton.setEnabled(true);
    this.deletePushButton.setEnabled(true);
    this.findToggleButton.setEnabled(true);
    this.stopPushButton.setEnabled(true);
    this.fbackPushButton.setEnabled(true);
    this.backPushButton.setEnabled(true);
    this.currentEntry.setEnabled(true);
    this.okPushButton.setEnabled(true);
    this.forwardPushButton.setEnabled(true);
    this.fforwardPushButton.setEnabled(true);
    this.userdefinedPushButton.setEnabled(true);
  }

  /**
   * refresh button state.
   */
  private void refreshButtonState() {
    this.panel.setVisible(false);
    if (this.minDBNumber == null || this.maxDBNumber == null) {
      this.newPushButton.setEnabled(false);
      this.savePushButton.setEnabled(this.savePushButtonEnabled);
      this.deletePushButton.setEnabled(false);
      this.findToggleButton.setEnabled(false);
      this.stopPushButton.setEnabled(false);
      this.fbackPushButton.setEnabled(false);
      this.backPushButton.setEnabled(false);
      this.currentEntry.setEnabled(false);
      this.okPushButton.setEnabled(false);
      this.forwardPushButton.setEnabled(false);
      this.fforwardPushButton.setEnabled(false);
      this.userdefinedPushButton.setEnabled(this.userdefinedPushButtonEnabled);
    } else {
      if (this.oldDBNumber == null) {
        this.newPushButton.setEnabled(false);
        this.deletePushButton.setEnabled(false);
        this.savePushButton.setEnabled(this.savePushButtonEnabled);

        this.findToggleButton.setEnabled(this.findPushButtonEnabled);
        this.stopPushButton.setEnabled(false);
        this.fbackPushButton.setEnabled(this.fbackPushButtonEnabled);
        this.backPushButton.setEnabled(this.backPushButtonEnabled);

        this.currentEntry.setEnabled(this.currentEntryEnabled);
        this.okPushButton.setEnabled(this.okPushButtonEnabled);
        this.forwardPushButton.setEnabled(false);
        this.fforwardPushButton.setEnabled(false);
        this.userdefinedPushButton.setEnabled(this.userdefinedPushButtonEnabled);
      } else {
        this.newPushButton.setEnabled(this.newPushButtonEnabled);
        if (this.newPushButtonEnabled) {
          this.newPushButton.getUpFace().setImage(new Image(DBNaviBarWidget.IMAGES.filenew()));
        }
        this.savePushButton.setEnabled(this.savePushButtonEnabled);
        if (this.savePushButtonEnabled) {
          this.savePushButton.getUpFace().setImage(new Image(DBNaviBarWidget.IMAGES.save()));
        }
        this.deletePushButton.setEnabled(this.deletePushButtonEnabled);
        if (this.deletePushButtonEnabled) {
          this.deletePushButton.getUpFace().setImage(new Image(DBNaviBarWidget.IMAGES.delete()));
        }
        this.findToggleButton.setEnabled(this.findPushButtonEnabled);
        if (this.findPushButtonEnabled) {
          this.findToggleButton.getUpFace().setImage(new Image(DBNaviBarWidget.IMAGES.find()));
        }
        this.stopPushButton.setEnabled(this.stopPushButtonEnabled);
        if (this.stopPushButtonEnabled) {
          this.stopPushButton.getUpFace().setImage(new Image(DBNaviBarWidget.IMAGES.stop()));
        }

        if (this.oldDBNumber.equals(this.minDBNumber)) {
          this.fbackPushButton.setEnabled(false);
          this.backPushButton.setEnabled(false);
        } else {
          this.fbackPushButton.setEnabled(this.fbackPushButtonEnabled);
          this.backPushButton.setEnabled(this.backPushButtonEnabled);
          if (this.fbackPushButtonEnabled) {
            this.fbackPushButton.getUpFace().setImage(new Image(DBNaviBarWidget.IMAGES.fback()));
          }
          if (this.backPushButtonEnabled) {
            this.backPushButton.getUpFace().setImage(new Image(DBNaviBarWidget.IMAGES.back()));
          }
        }
        this.currentEntry.setEnabled(this.currentEntryEnabled);
        this.okPushButton.setEnabled(this.okPushButtonEnabled);
        if (this.okPushButtonEnabled) {
          this.okPushButton.getUpFace().setImage(new Image(DBNaviBarWidget.IMAGES.ok()));
        }
        if (this.oldDBNumber.equals(this.maxDBNumber)) {
          this.forwardPushButton.setEnabled(false);
          this.fforwardPushButton.setEnabled(false);
        } else {
          this.forwardPushButton.setEnabled(this.forwardPushButtonEnabled);
          this.fforwardPushButton.setEnabled(this.fforwardPushButtonEnabled);
          if (this.forwardPushButtonEnabled) {
            this.forwardPushButton.getUpFace()
                .setImage(new Image(DBNaviBarWidget.IMAGES.forward()));
          }
          if (this.fforwardPushButtonEnabled) {
            this.fforwardPushButton.getUpFace()
                .setImage(new Image(DBNaviBarWidget.IMAGES.fforward()));
          }
        }
        this.userdefinedPushButton.setEnabled(this.userdefinedPushButtonEnabled);
      }
    }
    this.minLabel.setVisible(this.minMaxEnabled);
    this.minEntry.setVisible(this.minMaxEnabled);
    this.maxLabel.setVisible(this.minMaxEnabled);
    this.maxEntry.setVisible(this.minMaxEnabled);
    this.panel.setVisible(true);
  }

  /**
   * set database min max and current number.
   *
   * @param pMin minimum number
   * @param pMax maximum number
   * @param pCur current number
   */
  public final void setDBMinMaxCurNumber(final String pMin, final String pMax, final String pCur) {
    this.currentDBNumber = pCur;
    this.oldDBNumber = pCur;
    this.minDBNumber = pMin;
    this.maxDBNumber = pMax;
    this.minEntry.setText(pMin);
    this.maxEntry.setText(pMax);
    this.currentEntry.setText(pCur);

    this.refreshButtonState();
  }

  /**
   * change search fields.
   *
   * @param pSearchFields new search fields
   */
  public final void changeSearchfields(final String[] pSearchFields) {
    if (pSearchFields != null && (this.fieldSelect.getItemCount() != pSearchFields.length
        || this.fieldSelect.getItemText(0) != pSearchFields[0])) {

      for (int j = this.fieldSelect.getItemCount() - 1; j >= 0; j--) {
        this.fieldSelect.removeItem(j);
      }

      for (int i = 0; i < pSearchFields.length; i++) {
        this.fieldSelect.addItem(pSearchFields[i]);
        if (pSearchFields[i].equals(this.searchFieldString)) {
          this.fieldSelect.setSelectedIndex(i);
        }
      }
    }
  }

  /**
   * set read only.
   */
  public final void setReadOnly() {
    this.newPushButtonEnabled = false;
    this.deletePushButtonEnabled = false;
    this.savePushButtonEnabled = false;
    this.refreshButtonState();
  }

  /**
   * set to read/write.
   */
  public final void setReadWrite() {
    this.newPushButtonEnabled = true;
    this.deletePushButtonEnabled = true;
    this.savePushButtonEnabled = true;
    this.refreshButtonState();
  }

  /**
   * enable new button.
   */
  public final void enablenewPushButton() {
    this.newPushButtonEnabled = true;
    this.refreshButtonState();
  }

  /**
   * disable new button.
   */
  public final void disablenewPushButton() {
    this.newPushButtonEnabled = false;
    this.refreshButtonState();
  }

  /**
   * enable save button.
   */
  public final void enablesavePushButton() {
    this.savePushButtonEnabled = true;
    this.refreshButtonState();
  }

  /**
   * disable save button.
   */
  public final void disablesavePushButton() {
    this.savePushButtonEnabled = false;
    this.refreshButtonState();
  }

  /**
   * enable delete button.
   */
  public final void enabledeletePushButton() {
    this.deletePushButtonEnabled = true;
    this.refreshButtonState();
  }

  /**
   * disable delete button.
   */
  public final void disabledeletePushButton() {
    this.deletePushButtonEnabled = false;
    this.refreshButtonState();
  }

  /**
   * enable find button.
   */
  public final void enablefindPushButton() {
    this.findPushButtonEnabled = true;
    this.refreshButtonState();
  }

  /**
   * disable find button.
   */
  public final void disablefindPushButton() {
    this.findPushButtonEnabled = false;
    this.refreshButtonState();
  }

  /**
   * enable stop button.
   */
  public final void enablestopPushButton() {
    this.stopPushButtonEnabled = true;
    this.refreshButtonState();
  }

  /**
   * disable stop button.
   */
  public final void disablestopPushButton() {
    this.stopPushButtonEnabled = false;
    this.refreshButtonState();
  }

  /**
   * enable fast back button.
   */
  public final void enablefbackPushButton() {
    this.fbackPushButtonEnabled = true;
    this.refreshButtonState();
  }

  /**
   * disable fast back button.
   */
  public final void disablefbackPushButton() {
    this.fbackPushButtonEnabled = false;
    this.refreshButtonState();
  }

  /**
   * enable back button.
   */
  public final void enablebackPushButton() {
    this.backPushButtonEnabled = true;
    this.refreshButtonState();
  }

  /**
   * disable back button.
   */
  public final void disablebackPushButton() {
    this.backPushButtonEnabled = false;
    this.refreshButtonState();
  }

  /**
   * enable current entry.
   */
  public final void enablecurrentEntry() {
    this.currentEntryEnabled = true;
    this.refreshButtonState();
  }

  /**
   * disable current entry.
   */
  public final void disablecurrentEntry() {
    this.currentEntryEnabled = false;
    this.refreshButtonState();
  }

  /**
   * enable ok button.
   */
  public final void enableokPushButton() {
    this.okPushButtonEnabled = true;
    this.refreshButtonState();
  }

  /**
   * disable ok button.
   */
  public final void disableokPushButton() {
    this.okPushButtonEnabled = false;
    this.refreshButtonState();
  }

  /**
   * enable forward button.
   */
  public final void enableforwardPushButton() {
    this.forwardPushButtonEnabled = true;
    this.refreshButtonState();
  }

  /**
   * disable forward button.
   */
  public final void disableforwardPushButton() {
    this.forwardPushButtonEnabled = false;
    this.refreshButtonState();
  }

  /**
   * enable fast forward button.
   */
  public final void enablefforwardPushButton() {
    this.fforwardPushButtonEnabled = true;
    this.refreshButtonState();
  }

  /**
   * disable fast forward button.
   */
  public final void disablefforwardPushButton() {
    this.fforwardPushButtonEnabled = false;
    this.refreshButtonState();
  }

  /**
   * enable user defined button.
   */
  public final void enableUserdefinedPushButtonEnabled() {
    this.userdefinedPushButtonEnabled = true;
    this.refreshButtonState();
  }

  /**
   * disable user defined button.
   */
  public final void disableUserdefinedPushButtonEnabled() {
    this.userdefinedPushButtonEnabled = false;
    this.refreshButtonState();
  }

  /**
   * enable min/max field.
   */
  public final void enableMinMax() {
    this.minMaxEnabled = true;
    this.refreshButtonState();
  }

  /**
   * disable min/max field.
   */
  public final void disableMinMax() {
    this.minMaxEnabled = false;
    this.refreshButtonState();
  }

  /**
   * set a new entry.
   */
  public final void setNewEntry() {
    this.currentDBNumber = null;
    this.oldDBNumber = null;
    this.currentEntry.setText(null);

    this.refreshButtonState();
  }

  /**
   * create yes/no dialog box.
   *
   * @param pConstants texts for box
   * @return DialogBox
   */
  private DialogBox createYesNoDialogBox(final DBNaviBarWidgetConstants pConstants) {
    // Create a dialog box and set the caption text
    final DialogBox dialogBox = new DialogBox();
    dialogBox.ensureDebugId("yesNoDialogBox");
    dialogBox.setText(pConstants.deleteDialogHeader());

    // Create a table to layout the content
    final VerticalPanel dialogContents = new VerticalPanel();
    dialogBox.setWidget(dialogContents);

    // Add some text to the top of the dialog
    final HTML details = new HTML(pConstants.deleteDialogText());
    dialogContents.add(details);
    dialogContents.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

    final HorizontalPanel dialogButtons = new HorizontalPanel();
    // Add a yes button at the bottom of the dialog
    final Button yesButton = new Button(pConstants.yes(), (ClickHandler) pEvent -> {
      dialogBox.hide();
      DBNaviBarWidget.this.buttonState = ButtonState.DELETE;
      DBNaviBarWidget.this.fireEvent(pEvent);
    });
    yesButton.setAccessKey(pConstants.yesKey().trim().charAt(0));
    dialogButtons.add(yesButton);
    dialogButtons.setCellHorizontalAlignment(yesButton, HasHorizontalAlignment.ALIGN_LEFT);
    // Add a no button at the bottom of the dialog
    final Button noButton = new Button(pConstants.no(), (ClickHandler) pEvent -> dialogBox.hide());
    noButton.setAccessKey(pConstants.noKey().trim().charAt(0));
    dialogButtons.add(noButton);
    dialogButtons.setCellHorizontalAlignment(noButton, HasHorizontalAlignment.ALIGN_RIGHT);
    dialogButtons.setWidth("80%");
    dialogContents.add(dialogButtons);

    // Return the dialog box
    return dialogBox;
  }

  /**
   * add a click handler.
   *
   * @param pHandler handler to set
   * @return handler registration
   */
  @Override
  public final HandlerRegistration addClickHandler(final ClickHandler pHandler) {
    return super.addHandler(pHandler, ClickEvent.getType());
  }

  /**
   * add a key up handler.
   *
   * @param pHandler key up handler
   * @return handler registration
   */
  public final HandlerRegistration addKeyUpHandler(final KeyUpHandler pHandler) {
    return super.addHandler(pHandler, KeyUpEvent.getType());
  }
}

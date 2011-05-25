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
package de.knightsoft.DBNavigationBar.client.ui.widget;

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

public class DBNavigationBarWidget extends Composite implements HasClickHandlers {

	public static enum ButtonState {
		NEW,
		SAVE,
		DELETE,
		STOP,
		FAST_BACK,
		FAST_BACK_FIND,
		BACK,
		BACK_FIND,
		CHANGE,
		FORWARD,
		FORWARD_FIND,
		FAST_FORWARD,
		FAST_FORWARD_FIND,
		USER_DEFINED;
	}

	private static final DBNavigationBarImages images = (DBNavigationBarImages) GWT.create(DBNavigationBarImages.class);
	
	public interface DBNavigationBarImages extends ClientBundle {
		@Source("filenew.png")
		ImageResource filenew();

		@Source("save.png")
		ImageResource save();

		@Source("delete.png")
		ImageResource delete();

		@Source("find.png")
		ImageResource find();

		@Source("stop.png")
		ImageResource stop();

		@Source("fback.png")
		ImageResource fback();

		@Source("back.png")
		ImageResource back();

		@Source("ok.png")
		ImageResource ok();

		@Source("forward.png")
		ImageResource forward();

		@Source("fforward.png")
		ImageResource fforward();

		@Source("userdefined.png")
		ImageResource userdefined();
	}
	
	private final DialogBox dialogYesNoBox;

	private final String userdefinedText;
	private ButtonState buttonState;
	private String searchFieldString;
	private String searchFieldEntry;
	private String searchFieldMethode;
	
	private String currentDBNumber;
	private String oldDBNumber;
	private String minDBNumber;
	private String maxDBNumber;

	private final VerticalPanel bigpanel		=	new VerticalPanel();
	private final HorizontalPanel panel			=	new HorizontalPanel();
	private final HorizontalPanel searchpanel	=	new HorizontalPanel();
	private final Grid searchgridpanel			=	new Grid(2,3);

	private final PushButton newPushButton		=	new PushButton(
	        new Image(images.filenew()));
	private final PushButton savePushButton	=	new PushButton(
			new Image(images.save()));
	private final PushButton deletePushButton	=	new PushButton(
			new Image(images.delete()));
	private final ToggleButton findToggleButton	=	new ToggleButton(
			new Image(images.find()));
	private final PushButton stopPushButton	=	new PushButton(
			new Image(images.stop()));
	private final PushButton fbackPushButton	=	new PushButton(
			new Image(images.fback()));
	private final PushButton backPushButton	=	new PushButton(
			new Image(images.back()));
	//private final VerticalPanel minmaxcurpanel	=	new VerticalPanel();
	//private final HorizontalPanel minpanel	=	new HorizontalPanel();
	//private final HorizontalPanel maxpanel	=	new HorizontalPanel();
	//private final HorizontalPanel curpanel		=	new HorizontalPanel();
	private final Grid minmaxcurpanel			=	new Grid(3,2);
	private final Label minLabel				=	new Label();
	private final Label minEntry				=	new Label();
	private final Label maxLabel				=	new Label();
	private final Label maxEntry				=	new Label();
	private final Label currentLabel			=	new Label();
	private final TextBox currentEntry			=	new TextBox();
	private final PushButton okPushButton		=	new PushButton(
			new Image(images.ok()));
	private final PushButton forwardPushButton	=	new PushButton(
			new Image(images.forward()));
	private final PushButton fforwardPushButton	=	new PushButton(
			new Image(images.fforward()));
	private final PushButton userdefinedPushButton = new PushButton(
			new Image(images.userdefined()));
	private final Label hintText				=	new Label();

	private final Label fieldSelectLabel		=	new Label();
	private final ListBox fieldSelect			=	new ListBox(false);
	private final Label fieldEntryLabel			=	new Label();
	private final TextBox fieldEntry			=	new TextBox();
	private final ListBox searchMethodeSelect	=	new ListBox(false);
	private final Label SearchMethodesLabel		=	new Label();
	private static final String SearchMethodesSmall[] = {"=", ">", ">=", "<=", "<", "like"};
	private static final String SearchMethodes[] = {"=", ">", ">=", "<=", "<", "like"};


	private boolean newPushButtonEnabled			=	true;
	private boolean savePushButtonEnabled			=	true;
	private boolean deletePushButtonEnabled			=	true;
	private boolean findPushButtonEnabled			=	true;
	private boolean stopPushButtonEnabled			=	true;
	private boolean fbackPushButtonEnabled			=	true;
	private boolean backPushButtonEnabled			=	true;
	private boolean currentEntryEnabled				=	true;
	private boolean okPushButtonEnabled				=	true;
	private boolean forwardPushButtonEnabled		=	true;
	private boolean fforwardPushButtonEnabled		=	true;
	private boolean userdefinedPushButtonEnabled	=	true;
	private boolean minMaxEnabled					=	true;

	private static final int SPACING			=	10;
	private static final String PANELSTYLE		=	"PANELSTYLE";

	private final String[] searchFieldsRemember;

	private final DBNavigationBarWidgetConstants constants;

	public DBNavigationBarWidget(
			String[] searchfields,
			String[] searchFieldsDisplay,
			String userdefinedText) {

		this.constants = (DBNavigationBarWidgetConstants) GWT.create(DBNavigationBarWidgetConstants.class);

		this.dialogYesNoBox = createYesNoDialogBox(constants);
		this.dialogYesNoBox.hide();

		this.searchFieldsRemember	=	searchfields;

		this.initWidget(this.bigpanel);
		this.bigpanel.setBorderWidth(1);
		this.bigpanel.setStyleName(PANELSTYLE);
		this.panel.setSpacing(SPACING);
		this.minLabel.setText(constants.labelMin());
		this.minEntry.setText("");
		this.maxLabel.setText(constants.labelMax());
		this.maxEntry.setText("");
		this.currentLabel.setText(constants.labelCur());
		this.currentEntry.setText("");
		this.userdefinedText	=	userdefinedText;
		this.hintText.setText("");

		DBNavigationBarWidget.SearchMethodes[0]	=	constants.findEquals();
		DBNavigationBarWidget.SearchMethodes[1]	=	constants.findGreater();
		DBNavigationBarWidget.SearchMethodes[2]	=	constants.findGreaterEquals();
		DBNavigationBarWidget.SearchMethodes[3]	=	constants.findLowerEquals();
		DBNavigationBarWidget.SearchMethodes[4]	=	constants.findLower();
		DBNavigationBarWidget.SearchMethodes[5]	=	constants.findContains();

		this.newPushButton.setAccessKey(constants.buttonNewAccessKey().trim().charAt(0));
		this.newPushButton.setTitle(constants.buttonNewMessage());

		this.savePushButton.setAccessKey(constants.buttonSaveAccessKey().trim().charAt(0));
		this.savePushButton.setTitle(constants.buttonSaveMessage());

		this.deletePushButton.setAccessKey(constants.buttonDeleteAccessKey().trim().charAt(0));
		this.deletePushButton.setTitle(constants.buttonDeleteMessage());

		this.findToggleButton.setAccessKey(constants.buttonFindAccessKey().trim().charAt(0));
		this.findToggleButton.setTitle(constants.buttonFindMessage());
		this.findToggleButton.setDown(false);

		this.stopPushButton.setAccessKey(constants.buttonStopAccessKey().trim().charAt(0));
		this.stopPushButton.setTitle(constants.buttonStopMessage());

		this.fbackPushButton.setAccessKey(constants.buttonFBackAccessKey().trim().charAt(0));
		this.fbackPushButton.setTitle(constants.buttonFBackMessage());

		this.backPushButton.setAccessKey(constants.buttonBackAccessKey().trim().charAt(0));
		this.backPushButton.setTitle(constants.buttonBackMessage());

		this.currentEntry.setAccessKey(constants.currentAccessKey().trim().charAt(0));
		this.currentEntry.setTitle(constants.currentMessage());

		this.okPushButton.setAccessKey(constants.buttonOkAccessKey().trim().charAt(0));
		this.okPushButton.setTitle(constants.buttonOkMessage());

		this.forwardPushButton.setAccessKey(constants.buttonForwardAccessKey().trim().charAt(0));
		this.forwardPushButton.setTitle(constants.buttonForwardMessage());

		this.fforwardPushButton.setAccessKey(constants.buttonFForwardAccessKey().trim().charAt(0));
		this.fforwardPushButton.setTitle(constants.buttonFForwardMessage());

		this.userdefinedPushButton.setAccessKey(constants.buttonUserDefinedAccessKey().trim().charAt(0));

		this.fieldSelectLabel.setText(constants.fieldNameLabel());
		this.fieldEntryLabel.setText(constants.fieldEntryLabel());
		if( searchfields != null && searchFieldsDisplay != null) {
			for (int i = 0; i < searchfields.length && i < searchFieldsDisplay.length; i++) {
				this.fieldSelect.addItem(searchFieldsDisplay[i], searchfields[i]);
			}
		}
	    this.SearchMethodesLabel.setText(constants.findTypeLabel());
	    for (int j = 0; j < SearchMethodes.length; j++) {
	    	this.searchMethodeSelect.addItem(SearchMethodes[j]);
	    }
	    this.fieldSelect.setAccessKey(constants.findFieldKey().trim().charAt(0));
	    this.searchMethodeSelect.setAccessKey(constants.findTypeKey().trim().charAt(0));
	    this.fieldEntry.setAccessKey(constants.findEntryKey().trim().charAt(0));

	    //this.searchgridpanel.setHeight("2.5em");
	    this.searchpanel.setVisible(this.findToggleButton.isDown());

		if( this.userdefinedText	==	null ) {
			this.userdefinedPushButton.setEnabled(false);
			this.userdefinedPushButtonEnabled	=	false;
		} else {
			this.userdefinedPushButton.setTitle(userdefinedText);
		}
		this.setDBMinMaxCurNumber(null, null, null);
		
	    // Create a handler for the sendButton and nameField
	    class MyHandler implements ClickHandler {

			@Override
			public void onClick(ClickEvent event) {
				PushButton eventPushButton		=	null;
				eventPushButton		=	(PushButton)event.getSource();
					
				DBNavigationBarWidget.this.hintText.setText("");

				if( DBNavigationBarWidget.this.newPushButton.equals(eventPushButton)) {
					DBNavigationBarWidget.this.buttonState	=	ButtonState.NEW;
					fireEvent(event);
				} else if ( DBNavigationBarWidget.this.savePushButton.equals(eventPushButton)) {
					DBNavigationBarWidget.this.buttonState	=	ButtonState.SAVE;
					fireEvent(event);
				} else if ( DBNavigationBarWidget.this.deletePushButton.equals(eventPushButton)) {
					DBNavigationBarWidget.this.dialogYesNoBox.center();
					DBNavigationBarWidget.this.dialogYesNoBox.show();
				} else if ( DBNavigationBarWidget.this.stopPushButton.equals(eventPushButton)) {
					DBNavigationBarWidget.this.buttonState	=	ButtonState.STOP;
					fireEvent(event);
				} else if ( DBNavigationBarWidget.this.fbackPushButton.equals(eventPushButton)) {
					DBNavigationBarWidget.this.buttonState	=	ButtonState.FAST_BACK;
					if( DBNavigationBarWidget.this.findToggleButton.isDown() ) {
						DBNavigationBarWidget.this.searchFieldString	=	DBNavigationBarWidget.this.searchFieldsRemember[(fieldSelect.getSelectedIndex() < 0 ? 0 : fieldSelect.getSelectedIndex())];
						DBNavigationBarWidget.this.searchFieldMethode	=	DBNavigationBarWidget.SearchMethodesSmall[(searchMethodeSelect.getSelectedIndex() < 0 ? 0 : searchMethodeSelect.getSelectedIndex())];
						DBNavigationBarWidget.this.searchFieldEntry		=	DBNavigationBarWidget.this.fieldEntry.getText();
						if( DBNavigationBarWidget.this.searchFieldEntry != null && !"".equals(DBNavigationBarWidget.this.searchFieldEntry) )
							DBNavigationBarWidget.this.buttonState	=	ButtonState.FAST_BACK_FIND;
					}
					fireEvent(event);
				} else if ( DBNavigationBarWidget.this.backPushButton.equals(eventPushButton)) {
					DBNavigationBarWidget.this.buttonState	=	ButtonState.BACK;
					if( DBNavigationBarWidget.this.findToggleButton.isDown() ) {
						DBNavigationBarWidget.this.searchFieldString	=	DBNavigationBarWidget.this.searchFieldsRemember[(fieldSelect.getSelectedIndex() < 0 ? 0 : fieldSelect.getSelectedIndex())];
						DBNavigationBarWidget.this.searchFieldMethode	=	DBNavigationBarWidget.SearchMethodesSmall[(searchMethodeSelect.getSelectedIndex() < 0 ? 0 : searchMethodeSelect.getSelectedIndex())];
						DBNavigationBarWidget.this.searchFieldEntry	=	DBNavigationBarWidget.this.fieldEntry.getText();
						if( DBNavigationBarWidget.this.searchFieldEntry != null && !"".equals(searchFieldEntry) )
							DBNavigationBarWidget.this.buttonState	=	ButtonState.BACK_FIND;
					}
					fireEvent(event);
				} else if ( DBNavigationBarWidget.this.okPushButton.equals(eventPushButton)) {
					DBNavigationBarWidget.this.buttonState	=	ButtonState.CHANGE;
					DBNavigationBarWidget.this.currentDBNumber	=	DBNavigationBarWidget.this.currentEntry.getText();
					fireEvent(event);
				} else if ( DBNavigationBarWidget.this.forwardPushButton.equals(eventPushButton)) {
					DBNavigationBarWidget.this.buttonState	=	ButtonState.FORWARD;
					if( DBNavigationBarWidget.this.findToggleButton.isDown() ) {
						DBNavigationBarWidget.this.searchFieldString	=	DBNavigationBarWidget.this.searchFieldsRemember[(fieldSelect.getSelectedIndex() < 0 ? 0 : fieldSelect.getSelectedIndex())];
						DBNavigationBarWidget.this.searchFieldMethode	=	DBNavigationBarWidget.SearchMethodesSmall[(searchMethodeSelect.getSelectedIndex() < 0 ? 0 : searchMethodeSelect.getSelectedIndex())];
						DBNavigationBarWidget.this.searchFieldEntry		=	DBNavigationBarWidget.this.fieldEntry.getText();
						if( DBNavigationBarWidget.this.searchFieldEntry != null && !"".equals(searchFieldEntry) )
							DBNavigationBarWidget.this.buttonState	=	ButtonState.FORWARD_FIND;
					}
					fireEvent(event);
				} else if ( DBNavigationBarWidget.this.fforwardPushButton.equals(eventPushButton)) {
					DBNavigationBarWidget.this.buttonState	=	ButtonState.FAST_FORWARD;
					if( DBNavigationBarWidget.this.findToggleButton.isDown() ) {
						DBNavigationBarWidget.this.searchFieldString	=	DBNavigationBarWidget.this.searchFieldsRemember[(fieldSelect.getSelectedIndex() < 0 ? 0 : fieldSelect.getSelectedIndex())];
						DBNavigationBarWidget.this.searchFieldMethode	=	DBNavigationBarWidget.SearchMethodesSmall[(searchMethodeSelect.getSelectedIndex() < 0 ? 0 : searchMethodeSelect.getSelectedIndex())];
						DBNavigationBarWidget.this.searchFieldEntry		=	DBNavigationBarWidget.this.fieldEntry.getText();
						if( DBNavigationBarWidget.this.searchFieldEntry != null && !"".equals(searchFieldEntry) )
							DBNavigationBarWidget.this.buttonState	=	ButtonState.FAST_FORWARD_FIND;
					}
					fireEvent(event);
				} else if ( DBNavigationBarWidget.this.userdefinedPushButton.equals(eventPushButton)) {
					DBNavigationBarWidget.this.buttonState	=	ButtonState.USER_DEFINED;
					fireEvent(event);
				}
			}

	    }

	    class MyEntryHandler implements KeyUpHandler {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					class OurClickEvent extends ClickEvent {};
					DBNavigationBarWidget.this.okPushButton.fireEvent((new OurClickEvent()));
				}
			}
	    }

	    class MySearchHandler implements KeyUpHandler {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					class OurClickEvent extends ClickEvent {};
					if( DBNavigationBarWidget.this.fbackPushButton.isEnabled() && DBNavigationBarWidget.this.fbackPushButton.isVisible() )
						DBNavigationBarWidget.this.fbackPushButton.fireEvent((new OurClickEvent()));
					else
						DBNavigationBarWidget.this.forwardPushButton.fireEvent((new OurClickEvent()));
				}
			}
	    }

	    MyHandler handler = new MyHandler();
	    MyEntryHandler entryhandler	=	new MyEntryHandler();
	    MySearchHandler searchhandler	=	new MySearchHandler();
	    this.newPushButton.addClickHandler(handler);
	    this.savePushButton.addClickHandler(handler);
	    this.deletePushButton.addClickHandler(handler);
	    ClickHandler myFindToggleClick	=	new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
			    if( ((ToggleButton)event.getSource()).isDown() ) {
			    	DBNavigationBarWidget.this.searchpanel.setVisible(true);
			    	DBNavigationBarWidget.this.fbackPushButton.setTitle(DBNavigationBarWidget.this.constants.buttonFBackMessageFind());
			    	DBNavigationBarWidget.this.backPushButton.setTitle(DBNavigationBarWidget.this.constants.buttonBackMessageFind());
			    	DBNavigationBarWidget.this.forwardPushButton.setTitle(DBNavigationBarWidget.this.constants.buttonForwardMessageFind());
			    	DBNavigationBarWidget.this.fforwardPushButton.setTitle(DBNavigationBarWidget.this.constants.buttonFForwardMessageFind());
			    } else {
			    	DBNavigationBarWidget.this.searchpanel.setVisible(false);
			    	DBNavigationBarWidget.this.fbackPushButton.setTitle(DBNavigationBarWidget.this.constants.buttonFBackMessage());
			    	DBNavigationBarWidget.this.backPushButton.setTitle(DBNavigationBarWidget.this.constants.buttonBackMessage());
			    	DBNavigationBarWidget.this.forwardPushButton.setTitle(DBNavigationBarWidget.this.constants.buttonForwardMessage());
			    	DBNavigationBarWidget.this.fforwardPushButton.setTitle(DBNavigationBarWidget.this.constants.buttonFForwardMessage());
			    }
			}
	    };
	    this.findToggleButton.addClickHandler(myFindToggleClick);
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

	public DBNavigationBarWidget(
			String[] searchfields,
			String userdefinedText) {
		this(searchfields, searchfields, userdefinedText);
	}

	public DBNavigationBarWidget(
			String[] searchfields,
			String[] searchFieldsDisplay ) {
		this(searchfields, searchFieldsDisplay, null);
	}

	public DBNavigationBarWidget(
			String[] searchfields ) {
		this(searchfields, searchfields, null);
	}

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
		//this.minpanel.add(this.minLabel);
		//this.minpanel.add(this.minEntry);
		//this.maxpanel.add(this.maxLabel);
		//this.maxpanel.add(this.maxEntry);
		//this.curpanel.add(this.currentLabel);
		//this.curpanel.add(this.currentEntry);
		//this.minmaxcurpanel.add(this.minpanel);
		//this.minmaxcurpanel.add(this.maxpanel);
		//this.minmaxcurpanel.add(this.curpanel);
		this.panel.add(this.minmaxcurpanel);
		this.panel.add(this.okPushButton);
		this.panel.add(this.forwardPushButton);
		this.panel.add(this.fforwardPushButton);
		this.panel.add(this.userdefinedPushButton);
		this.searchgridpanel.setWidget(0, 0, this.fieldSelectLabel);
		this.searchgridpanel.setWidget(0, 1, this.SearchMethodesLabel);
		this.searchgridpanel.setWidget(0, 2, this.fieldEntryLabel);
		this.searchgridpanel.setWidget(1, 0, this.fieldSelect);
    	this.searchgridpanel.setWidget(1, 1, this.searchMethodeSelect);
		this.searchgridpanel.setWidget(1, 2, this.fieldEntry);
		this.searchpanel.add(this.searchgridpanel);
		/*VerticalPanel Searchfield	=	new VerticalPanel();
		Searchfield.add(this.fieldSelectLabel);
		Searchfield.add(this.fieldSelect);
		VerticalPanel Searchmethodes	=	new VerticalPanel();
		Searchmethodes.add(this.SearchMethodesLabel);
		Searchmethodes.add(this.searchMethodeSelect);
		VerticalPanel Searchentry	=	new VerticalPanel();
		Searchentry.add(this.fieldEntryLabel);
		Searchentry.add(this.fieldEntry);
		this.searchpanel.add(Searchfield);
		this.searchpanel.add(Searchmethodes);
		this.searchpanel.add(Searchentry);*/
		this.bigpanel.add(this.panel);
		this.bigpanel.add(this.searchpanel);
		this.bigpanel.add(this.hintText);
	}

	public DBNavigationBarWidgetConstants getConstants() {
		return this.constants;
	}

	public ButtonState getButtonState() {
		return this.buttonState;
	}

	public String getCurrentDBNumber() {
		return this.currentDBNumber;
	}

	public String getOldDBNumber() {
		return this.oldDBNumber;
	}

	public String getSearchFieldField() {
		return this.searchFieldString;
	}
	
	public String getSearchFieldEntry() {
		return this.searchFieldEntry;
	}

	public String getSearchFieldMethode() {
		return this.searchFieldMethode;
	}

	public void displayHint( String hint ) {
		this.hintText.setText(hint);
	}

	public void enableAllButtons() {
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

	private void refreshButtonState() {
		this.panel.setVisible(false);
		if( this.minDBNumber == null || this.maxDBNumber == null ) {
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
			if( this.oldDBNumber == null ) {
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
				if( this.newPushButtonEnabled )
					this.newPushButton.getUpFace().setImage(new Image(images.filenew()));
				this.savePushButton.setEnabled(this.savePushButtonEnabled);
				if( this.savePushButtonEnabled )
					this.savePushButton.getUpFace().setImage(new Image(images.save()));
				this.deletePushButton.setEnabled(this.deletePushButtonEnabled);
				if( this.deletePushButtonEnabled )
					this.deletePushButton.getUpFace().setImage(new Image(images.delete()));
				this.findToggleButton.setEnabled(this.findPushButtonEnabled);
				if( this.findPushButtonEnabled )
					this.findToggleButton.getUpFace().setImage(new Image(images.find()));
				this.stopPushButton.setEnabled(this.stopPushButtonEnabled);
				if( this.stopPushButtonEnabled )
					this.stopPushButton.getUpFace().setImage(new Image(images.stop()));

				if( this.oldDBNumber.equals(this.minDBNumber)) {
					this.fbackPushButton.setEnabled(false);
					this.backPushButton.setEnabled(false);
				} else {
					this.fbackPushButton.setEnabled(this.fbackPushButtonEnabled);
					this.backPushButton.setEnabled(this.backPushButtonEnabled);
					if( this.fbackPushButtonEnabled )
						this.fbackPushButton.getUpFace().setImage(new Image(images.fback()));
					if( this.backPushButtonEnabled )
						this.backPushButton.getUpFace().setImage(new Image(images.back()));
				}
				this.currentEntry.setEnabled(this.currentEntryEnabled);
				this.okPushButton.setEnabled(this.okPushButtonEnabled);
				if( this.okPushButtonEnabled )
					this.okPushButton.getUpFace().setImage(new Image(images.ok()));
				if( this.oldDBNumber.equals(this.maxDBNumber)) {
					this.forwardPushButton.setEnabled(false);
					this.fforwardPushButton.setEnabled(false);
				} else {
					this.forwardPushButton.setEnabled(this.forwardPushButtonEnabled);
					this.fforwardPushButton.setEnabled(this.fforwardPushButtonEnabled);
					if( this.forwardPushButtonEnabled )
						this.forwardPushButton.getUpFace().setImage(new Image(images.forward()));
					if( this.fforwardPushButtonEnabled )
						this.fforwardPushButton.getUpFace().setImage(new Image(images.fforward()));
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

	public void setDBMinMaxCurNumber( String min, String max, String cur) {
		this.currentDBNumber	=	cur;
		this.oldDBNumber		=	cur;
		this.minDBNumber		=	min;
		this.maxDBNumber		=	max;
		this.minEntry.setText(min);
		this.maxEntry.setText(max);
		this.currentEntry.setText(cur);
		
		this.refreshButtonState();
	}

	public void changeSearchfields( String[] Searchfields) {
		if( Searchfields != null) {
			if( this.fieldSelect.getItemCount()	!=	Searchfields.length ||
				this.fieldSelect.getItemText(0)	!= 	Searchfields[0]) {

				for (int j = this.fieldSelect.getItemCount() - 1; j >= 0; j-- ) {
					this.fieldSelect.removeItem(j);
				}

				for (int i = 0; i < Searchfields.length; i++) {
					this.fieldSelect.addItem(Searchfields[i]);
					if( Searchfields[i].equals(this.searchFieldString) )
						this.fieldSelect.setSelectedIndex(i);
				}
			}
		}
	};


	public void setReadOnly() {
		this.newPushButtonEnabled		=	false;
		this.deletePushButtonEnabled	=	false;
		this.savePushButtonEnabled		=	false;
		this.refreshButtonState();
	}

	public void setReadWrite() {
		this.newPushButtonEnabled		=	true;
		this.deletePushButtonEnabled	=	true;
		this.savePushButtonEnabled		=	true;
		this.refreshButtonState();
	}

	public void enablenewPushButton() {
		this.newPushButtonEnabled		=	true;
		this.refreshButtonState();
	}
	public void disablenewPushButton() {
		this.newPushButtonEnabled		=	false;
		this.refreshButtonState();
	}

	public void enablesavePushButton() {
		this.savePushButtonEnabled		=	true;
		this.refreshButtonState();
	}
	public void disablesavePushButton() {
		this.savePushButtonEnabled		=	false;
		this.refreshButtonState();
	}

	public void enabledeletePushButton() {
		this.deletePushButtonEnabled	=	true;
		refreshButtonState();
	}
	public void disabledeletePushButton() {
		this.deletePushButtonEnabled	=	false;
		this.refreshButtonState();
	}

	public void enablefindPushButton() {
		this.findPushButtonEnabled		=	true;
		this.refreshButtonState();
	}
	public void disablefindPushButton() {
		this.findPushButtonEnabled		=	false;
		this.refreshButtonState();
	}

	public void enablestopPushButton() {
		this.stopPushButtonEnabled		=	true;
		this.refreshButtonState();
	}
	public void disablestopPushButton() {
		this.stopPushButtonEnabled		=	false;
		this.refreshButtonState();
	}

	public void enablefbackPushButton() {
		this.fbackPushButtonEnabled		=	true;
		this.refreshButtonState();
	}
	public void disablefbackPushButton() {
		this.fbackPushButtonEnabled		=	false;
		this.refreshButtonState();
	}

	public void enablebackPushButton() {
		this.backPushButtonEnabled		=	true;
		this.refreshButtonState();
	}
	public void disablebackPushButton() {
		this.backPushButtonEnabled		=	false;
		this.refreshButtonState();
	}

	public void enablecurrentEntry() {
		this.currentEntryEnabled		=	true;
		this.refreshButtonState();
	}
	public void disablecurrentEntry() {
		this.currentEntryEnabled		=	false;
		this.refreshButtonState();
	}

	public void enableokPushButton() {
		this.okPushButtonEnabled		=	true;
		this.refreshButtonState();
	}
	public void disableokPushButton() {
		this.okPushButtonEnabled		=	false;
		this.refreshButtonState();
	}

	public void enableforwardPushButton() {
		this.forwardPushButtonEnabled	=	true;
		this.refreshButtonState();
	}
	public void disableforwardPushButton() {
		this.forwardPushButtonEnabled	=	false;
		this.refreshButtonState();
	}

	public void enablefforwardPushButton() {
		this.fforwardPushButtonEnabled	=	true;
		this.refreshButtonState();
	}
	public void disablefforwardPushButton() {
		this.fforwardPushButtonEnabled	=	false;
		this.refreshButtonState();
	}

	public void enableMinMax() {
		this.minMaxEnabled				=	true;
		this.refreshButtonState();
	}
	public void disableMinMax() {
		this.minMaxEnabled				=	false;
		this.refreshButtonState();
	}

	public void setNewEntry() {
		this.currentDBNumber				=	null;
		this.oldDBNumber					=	null;
		this.currentEntry.setText(null);

		refreshButtonState();
	}

	private DialogBox createYesNoDialogBox(DBNavigationBarWidgetConstants constants) {
		// Create a dialog box and set the caption text
	    final DialogBox dialogBox = new DialogBox();
	    dialogBox.ensureDebugId("yesNoDialogBox");
	    dialogBox.setText(constants.deleteDialogHeader());

	    // Create a table to layout the content
	    VerticalPanel dialogContents = new VerticalPanel();
	    dialogBox.setWidget(dialogContents);

	    // Add some text to the top of the dialog
	    HTML details = new HTML(constants.deleteDialogText());
	    dialogContents.add(details);
	    dialogContents.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

	    HorizontalPanel dialogButtons = new HorizontalPanel();
	    // Add a yes button at the bottom of the dialog
	    Button yesButton = new Button(constants.yes(),
	    	new ClickHandler() {
	          @Override
			public void onClick(ClickEvent event) {
	        	  dialogBox.hide();
	        	  DBNavigationBarWidget.this.buttonState	=	ButtonState.DELETE;
	        	  fireEvent(event);
	          }
	        });
	    yesButton.setAccessKey(constants.yesKey().trim().charAt(0));
	    dialogButtons.add(yesButton);
	    dialogButtons.setCellHorizontalAlignment(yesButton, HasHorizontalAlignment.ALIGN_LEFT);
	    // Add a no button at the bottom of the dialog
	    Button noButton = new Button(constants.no(),
		    new ClickHandler() {
	          @Override
			public void onClick(ClickEvent event) {
	        	  dialogBox.hide();
	          }
	        });
	    noButton.setAccessKey(constants.noKey().trim().charAt(0));
	    dialogButtons.add(noButton);
	    dialogButtons.setCellHorizontalAlignment(noButton, HasHorizontalAlignment.ALIGN_RIGHT);
	    dialogButtons.setWidth("80%");
	    dialogContents.add(dialogButtons);

	    // Return the dialog box
	    return dialogBox;
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return super.addHandler(handler, ClickEvent.getType());
	}

	public HandlerRegistration addKeyUpHandler(KeyUpHandler handler) {
		return super.addHandler(handler, KeyUpEvent.getType());
	}
}

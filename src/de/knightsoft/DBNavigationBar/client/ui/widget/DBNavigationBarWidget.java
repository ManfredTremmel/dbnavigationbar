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
	
	private DialogBox dialogYesNoBox			=	null;

	private String userdefinedText				=	null;
	private String ButtonState					=	null;
	private String SearchFieldString			=	null;
	private String SearchFieldEntry				=	null;
	private String SearchFieldMethode			=	null;
	public final static String ButtonStateNew			=	"new";
	public final static String ButtonStateSave			=	"save";
	public final static String ButtonStateDelete		=	"delete";
	public final static String ButtonStateStop			=	"stop";
	public final static String ButtonStateFBack			=	"fback";
	public final static String ButtonStateFBackFind		=	"fbackfind";
	public final static String ButtonStateBack			=	"back";
	public final static String ButtonStateBackFind		=	"backfind";
	public final static String ButtonStateChange		=	"change";
	public final static String ButtonStateForward		=	"forward";
	public final static String ButtonStateForwardFind	=	"forwardfind";
	public final static String ButtonStateFForward		=	"fforward";
	public final static String ButtonStateFForwardFind	=	"fforwardfind";
	public final static String ButtonStateUserDef		=	"userdefined";
	
	private String CurrentDBNumber				=	null;
	private String OldDBNumber					=	null;
	private String MinDBNumber					=	null;
	private String MaxDBNumber					=	null;

	private final VerticalPanel Bigpanel		=	new VerticalPanel();
	private final HorizontalPanel panel		=	new HorizontalPanel();
	private final HorizontalPanel searchpanel	=	new HorizontalPanel();
	private final Grid searchgridpanel				=	new Grid(2,3);

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
	private String SearchMethodes[] = {"=", ">", ">=", "<=", "<", "like"};


	private boolean newPushButtonEnabled			=	true;
	private boolean savePushButtonEnabled			=	true;
	private boolean deletePushButtonEnabled		=	true;
	private boolean findPushButtonEnabled			=	true;
	private boolean stopPushButtonEnabled			=	true;
	private boolean fbackPushButtonEnabled			=	true;
	private boolean backPushButtonEnabled			=	true;
	private boolean currentEntryEnabled			=	true;
	private boolean okPushButtonEnabled			=	true;
	private boolean forwardPushButtonEnabled		=	true;
	private boolean fforwardPushButtonEnabled		=	true;
	private boolean userdefinedPushButtonEnabled	=	true;
	private boolean MinMaxEnabled					=	true;

	private static final int SPACING			=	10;
	private static final String PANELSTYLE		=	"PANELSTYLE";

	private String[] SearchfieldsRemember;

	private DBNavigationBarWidgetConstants constants;

	public DBNavigationBarWidget( String[] Searchfields, String[] SearchFieldsDisplay, String userdefinedText) {
		this.initComponents(Searchfields, SearchFieldsDisplay, userdefinedText);
		this.layoutComponents();
	}

	public DBNavigationBarWidget( String[] Searchfields, String userdefinedText) {
		this.initComponents(Searchfields, Searchfields, userdefinedText);
		this.layoutComponents();
	}

	public DBNavigationBarWidget( String[] Searchfields, String[] SearchFieldsDisplay ) {
		this.initComponents(Searchfields, SearchFieldsDisplay, null);
		this.layoutComponents();
	}
	public DBNavigationBarWidget( String[] Searchfields ) {
		this.initComponents(Searchfields, Searchfields, null);
		this.layoutComponents();
	}

	private void initComponents( String[] Searchfields, String[] SearchFieldsDisplay, String userdefinedText) {

		constants = (DBNavigationBarWidgetConstants) GWT.create(DBNavigationBarWidgetConstants.class);

		dialogYesNoBox = createYesNoDialogBox(constants);
		dialogYesNoBox.hide();

		this.SearchfieldsRemember	=	Searchfields;

		this.initWidget(this.Bigpanel);
		this.Bigpanel.setBorderWidth(1);
		this.Bigpanel.setStyleName(PANELSTYLE);
		this.panel.setSpacing(SPACING);
		this.minLabel.setText(constants.LabelMin());
		this.minEntry.setText("");
		this.maxLabel.setText(constants.LabelMax());
		this.maxEntry.setText("");
		this.currentLabel.setText(constants.LabelCur());
		this.currentEntry.setText("");
		this.userdefinedText	=	userdefinedText;
		this.hintText.setText("");

		this.SearchMethodes[0]	=	constants.FindEquals();
		this.SearchMethodes[1]	=	constants.FindGreater();
		this.SearchMethodes[2]	=	constants.FindGreaterEquals();
		this.SearchMethodes[3]	=	constants.FindLowerEquals();
		this.SearchMethodes[4]	=	constants.FindLower();
		this.SearchMethodes[5]	=	constants.FindContains();

		this.newPushButton.setAccessKey(constants.ButtonNewAccessKey().trim().charAt(0));
		this.newPushButton.setTitle(constants.ButtonNewMessage());

		this.savePushButton.setAccessKey(constants.ButtonSaveAccessKey().trim().charAt(0));
		this.savePushButton.setTitle(constants.ButtonSaveMessage());

		this.deletePushButton.setAccessKey(constants.ButtonDeleteAccessKey().trim().charAt(0));
		this.deletePushButton.setTitle(constants.ButtonDeleteMessage());

		this.findToggleButton.setAccessKey(constants.ButtonFindAccessKey().trim().charAt(0));
		this.findToggleButton.setTitle(constants.ButtonFindMessage());
		this.findToggleButton.setDown(false);

		this.stopPushButton.setAccessKey(constants.ButtonStopAccessKey().trim().charAt(0));
		this.stopPushButton.setTitle(constants.ButtonStopMessage());

		this.fbackPushButton.setAccessKey(constants.ButtonFBackAccessKey().trim().charAt(0));
		this.fbackPushButton.setTitle(constants.ButtonFBackMessage());

		this.backPushButton.setAccessKey(constants.ButtonBackAccessKey().trim().charAt(0));
		this.backPushButton.setTitle(constants.ButtonBackMessage());

		this.currentEntry.setAccessKey(constants.CurrentAccessKey().trim().charAt(0));
		this.currentEntry.setTitle(constants.CurrentMessage());

		this.okPushButton.setAccessKey(constants.ButtonOkAccessKey().trim().charAt(0));
		this.okPushButton.setTitle(constants.ButtonOkMessage());

		this.forwardPushButton.setAccessKey(constants.ButtonForwardAccessKey().trim().charAt(0));
		this.forwardPushButton.setTitle(constants.ButtonForwardMessage());

		this.fforwardPushButton.setAccessKey(constants.ButtonFForwardAccessKey().trim().charAt(0));
		this.fforwardPushButton.setTitle(constants.ButtonFForwardMessage());

		this.userdefinedPushButton.setAccessKey(constants.ButtonUserDefinedAccessKey().trim().charAt(0));

		this.fieldSelectLabel.setText(constants.FieldNameLabel());
		this.fieldEntryLabel.setText(constants.FieldEntryLabel());
		if( Searchfields != null && SearchFieldsDisplay != null) {
			for (int i = 0; i < Searchfields.length && i < SearchFieldsDisplay.length; i++) {
				this.fieldSelect.addItem(SearchFieldsDisplay[i], Searchfields[i]);
			}
		}
	    this.SearchMethodesLabel.setText(constants.FindTypeLabel());
	    for (int j = 0; j < SearchMethodes.length; j++) {
	    	this.searchMethodeSelect.addItem(SearchMethodes[j]);
	    }
	    this.fieldSelect.setAccessKey(constants.FindFieldKey().trim().charAt(0));
	    this.searchMethodeSelect.setAccessKey(constants.FindTypeKey().trim().charAt(0));
	    this.fieldEntry.setAccessKey(constants.FindEntryKey().trim().charAt(0));

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

			public void onClick(ClickEvent event) {
				PushButton eventPushButton		=	null;
				eventPushButton		=	(PushButton)event.getSource();
					
				hintText.setText("");

				if( newPushButton.equals(eventPushButton)) {
					ButtonState	=	ButtonStateNew;
					fireEvent(event);
				} else if ( savePushButton.equals(eventPushButton)) {
					ButtonState	=	ButtonStateSave;
					fireEvent(event);
				} else if ( deletePushButton.equals(eventPushButton)) {
					dialogYesNoBox.center();
					dialogYesNoBox.show();
				} else if ( stopPushButton.equals(eventPushButton)) {
					ButtonState	=	ButtonStateStop;
					fireEvent(event);
				} else if ( fbackPushButton.equals(eventPushButton)) {
					ButtonState	=	ButtonStateFBack;
					if( findToggleButton.isDown() ) {
						SearchFieldString	=	SearchfieldsRemember[(fieldSelect.getSelectedIndex() < 0 ? 0 : fieldSelect.getSelectedIndex())];
						SearchFieldMethode	=	SearchMethodesSmall[(searchMethodeSelect.getSelectedIndex() < 0 ? 0 : searchMethodeSelect.getSelectedIndex())];
						SearchFieldEntry	=	fieldEntry.getText();
						if( SearchFieldEntry != null && !"".equals(SearchFieldEntry) )
							ButtonState	=	ButtonStateFBackFind;
					}
					fireEvent(event);
				} else if ( backPushButton.equals(eventPushButton)) {
					ButtonState	=	ButtonStateBack;
					if( findToggleButton.isDown() ) {
						SearchFieldString	=	SearchfieldsRemember[(fieldSelect.getSelectedIndex() < 0 ? 0 : fieldSelect.getSelectedIndex())];
						SearchFieldMethode	=	SearchMethodesSmall[(searchMethodeSelect.getSelectedIndex() < 0 ? 0 : searchMethodeSelect.getSelectedIndex())];
						SearchFieldEntry	=	fieldEntry.getText();
						if( SearchFieldEntry != null && !"".equals(SearchFieldEntry) )
							ButtonState	=	ButtonStateBackFind;
					}
					fireEvent(event);
				} else if ( okPushButton.equals(eventPushButton)) {
					ButtonState	=	ButtonStateChange;
					CurrentDBNumber	=	currentEntry.getText();
					fireEvent(event);
				} else if ( forwardPushButton.equals(eventPushButton)) {
					ButtonState	=	ButtonStateForward;
					if( findToggleButton.isDown() ) {
						SearchFieldString	=	SearchfieldsRemember[(fieldSelect.getSelectedIndex() < 0 ? 0 : fieldSelect.getSelectedIndex())];
						SearchFieldMethode	=	SearchMethodesSmall[(searchMethodeSelect.getSelectedIndex() < 0 ? 0 : searchMethodeSelect.getSelectedIndex())];
						SearchFieldEntry	=	fieldEntry.getText();
						if( SearchFieldEntry != null && !"".equals(SearchFieldEntry) )
							ButtonState	=	ButtonStateForwardFind;
					}
					fireEvent(event);
				} else if ( fforwardPushButton.equals(eventPushButton)) {
					ButtonState	=	ButtonStateFForward;
					if( findToggleButton.isDown() ) {
						SearchFieldString	=	SearchfieldsRemember[(fieldSelect.getSelectedIndex() < 0 ? 0 : fieldSelect.getSelectedIndex())];
						SearchFieldMethode	=	SearchMethodesSmall[(searchMethodeSelect.getSelectedIndex() < 0 ? 0 : searchMethodeSelect.getSelectedIndex())];
						SearchFieldEntry	=	fieldEntry.getText();
						if( SearchFieldEntry != null && !"".equals(SearchFieldEntry) )
							ButtonState	=	ButtonStateFForwardFind;
					}
					fireEvent(event);
				} else if ( userdefinedPushButton.equals(eventPushButton)) {
					ButtonState	=	ButtonStateUserDef;
					fireEvent(event);
				}
			}

	    }

	    class MyEntryHandler implements KeyUpHandler {
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					class OurClickEvent extends ClickEvent {};
					okPushButton.fireEvent((ClickEvent)(new OurClickEvent()));
				}
			}
	    }

	    class MySearchHandler implements KeyUpHandler {
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					class OurClickEvent extends ClickEvent {};
					if( fbackPushButton.isEnabled() && fbackPushButton.isVisible() )
						fbackPushButton.fireEvent((ClickEvent)(new OurClickEvent()));
					else
						forwardPushButton.fireEvent((ClickEvent)(new OurClickEvent()));
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
			public void onClick(ClickEvent event) {
			    if( ((ToggleButton)event.getSource()).isDown() ) {
			    	searchpanel.setVisible(true);
			    	fbackPushButton.setTitle(constants.ButtonFBackMessageFind());
			    	backPushButton.setTitle(constants.ButtonBackMessageFind());
			    	forwardPushButton.setTitle(constants.ButtonForwardMessageFind());
			    	fforwardPushButton.setTitle(constants.ButtonFForwardMessageFind());
			    } else {
			    	searchpanel.setVisible(false);
			    	fbackPushButton.setTitle(constants.ButtonFBackMessage());
			    	backPushButton.setTitle(constants.ButtonBackMessage());
			    	forwardPushButton.setTitle(constants.ButtonForwardMessage());
			    	fforwardPushButton.setTitle(constants.ButtonFForwardMessage());
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
		this.Bigpanel.add(this.panel);
		this.Bigpanel.add(this.searchpanel);
		this.Bigpanel.add(this.hintText);
	}

	public DBNavigationBarWidgetConstants getConstants() {
		return constants;
	}

	public String getButtonState() {
		return this.ButtonState;
	}

	public String getCurrentDBNumber() {
		return this.CurrentDBNumber;
	}

	public String getOldDBNumber() {
		return this.OldDBNumber;
	}

	public String getSearchFieldField() {
		return this.SearchFieldString;
	}
	
	public String getSearchFieldEntry() {
		return this.SearchFieldEntry;
	}

	public String getSearchFieldMethode() {
		return this.SearchFieldMethode;
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
		if( this.MinDBNumber == null || this.MaxDBNumber == null ) {
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
			if( this.OldDBNumber == null ) {
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

				if( this.OldDBNumber.equals(this.MinDBNumber)) {
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
				if( this.OldDBNumber.equals(this.MaxDBNumber)) {
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
		this.minLabel.setVisible(this.MinMaxEnabled);
		this.minEntry.setVisible(this.MinMaxEnabled);
		this.maxLabel.setVisible(this.MinMaxEnabled);
		this.maxEntry.setVisible(this.MinMaxEnabled);
		this.panel.setVisible(true);
	}

	public void setDBMinMaxCurNumber( String min, String max, String cur) {
		this.CurrentDBNumber	=	cur;
		this.OldDBNumber		=	cur;
		this.MinDBNumber		=	min;
		this.MaxDBNumber		=	max;
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
					if( Searchfields[i].equals(this.SearchFieldString) )
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
		this.MinMaxEnabled				=	true;
		this.refreshButtonState();
	}
	public void disableMinMax() {
		this.MinMaxEnabled				=	false;
		this.refreshButtonState();
	}

	public void setNewEntry() {
		this.CurrentDBNumber				=	null;
		this.OldDBNumber					=	null;
		this.currentEntry.setText(null);

		refreshButtonState();
	}

	private DialogBox createYesNoDialogBox(DBNavigationBarWidgetConstants constants) {
		// Create a dialog box and set the caption text
	    final DialogBox dialogBox = new DialogBox();
	    dialogBox.ensureDebugId("yesNoDialogBox");
	    dialogBox.setText(constants.DeleteDialogHeader());

	    // Create a table to layout the content
	    VerticalPanel dialogContents = new VerticalPanel();
	    dialogBox.setWidget(dialogContents);

	    // Add some text to the top of the dialog
	    HTML details = new HTML(constants.DeleteDialogText());
	    dialogContents.add(details);
	    dialogContents.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

	    HorizontalPanel dialogButtons = new HorizontalPanel();
	    // Add a yes button at the bottom of the dialog
	    Button yesButton = new Button(constants.Yes(),
	    	new ClickHandler() {
	          public void onClick(ClickEvent event) {
	        	  dialogBox.hide();
	        	  ButtonState	=	ButtonStateDelete;
	        	  fireEvent(event);
	          }
	        });
	    yesButton.setAccessKey(constants.YesKey().trim().charAt(0));
	    dialogButtons.add(yesButton);
	    dialogButtons.setCellHorizontalAlignment(yesButton, HasHorizontalAlignment.ALIGN_LEFT);
	    // Add a no button at the bottom of the dialog
	    Button noButton = new Button(constants.No(),
		    new ClickHandler() {
	          public void onClick(ClickEvent event) {
	        	  dialogBox.hide();
	          }
	        });
	    noButton.setAccessKey(constants.NoKey().trim().charAt(0));
	    dialogButtons.add(noButton);
	    dialogButtons.setCellHorizontalAlignment(noButton, HasHorizontalAlignment.ALIGN_RIGHT);
	    dialogButtons.setWidth("80%");
	    dialogContents.add(dialogButtons);

	    // Return the dialog box
	    return dialogBox;
	}

	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return super.addHandler(handler, ClickEvent.getType());
	}

	public HandlerRegistration addKeyUpHandler(KeyUpHandler handler) {
		return super.addHandler(handler, KeyUpEvent.getType());
	}
}

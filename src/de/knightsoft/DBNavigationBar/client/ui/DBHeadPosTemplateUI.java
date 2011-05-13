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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import de.knightsoft.DBNavigationBar.client.Parent;
import de.knightsoft.DBNavigationBar.client.domain.DomainHeadPosDataBase;
import de.knightsoft.DBNavigationBar.client.ui.widget.DBNavigationBarWidgetConstants;

/**
 * 
 * The <code>DBHeadTemplateUI</code> class is a template for database
 * input mask
 * 
 * @author Manfred Tremmel
 * @version 1.0.0, 2011-02-19
 */
public abstract class DBHeadPosTemplateUI<E extends DomainHeadPosDataBase, F extends Parent> extends DBHeadTemplateUI<E, F> {

	protected boolean dosave = false;

	protected DBHeadPosTemplateUIConstants constantsPos;

	protected Button newPositionButton;
	protected VerticalPanel posPanel;
	protected FlexTable posTable;

	protected int rowToDelete						=	-1;

	protected DialogBox dialogYesNoBox;
	
	/**
	 * Constructor
	 * 
	 * @param parentwidget
	 * 		the parent widget, where this frame is displayed
	 * @param widgetlist
	 * 		list of widgets to display
	 * @param userdefinedfunction
	 * 		special function to include into the navigation bar
	 */
	public DBHeadPosTemplateUI(
			F parentwidget,
			Widget[] widgetlist,
			String userdefinedfunction) {

		super(parentwidget, widgetlist, userdefinedfunction);

		this.setNewPositionButton();

		this.dialogYesNoBox = createYesNoDialogBox(myNavigationBar.getConstants());
		this.dialogYesNoBox.hide();
	}

	protected void setNewPositionButton() {
		if( this.constantsPos == null )
			this.constantsPos = (DBHeadPosTemplateUIConstants) GWT.create(DBHeadPosTemplateUIConstants.class);

		if( this.newPositionButton == null )
			this.newPositionButton = new Button(constantsPos.addPositionButton(),
				new ClickHandler() {
					public void onClick(ClickEvent event) {
						fillPosition(
			        			  	posTable.getRowCount(),
			        				null);
			          }
				});
	}

	protected void setupPosPanel( String[] HeaderValues ) {
		posPanel	=	new VerticalPanel();
		posTable	=	new FlexTable();
		
		for( int i = 0; i < HeaderValues.length; i++ ) {
			posTable.setText(0, i, HeaderValues[i] );
			posTable.getCellFormatter().setStyleName(0, i, "thlistcentergrey");
		}

		posTable.setWidth("100%");
		this.posPanel.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		this.posPanel.setWidth("100%");
		this.posPanel.add(posTable);
	}

	/**
	 * <code>fillEntry</code> is called, to display a user
	 * 
	 *  @param entry
	 *  		entry to display
	 */
	protected abstract void fillEntry(E entry);
	

	/**
	 * <code>fillPositions</code> displays all Users
	 * 
	 *  @param entry
	 */
	protected void fillPositions( E entry) {

		posTable.setVisible(false);
		if( entry.getKeyPos() != null ) {
		
			for( int i = (posTable.getRowCount() - 1); i > entry.getKeyPos().length && i > 0; i--) {
				posTable.removeRow(i);
			}

			for( int numRows = 0; numRows < entry.getKeyPos().length; numRows++ ) {
				fillPosition(	numRows+1,
								entry
							);
			}
		} else {
			for( int i = (posTable.getRowCount() - 1); i > 0; i--) {
				posTable.removeRow(i);
			}
		}
		posTable.setVisible(true);
	}

	/**
	 * <code>fillPosition</code> displays one position
	 * 
	 *  @param row
	 *  @param entry
	 */
	protected abstract void fillPosition(
			int row,
			E entry
			);
	
	/**
	 * <code>getDeleteButton</code> creates a Button to delete a row
	 * 
	 * @return PushButton
	 */
	protected PushButton getDeleteButton() {
		PushButton DeleteButton =	new PushButton(
				new Image(images.DeletePosition()), new ClickHandler() {
					public void onClick(ClickEvent event) {
						PushButton sender = (PushButton)event.getSource();
						for( int i = 1; i < posTable.getRowCount(); i++) {
							if( ((PushButton)sender).equals( (PushButton)posTable.getWidget(i, (posTable.getCellCount(i) - 1))) ) {
								rowToDelete = i;
								dialogYesNoBox.center();
								dialogYesNoBox.show();
								i = posTable.getRowCount();
							}
						}
		            }
		        });
		return DeleteButton;
	}


    /**
     * The Method <code>createYesNoDialogBox</code> creates a Yes/No Dialog box
     * 
     * @return DialogBox
     */
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
	          public void onClick(ClickEvent event) {
	            dialogBox.hide();
				posTable.removeRow(rowToDelete);
				/*if( rowToDelete > 1 && rowToDelete == posTable.getRowCount() ) {
					PushButton downPushButton	=	(PushButton)posTable.getWidget(rowToDelete - 1, posTable.getCellCount(0) - 1);
					downPushButton.setEnabled(false);
				}*/
	          }
	        });
	    yesButton.setAccessKey(constants.yesKey().trim().charAt(0));
	    dialogButtons.add(yesButton);
	    dialogButtons.setCellHorizontalAlignment(yesButton, HasHorizontalAlignment.ALIGN_LEFT);
	    // Add a no button at the bottom of the dialog
	    Button noButton = new Button(constants.no(),
	    	new ClickHandler() {
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
}

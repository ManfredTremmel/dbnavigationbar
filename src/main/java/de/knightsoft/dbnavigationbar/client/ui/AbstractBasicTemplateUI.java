/**
 * This file is part of DBNavigationBar.
 *
 * DBNavigationBar is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * DBNavigationBar is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with DBNavigationBar. If not, see <a
 * href="http://www.gnu.org/licenses>http://www.gnu.org/licenses</a>
 *
 *
 * Copyright (c) 2011-2015 Manfred Tremmel
 *
 */

package de.knightsoft.dbnavigationbar.client.ui;

import de.knightsoft.dbnavigationbar.client.AbstractParent;
import de.knightsoft.dbnavigationbar.client.domain.AbstractDomainUser;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;

/**
 * The <code>AbstractBasicTemplateUI</code> class is a template for all input mask.
 *
 * @param <F> parent widget
 *
 * @author Manfred Tremmel
 * @version $Rev$, $Date$
 */
public abstract class AbstractBasicTemplateUI<F extends AbstractParent> extends Composite implements
    BasicTemplateUIInterface<F> {

  /**
   * three hundred milliseconds.
   */
  public static final int FOCUS_DELAY = 300;

  /**
   * parent widget.
   */
  private F parentwidget;

  /**
   * Constructor.
   */
  public AbstractBasicTemplateUI() {
    super();
  }

  /**
   * Constructor.
   *
   * @param pParentwidget the parent widget, where this frame is displayed
   */
  public AbstractBasicTemplateUI(final F pParentwidget) {
    super();
    this.parentwidget = pParentwidget;
  }

  /**
   * <code>setFocusOnFirstWidget</code> set focus to first enabled input field.
   *
   */
  protected abstract void setFocusOnFirstWidget();

  /**
   * set up the mask.
   *
   * @param pUser user information about the currently logged in user
   */
  protected abstract void setUpMask(AbstractDomainUser pUser);

  /**
   * The Method <code>matchesMenuSimple</code> looks if this UI is selected and makes the necessary changes.
   *
   * @param pItemtext selected menu item
   * @param pUser user information about the currently logged in user
   * @return true if it is allowed for this user
   */
  public final boolean matchesMenuSimple(final String pItemtext, final AbstractDomainUser pUser) {
    boolean matches = false;
    if (this.allowedToSee(pUser) && pItemtext != null && pItemtext.equals(this.getMenuText())) {
      // this.myNavigationBar.enableAllButtons();
      this.parentwidget.getMainPanel().clear();
      this.setUpMask(pUser);
      this.parentwidget.getMainPanel().add(this);
      matches = true;
      History.newItem("page=" + this.getMenuText(), false);
      Scheduler.get().scheduleFixedDelay(new RepeatingCommand() {
        @Override
        public boolean execute() {
          AbstractBasicTemplateUI.this.setFocusOnFirstWidget();
          return false;
        }
      }, FOCUS_DELAY);
    }
    return matches;
  }

  @Override
  public final void onLoad() {
    super.onLoad();
    Scheduler.get().scheduleFixedDelay(new RepeatingCommand() {
      @Override
      public boolean execute() {
        AbstractBasicTemplateUI.this.setFocusOnFirstWidget();
        return false;
      }
    }, FOCUS_DELAY);
  }

  @Override
  public final F getParentwidget() {
    return this.parentwidget;
  }

  public final void setParentwidget(final F pParentwidgetSet) {
    this.parentwidget = pParentwidgetSet;
  }
}

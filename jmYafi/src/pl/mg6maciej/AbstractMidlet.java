/*
 * Copyright (C) 2013 Maciej Górski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.mg6maciej;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Item;
import javax.microedition.midlet.MIDlet;

/**
 * @author mg6maciej
 */
public abstract class AbstractMidlet extends MIDlet {

	private boolean paused;

	public AbstractMidlet() {
		paused = false;
	}

	public final void startApp() {
		if (paused) {
			resumeMidlet();
		} else {
			initialize();
			startMidlet();
		}
		paused = false;
	}

	public final void pauseApp() {
		pauseMidlet();
		paused = true;
	}

	public final void destroyApp(boolean unconditional) {
		destroyMidlet(unconditional);
	}

	public void switchDisplayable(Displayable next) {
		getDisplay().setCurrent(next);
	}

	public void switchDisplayable(Item item) {
		getDisplay().setCurrentItem(item);
	}

	public void switchDisplayable(Alert alert, Displayable next) {
		getDisplay().setCurrent(alert, next);
	}

	protected void exitMidlet() {
		switchDisplayable((Displayable) null);
		destroyApp(true);
		notifyDestroyed();
	}

	protected Display getDisplay() {
		return Display.getDisplay(this);
	}

	protected void initialize() {
	}

	protected void startMidlet() {
	}

	protected void pauseMidlet() {
	}

	protected void resumeMidlet() {
	}

	protected void destroyMidlet(boolean uncoditional) {
	}
}

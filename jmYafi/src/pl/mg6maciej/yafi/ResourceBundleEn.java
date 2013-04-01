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
package pl.mg6maciej.yafi;

import pl.mg6maciej.ResourceBundle;
import pl.mg6maciej.StringUtils;

/**
 * @author mg6maciej
 */
public class ResourceBundleEn extends ResourceBundle {

	public ResourceBundleEn() {
		setProperty("title aliases", "Aliases");
		setProperty("title board", "Board");
		setProperty("title chat", "Chat");
		setProperty("title command line", "Command line");
		setProperty("title console", "Console");
		//setProperty("title disconnect", "Disconnecting");
		setProperty("title disconnected", "Disconnected");
		setProperty("title edit alias", "Edit alias");
		setProperty("title error", "Error");
		setProperty("title login", "Login");
		setProperty("title login script", "Login script");
		setProperty("title logout script", "Logout script");
		setProperty("title match", "Match");
		setProperty("title quit", "Quit");
		setProperty("title screens", "Switch to...");
		setProperty("title seek", "Seek");
		setProperty("title settings", "Settings");
		setProperty("title sought", "Sought");
		setProperty("title statistics", "Statistics");
		setProperty("label alias name", "Name");
		setProperty("label alias commands", "Commands");
		setProperty("label backgrounds", "Board backgrounds");
		setProperty("label received", "Received");
		setProperty("label sent", "Sent");
		setProperty("label free memory", "Free memory");
		setProperty("label free storage", "Free storage");
		setProperty("label used memory", "Used memory");
		setProperty("label used storage", "Used storage");
		setProperty("label prompt", "Prompt");
		setProperty("label board size", "Board size");
		setProperty("label board font size", "Board font size");
		setProperty("label console font size", "Console font size");
		setProperty("label pointer move style", "Pointer move style");
		setProperty("label seek increment", "Time increment");
		setProperty("label seek max rating", "Maximum rating");
		setProperty("label seek min rating", "Minimum rating");
		setProperty("label seek rated", "Rated game");
		setProperty("label seek time", "Initial time");
		setProperty("label seek type", "Game type");
		setProperty("label show options", StringUtils.EMPTY);
		setProperty("label match handle", "Username");
		setProperty("label match increment", "Time increment");
		setProperty("label match rated", "Rated game");
		setProperty("label match time", "Initial time");
		setProperty("label match type", "Game type");
		setProperty("text bytes", "B");
		//setProperty("text disconnect", "Disconnecting...");
		setProperty("text disconnected", "Good bye!");
		setProperty("text disconnected with error", "Disconnected with error:\n");
		setProperty("text error sending data", "Error sending data:\n");
		setProperty("text long name", "Yet Another FICS Interface");
		setProperty("text quit yafi", "Quit Yafi?");
		setProperty("text short name", "Yafi");
		setProperty("cmd add", "Add");
		setProperty("cmd back", "Back");
		setProperty("cmd backward", "Backward");
		setProperty("cmd cancel", "Cancel");
		setProperty("cmd castle short", "Castle short");
		setProperty("cmd castle long", "Castle long");
		setProperty("cmd chat", "Chat");
		setProperty("cmd chat privately", "Chat with");
		setProperty("cmd clean", "Clean");
		setProperty("cmd clear", "Clear");
		setProperty("cmd connect", "Connect");
		setProperty("cmd dec board size", "Smaller board");
		setProperty("cmd dec font size", "Smaller font");
		setProperty("cmd draw", "Draw");
		setProperty("cmd disconnect", "Disconnect");
		setProperty("cmd edit", "Edit");
		setProperty("cmd exit", "Exit");
		setProperty("cmd flip", "Flip");
		setProperty("cmd forward", "Forward");
		setProperty("cmd fullscreen", "More");
		setProperty("cmd inc board size", "Larger board");
		setProperty("cmd inc font size", "Larger font");
		setProperty("cmd insert newline", "Insert newline");
		setProperty("cmd match", "Match");
		setProperty("cmd ok", "OK");
		setProperty("cmd play", "Play");
		setProperty("cmd refresh", "Refresh");
		setProperty("cmd remove", "Remove");
		setProperty("cmd resign", "Resign");
		setProperty("cmd select", "Select");
		setProperty("cmd send", "Send");
		setProperty("cmd show aliases", "Aliases");
		setProperty("cmd show console", "Console");
		setProperty("cmd show login script", "Login script");
		setProperty("cmd show screens", "Switch to...");
		setProperty("cmd show statistics", "Statistics");
		setProperty("cmd unexamine", "Unexamine");
		setProperty("cmd unobserve", "Unobserve");
	}
}

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
package pl.mg6maciej.chess;

/**
 * @author mg6maciej
 */
public class Position {

	public static final int SIZE = 8;

	private char[][] placement;
	private char activePlayer;
	private int doublePawnPushFile;
	private boolean castlingWhiteShort;
	private boolean castlingWhiteLong;
	private boolean castlingBlackShort;
	private boolean castlingBlackLong;
	private int reversibleMovesQty;
	private int fullmoveNumber;

	public Position() {
		placement = new char[SIZE][];
		for (int i = 0; i < placement.length; i++) {
			placement[i] = new char[SIZE];
		}
	}

	public char getPieceAt(int file, int rank) {
		return placement[rank][file];
	}

	void setPieceAt(int file, int rank, char piece) {
		placement[rank][file] = piece;
	}

	public char getActivePlayer() {
		return activePlayer;
	}

	void setActivePlayer(char activePlayer) {
		this.activePlayer = activePlayer;
	}

	public int getDoublePawnPushFile() {
		return doublePawnPushFile;
	}

	void setDoublePawnPushFile(int doublePawnPushFile) {
		this.doublePawnPushFile = doublePawnPushFile;
	}

	public boolean getCastlingWhiteShort() {
		return castlingWhiteShort;
	}

	void setCastlingWhiteShort(boolean castlingWhiteShort) {
		this.castlingWhiteShort = castlingWhiteShort;
	}

	public boolean getCastlingWhiteLong() {
		return castlingWhiteLong;
	}

	void setCastlingWhiteLong(boolean castlingWhiteLong) {
		this.castlingWhiteLong = castlingWhiteLong;
	}

	public boolean getCastlingBlackShort() {
		return castlingBlackShort;
	}

	void setCastlingBlackShort(boolean castlingBlackShort) {
		this.castlingBlackShort = castlingBlackShort;
	}

	public boolean getCastlingBlackLong() {
		return castlingBlackLong;
	}

	void setCastlingBlackLong(boolean castlingBlackLong) {
		this.castlingBlackLong = castlingBlackLong;
	}

	public int getReversibleMovesQty() {
		return reversibleMovesQty;
	}

	void setReversibleMovesQty(int reversibleMovesQty) {
		this.reversibleMovesQty = reversibleMovesQty;
	}

	public int getFullmoveNumber() {
		return fullmoveNumber;
	}

	void setFullmoveNumber(int fullmoveNumber) {
		this.fullmoveNumber = fullmoveNumber;
	}
}

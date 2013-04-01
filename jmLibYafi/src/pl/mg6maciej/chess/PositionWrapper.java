
package pl.mg6maciej.chess;

import pl.mg6maciej.StringUtils;

/**
 * @author mg6maciej
 */
public class PositionWrapper {

	private Position position;

	public int gameNumber;
	public String whiteName;
	public String blackName;
	public int relation;
	public int initialTime;
	public int timeIncrement;
	public int whiteMaterial;
	public int blackMaterial;
	public long whiteTime;
	public long blackTime;
	public String moveVerbose;
	public String timeTaken;
	public String movePretty;
	public boolean flip;
	public boolean clockRunning;
	public int lag;

	public String result;
	public String description;

	public PositionWrapper() {
		position = new Position();
	}
	
	public Position getPosition() {
		return position;
	}

	public static PositionWrapper initial() {
		PositionWrapper pos = new PositionWrapper();
		char[] blackPieces = "rnbqkbnr".toCharArray();
		char[] whitePieces = "RNBQKBNR".toCharArray();
		for (int file = 0; file < Position.SIZE; file++) {
			pos.position.setPieceAt(file, 0, blackPieces[file]);
			pos.position.setPieceAt(file, 1, 'p');
			pos.position.setPieceAt(file, 6, 'P');
			pos.position.setPieceAt(file, 7, whitePieces[file]);
		}
		return pos;
	}

	public static PositionWrapper fromStyle12(String style12) {
		String[] data = StringUtils.split(style12, ' ');
		PositionWrapper pos = new PositionWrapper();
		for (int rank = 0; rank < Position.SIZE; rank++) {
			for (int file = 0; file < Position.SIZE; file++) {
				char piece = data[rank].charAt(file);
				pos.position.setPieceAt(file, rank, piece);
			}
		}
		char activePlayer = data[8].toLowerCase().charAt(0);
		pos.position.setActivePlayer(activePlayer);
		int doublePawnPushFile = Integer.parseInt(data[9]);
		pos.position.setDoublePawnPushFile(doublePawnPushFile);
		boolean castlingWhiteShort = "1".equals(data[10]);
		pos.position.setCastlingWhiteShort(castlingWhiteShort);
		boolean castlingWhiteLong = "1".equals(data[11]);
		pos.position.setCastlingWhiteLong(castlingWhiteLong);
		boolean castlingBlackShort = "1".equals(data[12]);
		pos.position.setCastlingBlackShort(castlingBlackShort);
		boolean castlingBlackLong = "1".equals(data[13]);
		pos.position.setCastlingBlackLong(castlingBlackLong);
		int reversibleMovesQty = Integer.parseInt(data[14]);
		pos.position.setReversibleMovesQty(reversibleMovesQty);
		int fullmoveNumber = Integer.parseInt(data[25]);
		pos.position.setFullmoveNumber(fullmoveNumber);
		pos.gameNumber = Integer.parseInt(data[15]);
		pos.whiteName = data[16];
		pos.blackName = data[17];
		pos.relation = Integer.parseInt(data[18]);
		pos.initialTime = Integer.parseInt(data[19]);
		pos.timeIncrement = Integer.parseInt(data[20]);
		pos.whiteMaterial = Integer.parseInt(data[21]);
		pos.blackMaterial = Integer.parseInt(data[22]);
		pos.whiteTime = Integer.parseInt(data[23]);
		pos.blackTime = Integer.parseInt(data[24]);
		pos.moveVerbose = data[26];
		pos.timeTaken = data[27];
		pos.movePretty = data[28];
		pos.flip = "1".equals(data[29]);
		pos.clockRunning = "1".equals(data[30]);
		pos.lag = Integer.parseInt(data[31]);
		return pos;
	}
}

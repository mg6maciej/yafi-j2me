
package pl.mg6maciej.chess;

import pl.mg6maciej.StringUtils;

/**
 * @author mg6maciej
 */
public class GameInfo {

	public int gameNumber;
	public boolean privateGame;
	public String type;
	public boolean rated;
	public boolean whiteUnreg;
	public boolean blackUnreg;
	public int startTime;
	public int timeIncrement;
	public int partnerGameNumber;
	public int whiteRating;
	public int blackRating;
	public boolean whiteTimeseal;
	public boolean blackTimeseal;
	public int minMoveTime;
	public boolean noescape;

	public static GameInfo fromFics(String str) {
		String[] data = StringUtils.split(str, ' ');
		GameInfo info = new GameInfo();
		// <g1> 622 p=0 t=blitz r=1 u=0,0 it=180,0 i=180,0 pt=0 rt=2058,2163 ts=1,1 m=2 n=0
		info.gameNumber = Integer.parseInt(data[0]);

		String[] ratings = StringUtils.split(data[8].substring("rt=".length()), ',');
		char whiteProv = ratings[0].charAt(ratings[0].length() - 1);
		if (whiteProv < '0' || whiteProv > '9') {
			ratings[0] = ratings[0].substring(0, ratings[0].length() - 1);
		}
		info.whiteRating = Integer.parseInt(ratings[0]);
		char blackProv = ratings[1].charAt(ratings[1].length() - 1);
		if (blackProv < '0' || blackProv > '9') {
			ratings[1] = ratings[1].substring(0, ratings[1].length() - 1);
		}
		info.blackRating = Integer.parseInt(ratings[1]);
		return info;
	}
}

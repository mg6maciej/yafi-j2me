
package pl.mg6maciej.yafi;

import pl.mg6maciej.StringUtils;

/**
 * @author mg6maciej
 */
public class SeekInfo {

	private int seekNumber;
	private String handle;
	private int titles;
	private int rating;
	private char provisional;
	private int time;
	private int increment;
	private boolean rated;
	private String type;
	private char color;
	private int minRating;
	private int maxRating;
	private boolean manual;
	private boolean formula;

	public int getSeekNumber() {
		return seekNumber;
	}

	public String getHandle() {
		return handle;
	}

	public int getTitles() {
		return titles;
	}

	public int getRating() {
		return rating;
	}

	public char getProvisional() {
		return provisional;
	}

	public int getTime() {
		return time;
	}

	public int getIncrement() {
		return increment;
	}

	public boolean getRated() {
		return rated;
	}

	public String getType() {
		return type;
	}

	public char getColor() {
		return color;
	}

	public int getMinRating() {
		return minRating;
	}

	public int getMaxRating() {
		return maxRating;
	}

	public boolean getManual() {
		return manual;
	}

	public boolean getFormula() {
		return formula;
	}
	
	public static SeekInfo fromFicsData(String seek) {
		String[] data = StringUtils.split(seek, ' ');
		SeekInfo s = new SeekInfo();
		int i = 4;
		s.seekNumber = Integer.parseInt(data[0]);
		s.handle = data[1].substring("w=".length());
		s.titles = Integer.parseInt(data[2].substring("ti=".length()), 16);
		if (data[i].length() == 0) {
			s.rating = Integer.parseInt(data[3].substring("rt=".length()));
			s.provisional = ' ';
			i++;
		} else {
			s.rating = Integer.parseInt(data[3].substring("rt=".length(), data[3].length() - 1));
			s.provisional = data[3].charAt(data[3].length() - 1);
		}
		s.time = Integer.parseInt(data[i++].substring("t=".length()));
		s.increment = Integer.parseInt(data[i++].substring("i=".length()));
		s.rated = "r".equals(data[i++].substring("r=".length()));
		s.type = data[i++].substring("tp=".length());
		s.color = data[i].charAt(data[i].length() - 1);
		i++;
		int ratingSeparator = data[i].indexOf('-');
		s.minRating = Integer.parseInt(data[i].substring("rr=".length(), ratingSeparator));
		s.maxRating = Integer.parseInt(data[i].substring(ratingSeparator + 1));
		i++;
		s.manual = "n".equals(data[i++].substring("a=".length()));
		s.formula = "t".equals(data[i++].substring("f=".length()));
		return s;
	}
}

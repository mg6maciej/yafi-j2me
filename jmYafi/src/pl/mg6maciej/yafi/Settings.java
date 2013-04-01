
package pl.mg6maciej.yafi;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;
import javax.microedition.lcdui.Font;
import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import pl.mg6maciej.StringUtils;
import pl.mg6maciej.Utils;

/**
 * @author mg6maciej
 */
public class Settings {

	public Settings() throws RecordStoreException, IOException {
		init();
	}

	private void init() throws RecordStoreException, IOException {
		RecordStore rs = null;
		try {
			rs = RecordStore.openRecordStore("settings", true);
			Vector v = new Vector();
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(buffer);
			dos.writeUTF(getUsername()); v.addElement(buffer.toByteArray()); buffer.reset();
			dos.writeUTF(getPassword()); v.addElement(buffer.toByteArray()); buffer.reset();
			dos.writeUTF(getPrompt()); v.addElement(buffer.toByteArray()); buffer.reset();
			dos.writeInt(getBoardSize()); v.addElement(buffer.toByteArray()); buffer.reset();
			dos.writeInt(getBoardFontSize()); v.addElement(buffer.toByteArray()); buffer.reset();
			dos.writeBoolean(getUseDragAndDrop()); v.addElement(buffer.toByteArray()); buffer.reset();
			dos.writeUTF(getLoginScript()); v.addElement(buffer.toByteArray()); buffer.reset();
			dos.writeInt(getConsoleFontSize()); v.addElement(buffer.toByteArray()); buffer.reset();
			dos.writeBoolean(getShowAdditionalGameInfo()); v.addElement(buffer.toByteArray()); buffer.reset();
			dos.writeBoolean(getRememberPassword()); v.addElement(buffer.toByteArray()); buffer.reset();
			dos.writeUTF(getLogoutScript()); v.addElement(buffer.toByteArray()); buffer.reset();
			dos.writeBoolean(getUseTimeseal()); v.addElement(buffer.toByteArray()); buffer.reset();
			dos.writeInt(getBoardBackgroundId()); v.addElement(buffer.toByteArray()); buffer.reset();
			dos.writeBoolean(getBoardFullscreenMode()); v.addElement(buffer.toByteArray()); buffer.reset();
			buffer.close();
			for (int i = rs.getNumRecords(); i < v.size(); i++) {
				byte[] record = (byte[]) v.elementAt(i);
				rs.addRecord(record, 0, record.length);
			}
		} finally {
			if (rs != null) {
				rs.closeRecordStore();
			}
		}
		rs = null;
		try {
			rs = RecordStore.openRecordStore("aliases", true);
			if (rs.getNextRecordID() == 1) {
				Alias[] _aliases = new Alias[] {
					new Alias("pa", "seek 5 5 u\nseek 6 5 u\nseek 8 5 u"),
					new Alias("pd", "seek 10 0 u\nseek 12 0 u\nseek 16 0 u"),
				};
				for (int i = 0; i < _aliases.length; i++) {
					Alias alias = _aliases[i];
					byte[] tmp = alias.toByteArray();
					rs.addRecord(tmp, 0, tmp.length);
				}
			}
		} finally {
			if (rs != null) {
				rs.closeRecordStore();
			}
		}
		rs = null;
		try {
			rs = RecordStore.openRecordStore("backgrounds", true);
			if (rs.getNextRecordID() == 1) {
				BoardBackground[] _backgrounds = new BoardBackground[] {
					new BoardBackground("blue", 0xc0dcc0, 0x008080),
					new BoardBackground("brown", 0xcebca4, 0xa98963),
					new BoardBackground("green", 0xc8c365, 0x77a26d),
					new BoardBackground("orange", 0xffce9e, 0xd18b47),
				};
				for (int i = 0; i < _backgrounds.length; i++) {
					BoardBackground background = _backgrounds[i];
					byte[] tmp = background.toByteArray();
					rs.addRecord(tmp, 0, tmp.length);
				}
			}
		} finally {
			if (rs != null) {
				rs.closeRecordStore();
			}
		}
	}
	
	public void reset() throws RecordStoreException, IOException {
		RecordStore.deleteRecordStore("settings");
		prompt = null;
		init();
	}

	private String getSetting(int id, String def) throws RecordStoreException, IOException {
		RecordStore rs = null;
		try {
			rs = RecordStore.openRecordStore("settings", false);
			if (rs.getNextRecordID() <= id) {
				return def;
			}
			byte[] tmp = rs.getRecord(id);
			return Utils.byteArrayToString(tmp);
		} finally {
			if (rs != null) {
				rs.closeRecordStore();
			}
		}
	}

	private int getSetting(int id, int def) throws RecordStoreException, IOException {
		RecordStore rs = null;
		try {
			rs = RecordStore.openRecordStore("settings", false);
			if (rs.getNextRecordID() <= id) {
				return def;
			}
			byte[] tmp = rs.getRecord(id);
			return Utils.byteArrayToInt(tmp);
		} finally {
			if (rs != null) {
				rs.closeRecordStore();
			}
		}
	}

	private boolean getSetting(int id, boolean def) throws RecordStoreException, IOException {
		RecordStore rs = null;
		try {
			rs = RecordStore.openRecordStore("settings", false);
			if (rs.getNextRecordID() <= id) {
				return def;
			}
			byte[] tmp = rs.getRecord(id);
			return Utils.byteArrayToBoolean(tmp);
		} finally {
			rs.closeRecordStore();
		}
	}

	public String getUsername() throws RecordStoreException, IOException {
		return getSetting(1, "guest");
	}

	public String getPassword() throws RecordStoreException, IOException {
		return getSetting(2, StringUtils.EMPTY);
	}

	private String prompt;

	public String getPrompt() throws RecordStoreException, IOException {
		if (prompt == null) {
			prompt = getSetting(3, "yafi% ");
		}
		return prompt;
	}

	public int getBoardSize() throws RecordStoreException, IOException {
		return getSetting(4, BoardScreen.SIZE_UNDEFINED);
	}

	public int getBoardFontSize() throws RecordStoreException, IOException {
		return getSetting(5, Font.SIZE_SMALL);
	}

	public boolean getUseDragAndDrop() throws RecordStoreException, IOException {
		return getSetting(6, true);
	}

	public String getLoginScript() throws RecordStoreException, IOException {
		return getSetting(7, "set autoflag 1\nset bell 0\nset chanoff 1\nset seek 0\nset style 12");
	}
	
	public int getConsoleFontSize() throws RecordStoreException, IOException {
		return getSetting(8, Font.SIZE_SMALL);
	}
	
	public boolean getShowAdditionalGameInfo() throws RecordStoreException, IOException {
		return getSetting(9, false);
	}
	
	public boolean getRememberPassword() throws RecordStoreException, IOException {
		return getSetting(10, false);
	}
	
	public String getLogoutScript() throws RecordStoreException, IOException {
		return getSetting(11, "set chanoff 0");
	}
	
	public boolean getUseTimeseal() throws RecordStoreException, IOException {
		return getSetting(12, true);
	}
	
	public int getBoardBackgroundId() throws RecordStoreException, IOException {
		return getSetting(13, 4);
	}

	public boolean getBoardFullscreenMode() throws RecordStoreException, IOException {
		return getSetting(14, false);
	}

	private void saveSetting(int id, String setting) throws RecordStoreException, IOException {
		RecordStore rs = null;
		try {
			rs = RecordStore.openRecordStore("settings", false);
			byte[] tmp = Utils.toByteArray(setting);
			rs.setRecord(id, tmp, 0, tmp.length);
		} finally {
			if (rs != null) {
				rs.closeRecordStore();
			}
		}
	}

	private void saveSetting(int id, int setting) throws RecordStoreException, IOException {
		RecordStore rs = null;
		try {
			rs = RecordStore.openRecordStore("settings", false);
			byte[] tmp = Utils.toByteArray(setting);
			rs.setRecord(id, tmp, 0, tmp.length);
		} finally {
			if (rs != null) {
				rs.closeRecordStore();
			}
		}
	}

	private void saveSetting(int id, boolean setting) throws RecordStoreException, IOException {
		RecordStore rs = null;
		try {
			rs = RecordStore.openRecordStore("settings", false);
			byte[] tmp = Utils.toByteArray(setting);
			rs.setRecord(id, tmp, 0, tmp.length);
		} finally {
			rs.closeRecordStore();
		}
	}

	public void saveUsername(String username) throws RecordStoreException, IOException {
		saveSetting(1, username);
	}

	public void savePassword(String password) throws RecordStoreException, IOException {
		saveSetting(2, password);
	}

	public void savePrompt(String prompt) throws RecordStoreException, IOException {
		saveSetting(3, prompt);
	}

	public void saveBoardSize(int boardSize) throws RecordStoreException, IOException {
		saveSetting(4, boardSize);
	}

	public void saveBoardFontSize(int boardFontSize) throws RecordStoreException, IOException {
		saveSetting(5, boardFontSize);
	}

	public void saveUseDragAndDrop(boolean useDragAndDrop) throws RecordStoreException, IOException {
		saveSetting(6, useDragAndDrop);
	}

	public void saveLoginScript(String loginScript) throws RecordStoreException, IOException {
		saveSetting(7, loginScript);
	}
	
	public void saveConsoleFontSize(int consoleFontSize) throws RecordStoreException, IOException {
		saveSetting(8, consoleFontSize);
	}
	
	public void saveShowAdditionalGameInfo(boolean showAdditionalGameInfo) throws RecordStoreException, IOException {
		saveSetting(9, showAdditionalGameInfo);
	}
	
	public void saveRememberPassword(boolean rememberPassword) throws RecordStoreException, IOException {
		saveSetting(10, rememberPassword);
	}
	
	public void saveLogoutScript(String logoutScript) throws RecordStoreException, IOException {
		saveSetting(11, logoutScript);
	}
	
	public void saveUseTimeseal(boolean useTimeseal) throws RecordStoreException, IOException {
		saveSetting(12, useTimeseal);
	}
	
	public void saveBoardBackgroundId(int boardBackgroundId) throws RecordStoreException, IOException {
		saveSetting(13, boardBackgroundId);
	}

	public void saveBoardFullscreenMode(boolean boardFullscreenMode) throws RecordStoreException, IOException {
		saveSetting(14, boardFullscreenMode);
	}

	private Alias[] aliases;

	public Alias[] getAliases() throws RecordStoreException, IOException {
		if (aliases == null) {
			Vector v = new Vector();
			RecordStore rs = null;
			try {
				rs = RecordStore.openRecordStore("aliases", false);
//				RecordEnumeration e = rs.enumerateRecords(null, this, false);
//				while (e.hasNextElement()) {
//					int rsIndex = e.nextRecordId();
//					byte[] tmp = rs.getRecord(rsIndex);
//					Alias alias = Alias.fromByteArray(tmp, rsIndex);
//					v.addElement(alias);
//				}
//				e.destroy();
				for (int i = 1; i < rs.getNextRecordID(); i++) {
					try {
						byte[] tmp = rs.getRecord(i);
						Alias alias = Alias.fromByteArray(tmp, i);
						v.addElement(alias);
					} catch (InvalidRecordIDException ignore) {
						//ignore.printStackTrace();
					}
				}
			} finally {
				if (rs != null) {
					rs.closeRecordStore();
				}
			}
			aliases = new Alias[v.size()];
			v.copyInto(aliases);
		}
		return aliases;
	}

	public void saveAlias(Alias alias) throws IOException, RecordStoreException {
		RecordStore rs = null;
		try {
			byte[] tmp = alias.toByteArray();
			rs = RecordStore.openRecordStore("aliases", false);
			if (alias.getRsIndex() == -1) {
				alias.setRsIndex(rs.getNextRecordID());
				rs.addRecord(tmp, 0, tmp.length);
			} else {
				rs.setRecord(alias.getRsIndex(), tmp, 0, tmp.length);
			}
		} finally {
			if (rs != null) {
				rs.closeRecordStore();
			}
		}
		aliases = null;
	}

	public void deleteAlias(Alias alias) throws RecordStoreException {
		RecordStore rs = null;
		try {
			int rsIndex = alias.getRsIndex();
			rs = RecordStore.openRecordStore("aliases", false);
			rs.deleteRecord(rsIndex);
		} finally {
			if (rs != null) {
				rs.closeRecordStore();
			}
		}
		aliases = null;
	}
//	
//	public int compare(byte[] rec1, byte[] rec2) {
//		try {
//			DataInputStream dis;
//			dis = new DataInputStream(new ByteArrayInputStream(rec1));
//			String name1 = dis.readUTF();
//			System.out.println("name 1 " + name1);
//			dis = new DataInputStream(new ByteArrayInputStream(rec2));
//			String name2 = dis.readUTF();
//			System.out.println("name 2 " + name2);
//			System.out.println("cmp " + name1.compareTo(name2));
//			return name1.compareTo(name2);
//		} catch (IOException ex) {
//			ex.printStackTrace();
//		}
//		return 0;
//	}
	
	BoardBackground[] backgrounds;

	public BoardBackground[] getBackgrounds() throws IOException, RecordStoreException {
		if (backgrounds == null) {
			Vector v = new Vector();
			RecordStore rs = null;
			try {
				rs = RecordStore.openRecordStore("backgrounds", false);
//				RecordEnumeration e = rs.enumerateRecords(null, this, false);
//				while (e.hasNextElement()) {
//					int rsIndex = e.nextRecordId();
//					byte[] tmp = rs.getRecord(rsIndex);
//					BoardBackground background = BoardBackground.fromByteArray(tmp, rsIndex);
//					v.addElement(background);
//				}
//				e.destroy();
				for (int i = 1; i < rs.getNextRecordID(); i++) {
					try {
						byte[] tmp = rs.getRecord(i);
						BoardBackground bg = BoardBackground.fromByteArray(tmp, i);
						v.addElement(bg);
					} catch (InvalidRecordIDException ignore) {
						//ignore.printStackTrace();
					}
				}
			} finally {
				if (rs != null) {
					rs.closeRecordStore();
				}
			}
			backgrounds = new BoardBackground[v.size()];
			v.copyInto(backgrounds);
		}
		return backgrounds;
	}

	public void saveBackground(BoardBackground background) throws IOException, RecordStoreException {
		RecordStore rs = null;
		try {
			byte[] tmp = background.toByteArray();
			rs = RecordStore.openRecordStore("backgrounds", false);
			if (background.getRsIndex() == -1) {
				background.setRsIndex(rs.getNextRecordID());
				rs.addRecord(tmp, 0, tmp.length);
			} else {
				rs.setRecord(background.getRsIndex(), tmp, 0, tmp.length);
			}
		} finally {
			if (rs != null) {
				rs.closeRecordStore();
			}
		}
		backgrounds = null;
	}

	public void deleteBackground(BoardBackground background) throws RecordStoreException {
		RecordStore rs = null;
		try {
			int rsIndex = background.getRsIndex();
			rs = RecordStore.openRecordStore("backgrounds", false);
			rs.deleteRecord(rsIndex);
		} finally {
			if (rs != null) {
				rs.closeRecordStore();
			}
		}
		backgrounds = null;
	}
}

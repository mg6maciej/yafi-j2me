
package pl.mg6maciej;

import java.util.Hashtable;

/**
 * @author mg6maciej
 */
public class ResourceBundle {

	private Hashtable resources = new Hashtable();

	public Object getProperty(String key) {
		return resources.get(key);
	}

	protected void setProperty(String key, Object value) {
		resources.put(key, value);
	}

	public String getString(String key) {
		return (String) getProperty(key);
	}
}

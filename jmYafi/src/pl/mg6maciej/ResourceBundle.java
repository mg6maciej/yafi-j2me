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

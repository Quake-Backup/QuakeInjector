/*
Copyright 2009 Hauke Rehfeld


This file is part of QuakeInjector.

QuakeInjector is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

QuakeInjector is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with QuakeInjector.  If not, see <http://www.gnu.org/licenses/>.
*/
package de.haukerehfeld.quakeinjector;

public class FileInfo implements Comparable<FileInfo> {
	private String name;
	private long checksum;

	public FileInfo(String name, long checksum) {
		this.name = name;
		this.checksum = checksum;
	}

	/**
	 * get checksum
	 */
	public long getChecksum() { return checksum; }
	
/**
 * set checksum
 */
	public void setChecksum(long checksum) { this.checksum = checksum; }

	/**
	 * get name
	 */
	public String getName() { return name; }
	
/**
 * set name
 */
	public void setName(String name) { this.name = name; }

	public int compareTo(FileInfo o) {
		int i = o.getName().compareTo(getName());
		if (i == 0) {
			i = new Long(o.getChecksum()).compareTo(new Long(getChecksum()));
		}
		return i;
	}

	public boolean equals(Object o) {
		return o instanceof FileInfo && ((FileInfo) o).getName().equals(getName()) && ((FileInfo) o).getChecksum() == getChecksum();
	}

	public int hashCode() {
		return name.hashCode() + 17 * new Long(checksum).hashCode();
	}

	public void clean() {
		name = name.replace('\\', '/');
	}

	
}

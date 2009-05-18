package de.haukerehfeld.quakeinjector;

import java.util.Iterator;
import java.util.TreeSet;

public class PackageFileList implements Iterable<String> {
	/**
	 * other classes rely on the sorted iteration this provides
	 */
	private TreeSet<String> files = new TreeSet<String>();

	private String id;

	public PackageFileList(String id) {
		this.id = id;
	}

	public void add(String file) {
		files.add(file.replace('\\', '/'));
	}

	/**
	 * iterate the files in ascending order by their filename
	 */
	public Iterator<String> iterator() {
		return files.iterator();
	}

	public Iterator<String> descendingIterator() {
		return files.descendingIterator();
	}
	
	public String getId() {
		return id;
	}
}
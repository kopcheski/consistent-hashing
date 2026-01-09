package org.kopcheski.consistenthashing.model;

public record Hash (Integer hashValue) implements Comparable<Hash> {

	@Override
	public int compareTo(Hash other) {
		return this.hashValue.compareTo(other.hashValue);
	}

}

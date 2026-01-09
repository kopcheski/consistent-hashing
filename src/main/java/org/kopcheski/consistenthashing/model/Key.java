package org.kopcheski.consistenthashing.model;

/**
 * A Key that maps a {@link org.kopcheski.consistenthashing.model.Value} to a {@link org.kopcheski.consistenthashing.Node}
 */
public record Key(String value) implements RingValue, Hashable, Comparable<Key> {

	@Override
	public int compareTo(Key other) {
		return this.value.compareTo(other.value);
	}

	@Override
	public String value() {
		return this.value;
	}
}

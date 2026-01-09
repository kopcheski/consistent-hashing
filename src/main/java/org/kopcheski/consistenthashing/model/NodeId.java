package org.kopcheski.consistenthashing.model;

public record NodeId (String value) implements RingValue, Hashable, Comparable<NodeId> {

	@Override
	public int compareTo(NodeId other) {
		return this.value.compareTo(other.value);
	}

	@Override
	public String value() {
		return this.value;
	}
}

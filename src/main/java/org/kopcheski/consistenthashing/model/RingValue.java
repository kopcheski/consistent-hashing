package org.kopcheski.consistenthashing.model;

public sealed interface RingValue permits NodeId, Key {
	
	String value();
}

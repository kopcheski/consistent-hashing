package org.kopcheski.consistenthashing;

import java.util.HashSet;
import java.util.Set;

public class HashRing {

	private Set<String> ring;

	public HashRing() {
		this.ring = new HashSet<>();
	}

	public void addNode(String nodeId, int replicas) {
		this.ring.add(nodeId);
	}

	int nodesCount() {
		return ring.size();
	}
}

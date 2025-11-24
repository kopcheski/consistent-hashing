package org.kopcheski.consistenthashing;

import java.util.HashSet;
import java.util.Set;

public class HashRing {

	private Set<String> ring;

	public HashRing() {
		this.ring = new HashSet<>();
	}

	public void addNode(String nodeId, int replicas) {
		for (; replicas > 0; replicas--) {
			String virtualNodeId = "%s_%d".formatted(nodeId, replicas);
			int idHashCode = virtualNodeId.hashCode();
			this.ring.add(virtualNodeId);
		}
	}

	int nodesCount() {
		return ring.size();
	}
}

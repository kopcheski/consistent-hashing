package org.kopcheski.consistenthashing;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.IntConsumer;

public class HashRing {

	private final Map<Integer, String> ring;

	private final Map<String, Integer> serversMeta;

	public HashRing() {
		this.ring = new TreeMap<>();
		this.serversMeta = new HashMap<>();
	}

	public void addNode(String nodeId, int replicas) {
		this.serversMeta.computeIfPresent(nodeId, (k, v) -> { throw new IllegalArgumentException("Node already exists"); });

		this.serversMeta.put(nodeId, replicas);
		acceptOnEachNode(nodeId, replicas, nodeIdHash -> this.ring.put(nodeIdHash, null));
	}

	public void removeNode(String nodeId) {
		this.serversMeta.computeIfAbsent(nodeId, k -> { throw new IllegalArgumentException("Node already exists"); });

		acceptOnEachNode(nodeId, this.serversMeta.get(nodeId), this.ring::remove);
		this.serversMeta.remove(nodeId);
	}

	private void acceptOnEachNode(String nodeId, int replicas, IntConsumer consumer) {
		consumer.accept(nodeId.hashCode());
		for (; replicas > 0; replicas--) {
			String virtualNodeId = "%s_%d".formatted(nodeId, replicas);
			int idHashCode = virtualNodeId.hashCode();
			consumer.accept(idHashCode);
		}
	}

	boolean isNodePresent(String nodeId) {
		return ring.containsKey(nodeId.hashCode());
	}

	int nodesCount() {
		return ring.size();
	}
}

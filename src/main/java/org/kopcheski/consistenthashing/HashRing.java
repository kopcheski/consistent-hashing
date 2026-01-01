package org.kopcheski.consistenthashing;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.IntConsumer;

public class HashRing {

	private final Map<Integer, String> ring;

	private final Map<String, Integer> serversMeta;

	private final HashFunction hashFunction;

	public HashRing() {
		this.ring = new TreeMap<>();
		this.serversMeta = new HashMap<>();
		this.hashFunction = new HashFunction();
	}

	public void addNode(Node node, int replicas) {
		var nodeId = node.getId();
		this.serversMeta.computeIfPresent(nodeId, (k, v) -> { throw new IllegalArgumentException("Node already exists"); });

		this.serversMeta.put(nodeId, replicas);
		acceptOnEachNode(node, replicas, nodeIdHash -> this.ring.put(nodeIdHash, null));
	}

	public void removeNode(Node node) {
		var nodeId = node.getId();
		this.serversMeta.computeIfAbsent(nodeId, k -> { throw new IllegalArgumentException("Node already exists"); });

		acceptOnEachNode(node, this.serversMeta.get(nodeId), this.ring::remove);
		this.serversMeta.remove(nodeId);
	}

	private void acceptOnEachNode(Node node, int replicas, IntConsumer consumer) {
		var nodeId = node.getId();
		consumer.accept(hashFunction.hash(nodeId));
		for (; replicas > 0; replicas--) {
			String virtualNodeId = "%s_%d".formatted(nodeId, replicas);
			int idHashCode = hashFunction.hash(virtualNodeId);
			consumer.accept(idHashCode);
		}
	}

	boolean isNodePresent(Node node) {
		return ring.containsKey(hashFunction.hash(node.getId()));
	}

	int nodesCount() {
		return ring.size();
	}

}

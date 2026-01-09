package org.kopcheski.consistenthashing;

import org.kopcheski.consistenthashing.model.Hash;
import org.kopcheski.consistenthashing.model.Key;
import org.kopcheski.consistenthashing.model.NodeId;
import org.kopcheski.consistenthashing.model.RingValue;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Consumer;

public class HashRing {

	private final TreeMap<Hash, RingValue> ring;

	// to keep track of how many nodes each node have
	private final Map<NodeId, Integer> serversMeta;

	private final HashFunction hashFunction;

	public HashRing() {
		this.ring = new TreeMap<>();
		this.serversMeta = new HashMap<>();
		this.hashFunction = new HashFunction();
	}

	public NodeId findNodeId(Key key) {
		if (ring.isEmpty()) {
			throw new IllegalStateException("Ring is empty");
		}
		var hash = hashFunction.hash(key);
		if (ring.containsKey(hash)) {
			SortedMap<Hash, RingValue> tailMap = ring.tailMap(hash);
			hash = tailMap.isEmpty() ? ring.firstKey() : tailMap.entrySet().stream()
					.filter(entry -> entry.getValue() instanceof NodeId)
					.map(Map.Entry::getKey)
					.findFirst()
					.orElse(ring.firstKey());
		}
		return (NodeId) ring.get(hash);
	}

	public void addKey(Key key) {
		ring.put(hashFunction.hash(key), key);
	}

	// fixit: an instance of Node shouldn't be kept here, only its id instead.
	// the Node represents a remote storage, so keeping instances of it is undoable.
	public void addNode(Node node, int replicas) {
		var nodeId = node.getId();
		this.serversMeta.computeIfPresent(nodeId, (k, v) -> { throw new IllegalArgumentException("Node already exists"); });

		this.serversMeta.put(nodeId, replicas);
		acceptOnEachNode(node, replicas, nodeIdHash -> this.ring.put(nodeIdHash, nodeId));
	}

	public void removeNode(Node node) {
		var nodeId = node.getId();
		this.serversMeta.computeIfAbsent(nodeId, k -> { throw new IllegalArgumentException("Node already exists"); });

		acceptOnEachNode(node, this.serversMeta.get(nodeId), this.ring::remove);
		this.serversMeta.remove(nodeId);
	}

	private void acceptOnEachNode(Node node, int replicas, Consumer<Hash> consumer) {
		var nodeId = node.getId();
		consumer.accept(hashFunction.hash(nodeId));
		for (; replicas > 0; replicas--) {
			String virtualNodeId = "%s_%d".formatted(nodeId, replicas);
			var hash = hashFunction.hash(new Key(virtualNodeId));
			consumer.accept(hash);
		}
	}

	boolean isNodePresent(Node node) {
		return ring.containsKey(hashFunction.hash(node.getId()));
	}

	int nodesCount() {
		return ring.size();
	}
}

package org.kopcheski.consistenthashing;

import org.kopcheski.consistenthashing.model.Key;
import org.kopcheski.consistenthashing.model.NodeId;

import java.util.HashMap;
import java.util.Map;

public class Client {

	private final HashRing hashRing;

	private Map<String, Node> nodes;

	public Client() {
		hashRing = new HashRing();
		nodes = Map.of("A", new Node("A"), "B", new Node("B"), "C", new Node("C"));
		nodes.values().forEach(node -> hashRing.addNode(node, 0));
	}

	public void put(String key, String value) {
		Key keyObject = new Key(key);
		var nodeId = hashRing.findNodeId(keyObject);
		if (nodeId == null) {
			hashRing.addKey(keyObject);
			nodeId = hashRing.findNodeId(keyObject);
		}
		nodes.get(nodeId.value()).add(key, value);
	}

	public String get(String key) {
		NodeId nodeId = hashRing.findNodeId(new Key(key));
		return nodes.get(nodeId.value()).readValue(key);
	}

	void addNewNode(String id) {
		nodes = new HashMap<>(nodes);
		nodes.put(id, new Node(id));
		hashRing.addNode(nodes.get(id), 0);
		// TODO ebalance keys
	}

	void removeNode(NodeId nodeId) {
		hashRing.removeNode(nodes.get(nodeId.value()));
		var removedNode = nodes.remove(nodeId.value());
		// TODO rebalance keys : check video for "ideal" rebalance.
		Map<String, String> nodesData = removedNode.dumpData();
		nodesData.forEach((key, value) -> put(key, value));
	}

	NodeId findNode(String key) {
		return hashRing.findNodeId(new Key(key));
	}

}

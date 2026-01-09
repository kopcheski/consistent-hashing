package org.kopcheski.consistenthashing;

import org.kopcheski.consistenthashing.model.Key;
import org.kopcheski.consistenthashing.model.NodeId;

import java.util.Map;

public class Client {

	private final HashRing hashRing;

	private final Map<String, Node> nodes;

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

}

package org.kopcheski.consistenthashing;

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
		var nodeId = hashRing.findNodeId(key);
		if (nodeId == null) {
			hashRing.addKey(key);
			nodeId = hashRing.findNodeId(key);
		}
		nodes.get(nodeId).add(key, value);
	}

	public String get(String key) {
		String nodeId = hashRing.findNodeId(key);
		return nodes.get(nodeId).readValue(key);
	}

}

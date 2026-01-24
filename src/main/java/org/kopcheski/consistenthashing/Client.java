package org.kopcheski.consistenthashing;

import org.kopcheski.consistenthashing.model.Key;
import org.kopcheski.consistenthashing.model.NodeId;

import java.util.HashMap;
import java.util.Map;

public class Client {

	private final HashRing hashRing;

	private Map<String, Node> nodes;

	public Client() {
		this(Map.of("A", new Node("A"), "B", new Node("B"), "C", new Node("C")));
	}

	Client(Map<String, Node> nodes) {
		hashRing = new HashRing();
		this.nodes = nodes;
		nodes.values().forEach(node -> hashRing.addNode(node.getId(), 0));
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
		Node newNode = new Node(id);
		nodes.put(id, newNode);
		hashRing.addNode(nodes.get(id).getId(), 0);

		NodeId newNodeId = newNode.getId();
		NodeId nodeId = hashRing.findNextNode(newNodeId);
		transfer(nodes.get(nodeId.value()), nodes.get(newNodeId.value()));
	}

	void removeNode(NodeId nodeId) {
		hashRing.removeNode(nodeId);
		var removedNode = nodes.remove(nodeId.value());

		Map<String, String> nodesData = removedNode.dumpData();
		nodesData.forEach(this::put);
	}

	NodeId findNode(String key) {
		return hashRing.findNodeId(new Key(key));
	}

	String getOwningNode(String key) {
		NodeId value = hashRing.findNodeId(new Key(key));
		// TODO: ideally double checking if the value is really where it was supposed to be is redundant. How to avoid that?
		nodes.get(value.value()).readValue(key);

		return value.value();
	}

	private void transfer(Node originNode, Node destinationNode) {
		Map<String, String> data = originNode.dumpData();
		data.forEach(destinationNode::add);
	}
}

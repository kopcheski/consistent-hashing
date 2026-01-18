package org.kopcheski.consistenthashing;

import org.kopcheski.consistenthashing.model.NodeId;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Node {

	static final int SIZE = 1_000;

	private final NodeId id;

	private final Map<String, String> storage = new HashMap<>();

	public Node(String id) {
		this.id = new NodeId(id);
	}

	public void add(String key, String value) {
		dropAnyIfStorageIsFull();
		this.storage.put(key, value);
	}

	private void dropAnyIfStorageIsFull() {
		if (storage.size() >= SIZE) {
			String any = this.storage.keySet()
					.stream()
					.findAny()
					.get();
			storage.remove(any);
		}
	}

	public String readValue(String key) {
		String value = this.storage.get(key);
		if (value == null) {
			throw new IllegalStateException("There's no value stored for this key");
		}

		return value;
	}

	public String removeValue(String key) {
		return this.storage.remove(key);
	}

	public Map<String, String> dumpData() {
		return new HashMap<>(this.storage);
	}

	public NodeId getId() {
		return this.id;
	}

	//TODO: OOP-wise, this looks good. However, this represent remote entities (nodes), so perhaps this communication
	// should be driven by the Client.
	public void transfer(Set<String> keys, Node originNode) {
		keys.forEach(key -> this.add(key, originNode.removeValue(key)));
	}
}

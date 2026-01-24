package org.kopcheski.consistenthashing;

import org.kopcheski.consistenthashing.model.NodeId;

import java.util.HashMap;
import java.util.Map;

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

}

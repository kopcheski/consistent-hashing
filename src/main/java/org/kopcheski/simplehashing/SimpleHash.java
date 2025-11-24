package org.kopcheski.simplehashing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleHash {

	private final Map<Integer, List<String>> dataServers = new HashMap<>();

	private final int serverCount;

	public SimpleHash(int servers) {
		this.serverCount = servers;
	}

	public void store(String data) {
		int key = Math.abs(data.hashCode()) % serverCount;
		dataServers.computeIfAbsent(key, k -> new ArrayList<>()).add(data);
	}

	public List<Integer> dataCountPerServer() {
		return dataServers.values()
				.stream()
				.map(List::size)
				.toList();
	}
}

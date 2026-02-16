package org.kopcheski.consistenthashing;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class ComponentTest {

	@Test
	void test() {
		var client = new Client(nodes(150));
		IntStream.range(0, 40_000).forEach(i -> {
			var key = "key_" + i;
			var value = "value_" + i;
			client.put(key, value);
		});

		client.dumpCountPerNode();

	}

	private Map<String, Node> nodes(int count) {
		return IntStream.range(0, count)
				.mapToObj(i -> "node_" + i)
				.collect(Collectors.toMap(id -> id, Node::new));
	}

}

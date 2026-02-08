package org.kopcheski.consistenthashing;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.stream.IntStream;

class ComponentTest {

	@Test
	void test() {
		var client = new Client(Map.of("A", new Node("A"), "B", new Node("B"), "C", new Node("C")));
		IntStream.range(0, 10_000).forEach(i -> {
			var key = "key_" + i;
			var value = "value_" + i;
			client.put(key, value);
		});

		client.dumpCountPerNode();

	}
}

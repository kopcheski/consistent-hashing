package org.kopcheski.consistenthashing;

import org.junit.jupiter.api.Test;

import java.util.IntSummaryStatistics;
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

		Map<String, Node> nodes = client.getNodes();

		IntSummaryStatistics stats = nodes.values().stream()
				.mapToInt(node -> node.dumpData().size())
				.summaryStatistics();

		System.out.println("Node Usage Statistics:");
		System.out.println("Total Nodes: " + stats.getCount());
		System.out.println("Total Keys Stored: " + stats.getSum());
		System.out.println("Min Keys per Node: " + stats.getMin());
		System.out.println("Max Keys per Node: " + stats.getMax());
		System.out.println("Average Keys per Node: " + stats.getAverage());
	}

	private Map<String, Node> nodes(int count) {
		return IntStream.range(0, count)
				.mapToObj(i -> "node_" + i)
				.collect(Collectors.toMap(id -> id, Node::new));
	}

}

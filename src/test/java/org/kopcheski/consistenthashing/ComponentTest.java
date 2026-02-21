package org.kopcheski.consistenthashing;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.util.IntSummaryStatistics;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class ComponentTest {

	private static final Logger logger = LogManager.getLogger(ComponentTest.class);

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

		logger.info("Node Usage Statistics:");
		logger.info("Total Nodes: {}", stats.getCount());
		logger.info("Total Keys Stored: {}", stats.getSum());
		logger.info("Min Keys per Node: {}", stats.getMin());
		logger.info("Max Keys per Node: {}", stats.getMax());
		logger.info("Average Keys per Node: {}", stats.getAverage());
	}

	private Map<String, Node> nodes(int count) {
		return IntStream.range(0, count)
				.mapToObj(i -> "node_" + i)
				.collect(Collectors.toMap(id -> id, Node::new));
	}

}

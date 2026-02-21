package org.kopcheski.consistenthashing;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.IntSummaryStatistics;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ConsistentHashingApp {

	private static final Logger logger = LogManager.getLogger(ConsistentHashingApp.class);
	private static final int DEFAULT_KEY_COUNT = 40_000;
	private static final int DEFAULT_NODE_COUNT = 150;

	public static void main(String[] args) {
		int keyCount = DEFAULT_KEY_COUNT;
		if (args.length > 0) {
			try {
				keyCount = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				logger.warn("Invalid key count argument: {}. Using default value {}.", args[0], DEFAULT_KEY_COUNT);
			}
		}

		var client = new Client(nodes(DEFAULT_NODE_COUNT));
		IntStream.range(0, keyCount).forEach(i -> {
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

	private static Map<String, Node> nodes(int count) {
		return IntStream.range(0, count)
				.mapToObj(i -> "node_" + i)
				.collect(Collectors.toMap(id -> id, Node::new));
	}

}

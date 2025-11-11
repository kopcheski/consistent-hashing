package org.kopcheski.consistenthashing;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HashRingTest {

	@Test
	void testAddNode() {
		var hashRing = new HashRing();
		int replicasEach = 2;
		List<String> nodeIds = List.of("A", "B");
		nodeIds.forEach(id -> hashRing.addNode(id, replicasEach));

		int expected = nodeIds.size() * replicasEach;
		assertEquals(expected, hashRing.nodesCount());
	}

}

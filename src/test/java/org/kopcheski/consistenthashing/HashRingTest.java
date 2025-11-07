package org.kopcheski.consistenthashing;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HashRingTest {

	@Test
	void testAddNode() {
		var hashRing = new HashRing();
		hashRing.addNode("A", 2);
		hashRing.addNode("B", 2);

		assertEquals(2, hashRing.nodesCount());
	}

}

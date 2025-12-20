package org.kopcheski.consistenthashing;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class HashRingTest {

	@Test
	void testAddNode() {
		String serverA = "A";
		String serverB = "B";

		var hashRing = new HashRing();
		hashRing.addNode(serverA, 0);
		hashRing.addNode(serverB, 0);

		assertTrue(hashRing.isNodePresent(serverA));
		assertTrue(hashRing.isNodePresent(serverB));
	}

}

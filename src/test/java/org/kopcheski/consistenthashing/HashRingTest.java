package org.kopcheski.consistenthashing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HashRingTest {

	private HashRing hashRing;

	@BeforeEach
	void setUp() {
		hashRing = new HashRing();
	}

	@Test
	void testAddNode() {
		String serverA = "A";
		String serverB = "B";

		hashRing.addNode(serverA, 0);
		hashRing.addNode(serverB, 0);

		assertTrue(hashRing.isNodePresent(serverA));
		assertTrue(hashRing.isNodePresent(serverB));
	}

	@Test
	void testAddNodeFailsWhenItAlreadyExists() {
		hashRing.addNode("A", 0);
		assertThrows(IllegalArgumentException.class, () -> hashRing.addNode("A", 0));
	}

}

package org.kopcheski.consistenthashing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddNodeTest {

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
	void testReplicasPresentInTheRing() {
		String serverA = "A";
		int replicas = 3;

		hashRing.addNode(serverA, replicas);

		assertEquals(replicas + 1, hashRing.nodesCount(),
				"A total # of replicas + 1 server should be present in the ring.");
	}

	@Test
	void testAddNodeFailsWhenItAlreadyExists() {
		hashRing.addNode("A", 0);
		assertThrows(IllegalArgumentException.class, () -> hashRing.addNode("A", 0));
	}

}

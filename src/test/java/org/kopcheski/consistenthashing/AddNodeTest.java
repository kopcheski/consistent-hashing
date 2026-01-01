package org.kopcheski.consistenthashing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddNodeTest {

	private HashRing hashRing;

	private Node nodeA, nodeB;

	@BeforeEach
	void setUp() {
		hashRing = new HashRing();
		nodeA = new Node("A");
		nodeB = new Node("B");
	}

	@Test
	void testAddNode() {
		hashRing.addNode(nodeA, 0);
		hashRing.addNode(nodeB, 0);

		assertTrue(hashRing.isNodePresent(nodeA));
		assertTrue(hashRing.isNodePresent(nodeB));
	}

	@Test
	void testReplicasPresentInTheRing() {
		int replicas = 3;

		hashRing.addNode(nodeA, replicas);

		assertEquals(replicas + 1, hashRing.nodesCount(),
				"A total # of replicas + 1 server should be present in the ring.");
	}

	@Test
	void testAddNodeFailsWhenItAlreadyExists() {
		hashRing.addNode(nodeA, 0);
		assertThrows(IllegalArgumentException.class, () -> hashRing.addNode(nodeA, 0));
	}

}

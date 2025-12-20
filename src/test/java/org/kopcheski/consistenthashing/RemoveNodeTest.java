package org.kopcheski.consistenthashing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RemoveNodeTest {

	private HashRing hashRing;

	@BeforeEach
	void setUp() {
		hashRing = new HashRing();
	}

	@Test
	void removeUnexistentNodeFails() {
		assertThrows(IllegalArgumentException.class, () -> hashRing.removeNode("A"));
	}

	@Test
	void removeExistentNodeSucceeds() {
		String nodeId = "A";

		hashRing.addNode(nodeId, 0);
		assertTrue(hashRing.isNodePresent(nodeId));

		hashRing.removeNode(nodeId);
		assertFalse(hashRing.isNodePresent(nodeId));
	}
}

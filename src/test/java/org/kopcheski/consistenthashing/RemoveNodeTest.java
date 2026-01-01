package org.kopcheski.consistenthashing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RemoveNodeTest {

	private HashRing hashRing;

	private Node node;

	@BeforeEach
	void setUp() {
		hashRing = new HashRing();
		node = new Node("A");
	}

	@Test
	void removeUnexistentNodeFails() {
		assertThrows(IllegalArgumentException.class, () -> hashRing.removeNode(node));
	}

	@Test
	void removeExistentNodeSucceeds() {
		hashRing.addNode(node, 0);
		assertTrue(hashRing.isNodePresent(node));

		hashRing.removeNode(node);
		assertFalse(hashRing.isNodePresent(node));
	}
}

package org.kopcheski.consistenthashing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

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
}

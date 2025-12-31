package org.kopcheski;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {

	private static final String KEY = "key";

	private Node storageNode;

	@BeforeEach
	void setUp() {
		this.storageNode = new Node("node-1");
	}

	@Test
	void testAddedValueIsStored() {
		this.storageNode.add(KEY, "value");
		assertEquals("value", this.storageNode.readValue(KEY));
	}

	@Test
	void testAlreadyPresentValueIsOverwritten() {
		this.storageNode.add(KEY, "value");
		this.storageNode.add(KEY, "newValue");
		assertEquals("newValue", this.storageNode.readValue(KEY));
	}

	@Test
	void testStorageStaysAtIsMaximumSizeWhenAddingMoreThanItSupports() {
		for (int i = 0; i <= Node.SIZE + 2; i++) {
			this.storageNode.add(KEY + i, "value" + i);
		}

		assertEquals(Node.SIZE, this.storageNode.dumpData().size());
	}

	@Test
	void testRemoveExistingValue() {
		this.storageNode.add(KEY, "value");
		this.storageNode.removeValue(KEY);
		assertThrows(IllegalStateException.class, () -> this.storageNode.readValue(KEY));
	}

	@Test
	void testRemoveNonExistingValueWorksSilently() {
		assertDoesNotThrow(() -> this.storageNode.removeValue(KEY));
	}
}

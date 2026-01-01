package org.kopcheski.consistenthashing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {

	private static final String KEY = "key";
	
	private Node storageNode;

	@BeforeEach
	void setUp() {
		this.storageNode = new Node("node-1");
	}

	@Nested
	class GivenAValueBeingAdded {

		@Test
		void thenItIsStored() {
			storageNode.add(KEY, "value");
			assertEquals("value", storageNode.readValue(KEY));
		}

		@Test
		void whenInAKeyThatIsAlreadyPresent_thenTheOldValueIsOverwritten() {
			storageNode.add(KEY, "value");
			storageNode.add(KEY, "newValue");
			assertEquals("newValue", storageNode.readValue(KEY));
		}

		@Test
		void whenTheStorageIsFull_thenItDropAnyValueToFitTheNewOne() {
			for (int i = 0; i <= Node.SIZE + 2; i++) {
				storageNode.add(KEY + i, "value" + i);
			}

			assertEquals(Node.SIZE, storageNode.dumpData().size());
		}

	}

	@Nested
	class GivenAValueBeingRead {

		@Test
		void whenItDoesNotExist_thenItFails() {
			assertThrows(IllegalStateException.class, () -> storageNode.readValue(KEY));
		}

	}

	@Nested
	class GivenAValueBeingRemoved {

		@Test
		void whenItExists_thenItIsRemoved() {
			storageNode.add(KEY, "value");
			storageNode.removeValue(KEY);
			assertThrows(IllegalStateException.class, () -> storageNode.readValue(KEY));
		}

		@Test
		void whenItDoesNotExist_thenItWorksSilently() {
			assertDoesNotThrow(() -> storageNode.removeValue(KEY));
		}

	}

}

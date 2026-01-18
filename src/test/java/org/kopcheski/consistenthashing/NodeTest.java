package org.kopcheski.consistenthashing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;

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

	@Test
	void testTransferData() {
		var value = "value";
		storageNode.add(KEY, value);

		var destination = new Node("destination");
		destination.transfer(Set.of(KEY), storageNode);

		assertEquals(value, destination.readValue(KEY), "The \"value\" should have been transferred to the destination node.");
		assertThrows(IllegalStateException.class, () -> storageNode.readValue(KEY), "The \"value\" should not be in this node anymore.");
	}

}

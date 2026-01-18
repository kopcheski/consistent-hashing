package org.kopcheski.consistenthashing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.kopcheski.consistenthashing.model.Key;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class HashRingTest {

	private HashRing hashRing;
	private Node nodeA, nodeB, nodeC;
	private Key key1, key2, key3, key4;


	@BeforeEach
	void setUp() {
		hashRing = new HashRing();
		nodeA = new Node("A");
		nodeB = new Node("B");
		nodeC = new Node("C");

		key1 = new Key("1");
		key2 = new Key("2");
		key3 = new Key("3");
		key4 = new Key("4");
	}
	
	@Nested
	class FindNodeTest {
		
		@Test
		void testFindNodeByKeyWhenTheRingIsEmpty() {
			assertThrows(IllegalStateException.class, () -> hashRing.findNodeId(key1));
		}

		/*
		 * The ring is represented as a circle, where the nodes are placed based on their hash.
		 * The keys are also placed on the ring based on their hash.
		 * To find the node responsible for a key, we move clockwise on the ring until we find a node.
		 *
		 * The hashes for the nodes and keys are:
		 *
		 * Node/Key | Hash
		 * ---------|-----------
		 * Key 1    | -1810453357
		 * Node B   |  -861508982
		 * Key 4    |  -516830072
		 * Node C   |  -367198581
		 * Key 2    |    19522071
		 * Key 3    |   264741300
		 * Node A   |  1423767502
		 *
		 * Based on the hashes, the ring looks like this:
		 *
		 * ... -> Key 1 -> Node B -> Key 4 -> Node C -> Key 2 -> Key 3 -> Node A -> ...
		 *
		 * So:
		 * - Key 1 belongs to Node B
		 * - Key 2 belongs to Node A
		 * - Key 3 belongs to Node A
		 * - Key 4 belongs to Node C
		 */
		@Test
		void testFindNodeByKey() {
			initialLoadOfKeysAndNodes();
			assertionOfInitialLoadOfKeysAndNodes();
		}

		/*
		 * As a next step on the state of previous test, a new node is added to cause rebalance of keys.
		 * Node/Key   | Hash
		 * -----------|-----------
		 * Key 1      | -1810453357
		 * Node B     |  -861508982
		 * Key 4      |  -516830072
		 * Node C     |  -367198581
		 * Key 2      |    19522071
		 * Key 3      |   264741300
		 * *Node new* |  *422208903*
		 * Node A     |  1423767502
		 *
		 * The addition of a new node causes the keys previously from 'Node A' to be moved to it.
		 */
		@Test
		void testAddNodeToLiveRing() {
			initialLoadOfKeysAndNodes();
			assertionOfInitialLoadOfKeysAndNodes();

			var nodeNew = new Node("new");
			hashRing.addNode(nodeNew, 0);

			//keys 1 and 4 still belong to nodes B and C respectively.
			assertEquals(nodeB.getId(), hashRing.findNodeId(key1));
			assertEquals(nodeC.getId(), hashRing.findNodeId(key4));

			//but now keys 2 and 3 belong to node new instead of node A.
			assertEquals(nodeNew.getId(), hashRing.findNodeId(key2));
			assertEquals(nodeNew.getId(), hashRing.findNodeId(key3));
		}

		@Test
		void testFindNextNode() {
			initialLoadOfKeysAndNodes();
			assertEquals(nodeC.getId(), hashRing.findNextNode(nodeB.getId()));
		}

	}

	@Nested
	class AddKeyTest {

	}

	@Nested
	class AddNodeTest {

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

	@Nested
	class RemoveNodeTest {

		@Test
		void removeUnexistentNodeFails() {
			assertThrows(IllegalArgumentException.class, () -> hashRing.removeNode(nodeA));
		}

		@Test
		void removeExistentNodeSucceeds() {
			hashRing.addNode(nodeA, 0);
			assertTrue(hashRing.isNodePresent(nodeA));

			hashRing.removeNode(nodeA);
			assertFalse(hashRing.isNodePresent(nodeA));
		}
	}

	@Nested
	class ReadKeysFromNodeTest {

		@Test
		void testReadKeysFromNode() {
			initialLoadOfKeysAndNodes();
			Set<String> allKeys = hashRing.getAllKeys(nodeA.getId());
			assertEquals(Set.of(key2.value(), key3.value()), allKeys);
		}

	}

	private void assertionOfInitialLoadOfKeysAndNodes() {
		assertEquals(nodeB.getId(), hashRing.findNodeId(key1));
		assertEquals(nodeA.getId(), hashRing.findNodeId(key2));
		assertEquals(nodeA.getId(), hashRing.findNodeId(key3));
		assertEquals(nodeC.getId(), hashRing.findNodeId(key4));
	}

	/**
	 * Adds Nodes and Keys with the following disposition.
	 * Node/Key | Hash
	 * ---------|-----------
	 * Key 1    | -1810453357
	 * Node B   |  -861508982
	 * Key 4    |  -516830072
	 * Node C   |  -367198581
	 * Key 2    |    19522071
	 * Key 3    |   264741300
	 * Node A   |  1423767502
	 */
	private void initialLoadOfKeysAndNodes() {
		addNodes();
		addKeys();
	}

	private void addKeys() {
		hashRing.addKey(key1);
		hashRing.addKey(key2);
		hashRing.addKey(key3);
		hashRing.addKey(key4);
	}

	private void addNodes() {
		hashRing.addNode(nodeA, 0);
		hashRing.addNode(nodeB, 0);
		hashRing.addNode(nodeC, 0);
	}
}

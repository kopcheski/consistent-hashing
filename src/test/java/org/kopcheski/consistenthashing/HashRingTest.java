package org.kopcheski.consistenthashing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HashRingTest {

	private HashRing hashRing;
	private HashFunction hashFunction;
	private Node nodeA, nodeB, nodeC;

	@BeforeEach
	void setUp() {
		hashRing = new HashRing();
		hashFunction = new HashFunction();
		nodeA = new Node("A");
		nodeB = new Node("B");
		nodeC = new Node("C");
	}
	
	@Nested
	class FindNodeTest {
		
		@Test
		void testFindNodeByKeyWhenTheRingIsEmpty() {
			assertThrows(IllegalStateException.class, () -> hashRing.findNodeId("A"));
		}

		@Test
		void testFindNodeByKey() {
			hashRing.addNode(nodeA, 0); //NA 1423767502
			hashRing.addNode(nodeB, 0); //NB -861508982
			hashRing.addNode(nodeC, 0); //NC -367198581

			var key1 = "1"; //K1 -1810453357
			var key2 = "2"; //K2    19522071
			var key3 = "3"; //K3   264741300

			assertEquals(nodeB.getId(), hashRing.findNodeId("1"));
			assertEquals(nodeA.getId(), hashRing.findNodeId("2"));
			assertEquals(nodeA.getId(), hashRing.findNodeId("3"));


			//K1 -1810453357
			//NB  -861508982

			//NC  -367198581

			//K2    19522071
			//K3   264741300
			//NA  1423767502

			// nodeB = 1
			// nodeC =
			// nodeA = 2, 3

			assertEquals(nodeB.getId(), hashRing.findNodeId(key1));
			assertEquals(nodeA.getId(), hashRing.findNodeId(key2));
			assertEquals(nodeA.getId(), hashRing.findNodeId(key3));
			System.out.printf("The hash of key %s is %d%n", key1, hashFunction.hash(key1));
			System.out.printf("The hash of key %s is %d%n", key2, hashFunction.hash(key2));
			System.out.printf("The hash of key %s is %d%n", key3, hashFunction.hash(key3));
			System.out.printf("The hash of node %s is %d%n", "A", hashFunction.hash("A"));
			System.out.printf("The hash of node %s is %d%n", "B", hashFunction.hash("B"));
			System.out.printf("The hash of node %s is %d%n", "C", hashFunction.hash("C"));
		}

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
}

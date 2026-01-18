package org.kopcheski.consistenthashing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClientTest {

	private Client client;

	@BeforeEach
	void setUp() {
		client = new Client();
	}

	@Test
	void testStoreValue() {
		client.put("key", "value");
		assertEquals("value", client.get("key"));
	}

	@Test
	void testFindValueAfterNodeIsRemoved() {
		client.put("key", "value");

		var node = client.findNode("key");
		client.addNewNode("D");
		client.removeNode(node);

		assertEquals("value", client.get("key"));
	}

}

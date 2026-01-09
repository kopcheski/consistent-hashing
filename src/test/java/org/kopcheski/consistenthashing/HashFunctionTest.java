package org.kopcheski.consistenthashing;

import org.junit.jupiter.api.Test;
import org.kopcheski.consistenthashing.model.Key;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HashFunctionTest {

	@Test
	void testHashIsConsistent() {
		HashFunction hashFunction = new HashFunction();
		Key testKey = new Key("test");
		assertEquals(hashFunction.hash(testKey), hashFunction.hash(testKey));
	}

}

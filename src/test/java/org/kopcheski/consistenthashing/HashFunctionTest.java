package org.kopcheski.consistenthashing;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HashFunctionTest {

	@Test
	void testHashIsConsistent() {
		HashFunction hashFunction = new HashFunction();
		assertEquals(hashFunction.hash("test"), hashFunction.hash("test"));
	}

}

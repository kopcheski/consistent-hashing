package org.kopcheski.simplehashing;

import org.junit.jupiter.api.Test;

class SimpleHashTest {

	@Test
	void testSimpleHash() {
		var simpleHash = new SimpleHash(5);

		for (int i = 0; i < 10_000; i++) {
			simpleHash.store("");
		}
	}

}
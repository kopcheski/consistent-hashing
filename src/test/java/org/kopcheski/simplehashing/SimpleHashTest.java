package org.kopcheski.simplehashing;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SimpleHashTest {

	private static final int SERVERS = 5;
	private static final int ENTRIES_COUNT = 10_000;
	private static final double TOLERANCE_PCT = .05;

	@Test
	@Disabled
	//FIXME non-deterministic test.
	void testEntriesDistributedEvenlyAcrossServers() {
		var simpleHash = new SimpleHash(SERVERS);

		for (int i = 0; i < ENTRIES_COUNT; i++) {
			byte[] array = new byte[7];
			new SecureRandom().nextBytes(array);
			var generatedString = new String(array, StandardCharsets.UTF_8);

			simpleHash.store(generatedString);
		}

		List<Integer> dataCount = simpleHash.dataCountPerServer();
		assertEquals(SERVERS, dataCount.size());
		dataCount.forEach(entry -> assertTrue(approximation(entry)));
	}

	private boolean approximation(Integer value) {
		int avg = ENTRIES_COUNT / SERVERS;
		int max = (int) (avg * (1 + TOLERANCE_PCT));
		int min = (int) (avg * (1 - TOLERANCE_PCT));
		return value <= max && value >= min;
	}

}
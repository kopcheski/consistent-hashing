package org.kopcheski.consistenthashing;

import static org.apache.commons.codec.digest.MurmurHash3.hash32x86;

public class HashFunction {

	public int hash(String key) {
		return hash32x86(key.getBytes());
	}

}

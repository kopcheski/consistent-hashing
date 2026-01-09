package org.kopcheski.consistenthashing;

import org.kopcheski.consistenthashing.model.Hash;

import static org.apache.commons.codec.digest.MurmurHash3.hash32x86;

public class HashFunction {

	public Hash hash(String key) {
		return new Hash(hash32x86(key.getBytes()));
	}

}

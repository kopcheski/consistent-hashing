package org.kopcheski.consistenthashing;

import org.kopcheski.consistenthashing.model.Hash;
import org.kopcheski.consistenthashing.model.Hashable;

import static org.apache.commons.codec.digest.MurmurHash3.hash32x86;

public class HashFunction {

	public Hash hash(Hashable key) {
		return new Hash(hash32x86(key.value().getBytes()));
	}

}

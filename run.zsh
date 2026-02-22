#!/bin/zsh
mvnd compile exec:java -Dexec.mainClass="org.kopcheski.consistenthashing.ConsistentHashingApp" -Dexec.args="$*"
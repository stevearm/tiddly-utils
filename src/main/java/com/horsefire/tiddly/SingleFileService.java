package com.horsefire.tiddly;

public interface SingleFileService {

	byte[] get();

	void put(byte[] file);
}

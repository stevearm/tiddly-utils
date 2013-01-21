package com.horsefire.tiddly;

import java.io.IOException;

public interface SingleFileService {

	byte[] get() throws IOException;

	void put(byte[] file) throws IOException;
}

package com.horsefire.tiddly;

import java.io.IOException;

public interface WikiService {

	Wiki get() throws IOException;

	void put(Wiki wiki) throws IOException;
}

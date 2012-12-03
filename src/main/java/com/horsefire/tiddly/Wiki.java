package com.horsefire.tiddly;

public final class Wiki {

	private final String m_header;
	private final String m_store;
	private final String m_postStore;
	private final String m_postScript;

	public Wiki(String header, String store, String postStore, String postScript) {
		m_header = header;
		m_store = store;
		m_postStore = postStore;
		m_postScript = postScript;
	}

	public String[] getParts() {
		return new String[] { m_header, m_store, m_postStore, m_postScript };
	}

	public String getHeader() {
		return m_header;
	}

	public String getStore() {
		return m_store;
	}

	public String getPostStore() {
		return m_postStore;
	}

	public String getPostScript() {
		return m_postScript;
	}

	public Wiki setStore(String store) {
		return new Wiki(m_header, store, m_postStore, m_postScript);
	}
}

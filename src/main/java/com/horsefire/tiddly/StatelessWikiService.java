package com.horsefire.tiddly;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatelessWikiService implements WikiService {

	private static final Logger LOG = LoggerFactory
			.getLogger(StatelessWikiService.class);

	private final SingleFileService m_fileService;

	public StatelessWikiService(SingleFileService fileService) {
		m_fileService = fileService;
	}

	@Override
	public Wiki get() {
		try {
			String wiki = new String(m_fileService.get(), "UTF-8");
			String tag = "<!--POST-SHADOWAREA-->";
			int from = wiki.indexOf(tag);
			if (from == -1) {
				LOG.warn("Could not find {} in wiki", tag);
				throw new IndexOutOfBoundsException(
						"Wiki is not formatted as expected");
			}
			from += tag.length();
			String header = wiki.substring(0, from);

			tag = "<!--POST-STOREAREA-->";
			int to = wiki.indexOf(tag, from);
			if (to == -1) {
				LOG.warn("Could not find {} in wiki", tag);
				throw new IndexOutOfBoundsException(
						"Wiki is not formatted as expected");
			}
			String store = wiki.substring(from, to);
			from = to;

			tag = "<!--POST-SCRIPT-END-->";
			to = wiki.indexOf(tag, from);
			if (to == -1) {
				LOG.warn("Could not find {} in wiki", tag);
				throw new IndexOutOfBoundsException(
						"Wiki is not formatted as expected");
			}
			String postStore = wiki.substring(from, to);
			String postScript = wiki.substring(to);
			return new Wiki(header, store, postStore, postScript);

		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(
					"UTF-8 not supported. Should never happen", e);
		}
	}

	@Override
	public void put(Wiki wiki) {
		try {
			String[] parts = wiki.getParts();
			int i = 0;
			for (String part : parts) {
				i += part.length();
			}
			byte[] file = new byte[i];
			i = 0;
			for (String part : parts) {
				byte[] partArray = part.getBytes("UTF-8");
				System.arraycopy(partArray, 0, file, i, partArray.length);
				i += partArray.length;
			}
			m_fileService.put(file);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(
					"UTF-8 not supported. Should never happen", e);
		}
	}
}

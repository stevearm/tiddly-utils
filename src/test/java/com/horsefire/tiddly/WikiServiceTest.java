package com.horsefire.tiddly;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.atomic.AtomicReference;

import junit.framework.TestCase;

public class WikiServiceTest extends TestCase {

	public static String getFile(String filename) throws IOException {
		InputStream in = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(filename);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuilder result = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			result.append(line).append('\n');
		}
		in.close();
		return result.toString();
	}

	public static void assertByLine(String expected, String actual)
			throws IOException {
		BufferedReader expectedReader = new BufferedReader(new StringReader(
				expected));
		BufferedReader actualReader = new BufferedReader(new StringReader(
				actual));

		String previous = null;
		while (true) {
			String expectedLine = expectedReader.readLine();
			String actualLine = actualReader.readLine();
			previous = actualLine;
			if (expectedLine == null) {
				if (actualLine != null) {
					fail("Expected null, but got '" + actualLine + "'");
				}
				return;
			}
			if (!expectedLine.equals(actualLine)) {
				System.out.println("Expected: " + expectedLine);
				System.out.println("Got:");
				System.out.println(previous);
				System.out.println(actualLine);
				System.out.println(actualReader.readLine());
				fail("Expected '" + expectedLine + "', but got '" + actualLine
						+ "'");
			}
		}
	}

	public void testRoundTrip() throws Exception {
		final String original = getFile("base.html");
		final AtomicReference<String> savedFile = new AtomicReference<String>();
		SingleFileService fileService = new SingleFileService() {
			@Override
			public byte[] get() {
				try {
					return original.getBytes("UTF-8");
				} catch (UnsupportedEncodingException e) {
					fail("Should never happen");
					throw new RuntimeException(e);
				}
			}

			@Override
			public void put(byte[] file) {
				try {
					savedFile.set(new String(file, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					fail("Should never happen");
					throw new RuntimeException(e);
				}
			}
		};

		WikiService service = new StatelessWikiService(fileService);
		service.put(service.get());
		assertByLine(original, savedFile.get());
	}
}

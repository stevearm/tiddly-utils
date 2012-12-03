package com.horsefire.tiddly;

import junit.framework.TestCase;

import org.easymock.EasyMock;

public class TiddlerServiceTest extends TestCase {

	public void testGet() throws Exception {
		final String file = WikiServiceTest.getFile("base.html");
		SingleFileService fileService = EasyMock.createMock(SingleFileService.class);
		EasyMock.expect(fileService.get()).andStubReturn(file.getBytes("UTF-8"));
		EasyMock.replay(fileService);

		TiddlerService service = new TiddlerService(new StatelessWikiService(
				fileService));
		Tiddler tiddler = service.get("Blank");
		assertEquals("Steve", tiddler.creator);
		assertEquals("Steve", tiddler.modifier);
		assertEquals("201111301833", tiddler.created);
		assertEquals("201201091915", tiddler.modified);
		assertEquals("2", tiddler.changecount);
		assertEquals("!!Simple tiddler\n* Important\n* Special\n* Epic",
				tiddler.content);
	}

	public void testAddNew() throws Exception {
		final String file = WikiServiceTest.getFile("base.html");
		SingleFileService fileService = EasyMock.createMock(SingleFileService.class);
		EasyMock.expect(fileService.get()).andStubReturn(file.getBytes("UTF-8"));
		EasyMock.replay(fileService);

		TiddlerService service = new TiddlerService(new StatelessWikiService(
				fileService));
		Tiddler tiddler = service.get("Blank");
		assertEquals("Steve", tiddler.creator);
		assertEquals("Steve", tiddler.modifier);
		assertEquals("201111301833", tiddler.created);
		assertEquals("201201091915", tiddler.modified);
		assertEquals("2", tiddler.changecount);
		assertEquals("!!Simple tiddler\n* Important\n* Special\n* Epic",
				tiddler.content);
	}

	public void testUpdateExisting() throws Exception {
		final String file = WikiServiceTest.getFile("base.html");
		SingleFileService fileService = EasyMock.createMock(SingleFileService.class);
		EasyMock.expect(fileService.get()).andStubReturn(file.getBytes("UTF-8"));
		EasyMock.replay(fileService);

		TiddlerService service = new TiddlerService(new StatelessWikiService(
				fileService));
		Tiddler tiddler = service.get("Blank");
		assertEquals("Steve", tiddler.creator);
		assertEquals("Steve", tiddler.modifier);
		assertEquals("201111301833", tiddler.created);
		assertEquals("201201091915", tiddler.modified);
		assertEquals("2", tiddler.changecount);
		assertEquals("!!Simple tiddler\n* Important\n* Special\n* Epic",
				tiddler.content);
	}

	public void testDelete() throws Exception {
		final String file = WikiServiceTest.getFile("base.html");
		SingleFileService fileService = EasyMock.createMock(SingleFileService.class);
		EasyMock.expect(fileService.get()).andStubReturn(file.getBytes("UTF-8"));
		EasyMock.replay(fileService);

		TiddlerService service = new TiddlerService(new StatelessWikiService(
				fileService));
		Tiddler tiddler = service.get("Blank");
		assertEquals("Steve", tiddler.creator);
		assertEquals("Steve", tiddler.modifier);
		assertEquals("201111301833", tiddler.created);
		assertEquals("201201091915", tiddler.modified);
		assertEquals("2", tiddler.changecount);
		assertEquals("!!Simple tiddler\n* Important\n* Special\n* Epic",
				tiddler.content);
	}
}

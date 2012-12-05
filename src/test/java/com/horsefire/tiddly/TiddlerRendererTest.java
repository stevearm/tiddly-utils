package com.horsefire.tiddly;

import static junit.framework.Assert.assertNotNull;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.io.IOException;

import org.junit.Test;

public class TiddlerRendererTest {

	@Test
	public void render() throws IOException {
		final String file = WikiServiceTest.getFile("base.html");
		SingleFileService fileService = createMock(SingleFileService.class);
		expect(fileService.get()).andStubReturn(file.getBytes("UTF-8"));
		replay(fileService);

		TiddlerService service = new TiddlerService(new StatelessWikiService(
				fileService));
		Tiddler tiddler = service.get("Home");
		assertNotNull(tiddler);
		
		TiddlerRenderer renderer = new TiddlerRenderer("/link/to.html?tiddler=");
		
		String htmlPage = renderer.render(tiddler);
		System.out.println(htmlPage);
	}
}

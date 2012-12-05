package com.horsefire.tiddly;

public class TiddlerRenderer {
	
	private final String m_prefix;
	
	public TiddlerRenderer(String linkPrefix) {
		m_prefix = linkPrefix;
	}

	public String render(Tiddler tiddler) {
		StringBuilder result = new StringBuilder();
		result.append("<html><body><pre>");
		
		String content = tiddler.content;
		content = content.replaceAll("\\[\\[([^|\\]]+)\\]\\]", "<a href=\""+m_prefix+"$1\">$1</a>");
		content = content.replaceAll("\\[\\[([^|\\]]+)\\|([^\\]]+)\\]\\]", "<a href=\""+m_prefix+"$2\">$1</a>");
		result.append(content);
		
		result.append("</pre></body></html>");
		return result.toString();
	}
}

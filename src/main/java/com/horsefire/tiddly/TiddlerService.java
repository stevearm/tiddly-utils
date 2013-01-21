package com.horsefire.tiddly;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class TiddlerService {

	private final WikiService m_fileService;

	public TiddlerService(WikiService fileService) {
		m_fileService = fileService;
	}

	private static NodeList parseTiddlerNodes(Wiki wiki) {
		try {
			String store = wiki.getStore();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();

			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(store));
			Document doc = db.parse(is);
			NodeList nodes = doc.getChildNodes();
			if (nodes.getLength() != 1) {
				throw new RuntimeException("Store malformed");
			}
			Node node = nodes.item(0);
			if (node.getNodeType() != Node.ELEMENT_NODE) {
				throw new RuntimeException("Central node is not an element");
			}

			Element element = (Element) node;
			if (!"div".equals(element.getTagName())
					|| !"storeArea".equals(element.getAttribute("id"))) {
				throw new RuntimeException("Store node not correct id");
			}

			return element.getChildNodes();
		} catch (ParserConfigurationException e) {
			throw new RuntimeException(e);
		} catch (SAXException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static List<Tiddler> parseTiddlers(Wiki wiki) {
		List<Tiddler> tiddlers = new ArrayList<Tiddler>();
		NodeList nodes = parseTiddlerNodes(wiki);
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node.getNodeType() != Node.ELEMENT_NODE) {
				if (node.getNodeType() == Node.TEXT_NODE) {
					continue;
				}
				System.out.println(node.getNodeType());
				throw new RuntimeException("Tiddler malformed");
			}
			Element element = (Element) node;
			if (!"div".equals(element.getTagName())) {
				throw new RuntimeException("Tiddler malformed");
			}
			Tiddler tiddler = new Tiddler();
			tiddler.title = element.getAttribute("title");
			tiddler.creator = element.getAttribute("creator");
			tiddler.modifier = element.getAttribute("modifier");
			tiddler.created = element.getAttribute("created");
			tiddler.modified = element.getAttribute("modified");
			tiddler.tags = element.getAttribute("tags");
			tiddler.changecount = element.getAttribute("changecount");

			NodeList contents = element.getElementsByTagName("pre");
			if (contents.getLength() != 1) {
				throw new RuntimeException("Content missing from Tiddler");
			}
			contents = contents.item(0).getChildNodes();
			if (contents.getLength() != 1) {
				throw new RuntimeException("Should only have one blob of text");
			}
			node = contents.item(0);
			if (node.getNodeType() != Node.TEXT_NODE) {
				throw new RuntimeException("Should be a text blob");
			}
			tiddler.content = node.getTextContent();
			tiddlers.add(tiddler);
		}
		return tiddlers;
	}

	private static String renderTiddlers(List<Tiddler> tiddlers) {
		Collections.sort(tiddlers, new Comparator<Tiddler>() {
			@Override
			public int compare(Tiddler o1, Tiddler o2) {
				return o1.title.compareTo(o2.title);
			}
		});
		StringBuilder result = new StringBuilder();
		result.append("\n<div id=\"storeArea\">\n");
		for (Tiddler tiddler : tiddlers) {
			result.append("<div title=\"").append(tiddler.title)
					.append("\" creator=\"").append(tiddler.creator)
					.append("\" modifier=\"").append(tiddler.modifier)
					.append("\" created=\"").append(tiddler.created);
			if (tiddler.modified != null && !tiddler.modified.isEmpty()) {
				result.append("\" modified=\"").append(tiddler.modified);
			}
			if (tiddler.tags != null && !tiddler.tags.isEmpty()) {
				result.append("\" tags=\"").append(tiddler.tags);
			}
			result.append("\" changecount=\"").append(tiddler.changecount)
					.append("\">\n");
			result.append("<pre>").append(tiddler.content).append("</pre>\n");
			result.append("</div>\n");
		}
		result.append("</div>\n");
		return result.toString();
	}

	public Tiddler get(String title) throws IOException {
		List<Tiddler> tiddlers = parseTiddlers(m_fileService.get());
		for (Tiddler tiddler : tiddlers) {
			if (title.equals(tiddler.title)) {
				return tiddler;
			}
		}
		return null;
	}

	public boolean delete(String title) throws IOException {
		Wiki wiki = m_fileService.get();
		List<Tiddler> tiddlers = parseTiddlers(wiki);
		for (Iterator<Tiddler> it = tiddlers.iterator(); it.hasNext();) {
			Tiddler tiddler = it.next();
			if (title.equals(tiddler.title)) {
				it.remove();
				wiki.setStore(renderTiddlers(tiddlers));
				m_fileService.put(wiki);
				return true;
			}
		}
		return false;
	}

	public void put(Tiddler tiddler) throws IOException {
		Wiki wiki = m_fileService.get();
		List<Tiddler> tiddlers = parseTiddlers(wiki);
		for (Iterator<Tiddler> it = tiddlers.iterator(); it.hasNext();) {
			if (tiddler.title.equals(it.next().title)) {
				it.remove();
			}
		}
		tiddlers.add(tiddler);
		wiki = wiki.setStore(renderTiddlers(tiddlers));
		m_fileService.put(wiki);
	}
}

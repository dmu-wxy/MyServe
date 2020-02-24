package core;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class WebHandler extends DefaultHandler{
	public List<Entity> entitys;
	public List<Mapping> mappings;
	private Entity entity;
	private Mapping mapping;
	private String tag;
	private boolean isMapping;
	public WebHandler() {
		entitys = new ArrayList<Entity>();
		mappings = new ArrayList<Mapping>();
		isMapping = false;
		tag = "";
	}
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if(qName.equals("servlet")) {
			isMapping = false;
			entity = new Entity();
		}else if(qName.equals("servlet-mapping")) {
			isMapping = true;
			mapping = new Mapping();
		}
		tag = qName;
	}
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		String msg = new String(ch,start,length);
		msg = msg.trim();
		if(msg.length() > 0) {
			if(!isMapping) {
				if(tag.equals("servlet-name")) {
					entity.setName(msg);
				}else if(tag.equals("servlet-class")){
					entity.setClz(msg);
				}
			}else if(isMapping) {
				if(tag.equals("servlet-name")) {
					mapping.setName(msg);
				}else if(tag.equals("url-pattern")) {
					mapping.addPattern(msg);
				}
			}
		}
	}
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		tag = "";
		if(qName.equals("servlet")) {
			entitys.add(entity);
		}else if(qName.equals("servlet-mapping")) {
			mappings.add(mapping);
		}
	}
}
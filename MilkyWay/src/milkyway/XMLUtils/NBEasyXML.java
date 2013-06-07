package milkyway.XMLUtils;

import java.io.Serializable;
import java.net.URI;
import java.util.Vector;
import java.util.Hashtable;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import milkyway.XMLUtils.NBEasyXMLContent;
import milkyway.XMLUtils.NBEasyXMLElement;
import milkyway.XMLUtils.NBEasyXMLEndElement;
import milkyway.XMLUtils.NBEasyXMLPart;


public class NBEasyXML extends DefaultHandler implements ContentHandler,
		Serializable {
	private int depth, parseMode;

	protected Vector data;

	public String tmpmsg;

	public NBEasyXML(String theXML) {
		
		IO.ConsoleLog.println("Parser instantiated with  ["+theXML+"]\n");
		
		data = new Vector();

		//java.io.StringReader reader = new java.io.StringReader(theXML);
		//org.xml.sax.InputSource source = new org.xml.sax.InputSource(reader);
	
		//SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			// Parse the input
			//factory.setValidating(false);
			
			  SAXParserFactory spf = SAXParserFactory.newInstance(); 
			  SAXParser sp = spf.newSAXParser(); 
			 
			  XMLReader parser = sp.getXMLReader(); 
			 
			  parser.setContentHandler(this); 
			    
			
			depth = 0;

			  parser.parse(theXML);
			//parser.parse(source, this);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	
public NBEasyXML(InputSource theXML) {
		
		IO.ConsoleLog.println("Parser instantiated with  ["+theXML+"]\n");
		
		data = new Vector();

		//java.io.StringReader reader = new java.io.StringReader(theXML);
		//org.xml.sax.InputSource source = new org.xml.sax.InputSource(reader);
	
		//SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			// Parse the input
			//factory.setValidating(false);
			
			  SAXParserFactory spf = SAXParserFactory.newInstance(); 
			  SAXParser sp = spf.newSAXParser(); 
			 
			  XMLReader parser = sp.getXMLReader(); 
			 
			  parser.setContentHandler(this); 
			    
			
			depth = 0;

			  parser.parse(theXML);
			//parser.parse(source, this);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	


	// start of changed code 20050602 Harrie Martens and Hubert Vogten
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		NBEasyXMLElement tmp = new NBEasyXMLElement(qName, atts);
		tmp.depth = depth;
		data.add(tmp);
		// IndexElement(data.size()-1, localName, depth); // this didn't work
		// :-(
		depth++;
	}

	void IndexElement(int index, String name, int depth) {

	}

	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		NBEasyXMLEndElement tmp = new NBEasyXMLEndElement(qName);
		tmp.depth = depth;
		data.add(tmp);
		// IndexEndElement(data.size()-1, localName, depth);
		depth--;
	}

	void IndexEndElement(int index, String name, int depth) {

	}

	public void characters(char[] ch, int start, int length)
			throws SAXException {
		String st = "";
		for (int n = start; n < length + start; n++)
			st += ch[n];
		NBEasyXMLContent tmp = new NBEasyXMLContent(st);
		tmp.depth = depth;
		data.add(tmp);
	}

	// This is the EasyXML interface stuff
	public String ExportBlock(int startIdx) {
		String out = "";
		if (startIdx < 0)
			startIdx = 0; // should really be an ASSERT
		NBEasyXMLPart tmp = (NBEasyXMLPart) data.get(startIdx);
		if (tmp.type == 1) {
			int startDepth = tmp.depth;
			int idx = startIdx;
			do {
				out += tmp.getAsString();
				idx++;
				if (idx >= data.size())
					return out; // probably an error, maybe should catch it....
				tmp = (NBEasyXMLPart) data.get(idx);
			} while (tmp.depth > startDepth);
		} else if (tmp.type == 4) {
			out = tmp.getAsString();
		}
		return out;
	}

	public String ExportSection(int startIdx, int endIdx) {
		String out = "";
		NBEasyXMLPart tmp;
		int idx;
		if (startIdx < 0)
			startIdx = 0; // should really be an ASSERT
		if (endIdx < startIdx)
			endIdx = startIdx; // should really be an ASSERT
		for (idx = startIdx; idx <= endIdx; idx++)
			;
		{
			if (idx >= data.size())
				return out; // probably an error, maybe should catch it....
			tmp = (NBEasyXMLPart) data.get(idx);
			out += tmp.getAsString();
		}
		return out;
	}

	public int findNextPeer(int startIdx) {
		// find tag that its depth == actual
		NBEasyXMLPart tmp = (NBEasyXMLPart) data.get(startIdx);
		if (tmp.type == 1) {
			int startDepth = tmp.depth;
			int idx = startIdx;
			do {
				idx++;
				if (idx >= data.size())
					return -1; // probably an error, maybe should catch it....
				tmp = (NBEasyXMLPart) data.get(idx);
			} while ((tmp.depth > startDepth) || (tmp.type != 1));
			String s = tmp.getAsString();
			if (tmp.depth == startDepth)
				return idx;
			else
				return -1;
		} else
			return -1;
	}

	public int findChildElement(int idx) {
		//find next peer en que depth = depth+1
		NBEasyXMLPart tmp = (NBEasyXMLPart) data.get(idx);
		if (tmp.type == 1) {
			int depth = tmp.depth + 1;
			int cidx = idx;
			do {
				cidx++;
				if (cidx >= data.size())
					return -1; // probably an error, maybe should catch it....
				tmp = (NBEasyXMLPart) data.get(cidx);
				if ((tmp.depth == depth) && (tmp.type == 1))
					return cidx;
			} while (tmp.depth >= depth);
			return -1; // if it has got this far there is no next peer
		} else
			return -1;

	}
	
	public int findOtherElement(int idx){ // encuentra el elemento de profundidad -1 al actual
		NBEasyXMLPart tmp = (NBEasyXMLPart) data.get(idx);
		int depth = tmp.depth - 1;
		
		int cidx = idx;
		do {
			cidx++;
			if (cidx >= data.size())
				return -1; // probably an error, maybe should catch it....
			tmp = (NBEasyXMLPart) data.get(cidx);
			if ((tmp.depth == depth) && (tmp.type == 1))
					return cidx;
			} while (tmp.depth >= depth);
			return -1; // if it has got this far there is no next peer

	}
	public int findEnd(int idx) {
		NBEasyXMLPart tmp = (NBEasyXMLPart) data.get(idx);
		// assert(tmp.type == 1);
		String nm = getName(idx);
		int depth = tmp.depth;
		int cidx = idx;
		do {
			cidx++;
			tmp = (NBEasyXMLPart) data.get(cidx);
		} while (tmp.depth > depth);
		// assert((((NBEasyXMLPart) data.get(cidx-1)).depth ==
		// depth+1)&&(getName(cidx-1).equals(nm)));
		return cidx - 1;
	}


	public int root() {
		NBEasyXMLPart tmp;
		int cidx = 0;
		while (cidx < data.size()) {
			tmp = (NBEasyXMLPart) data.get(cidx);
			if (tmp.type == 1)
				return cidx;
			cidx++;
		}
		return -1;
	}

	public int getType(int idx) {
		NBEasyXMLPart tmp = (NBEasyXMLPart) data.get(idx);
		return tmp.type;
	}

	public int getDepth(int idx) {
		NBEasyXMLPart tmp = (NBEasyXMLPart) data.get(idx);
		return tmp.depth;
	}

	public String getName(int idx) {
		NBEasyXMLPart tmp = (NBEasyXMLPart) data.get(idx);
		switch (tmp.type) {
		case 1:
			NBEasyXMLElement t2 = (NBEasyXMLElement) tmp;
			return t2.localName;
		case 2:
			NBEasyXMLEndElement t3 = (NBEasyXMLEndElement) tmp;
			return t3.localName;
		default:
			return null;
		}
	}

	public int size() {
		return data.size();
	}

	public String getAttribute(int idx, String attName) {
		NBEasyXMLPart tmp = (NBEasyXMLPart) data.get(idx);
		if (tmp.type == 1) {
			NBEasyXMLElement elm = (NBEasyXMLElement) tmp;
			return elm.atts.getValue(attName);
		} else
			return null; // actually an error.
	}

	// # only for unmixed content (stops at the first element;
	public String getContent(int idx) {
		String out = "";
		NBEasyXMLPart tmp = (NBEasyXMLPart) data.get(idx);
		if (tmp.type == 1) {
			int cidx = idx + 1;
			tmp = (NBEasyXMLPart) data.get(cidx);
			while (tmp.type == 4) {
				out += tmp.getAsString();
				cidx++;
				tmp = (NBEasyXMLPart) data.get(cidx);
			}
		}
		return out;
	}
	
	public double getDoubleContent(int idx){
		return Double.parseDouble(getContent(idx));
	}
	
	public int getIntContent(int idx){
		return Integer.parseInt(getContent(idx));
	}

	public boolean getBooleanContent(int idx){
		return Boolean.parseBoolean(getContent(idx));
	}
	
	public int getIntegerAttribute(int idx, String attName, int defval) {
		String tmp = getAttribute(idx, attName);
		if (tmp == null)
			return defval;
		else {
			int out;
			try {
				out = Integer.parseInt(tmp);
			} catch (NumberFormatException e) {
				return defval;
			}
			return out;
		}
	}

	public String getAsString(int idx) {
		NBEasyXMLPart tmp = (NBEasyXMLPart) data.get(idx);
		return tmp.getAsString();
	}

	// following methods are required by the ContentHandler interface, but
	// unused...
	public void startDocument() throws SAXException {
	}

	public void endDocument() throws SAXException {
	}

	public void setDocumentLocator(Locator locator) {
	}

	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
	}

	public void endPrefixMapping(String prefix) throws SAXException {
	}

	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException {
	}

	public void processingInstruction(String target, String data)
			throws SAXException {
	}

	public void skippedEntity(String name) throws SAXException {
	}

}

abstract class NBEasyXMLPart {
	int type;
	int depth;

	abstract String getAsString();
}

class NBEasyXMLElement extends NBEasyXMLPart {
	String localName;
	AttributesImpl atts;

	NBEasyXMLElement(String name, Attributes nAtts) {
		type = 1; // in C++ ver this would be EXML_START_TAG, probably should
					// make it a const var....
		localName = new String(name);
		atts = new AttributesImpl(nAtts);
	}

	String getAsString() {
		String out = "";
		out += "<" + localName;
		for (int n = 0; n < atts.getLength(); n++) {
			if (atts.getValue(n).indexOf("\"") == -1)
				out += " " + atts.getQName(n) + "=\"" + atts.getValue(n) + "\"";
			else
				out += " " + atts.getQName(n) + "='" + atts.getValue(n) + "'";
		}
		out += ">";
		return out;
	}

	String getAsWebString() {
		String out = "";
		out += "&lt;" + localName;
		for (int n = 0; n < atts.getLength(); n++) {
			if (atts.getValue(n).indexOf("\"") == -1)
				out += " " + atts.getQName(n) + "=\"" + atts.getValue(n) + "\"";
			else
				out += " " + atts.getQName(n) + "='" + atts.getValue(n) + "'";
		}
		out += "&gt;";
		return out;
	}
}

class NBEasyXMLEndElement extends NBEasyXMLPart {
	String localName;

	NBEasyXMLEndElement(String name) {
		type = 2; // in C++ ver this would be EXML_END_TAG, probably should make
					// it a const var....
		localName = new String(name);
	}

	String getAsString() {
		return "</" + localName + ">";
	}

	String getAsWebString() {
		return "&lt;/" + localName + "&gt;";
	}
}

class NBEasyXMLContent extends NBEasyXMLPart {
	String content;

	NBEasyXMLContent(String nContent) {
		type = 4; // in C++ ver this would be EXML_CONTENT, probably should make
					// it a const var....
		content = nContent;
	}

	String getAsString() {
		return content;
	}

	String getAsWebString() {
		// # should really do translation of chars, however for now this will
		// do.
		return content;
	}
}

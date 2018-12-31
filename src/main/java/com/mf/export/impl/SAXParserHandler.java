package com.mf.export.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class SAXParserHandler extends DefaultHandler {
    private static final Logger logger = LoggerFactory.getLogger(SAXParserHandler.class);

    String value = null;

    List<SheetMeta> sheetMetas = new ArrayList<>();
    public List<SheetMeta> getSheetMetas() {
        return sheetMetas;
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        logger.debug("SAX解析开始");
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
        logger.debug("SAX解析结束");
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);


    }


    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        // TODO Auto-generated method stub
        super.characters(ch, start, length);
        value = new String(ch, start, length);
        if (!value.trim().equals("")) {
            System.out.println("节点值是：" + value);
        }
    }
}

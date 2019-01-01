package com.mf.export.impl;

import com.mf.export.IExcelExportConsumer;
import com.mf.util.SpringContextUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class SAXParserHandler extends DefaultHandler {
    private static final Logger logger = LoggerFactory.getLogger(SAXParserHandler.class);

    String value = null;

    private SheetMeta sheetMeta = null;

    private ColumnMeta columnMeta = null;

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

        logger.debug("uri:" + uri);
        logger.debug("localName:" + localName);
        logger.debug("qName:" + qName);

        if ("sheet".equals(qName)) {
            sheetMeta = new SheetMeta();
            parseXML2Entity(sheetMeta, attributes);

        } else if ("column".equals(qName)) {
            columnMeta = new ColumnMeta();
            parseXML2Entity(columnMeta, attributes);
        }
    }


    private void parseXML2Entity(Object entityObj, Attributes attributes) {
        int attrNum = attributes.getLength();
        for (int i = 0; i < attrNum; i ++) {
            String attrName = attributes.getQName(i);

            try {
                BeanUtils.setProperty(entityObj, attrName, attributes.getValue(i));
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }

        logger.debug("end");
    }


    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);

        if ("sheet".equals(qName)) {
            sheetMetas.add(sheetMeta);
            sheetMeta = null;
        } else if ("column".equals(qName)) {

            columnMeta.setDisplayName(value);
            List<ColumnMeta> columnMetas = sheetMeta.getColumnMetas();
            columnMetas.add(columnMeta);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        // TODO Auto-generated method stub
        super.characters(ch, start, length);
        value = new String(ch, start, length);
        if (!value.trim().equals("")) {
            logger.debug("节点值是：" + value);
        }
    }
}

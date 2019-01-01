package com.mf.export.impl;

import com.mf.util.SpringContextUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SAXExportFileParserHandler extends DefaultHandler {
    private static final Logger logger = LoggerFactory.getLogger(SAXExportFileParserHandler.class);

    String value = null;

    private SheetMeta sheetMeta = null;

    private ColumnMeta columnMeta = null;

    private final ExcelContext context;

    public SAXExportFileParserHandler(ExcelContext context) {
        this.context = context;
    }

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
        } else if ("excel".equals(qName)) {
            int attrNum = attributes.getLength();
            for (int i = 0; i < attrNum; i ++) {
                String attrName = attributes.getQName(i);

                if ("name".equals(attrName)) {
                    Environment env = SpringContextUtils.getBean("environment", Environment.class);
                    String appFilePath = env.getProperty("excel.export.path");

                    String fileName = appFilePath + attributes.getValue(i) + new Date().getTime() + ".xlsx";
                    context.setFileName(fileName);
                } else if("rows".equals(attrName)) {
                    context.setRows(Integer.valueOf(attributes.getValue(i)));
                }
            }
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

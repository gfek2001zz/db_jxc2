package com.mf.export.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelExportParser {
    private static final Logger logger = LoggerFactory.getLogger(ExcelExportParser.class);


    public List<SheetMeta> parse(String xmlFile) {
        List<SheetMeta> sheetMetas = new ArrayList<>();

        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParserHandler handler = new SAXParserHandler();
            SAXParser parser = factory.newSAXParser();

            parser.parse(xmlFile, handler);
            sheetMetas = handler.getSheetMetas();

        } catch (SAXException e) {
            logger.error(e.getMessage(), e);
        } catch (ParserConfigurationException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            return sheetMetas;
        }
    }

}

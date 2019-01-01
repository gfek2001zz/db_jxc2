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


    public List<SheetMeta> parse(ExcelContext context) {
        List<SheetMeta> sheetMetas = new ArrayList<>();
        SAXParserFactory factory = SAXParserFactory.newInstance();

        try {
            SAXExportFileParserHandler handler = new SAXExportFileParserHandler(context);
            SAXParser parser = factory.newSAXParser();

            parser.parse(context.getXmlFileName(), handler);
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

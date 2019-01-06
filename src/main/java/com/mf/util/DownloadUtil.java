package com.mf.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

public class DownloadUtil {
    private static final Logger logger = LoggerFactory.getLogger(DownloadUtil.class);

    public static void response(File excelFile, HttpServletResponse httpResponse) throws UnsupportedEncodingException {
        if (excelFile.exists()) {
            httpResponse.setHeader("content-type", "application/octet-stream");
            httpResponse.setContentType("application/octet-stream");
            httpResponse.setHeader("Content-Disposition", "attachment;filename=" +
                    URLEncoder.encode(excelFile.getName(), "UTF-8"));

            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(excelFile);
                bis = new BufferedInputStream(fis);

                OutputStream os = httpResponse.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    os.flush();
                    i = bis.read(buffer);
                }
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        logger.error(e.getMessage(), e);
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            }
        }
    }
}

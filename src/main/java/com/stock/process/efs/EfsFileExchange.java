package com.stock.process.efs;

import com.google.gson.Gson;
import com.stock.process.util.BarcoUtil;
import com.stock.process.util.ExceptionUtil;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Nabeel Ahmed
 */
@Component
public class EfsFileExchange {

    private Logger logger = LoggerFactory.getLogger(EfsFileExchange.class);

    public EfsFileExchange() {}

    /**
     * Method use to make directory
     * @param filePath
     * @return Boolean
     * */
    public boolean doesFileExist(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            logger.info("File exists at path: [ {} ] ", filePath);
            return true;
        } else {
            logger.info("File does not exist at path: [ {} ]", filePath);
            return false;
        }
    }

    /**
    * Method use to make directory
     * @param basePath
     * @return Boolean
    * */
    public Boolean makeDir(String basePath) {
        try {
            File finalDir = new File(basePath);
            if (!finalDir.exists()) {
                logger.info("Making New Directory at path [ {} ]", basePath);
                return finalDir.mkdirs();
            } else {
                logger.info("Directory Already Exist At Path [ {} ]", basePath);
                return true;
            }
        } catch (Exception ex) {
            logger.error("Exception :- makeDir [ {} ] ", ExceptionUtil.getRootCauseMessage(ex));
        }
        return false;
    }

    /**
     * Method use to save file
     * @param byteStream
     * @param targetFileName
     * */
    public void saveFile(ByteArrayOutputStream byteStream, String targetFileName) throws Exception {
        if (!BarcoUtil.isNull(byteStream) && byteStream.size() > 0) {
            try (OutputStream outputStream = Files.newOutputStream(Paths.get(targetFileName))) {
                byteStream.writeTo(outputStream);
            } finally {
                if (!BarcoUtil.isNull(byteStream)) {
                    byteStream.flush();
                    byteStream.close();
                }
            }
            logger.info("File Convert and Store into local path");
        }
    }

    /***
     * Method use to get the file
     * @param filePath
     * @return File
     * */
    public File getFile(String filePath) {
        return new File(filePath);
    }

    /***
     * Method use delete the directory
     * @param basePath
     * */
    public void deleteDir(String basePath) {
        try {
            File file = new File(basePath);
            if (file.exists()) {
                logger.info("Deleting Directory At Path [ {} ]", basePath);
                FileUtils.deleteDirectory(file);
            }
        } catch (Exception ex) {
            logger.error("Exception :- deleteDir [ {} ]", ExceptionUtil.getRootCauseMessage(ex));
        }
    }

    /**
     * Method use to clean directory
     * @param basePath
     * */
    public void cleanDir(String basePath) {
        try {
            File file = new File(basePath);
            if (file.exists()) {
                logger.info("Cleaning Directory At Path [ {} ]", basePath);
                FileUtils.cleanDirectory(file);
            }
        } catch (Exception ex) {
            logger.error("Exception :- cleanDir [ {} ]", ExceptionUtil.getRootCauseMessage(ex));
        }
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}

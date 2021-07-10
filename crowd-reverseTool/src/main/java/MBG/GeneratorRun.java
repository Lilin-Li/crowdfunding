package MBG;

import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.JavaClientGeneratorConfiguration;
import org.mybatis.generator.config.JavaModelGeneratorConfiguration;
import org.mybatis.generator.config.SqlMapGeneratorConfiguration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GeneratorRun {
    public static void main(String[] args) throws Exception {
        GeneratorRun app = new GeneratorRun();
        app.generator();

    }

    public void generator() throws Exception {

        List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("MyBatisGeneratorConfig.xml");
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(resourceAsStream);

        System.out.println("===== Clean：正在刪除以下目錄內的所有目錄與檔案。 （裡面是產生器上次所產內容）=====");
        deleteEarlyFile(config);
        System.out.println("===== 清理完畢 =====");


        System.out.println("==========產生器正在產生code..需要一點時間。 (log相關ERROR會被自動處理)==========");
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);
        System.out.println("==========code產生完成==========");

        System.out.println("==========驗證程序==========");
        System.out.println("掃描非以英文開頭的Table Name 所產的Code。");
        System.out.println("-------------------------------------------------------------");
        System.out.println("為什麼這麼做？ Ans：以名稱5ABC的Table為例，其產生5ABC.java檔 會因'5'讓專案build不起來。");
        System.out.println("因此：產生器不會產生非英文開頭的Table Name的code。");
        System.out.println("建議處理方式：修改Table Name，讓其以英文開頭 或著 這些Table的code 手動撰寫 不要用代碼產生器。");
        System.out.println("-------------------------------------------------------------");
        System.out.println("掃描結果如下： （若有，則顯示並刪除。若沒有，則不另行通知。）");
        dropIllIllegaFile(config);

        System.out.println("==========在每包中建立說明防錯文件==========");
        createDummyFileFlow(config);
        System.out.println("==========建立完成==========");


        System.out.println("==========除了驗證程序所掃瞄出的檔案外，其他Table對應的entity與mapper均已產生完成。==========");
    }

    private void createDummyFileFlow(Configuration config) throws Exception {
        String targetPath = readJavaModelGeneratorTargetPath(config);
        createDummyFile(targetPath);

        targetPath = readSqlMapGeneratorTargetPath(config);
        createDummyFile(targetPath);

        targetPath = readSqlJavaClientGeneratorTargetPath(config);
        createDummyFile(targetPath);

    }

    private void createDummyFile(String targetPath) throws Exception {
        String path = targetPath + findSeparator();
        String fileName1 ="-此包內的所有檔案都會在下次執行code產生器時，被刪除。";
        File dummyFile = new File( path+ fileName1);
        dummyFile.createNewFile();
        System.out.println("建立防錯檔案" + path + fileName1);

        String fileName = "-此包目前僅供私人評估使用，有些部分還在觀察，請暫勿使用。";
        dummyFile = new File(path + fileName);
        dummyFile.createNewFile();
        System.out.println("建立防錯檔案" + path + fileName);
    }


    private void dropIllIllegaFile(Configuration config) throws Exception {
        String targetPath = readJavaModelGeneratorTargetPath(config);
        deleteIllIllegalTableName(targetPath);

        targetPath = readSqlMapGeneratorTargetPath(config);
        deleteIllIllegalTableName(targetPath);

        targetPath = readSqlJavaClientGeneratorTargetPath(config);
        deleteIllIllegalTableName(targetPath);

    }

    private void deleteEarlyFile(Configuration config) throws Exception {

        String str = "掃描目錄並刪除其內檔案：";

        String targetPath = readJavaModelGeneratorTargetPath(config);
        System.out.println(str + targetPath.replace("\\\\", "\\"));
        deleteTargetDirContent(targetPath);

        targetPath = readSqlMapGeneratorTargetPath(config);
        System.out.println(str + targetPath.replace("\\\\", "\\"));
        deleteTargetDirContent(targetPath);


        targetPath = readSqlJavaClientGeneratorTargetPath(config);
        System.out.println(str + targetPath.replace("\\\\", "\\"));
        deleteTargetDirContent(targetPath);
    }

    private String readSqlJavaClientGeneratorTargetPath(Configuration config) throws Exception {

        JavaClientGeneratorConfiguration javaClientGeneratorConfiguration = config.getContexts().get(0).getJavaClientGeneratorConfiguration();
        String targetProject = javaClientGeneratorConfiguration.getTargetProject();
        String targetPackage = javaClientGeneratorConfiguration.getTargetPackage();
        return fixPath(targetProject, targetPackage);
    }

    private String readSqlMapGeneratorTargetPath(Configuration config) throws Exception {

        SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration = config.getContexts().get(0).getSqlMapGeneratorConfiguration();
        String targetProject = sqlMapGeneratorConfiguration.getTargetProject();
        String targetPackage = sqlMapGeneratorConfiguration.getTargetPackage();
        return fixPath(targetProject, targetPackage);
    }

    private String readJavaModelGeneratorTargetPath(Configuration config) throws Exception {
        JavaModelGeneratorConfiguration javaModelGeneratorConfiguration = config.getContexts().get(0).getJavaModelGeneratorConfiguration();
        String targetProject = javaModelGeneratorConfiguration.getTargetProject();
        String targetPackage = javaModelGeneratorConfiguration.getTargetPackage();
        return fixPath(targetProject, targetPackage);
    }


    private void deleteIllIllegalTableName(String targetPath) throws Exception {
        int[] dummyArray = new int[1];
        dummyArray[0] = 0;
        File file = new File(targetPath);
        if (file.exists() && file.isDirectory()) {
            Path path = Paths.get(targetPath);

            //請注意，此為遞迴刪除流程。
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {

                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                            String fileName = file.getFileName().toString();


                            if (!fileName.matches("^[A-Za-z].*")) {
                                dummyArray[0] = 1;
                                Files.delete(file);
                                System.out.println("delete: " + file.toString());
                            }
                            ;
                            return FileVisitResult.CONTINUE;

                        }

                        public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {
                            return FileVisitResult.CONTINUE;
                        }
                    }
            );
            file.mkdir();
        } else {
            file.mkdir();
        }
        if (dummyArray[0] == 0) {
            System.out.println("掃描完畢：沒有檔案被刪除。");
        }
    }

    private String fixPath(String targetProject, String targetPackage) {
        String separator = findSeparator();

        String fixedTargetProject = targetProject.replace("/", separator) + separator;
        String fixedTargetPackage = targetPackage.replace(".", separator);
        return fixedTargetProject + fixedTargetPackage;
    }

    private String findSeparator() {
        // OS平台的路徑分隔符：win "\"   linux "/"
        String separator = java.io.File.separator;
        if (separator.equals("\\")) separator = "\\\\";

        return separator;
    }

    private void deleteTargetDirContent(String targetPath) throws Exception {
        //       若目錄不存在就建立。
        //       若目錄存在，就遞迴刪除裡面所有檔案與目錄。

        File file = new File(targetPath);
        if (file.exists() && file.isDirectory()) {
            Path path = Paths.get(targetPath);
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                            Files.delete(file);
                            return FileVisitResult.CONTINUE;
                        }

                        public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {
                            if (e != null) throw e;
                            Files.delete(dir);
                            return FileVisitResult.CONTINUE;
                        }
                    }
            );
            file.mkdir();
        } else {
            file.mkdir();
        }
    }
}

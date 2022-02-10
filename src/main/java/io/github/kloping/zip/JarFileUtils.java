package io.github.kloping.zip;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import static io.github.kloping.io.ReadUtils.copy;
import static io.github.kloping.io.ReadUtils.readAll;

/**
 * @author github-kloping
 */
public class JarFileUtils {

    /**
     * 获取主类
     * 例如:cn.kloping.Main
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static String getMainClass(File file) throws IOException {
        String[] sss = getAllMANIFEST(file).split("\n");
        for (String s : sss) {
            if (s.trim().isEmpty()) continue;
            String[] ss = s.split(":");
            if (ss[0].trim().equals("Main-Class")) return ss[1].trim();
        }
        return null;
    }

    private static String getAllMANIFEST(File file) throws IOException {
        ZipFile jf = new ZipFile(file);
        Enumeration entries = jf.entries();
        ZipEntry entry = jf.getEntry("META-INF/MANIFEST.MF");
        InputStream inputStream = jf.getInputStream(entry);
        byte[] bytes = readAll(inputStream);
        return new String(bytes, "utf-8");
    }

    /**
     * 从有清单文件(META-INF/MANIFEST.MF)的
     * 压缩文件 复制清单文件到 - 没有 - 的清单文件压缩文件里
     *
     * @param oZipFile
     * @param fZipFile
     * @return
     * @throws IOException
     */
    public static File copyManifest(File oZipFile, File fZipFile) throws IOException {
        ZipFile jarZip = new ZipFile(oZipFile);
        ZipEntry entry = jarZip.getEntry("META-INF/MANIFEST.MF");
        InputStream inputStream = jarZip.getInputStream(entry);
        byte[] bytes = readAll(inputStream);
        ZipFile dexZip = new ZipFile(fZipFile);
        Enumeration<? extends ZipEntry> entries = dexZip.entries();
        File tempFile = File.createTempFile("temp", ".jar");
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(tempFile));
        while (entries.hasMoreElements()) {
            ZipEntry e = entries.nextElement();
            zos.putNextEntry(e);
            if (!e.isDirectory()) {
                copy(dexZip.getInputStream(e), zos);
            }
            zos.closeEntry();
        }
        zos.putNextEntry(new ZipEntry("META-INF/MANIFEST.MF"));
        zos.write(bytes);
        zos.closeEntry();
        zos.close();
        FileInputStream fis = new FileInputStream(tempFile);
        FileOutputStream fos = new FileOutputStream(fZipFile);
        copy(fis, fos);
        fis.close();
        fos.close();
        tempFile.deleteOnExit();
        return fZipFile;
    }
}

package com.nullptr.utils.system;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;

/**
 * 文件处理工具，主要包含写入和读取文件数据
 *
 * @author nullptr
 * @version 1.0 2020-8-12
 * @version 1.1 2020-8-17 修改底层使用javax.xml.ws方式进行请求
 * @version 1.2 2020-8-20 接口更名为WsdlServiceFileUtils，与请求发送和密级信息生成模块相互独立
 * @version 1.3 2020-8-24 新增写入文件数据到流方法, 剔除掉与wsdl接口相关的部分，只负责对文件的处理
 * @version 1.4 2020-8-26 新增对文件路径和类型的处理方法，如获取文件名，获取绝对路径，修改文件类型等方法
 * @version 1.5 2020-8-27 修复无法解析\路径的文件, 修复文件名解析错误, 增加获取文件父路径方法
 * @version 1.6 2020-8-28 增加写入加密文件信息和写入原始文件信息方法
 * @version 1.7 2020-9-2 新增判断文件是否存在方法，废弃写入原文数据信息方法，将此工具类通用化
 * @version 1.8 2020-9-9 修改获取UUID文件名方法，内部增加将传入的文件路径转换为文件名操作
 * @version 1.9 2020-9-10 增加获取文件类型，获取文件名称（不包含类型名称），修改文件名称（不修改类型），修改文件类型等方法
 * @version 1.10 2020-9-24 新增从输入流写入至文件方法, 根据字符编码获取文件数据方法，修复路径分隔符转换失败bug
 * @version 1.11 2020-11-12 新增根据文件名称读取文件行内容
 * @since 1.0 2020-8-12
 */
public class FileUtils extends org.apache.commons.io.FileUtils {
    /** 日志工具 */
    protected static final Log logger = LogFactory.getLog(FileUtils.class);
    protected static final Base64 BASE_64 = new Base64();
    /** 文件路径分隔符 */
    public static final String SPLIT = "/";
    /** 文件类型分隔符 */
    public static final String TYPE_SPLIT = ".";

    /** 构造方法私有化，防止生成实例 */
    protected FileUtils() {}

    /**
     * 写入加密文件信息，会将原始文件在磁盘上替换为加密文件
     *
     * @param parentPath 父路径
     * @param filePath 原始文件相对路径，如/temp/senddoc/2020828/xxx.docx
     * @param encodeFileData base64编码的文件数据流字符串
     * @since 1.6
     */
    public static boolean writeFileData(String parentPath, String filePath, String encodeFileData) {
        return writeFileData(parentPath, filePath, BASE_64.decode(encodeFileData));
    }

    /**
     * 写入加密文件信息，会将原始文件在磁盘上替换为加密文件
     *
     * @param filePath 原始文件相对路径，如/temp/senddoc/2020828/xxx.docx
     * @param encodeFileData base64编码的文件数据流字符串
     * @since 1.6
     */
    public static boolean writeFileData(String filePath, String encodeFileData) {
        return writeFileData(filePath, BASE_64.decode(encodeFileData));
    }

    /**
     * 写入加密文件信息，会将原始文件在磁盘上替换为加密文件
     *
     * @param parentPath 父路径
     * @param filePath 原始文件相对路径，如/temp/senddoc/2020828/xxx.docx
     * @param fileData 加密文件数据流
     * @since 1.6
     */
    public static boolean writeFileData(String parentPath, String filePath, byte[] fileData) {
        filePath = getAbsolutePath(parentPath, filePath);
        return writeFileData(filePath, fileData);
    }

    /**
     * 写入加密文件信息，会将原始文件在磁盘上替换为加密文件
     *
     * @param filePath 原始文件相对路径，如/temp/senddoc/2020828/xxx.docx
     * @param fileData 加密文件数据流
     * @since 1.6
     */
    public static boolean writeFileData(String filePath, byte[] fileData) {
        filePath = FileUtils.replaceSplit(filePath);
        // 获取文件实例
        File file = new File(filePath);
        // 向文件中写入加密数据
        try {
            FileUtils.writeByteArrayToFile(file, fileData);
        } catch (IOException e) {
            logger.error("向文件" + getFileName(filePath) +
                    "写入数据时失败, 文件路径为" + filePath);
            return false;
        }
        return true;
    }

    /**
     * 使用UUID和当前时间生成编码后的文件名称，
     *
     * @param filePath 原文件路径
     * @return 编码后的文件名称
     * @since 1.1
     */
    public static String getUUIDFileName(String filePath) {
        String uuid = UUID.randomUUID().toString() + System.currentTimeMillis();
        String fileType = getFileType(filePath);
        return Md5Crypt.md5Crypt(uuid.getBytes()) + TYPE_SPLIT + fileType;
    }

    /**
     * 写入文件数据到流之中
     *
     * @param stream 输出流
     * @param fileData 文件数据流
     * @since 1.3
     */
    public static void writeToStream(OutputStream stream, byte[] fileData) {
        try {
            stream.write(fileData);
        } catch (IOException e) {
            logger.error("向流中写入数据时发生错误", e);
        }
    }

    /**
     * 写入文件数据到流之中
     *
     * @param stream 输出流
     * @param fileName 文件名称
     * @since 1.10
     */
    public static void writeFileToStream(OutputStream stream, String parentPath, String fileName) {
        String filePath = getAbsolutePath(parentPath, fileName);
        writeFileToStream(stream, filePath);
    }

    /**
     * 写入文件数据到流之中
     *
     * @param stream 输出流
     * @param fileName 文件名称
     * @since 1.10
     */
    public static void writeFileToStream(OutputStream stream, String fileName) {
        byte[] fileData = readFileData(fileName);
        writeToStream(stream, fileData);
    }

    /**
     * 写入文件数据到流之中
     *
     * @param stream 输出流
     * @param encodeFileData 文件数据编码字符串
     * @since 1.3
     */
    public static void writeToStream(OutputStream stream, String encodeFileData) {
       writeToStream(stream, BASE_64.decode(encodeFileData));
    }

    /**
     * 写入文件数据
     *
     * @param fileName 文件名
     * @param encodeFileData 文件数据编码字符串
     * @since 1.2
     */
    public static void writeToFile(String fileName, String encodeFileData) {
       writeToFile(fileName, BASE_64.decode(encodeFileData));
    }

    /**
     * 写入文件数据
     *
     * @param fileName 文件名
     * @param fileData 文件数据流
     * @since 1.2
     */
    public static void writeToFile(String fileName, byte[] fileData) {
        File file = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(file, false)) {
            fos.write(fileData);
            fos.flush();
        } catch (Exception e) {
            logger.error("写入文件" + fileName + "时发生错误", e);
        }
    }

    /**
     * 写入文件数据
     *
     * @param parentPath 父路径
     * @param fileName 文件名
     * @param input 输入流
     * @since 1.10
     */
    public static void writeToFile(String parentPath, String fileName, InputStream input) {
        String filePath = getAbsolutePath(parentPath, fileName);
        writeToFile(new File(filePath), input);
    }

    /**
     * 写入文件数据
     *
     * @param fileName 文件名
     * @param input 输入流
     * @since 1.10
     */
    public static void writeToFile(String fileName, InputStream input) {
        writeToFile(new File(fileName), input);
    }

    /**
     * 写入文件数据
     *
     * @param file 文件
     * @param input 输入流
     * @since 1.10
     */
    public static void writeToFile(File file, InputStream input) {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            IOUtils.copy(input, fos);
        } catch (IOException e) {
            logger.error("写入文件" + file.getName() + "时发生错误", e);
        }
    }

    /**
     * 根据虚拟机默认编码获取文件各行内容
     *
     * @param fileName 待读取的文件名
     * @return 文件各行内容
     * @since 1.11
     */
    public static List<String> readLines(String fileName) throws IOException {
        return readLines(fileName, Charset.defaultCharset());
    }

    /**
     * 根据给定编码获取文件各行内容
     *
     * @param fileName 待读取的文件名
     * @param charset 文件编码
     * @return 文件各行内容
     * @since 1.11
     */
    public static List<String> readLines(String fileName, Charset charset) throws IOException {
        File file = new File(fileName);
        if (file.exists()) {
            return readLines(file, charset);
        } else {
            throw new IOException("文件不存在");
        }
    }

    /**
     * 读取文件数据流
     *
     * @param fileName 待读取的文件名
     * @return 文件数据流
     * @since 1.2
     */
    public static byte[] readFileData(String fileName) {
        File file = new File(fileName);
        try (InputStream inputStream = new FileInputStream(file)) {
            byte[] dataBytes = new byte[inputStream.available()];
            inputStream.read(dataBytes);
            return dataBytes;
        } catch (FileNotFoundException e) {
            logger.error("文件" + fileName + "不存在", e);
        } catch (IOException e) {
            logger.error("读取文件" + fileName + "失败", e);
        }
        return null;
    }

    /**
     * 读取文件数据流
     *
     * @param fileName 待读取的文件名
     * @param charset 字符编码名称
     * @return 文件数据流
     * @since 1.10
     */
    public static byte[] readFileData(String fileName, String charset) {
        return readFileData(fileName, Charset.forName(charset));
    }

    /**
     * 读取文件数据流
     *
     * @param fileName 待读取的文件名
     * @param charset 字符编码
     * @return 文件数据流
     * @since 1.10
     */
    public static byte[] readFileData(String fileName, Charset charset) {
        byte[] fileData = readFileData(fileName);
        if (fileData != null) {
            return new String(fileData).getBytes(charset);
        }
        return null;
    }

    /**
     * 读取文件数据编码字符串
     *
     * @param fileName 待读取的文件名
     * @return 文件数据编码字符串
     * @since 1.2
     */
    public static String readEncodeFileData(String fileName) {
        byte[] fileData = readFileData(fileName);
        return BASE_64.encodeAsString(fileData);
    }

    /**
     * 获取文件名称
     *
     * @param filePath 文件路径
     * @return 文件相对路径
     * @since 1.4
     */
    public static String getFileName(String filePath) {
        filePath = replaceSplit(filePath);
        int index = filePath.lastIndexOf(SPLIT);
        return filePath.substring(index + 1);
    }

    /**
     * 获取文件绝对路径
     *
     * @param parentPath 父路径
     * @param filePath 文件路径
     * @return 文件绝对路径
     * @since 1.4
     */
    public static String getAbsolutePath(String parentPath, String filePath) {
        parentPath = replaceSplit(parentPath);
        filePath = replaceSplit(filePath);

        // 判断是否具备相对路径
        return filePath.startsWith(SPLIT) ? parentPath + filePath : parentPath + SPLIT + filePath;
    }

    /**
     * 获取文件父路径
     *
     * @param filePath 文件路径
     * @return 父路径
     * @since 1.5
     */
    public static String getParentPath(String filePath) {
        filePath = replaceSplit(filePath);
        int index = filePath.lastIndexOf(SPLIT);
        return filePath.substring(0, index);
    }

    /**
     * 将路径分隔符\\替换为/,
     * 如: c:\\file\\userFiles\\temp\\test.doc会被替换为
     * c:/file/userFiles/temp/test.doc
     *
     * @param filePath 文件路径
     * @return 替换后的文件路径
     * @since 1.5
     */
    public static String replaceSplit(String filePath) {
        return filePath.replaceAll(Matcher.quoteReplacement("\\"), SPLIT);
    }

    /**
     * 修改文件名称
     *
     * @param fileName 文件名
     * @param newFileName 新文件名称，不包含文件类型
     * @return 修改后的文件绝对路径
     * @since 1.9
     */
    public static String changeFileName(String fileName, String newFileName) {
        return changeFileName("", fileName, newFileName);
    }

    /**
     * 修改文件名称并保留原本的类型名称，如<br />
     * {@code
     *     String newFile = changeFileName("", "123.txt", "456");
     *     // newFile = "456.txt";
     * }
     *
     * @param parentPath 父路径，可为空
     * @param fileName 文件名
     * @param newFileName 新文件名称，不包含文件类型
     * @return 修改后的文件名称
     * @since 1.9
     */
    public static String changeFileName(String parentPath, String fileName, String newFileName) {
        if (StringUtils.isNotEmpty(parentPath)) {
            fileName = getAbsolutePath(parentPath, fileName);
            newFileName = getAbsolutePath(parentPath, newFileName);
        }
        String fileType = getFileType(fileName);
        return newFileName + TYPE_SPLIT + fileType;
    }

    /**
     * 修改文件类型
     *
     * @param fileName 文件名
     * @param newFileType 新文件类型
     * @return 修改后的文件名称
     * @since 1.9
     */
    public static String changeFileType(String fileName, String newFileType) {
       return changeFileType("", fileName, newFileType);
    }

    /**
     * 修改文件类型
     *
     * @param parentPath 父路径，不需要此项时可为空
     * @param fileName 文件名称
     * @param newFileType 新文件类型
     * @return 修改后的文件名称, 修改失败则返回空字符串
     * @since 1.4
     */
    public static String changeFileType(String parentPath, String fileName, String newFileType) {
        if (StringUtils.isNotEmpty(parentPath)) {
            fileName = getAbsolutePath(parentPath, fileName);
        }
        String newFileName = getFileNameWithoutType(fileName) + TYPE_SPLIT + newFileType;
        File file = new File(fileName);
        if (file.exists()) {
            File newFile = new File(newFileName);
            file.renameTo(newFile);
        }
        return newFileName;
    }

    /**
     * 获取文件类型，如123.txt将返回txt
     *
     * @param fileName 文件名
     * @return 修改后的文件绝对路径
     * @since 1.9
     */
    public static String getFileType(String fileName) {
        int index = fileName.lastIndexOf(TYPE_SPLIT) + 1;
        return fileName.substring(index);
    }

    /**
     * 获取文件名称，不包含文件类型，如123.txt将返回123
     *
     * @param fileName 文件名
     * @return 文件名称，不包含文件类型
     * @since 1.9
     */
    public static String getFileNameWithoutType(String fileName) {
        int index = fileName.lastIndexOf(TYPE_SPLIT);
        return fileName.substring(0, index);
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath 文件路径
     * @return 存在则返回true，否则为false
     * @since 1.7
     */
    public static boolean isExist(String filePath) {
        return new File(filePath).exists();
    }

    /**
     * 判断文件是否存在
     *
     * @param parentPath 父路径
     * @param filePath 文件路径
     * @return 存在则返回true，否则为false
     * @since 1.9
     */
    public static boolean isExist(String parentPath, String filePath) {
        String realPath = getAbsolutePath(parentPath, filePath);
        return isExist(realPath);
    }

    /**
     * 判断文件是否存在，存在则删除
     * @param filePath 文件路径
     * @since 1.7
     */
    public static void deleteIfExist(String filePath) {
        if (isExist(filePath)) {
            deleteIfExist(filePath);
        }
    }
 }
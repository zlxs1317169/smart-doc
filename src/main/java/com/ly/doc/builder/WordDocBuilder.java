package com.ly.doc.builder;

import com.ly.doc.constants.DocGlobalConstants;
import com.ly.doc.factory.BuildTemplateFactory;
import com.ly.doc.helper.JavaProjectBuilderHelper;
import com.ly.doc.model.ApiConfig;
import com.ly.doc.model.ApiDoc;
import com.ly.doc.template.IDocBuildTemplate;
import com.power.common.util.FileUtil;
import com.thoughtworks.qdox.JavaProjectBuilder;
import org.beetl.core.Template;
import org.beetl.core.io.IOUtil;
import org.beetl.core.resource.ClasspathResourceLoader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author <a href="mailto:cqmike0315@gmail.com">chenqi</a>
 * @version 1.0
 */
public class WordDocBuilder {
    private static final String BUILD_DOCX = "AllInOne.docx";
    private static final String TEMPLATE_DOCX = "template/word/template.docx";

    /**
     * build controller api
     *
     * @param config config
     */
    public static void buildApiDoc(ApiConfig config) throws Exception {
        JavaProjectBuilder javaProjectBuilder = JavaProjectBuilderHelper.create();
        buildApiDoc(config, javaProjectBuilder);
    }

    /**
     * build controller api
     *
     * @param config config
     */
    public static void buildApiDoc(ApiConfig config, JavaProjectBuilder javaProjectBuilder) throws Exception {
        DocBuilderTemplate builderTemplate = new DocBuilderTemplate();
        builderTemplate.checkAndInit(config, Boolean.TRUE);
        config.setParamsDataToTree(false);
        ProjectDocConfigBuilder configBuilder = new ProjectDocConfigBuilder(config, javaProjectBuilder);
        IDocBuildTemplate<ApiDoc> docBuildTemplate = BuildTemplateFactory.getDocBuildTemplate(config.getFramework());
        Objects.requireNonNull(docBuildTemplate, "doc build template is null");
        List<ApiDoc> apiDocList = docBuildTemplate.getApiData(configBuilder);

        if (config.isAllInOne()) {
            String docName = builderTemplate.allInOneDocName(config, BUILD_DOCX, ".docx");
            apiDocList = docBuildTemplate.handleApiGroup(apiDocList, config);
            String outPath = config.getOutPath();
            FileUtil.mkdirs(outPath);
            Template tpl = builderTemplate.buildAllRenderDocTemplate(apiDocList, config, javaProjectBuilder, DocGlobalConstants.ALL_IN_ONE_WORD_XML_TPL, null, null);
            replaceDocx(tpl.render(), outPath + DocGlobalConstants.FILE_SEPARATOR + docName);
            // todo docs模板还有错误码和字典没有弄
            // todo 单个的文档还未渲染
        } else {

        }
    }


    /**
     * replace docx content
     *
     * @param content        doc content
     * @param docxOutputPath docx output path
     * @return
     * @since 1.0.0
     */
    public static void replaceDocx(String content, String docxOutputPath) throws Exception {
        InputStream resourceAsStream = WordDocBuilder.class.getClassLoader().getResourceAsStream(TEMPLATE_DOCX);
        Objects.requireNonNull(resourceAsStream, "word template docx is not found");

        ZipInputStream zipInputStream = new ZipInputStream(resourceAsStream);
        ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(Paths.get(docxOutputPath)));
        // 遍历压缩包中的文件
        ZipEntry entry;
        while ((entry = zipInputStream.getNextEntry()) != null) {
            String entryName = entry.getName();

            // 判断是否为要修改的文件
            if (entryName.equals("word/document.xml")) {
                // 创建新的压缩包文件项
                zipOutputStream.putNextEntry(new ZipEntry(entryName));
                // 写入修改后的内容
                byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
                zipOutputStream.write(bytes, 0, bytes.length);
            } else {
                // 复制其他文件项
                zipOutputStream.putNextEntry(entry);
                byte[] buffer = new byte[1024];
                int len;
                while ((len = zipInputStream.read(buffer)) > 0) {
                    zipOutputStream.write(buffer, 0, len);
                }
            }

            // 关闭当前文件项
            zipOutputStream.closeEntry();
            zipInputStream.closeEntry();
        }

        // 关闭压缩包
        zipInputStream.close();
        zipOutputStream.close();
    }

}

package com.zhangyue.translate;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by zy on 2019/3/21.
 */
public class TranslateStringXml {
    private static final String[] targetLanguages = new String[]{"ar", "bn", "de", "es", "fr", "in", "ja", "pt", "ro", "ru"};

    public static void main(String[] args) {
        translateAndroidXmlString("src/res/strings.xml");
    }

    /**
     * 翻译的总入口  需要传递两个路径
     *
     * @param sourcePath 源文件的路径
     */
    public static void translateAndroidXmlString(String sourcePath) {
        // 解析xml
        SAXReader reader = new SAXReader();
        ITranslate translate = new YouDaoTranslate();
        try {
            // 通过reader对象的read方法加载books.xml文件,获取docuemnt对象。
            Document document = reader.read(new File(sourcePath));
            // 通过document对象获取根节点bookstore
            Element rootElement = document.getRootElement();
            // 通过element对象的elementIterator方法获取迭代器
            for (String targetLanguage : targetLanguages) {
                Iterator it = rootElement.elementIterator();
                // 遍历迭代器，获取根节点中的信息
                while (it.hasNext()) {
                    Element element = (Element) it.next();
                    String name = element.attribute("name").getValue();
                    String value = element.getStringValue();
                    System.out.println(name + "-" + value);
                    element.setText(translate.translate(value, "EN", targetLanguage));
                }
                System.out.println(document.asXML());
                FileWriter writer = new FileWriter(getPathWithoutLastSegment(sourcePath) + "result-" + targetLanguage + ".xml");
                document.write(writer);
                writer.flush();
            }
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    public static String getPathWithoutLastSegment(String input) {
        int index = input.lastIndexOf("/");
        if (index != -1) {
            String[] split = input.split("/");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < split.length - 1; i++) {
                sb.append(split[i]).append("/");
            }
            return sb.toString();
        } else {
            return input;
        }
    }
}

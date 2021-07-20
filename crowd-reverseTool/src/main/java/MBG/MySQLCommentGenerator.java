package MBG;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

import java.util.Properties;

public class MySQLCommentGenerator extends EmptyCommentGenerator {

    public MySQLCommentGenerator() {
    }

    @Override
    public void addModelClassComment(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        // 获取表注释
        String remarks = introspectedTable.getRemarks();
        remarks = remarks.trim().length() == 0 ? "": remarks + "。";

        topLevelClass.addAnnotation("/**");
        topLevelClass.addAnnotation("* " + remarks);
        topLevelClass.addAnnotation("* ");
        topLevelClass.addAnnotation("* 請文件由代碼產生器(myBatisGenerator)產生，任何修改都會在下次產生代碼時被覆蓋。");
        topLevelClass.addAnnotation("* ");
        topLevelClass.addAnnotation("* @Data = Getter/Setter + ToString + Equals + HashCode");
        topLevelClass.addAnnotation("* @Builder = 「此ClassName.builder().id(1).name(\"Join\").build(); 」");
        topLevelClass.addAnnotation("* ");
        topLevelClass.addAnnotation("*/");
    }
    @Override
    public void addRootComment(XmlElement xmlElement) {
        xmlElement.addElement(0, new TextElement("<!-- ————————————————————————————————————————————————————————————————————————— -->"));
        xmlElement.addElement(0, new TextElement("<!--請注意，此文件由代碼產生器(MybatisGenerattor)產生，任何修改都會在下次產生代碼時被覆寫。-->"));
        xmlElement.addElement(0, new TextElement("<!-- ————————————————————————————————————————————————————————————————————————— -->"));
    }


    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        // 获取列注释
        String remarks = introspectedColumn.getRemarks();
        field.addJavaDocLine("/**");
        field.addJavaDocLine(" * " + remarks);
        field.addJavaDocLine(" */");
    }
}
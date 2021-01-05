import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;

/**
 * ClassName:Test
 * Package:PACKAGE_NAME
 * Description: 描述信息
 *
 * @date:2020/12/23 11:32
 * @author:动力节点
 */
public class Test {
    public static void main(String[] args) throws DocumentException {
        String xmlString = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" +
                "\n" +
                "<bookstore>\n" +
                "\n" +
                "<book>\n" +
                "  <title lang=\"eng\">Harry Potter</title>\n" +
                "  <price>29.99</price>\n" +
                "</book>\n" +
                "\n" +
                "<book>\n" +
                "  <title lang=\"eng\">Learning XML</title>\n" +
                "  <price>39.95</price>\n" +
                "</book>\n" +
                "\n" +
                "</bookstore>";
        //获取xml中第一个title的内容
        Document document = DocumentHelper.parseText(xmlString);
        // 使用xpath路径表达式来过title标签  bookstore/book[1]/title
        Node node = document.selectSingleNode("bookstore/book[1]/title");
        String text = node.getText();
        System.out.println(text);
    }
}

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * 
 * 此类描述的是： 用于生成http://localhost:8080/restbus/publicXMLFeed?command=agencyList页面XML生成类
 * @author: dmnrei@gmail.com
 * @version: Oct 22, 2012 9:33:29 PM
 */
public class AgencyXMLCreator {
	private Document document;
	private Element body;
	
	public AgencyXMLCreator() {
		document = DocumentHelper.createDocument();
		body = document.addElement("body");
		body.addAttribute("copyright", "All data copyright ZJUT 2011.");
	}
	
	public void addAgency(String tag, String title ) {
		body.addElement("agency").addAttribute("tag", tag).addAttribute("title", title);
	}
	
	public Document toDocument() {
		return document;
	}
	
	public static void main(String[] args) {
		AgencyXMLCreator c = new AgencyXMLCreator();
		c.addAgency("111", "222");
		String str = c.toDocument().toString();
		System.out.println("str = " + str);
	}
}

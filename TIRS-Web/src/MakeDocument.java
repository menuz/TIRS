

import java.util.ArrayList;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
/**
 * 将Document转换为String型
 * @author Administrator
 *
 */
public class MakeDocument {
	
	public static String getAllUserXML(ArrayList<MyBean> userlist) {

		Document document = DocumentHelper.createDocument();
		Element body = document.addElement("body");
		
		for (int i = 0; i < userlist.size(); i++) {
			MyBean temp=userlist.get(i);
			@SuppressWarnings("unused")
			Element userEle = body.addElement("user")
	
					.addAttribute("age", temp.getAge())
					.addAttribute("height", temp.getHeight())
					.addAttribute("name", temp.getName());
		}
		return MakeDocument.docToStreamSource(document);

	}
	public static String docToStreamSource(Document document) {
		return document.asXML();
	}

}

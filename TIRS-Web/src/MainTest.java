import java.util.ArrayList;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;


public class MainTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<MyBean> k=new ArrayList<MyBean>();
		for(int i=0;i<3;i++){
			MyBean a=new MyBean();
			a.setAge("10");
			a.setHeight("140");
			a.setName("luna");
			k.add(a);
		}
		System.out.println(MakeDocument.getAllUserXML(k));
	}

	
}

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.tirsweb.model.GPS;

/**
 * 文件名：Test.java
 *
 * 版本信息： version 1.0
 * 日期：Apr 22, 2013
 * Copyright by menuz
 */

public class Test {
	public static void main(String[] args) {
		GPS gps = new GPS();
		
		Class g = gps.getClass();
		Method[] ms = g.getDeclaredMethods();
		Field[] fs = g.getFields();
	
		for (Field field : fs) {
			System.out.println(field.getName());
			String property = field.getName();
			String methodName = "get";
			char c = Character.toUpperCase(property.charAt(0));
			methodName = methodName + c + property.substring(1);
			try {
				Method sm = g.getMethod(methodName, null);
				System.out.println(sm.getReturnType().toString());
				System.out.println(sm.getDefaultValue());
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}
		
		
		
		/*PropertyDescriptor pd;
		try {
		pd = new PropertyDescriptor(field[i].getName(),clazz);
		Method getMethod = pd.getReadMethod();
		if (field[i].getGenericType().toString().equals("class java.lang.String")) { 
		String val = (String) getMethod.invoke(vo); 
		cell.setCellValue(val);
		}else if(field[i].getGenericType().toString().equals("class java.lang.Integer")){
		Integer val=(Integer)getMethod.invoke(vo);
		if(val!=null){
		cell.setCellValue(val);
		}
		}
		} catch (Exception e) {
		e.printStackTrace();
		}*/
	}
}



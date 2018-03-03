package com.sapient.csvtoxml.common.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.sapient.csvtoxml.error.XmlCreationException;

@Component
public class CXConverterUtility<T> {


	private String destPath;

	public void convertToXml(JAXBElement<T> object) throws XmlCreationException{

		OutputStream os = null; 
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy h-mm-ss a");
		String formattedDate = sdf.format(date);
		String fileName = getDestPath()+object.getValue().getClass().getSimpleName()+"_"+formattedDate+".xml";

		try {
			File file =  new File(getDestPath());
			if(!file.exists())
				file.mkdirs();
			os = new FileOutputStream(fileName);
		} catch (FileNotFoundException e) {
			//e.printStackTrace();
			//throw new XmlCreationException(" Error creating file for "+getFieldValue(object.getValue().getClass()));
		}

		try {

			JAXBContext context = JAXBContext.newInstance(object.getValue().getClass());
			Marshaller marshaller=  context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.marshal(object, os);
		} catch (JAXBException e1) {
			e1.printStackTrace();
			//throw new XmlCreationException(" Error during marshalling for "+getFieldValue(object.getValue().getClass());
		}

	}


	/*private String getFieldValue(Class clazz)  {
		Object attrValue=null;
		try {
			Field field = clazz.getDeclaredField(clazz.getSimpleName()+"Id");
			field.setAccessible(Boolean.TRUE);

			for (Method method : clazz.getMethods())
			{
				if ((method.getName().startsWith("get")) && (method.getName().length() == (field.getName().length() + 3)))
				{
					if (method.getName().toLowerCase().endsWith(field.getName().toLowerCase()))
					{
						attrValue =  method.invoke((object));
					}
				}
			}
		} catch ( InvocationTargetException | IllegalAccessException | NoSuchFieldException  e) {
			e.printStackTrace();
			//throw new XmlCreationException(" Field doesn't exists for "+clazz.getSimpleName()+"Id"); 
		}
		return attrValue.toString();
	}
*/
	public String getDestPath() {
		return destPath;
	}

	@Value("${destPath}")
	public void setDestPath(String destPath) {
		this.destPath = destPath;
	}

}

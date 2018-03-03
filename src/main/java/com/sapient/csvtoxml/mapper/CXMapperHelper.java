package com.sapient.csvtoxml.mapper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.sapient.csvtoxml.common.util.CXErrorCodes;

public class CXMapperHelper {
	
	private static final String pattern = "yyyy-MM-dd'T'HH:mm:ss";
	private static final Map<String, Class[]> classMap = new HashMap<String, Class[]>();
	
	   public static XMLGregorianCalendar stringToXMLGregorianCalendar(String s) 
			     throws ParseException, 
			            DatatypeConfigurationException
			 {
			 XMLGregorianCalendar result = null;
			 Date date;
			 SimpleDateFormat simpleDateFormat;
			 GregorianCalendar gregorianCalendar;
			 
			 simpleDateFormat = new SimpleDateFormat(pattern);
			                date = simpleDateFormat.parse(s);        
			                gregorianCalendar = 
			                    (GregorianCalendar)GregorianCalendar.getInstance();
			                gregorianCalendar.setTime(date);
			                result = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
			                return result;
			 }

		public static Object toObject( Class clazz, String value ) throws ParseException, DatatypeConfigurationException {
			
			if(clazz.getName().equals("java.lang.String")) return value;	
		    if( Boolean.class.isAssignableFrom( clazz ) || clazz.getName().equals("boolean")) return Boolean.parseBoolean( value );
		    if( Byte.class.isAssignableFrom( clazz ) || clazz.getName().equals("byte")) return Byte.parseByte( value );
		    if( Short.class.isAssignableFrom( clazz ) ||  clazz.getName().equals("short")) return Short.parseShort( value );
		    if( Integer.class.isAssignableFrom( clazz ) ||  clazz.getName().equals("int")) return Integer.parseInt( value );
		    if( Long.class.isAssignableFrom( clazz ) || clazz.getName().equals("long")) return Long.parseLong( value );
		    if( Float.class.isAssignableFrom( clazz ) || clazz.getName().equals("float")) return Float.parseFloat( value );
		    if( Double.class.isAssignableFrom( clazz ) || clazz.getName().equals("double")) return Double.parseDouble( value );
//		    if( BigInteger.class.isAssignableFrom( clazz )) return BigInteger.;
		    
		    if(XMLGregorianCalendar.class.isAssignableFrom(clazz)) 
		    	return CXMapperHelper.stringToXMLGregorianCalendar(value);
		    
		    return value;
		}

	   
		public static boolean isJavaType(Class clazz){
			
			boolean result  = false;
			if(clazz.getName().startsWith("java.") || clazz.isPrimitive() || clazz.getName().startsWith("javax.")){
				
				result = true;
				
			}
			
			return result;
			
		}
		
		public static Class[] getClassesCached(String packageName) throws CXDataMapperException{
			
			Class[] result = null;
			if(packageName == null)
				throw new CXDataMapperException(CXErrorCodes.INVALID_REQUEST_DATA , "Package name is null");
			
			if(classMap.get(packageName) != null){
				
				result = classMap.get(packageName);
				
			}else{
				
				try {
					result = getClasses(packageName);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
					throw new CXDataMapperException(CXErrorCodes.CLASS_LOADING_FAILED , "Class Loading failed" , e);
					//e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
					throw new CXDataMapperException(CXErrorCodes.CLASS_LOADING_FAILED , "Class Loading failed" , e);
					//e.printStackTrace();
				}
				classMap.put(packageName, result);
				
			}
			
			return result;
		}
		
		public static  Class[] getClasses(String packageName)
				throws ClassNotFoundException, IOException {
			URLClassLoader classLoader = (URLClassLoader) Thread.currentThread().getContextClassLoader();
			assert classLoader != null;
			String path = packageName.replace('.', '/');
			Enumeration resources = classLoader.getResources(path);
			List<File> dirs = new ArrayList();
			while (resources.hasMoreElements()) {
				URL resource = (URL) resources.nextElement();
				//System.out.println(resource);
				dirs.add(new File(resource.getFile()));
			}
			ArrayList<Class> classes = new ArrayList();
			for (File directory : dirs) {
				classes.addAll(findClasses(directory, packageName));
			}
			return classes.toArray(new Class[classes.size()]);
		}
		
		   public static List findClasses(File directory, String packageName) throws ClassNotFoundException {
		        List classes = new ArrayList();
		        if (!directory.exists()) {
		            return classes;
		        }
		        File[] files = directory.listFiles();
		        for (File file : files) {
		            if (file.isDirectory()) {
		                assert !file.getName().contains(".");
		                classes.addAll(findClasses(file, packageName + "." + file.getName()));
		            } else if (file.getName().endsWith(".class")) {
		                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
		            }
		        }
		        return classes;
		    }


}

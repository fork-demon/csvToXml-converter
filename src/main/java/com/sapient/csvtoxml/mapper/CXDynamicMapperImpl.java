package com.sapient.csvtoxml.mapper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sapient.csvtoxml.common.util.CXCommonDTO;
import com.sapient.csvtoxml.common.util.CXConverterUtility;
import com.sapient.csvtoxml.common.util.CXErrorCodes;
import com.sapient.csvtoxml.error.XmlCreationException;

@Component
public class CXDynamicMapperImpl implements CXGenericDataMapper {

	static HashMap<String, String> genericMap = new HashMap<String, String>();
	@Autowired
	private CXConverterUtility converterUtil;

	static {

		genericMap.put("FIRSTNAME", "VIJAY");
		genericMap.put("LASTNAME", "BHASKAR");
		genericMap.put("AGE", "29");
		genericMap.put("PAGES", "10");
		genericMap.put("PUBLICATIONDATE", "2012-10-15T00:00:00");
		genericMap.put("TITLE", "MY BOOK");
		// genericMap.put("AGE" , "29");

	}

	public void mapdata(CXCommonDTO request) throws CXDataMapperException {
        System.out.println("mapData inside ********************* ENTRY"+request);
		// DynamicMapperImpl dynamic = new DynamicMapperImpl();

		if (request == null)
			throw new CXDataMapperException(CXErrorCodes.INVALID_REQUEST_DATA, "Incoming Request is null");

		genericMap = (HashMap<String, String>) request.getDataMap();

		Class[] array;
		try {
			array = CXMapperHelper.getClassesCached("com.sapient.assignment.xsd.TRADE");

			for (Class clas : array) {

				if (clas.getName().contains("Factory") || clas.getName().contains("factory")) {

					Object t = clas.newInstance();

					Method[] methods = clas.getMethods();

					for (Method method : methods) {

						if (method.getName().startsWith("create")) {

							Parameter[] params = method.getParameters();

							int index = 0;
							Object[] args = new Object[params.length];
							for (Parameter param : params) {
								// System.out.println();
								Object obj = drillObject(param, genericMap);

								args[index++] = obj;

							}
							// Class typeArgClass = null;
							// if(params.length > 0){
							//
							// java.lang.reflect.Type returnType =
							// method.getGenericReturnType();
							//
							// if(returnType instanceof ParameterizedType){
							// ParameterizedType type = (ParameterizedType)
							// returnType;
							// Type[] typeArguments =
							// type.getActualTypeArguments();
							// for(Type typeArgument : typeArguments){
							// typeArgClass = (Class) typeArgument;
							// System.out.println("typeArgClass = " +
							// typeArgClass);
							// }
							// }

							if(params.length > 0){
								
								JAXBElement jax = (JAXBElement) method.invoke(t, args);

								converterUtil.convertToXml(jax);

								
							}

							// JAXBContext context;
							// try {
							// context = JAXBContext.newInstance(typeArgClass);
							// Marshaller marshaller =
							// context.createMarshaller();
							// marshaller.setProperty("jaxb.formatted.output",Boolean.TRUE);
							// marshaller.marshal(jax,System.out);
							// System.out.println(jax.getClass().getName());
							//
							// } catch (JAXBException e) {
							// // TODO Auto-generated catch block
							// e.printStackTrace();
							// }
							//

							// }

						}

					}

				}

			}

		} catch (InstantiationException e) {
			e.printStackTrace();
			throw new CXDataMapperException(CXErrorCodes.REFLECTION_FAILED, "REFLECTION_FAILED", e);

		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new CXDataMapperException(CXErrorCodes.REFLECTION_FAILED, "REFLECTION_FAILED", e);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new CXDataMapperException(CXErrorCodes.REFLECTION_FAILED, "REFLECTION_FAILED", e);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new CXDataMapperException(CXErrorCodes.REFLECTION_FAILED, "REFLECTION_FAILED", e);
		} catch (XmlCreationException e) {
			e.printStackTrace();
			throw new CXDataMapperException(CXErrorCodes.JAXB_EXCEPTION, "JAXB_EXCEPTION", e);

		}

	}

	public Object drillObject(Parameter param, Map<String, String> map) throws CXDataMapperException {

		Class classs = param.getType();

		Object t = null;
		try {
			t = classs.newInstance();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
			throw new CXDataMapperException(CXErrorCodes.CLASS_LOADING_FAILED, "Class Loading failed", e1);
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
			throw new CXDataMapperException(CXErrorCodes.CLASS_LOADING_FAILED, "Class Loading failed", e1);
		}

		Method[] methods = classs.getDeclaredMethods();

		for (Method method : methods) {

			if (method.getName().startsWith("set")) {

				String paramname = method.getName().substring(3);

				System.out.println(paramname);

				Class[] types = method.getParameterTypes();

				// types[0].cast(obj)

				System.out.println(types[0].getName());

				if (CXMapperHelper.isJavaType(types[0])) {

					if (map.get(paramname.toUpperCase()) != null) {

						try {

							method.invoke(t, CXMapperHelper.toObject(types[0], map.get(paramname.toUpperCase())));
						} catch (IllegalAccessException e) {
							e.printStackTrace();
							throw new CXDataMapperException(CXErrorCodes.REFLECTION_FAILED, "REFLECTION_FAILED", e);
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
							throw new CXDataMapperException(CXErrorCodes.REFLECTION_FAILED, "REFLECTION_FAILED", e);
						} catch (InvocationTargetException e) {
							e.printStackTrace();
							throw new CXDataMapperException(CXErrorCodes.REFLECTION_FAILED, "REFLECTION_FAILED", e);
						} catch (ParseException e) {
							e.printStackTrace();
							throw new CXDataMapperException(CXErrorCodes.REFLECTION_FAILED, "REFLECTION_FAILED", e);
						} catch (DatatypeConfigurationException e) {
							e.printStackTrace();
							throw new CXDataMapperException(CXErrorCodes.REFLECTION_FAILED, "REFLECTION_FAILED", e);
						}
					} else {

						System.out.println(" Map does not contain value for Java type");
					}

				} else {

					System.out.println("going to make recursice call to create composite objects");

					try {
						method.invoke(t, drillObject(method.getParameters()[0], map));
					} catch (IllegalAccessException e) {
						e.printStackTrace();
						throw new CXDataMapperException(CXErrorCodes.REFLECTION_FAILED, "REFLECTION_FAILED", e);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
						throw new CXDataMapperException(CXErrorCodes.REFLECTION_FAILED, "REFLECTION_FAILED", e);
					} catch (InvocationTargetException e) {
						e.printStackTrace();
						throw new CXDataMapperException(CXErrorCodes.REFLECTION_FAILED, "REFLECTION_FAILED", e);
					}

				}

			}

		}

		return t;

	}

/*	public static void main(String[] args) {
		try {
			System.out.println();
			// HashMap<K, V>
			// Class[] array =
			// DynamicMapperImpl.getClasses("blog.thoughts.on.java");
			CXDynamicMapperImpl impl = new CXDynamicMapperImpl();

			// impl.mapdata();

		} finally {

		}

	}*/

}

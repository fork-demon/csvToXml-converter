package com.sapient.csvtoxml.xgen.app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.xml.sax.InputSource;

import com.sapient.csvtoxml.xgen.model.CXSharedData;
import com.sun.codemodel.JCodeModel;
import com.sun.tools.xjc.api.S2JJAXBModel;
import com.sun.tools.xjc.api.SchemaCompiler;
import com.sun.tools.xjc.api.XJC;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CXClassGenerator {

	public final static String[] EXT = { ".xsd" };
	public final static String JAVA_EXT = ".java";
	public final static String CLASS_EXT = ".class";
	public final static String CLASS_LOCATION = "classes";
	public final static String XSD_FOLDER_NAME = "xsd";
	public final static String PACKAGE_FOR_XSD = "com.sapient.csvtoxml.xsd.";
	private static final Logger logger = LoggerFactory.getLogger(CXClassGenerator.class);

	public static void generateFromXSD(File xsdPath, String xsdIdentifier) throws Exception {

		 logger.info("inside method ::+generateFromXSD()");
		
		 CXSharedData context = CXSharedData.getInstance();

		if (null == xsdIdentifier || null == xsdPath) {
			throw new IllegalArgumentException("Please provide the xsd name");
		}

		// Setup schema compiler
		SchemaCompiler sc = XJC.createSchemaCompiler();
		String packageNameForXSD = PACKAGE_FOR_XSD + xsdIdentifier;
		sc.forcePackageName(packageNameForXSD);
		// Setup SAX InputSource

		InputSource is = new InputSource(xsdPath.toURI().toString());
		// is.setSystemId(schemaFile.getAbsolutePath());

		// Parse & build
		sc.parseSchema(is);
		S2JJAXBModel model = null;
		model = sc.bind();

		JCodeModel jCodeModel = model.generateCode(null, null);

		File outputDirectory = new File(System.getProperty("user.dir") + File.separator + CLASS_LOCATION);

		// if the directory does not exist, create it

		createDirectory(outputDirectory);

		jCodeModel.build(outputDirectory);

		// compiling java classes
		generateClassFiles(outputDirectory + File.separator + packageNameForXSD.replace(".", File.separator),
				xsdIdentifier, context, packageNameForXSD);
		
		 logger.info("Exit from method ::+generateFromXSD()");

	}

	private static void createDirectory(final File outputDirectory) {
		
		logger.info("inside method ::+createDirectory()");
		if (!outputDirectory.exists()) {
			logger.info("creating directory for class files: " + outputDirectory);
			boolean result = false;

			try {
				outputDirectory.mkdir();
				result = true;
			} catch (SecurityException se) {
				se.printStackTrace();
			}
			if (result) {
				logger.info("DIR created for class files");
			}
		}
		logger.info("exit from method ::+createDirectory()");
	}

	private static void generateClassFiles(final String outputDirectory, final String xsdidentifier,
			final CXSharedData context, String packageName) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException  {
		
		logger.info("inside method ::+generateClassFiles()");
		
		File javaSourceFolder = new File(outputDirectory + File.separator);

		String[] javaFiles = javaSourceFolder.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(JAVA_EXT);
			}
		});

		List<String> fileList = null;
		StandardJavaFileManager fileManager = null;
		boolean success = false;
		if (null != javaFiles && javaFiles.length > 0) {

			try {
				fileList = new ArrayList<>();
				for (String srcFiles : javaFiles) {
					logger.info("Java source file"+outputDirectory + File.separator + srcFiles);
					fileList.add(outputDirectory + File.separator + srcFiles);
				}
				JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
				DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();

				fileManager = compiler.getStandardFileManager(diagnostics, null, null);

				Iterable<? extends JavaFileObject> compilationUnits = fileManager
						.getJavaFileObjectsFromStrings(fileList);

				JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null, null,
						compilationUnits);
				success = task.call();
			} finally {
				try {
					fileManager.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (success) {
				logger.info("packageName::::::" + packageName);

				// Load classes from external directory

				loadClassFiles(javaSourceFolder);
				context.setAttribute(xsdidentifier.toUpperCase(), packageName);
				deleteJavaSources(javaSourceFolder);
				logger.info("exiting methode ::generateClassFiles()");

			} else {
				throw new RuntimeException("JAXB objects could not compile sucessfully");
			}

		} else {
			throw new IOException("Java source files are not available.");
		}

	}

	private static void deleteJavaSources(File javaSourceFolder) {
		
		logger.info("inside method ::deleteJavaSources()");

		File[] javaFiles = javaSourceFolder.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {

				return name.endsWith(JAVA_EXT);
			}
		});

		for (File f : javaFiles) {
			if (f.delete()) {
				logger.info(f.getName() + " deleted");
			} else {
				logger.info(f.getName() + " deleted");
			}
		}

	}

	private static void loadClassFiles(final File javaSourceFolder) throws MalformedURLException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException  {
		
		logger.info("inside method loadClassFiles()");

		String[] classfiles = javaSourceFolder.list(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {

				return name.endsWith(CLASS_EXT);
			}
		});

		URL u = javaSourceFolder.toURI().toURL();
		URLClassLoader urlClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
		Class urlClass = URLClassLoader.class;
		Method method = urlClass.getDeclaredMethod("addURL", new Class[] { URL.class });
		method.setAccessible(true);
		method.invoke(urlClassLoader, new Object[] { u });
		
		logger.info("classes are loded");

		// TODO Auto-generated method stub

	}

	public static void init() throws Exception    {

		logger.info("inside method init()");
		System.out.println(System.getProperty("java.home"));
		String xsdFileLocation = System.getProperty("user.dir") + File.separator + XSD_FOLDER_NAME;

		File xsdDir = new File(xsdFileLocation);

		File[] files = xsdDir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				for (String extension : EXT) {
					if (name.toLowerCase().endsWith(extension))
						return true;
				}
				return false;
			}
		});

		if (null == files || files.length == 0)
			throw new IllegalStateException("XSD files are not present in " + xsdFileLocation);

		for (File xsd : files) {
			generateFromXSD(xsd, xsd.getName().split("\\.")[0]);
		}

		CXSharedData sd = CXSharedData.getInstance();
		logger.info("shareData ::"+sd.get());
		
		logger.info("exiting method init()");

		// for testing print class path

		/*
		 * ClassLoader cl = ClassLoader.getSystemClassLoader();
		 * 
		 * URL[] urls = ((URLClassLoader)cl).getURLs();
		 * 
		 * for(URL url: urls) System.out.println(url.getFile());
		 * 
		 * Class claaz =
		 * Class.forName("com.sapient.csvtoxml.xsd.Customer.AddressType");
		 * System.out.println("class ***"+claaz);
		 * System.out.println(claaz.getDeclaredFields());
		 */

	}
	
	public static void generateFile(InputStream is, String name) throws Exception{
		
		if(null == is || null == name || name.length() == 0)
		{
			throw new FileNotFoundException("Please provide xsd file and name");
		}
		String[] prefixSuffix = name.split("\\.");
		
		if(prefixSuffix.length == 2){
		File xsdFile = streamTofile(is,prefixSuffix[0],prefixSuffix[1]);
		
		generateFromXSD(xsdFile,prefixSuffix[0]);
		
		}
		
	}
	
	 private static File streamTofile (InputStream in,String pref, String suff) throws IOException {
	        final File tempFile = File.createTempFile(pref, suff);
	        tempFile.deleteOnExit();
	        try (FileOutputStream out = new FileOutputStream(tempFile)) {
	            IOUtils.copy(in, out);
	        }
	        return tempFile;
	    }

}

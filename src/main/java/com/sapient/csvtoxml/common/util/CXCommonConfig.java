package com.sapient.csvtoxml.common.util;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;


/*@PropertySource(
          value={"classpath:com/sapient/properties/application.properties"},
          ignoreResourceNotFound = true)*/
public class CXCommonConfig {

   	
	 @Bean
	    public static PropertyPlaceholderConfigurer properties(){
	      PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
	      ClassPathResource[] resources = new ClassPathResource[ ]
	        { new ClassPathResource( "application.properties" ) };
	      ppc.setLocations( resources );
	      ppc.setIgnoreUnresolvablePlaceholders( true );
	      return ppc;
	    }
}
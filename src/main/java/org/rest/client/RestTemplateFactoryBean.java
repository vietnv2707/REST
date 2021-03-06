package org.rest.client;

import org.apache.http.impl.client.DefaultHttpClient;
import org.rest.sec.model.Privilege;
import org.rest.sec.model.dto.User;
import org.rest.security.BasicHttpComponentsClientHttpRequestFactory;
import org.rest.security.DigestHttpComponentsClientHttpRequestFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestTemplateFactoryBean implements FactoryBean< RestTemplate >, InitializingBean{
	private RestTemplate restTemplate;
	
	@Value( "${sec.auth.basic}" ) boolean basicAuth;
	@Value( "${http.req.timeout}" ) int timeout;
	
	// API
	
	@Override
	public RestTemplate getObject(){
		return restTemplate;
	}
	
	@Override
	public Class< RestTemplate > getObjectType(){
		return RestTemplate.class;
	}
	
	@Override
	public boolean isSingleton(){
		return true;
	}
	
	@Override
	public void afterPropertiesSet(){
		final DefaultHttpClient httpClient = new DefaultHttpClient();
		final HttpComponentsClientHttpRequestFactory requestFactory;
		if( basicAuth ){
			requestFactory = new BasicHttpComponentsClientHttpRequestFactory( httpClient ){
				{
					setReadTimeout( timeout );
				}
			};
		}
		else{
			requestFactory = new DigestHttpComponentsClientHttpRequestFactory( httpClient ){
				{
					setReadTimeout( timeout );
				}
			};
		}
		restTemplate = new RestTemplate( requestFactory );
		
		restTemplate.getMessageConverters().add( marshallingHttpMessageConverter() );
	}
	
	//
	
	final MarshallingHttpMessageConverter marshallingHttpMessageConverter(){
		final MarshallingHttpMessageConverter marshallingHttpMessageConverter = new MarshallingHttpMessageConverter();
		marshallingHttpMessageConverter.setMarshaller( xstreamMarshaller() );
		marshallingHttpMessageConverter.setUnmarshaller( xstreamMarshaller() );
		
		return marshallingHttpMessageConverter;
	}
	
	final XStreamMarshaller xstreamMarshaller(){
		final XStreamMarshaller xStreamMarshaller = new XStreamMarshaller();
		xStreamMarshaller.setAutodetectAnnotations( true );
		// xStreamMarshaller.setSupportedClasses( new Class[ ] { User.class, Privilege.class } );
		xStreamMarshaller.setAnnotatedClass( User.class );
		xStreamMarshaller.setAnnotatedClass( Privilege.class );
		
		// xstreamMarshaller().getXStream().addDefaultImplementation( java.util.HashSet.class, PersistentSet.class );
		
		return xStreamMarshaller;
	}
}

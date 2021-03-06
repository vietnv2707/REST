package org.rest.persistence.event;

import org.rest.common.IEntity;
import org.springframework.context.ApplicationEvent;

import com.google.common.base.Preconditions;

/**
 * This event should be fired when entity is updated.
 */
public final class EntityUpdatedEvent< T extends IEntity > extends ApplicationEvent{
	
	private final Class< T > clazz;
	private final T entity;
	
	public EntityUpdatedEvent( final Object sourceToSet, final Class< T > clazzToSet, final T entityToSet ){
		super( sourceToSet );
		
		Preconditions.checkNotNull( clazzToSet );
		clazz = clazzToSet;
		
		Preconditions.checkNotNull( entityToSet );
		entity = entityToSet;
	}
	
	// API
	
	public final Class< T > getClazz(){
		return clazz;
	}
	
	public final T getEntity(){
		return Preconditions.checkNotNull( entity );
	}
	
}

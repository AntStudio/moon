package org.moon.base.service;

/**
 * the service for base CRUD operations, but not contains the get multiple entities operation.
 * This services usually used for persistence entities
 * @author GavinCook
 * @since  1.0.0
 */
public interface BaseCRUDService<T> {

    /**
     * persistent entity object
     * @param t the entity object
     * @return persistent entity object(generally would hold id value than persistence before)
     */
    T save(T t);

    /**
     * update entity object
     * @param t the entity object
     * @return then entity updated(however in most cases, the updated entity object is same with the entity which not updated)
     */
    T update(T t);

    /**
     * delete the entity based on the id property of entity
     * @param id the id property of entity
     * @return <code>true</code>,if operate successfully. or return <code>false</code>
     */
    boolean delete(Long id);

    /**
     * delete the entity,however some implements would just call the {@link #delete(Long)} to do the operation
     * @param t the entity
     * @return <code>true</code>,if operate successfully. or return <code>false</code>
     */
    boolean delete(T t);

    /**
     * Get the entity according to the id parameter
     * @param id the entity id
     * @return the corresponding entity for id parameter if found,or return <code>null</code>
     */
    T get(Long id);

}

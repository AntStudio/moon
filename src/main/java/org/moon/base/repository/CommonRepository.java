package org.moon.base.repository;

import org.springframework.stereotype.Repository;

/**
 * common repository , simply extend from the {@link BaseRepository}, and annotated with {@link Repository}, which should
 * be added in the spring container.
 * @see org.moon.base.domain.eventhandler.BaseEventHandler
 */
@Repository
public interface CommonRepository<T> extends BaseRepository<T>{

}

package org.moon.core.domain;

import org.moon.base.service.BaseDomainService;
import org.moon.exception.ApplicationRunTimeException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * domain loader, would use the registered domain service if present to load domain according to the <code>id</code>.
 * there must register corresponding domain loader before call {@link #load(Class, Long)},
 * OR would throw the {@link ApplicationRunTimeException}
 *
 * @author GavinCook
 * @since 1.0.0
 * @date 2014-07-05 00:43:53
 * @see org.moon.base.service.AbstractDomainService#setBeanName(String)
 */
@Component
public class DomainLoader {

    private Map<Class, BaseDomainService> loaders = new ConcurrentHashMap<>();

    public void registerDomainLoader(Class c, BaseDomainService s) {
        loaders.put(c, s);
    }

    @SuppressWarnings("unchecked")
    public <T> T load(Class<T> c, Long id) {
        BaseDomainService<T> domainService = loaders.get(c);
        if(Objects.isNull(domainService)){
            throw new ApplicationRunTimeException("there has no matched domain loader for "+c+", did you forget to register one?");
        }
        return domainService.get(id);
    }
}

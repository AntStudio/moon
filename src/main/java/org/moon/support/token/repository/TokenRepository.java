package org.moon.support.token.repository;

import org.apache.ibatis.annotations.Param;
import org.moon.support.token.Token;
import org.springframework.stereotype.Repository;

/**
 * @author:Gavin
 * @date 2015/2/27 0027
 */
@Repository
public interface TokenRepository {

    public void add(@Param("token") Token token);

    public void update(@Param("token") Token token);

    public void delete(@Param("token") Token token);

    public Token getTokenForUser(@Param("userId") Long userId);

    public Token getToken(@Param("token")String token);
}

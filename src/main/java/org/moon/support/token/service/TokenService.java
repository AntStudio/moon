package org.moon.support.token.service;

import org.moon.base.service.BaseService;
import org.moon.support.token.Token;

/**
 * @author:Gavin
 * @date 2015/2/27 0027
 */
public interface TokenService extends BaseService{

    /**
     * 添加令牌
     * @param token
     * @return
     */
    public Token add(Token token);

    /**
     * 添加或更新令牌
     * @param token
     * @return
     */
    public Token addOrUpdateToken(Token token);

    /**
     * 更新令牌
     * @param token
     * @return
     */
    public Token update(Token token);

    /**
     * 删除userId对应的令牌
     * @param userId
     */
    public void delete(Long userId);

    /**
     * 获取user对应的令牌
     * @param userId
     * @return
     */
    public Token getTokenForUser(Long userId);

    /**
     * 验证token是否合法
     * @param token
     * @return
     */
    public boolean validate(Token token);

    /**
     * 根据令牌获取令牌对象
     * @param token
     * @return
     */
    public Token getToken(String token);
}

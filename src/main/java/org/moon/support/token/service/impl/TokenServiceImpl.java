package org.moon.support.token.service.impl;

import org.moon.base.service.AbstractService;
import org.moon.support.token.Token;
import org.moon.support.token.repository.TokenRepository;
import org.moon.support.token.service.TokenService;
import org.moon.utils.Objects;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author:Gavin
 * @date 2015/4/23 0023
 */
@Service
public class TokenServiceImpl extends AbstractService implements TokenService {

    @Resource
    private TokenRepository tokenRepository;

    @Override
    public Token add(Token token) {
        tokenRepository.add(token);
        return token;
    }

    @Override
    public Token addOrUpdateToken(Token token) {
        if(Objects.nonNull(token.getId())){
            return update(token);
        }else{
            Token oldToken = getTokenForUser(token.getUserId());
            if(Objects.nonNull(oldToken)){//如果数据库存在该记录，则更新
                token.setId(oldToken.getId());
                return update(token);
            }else{
                return add(token);
            }
        }
    }

    @Override
    public Token update(Token token) {
        tokenRepository.update(token);
        return token;
    }

    @Override
    public void delete(Long userId) {
        Token token = new Token();
        token.setUserId(userId);
        tokenRepository.delete(token);
    }

    @Override
    public Token getTokenForUser(Long userId) {
        return tokenRepository.getTokenForUser(userId);
    }

    @Override
    public boolean validate(Token token) {
        if(Objects.isNull(token.getUserId()) || Objects.isNull(token.getToken())){
            return false;
        }
        Token tokenFromDB = getTokenForUser(token.getUserId());
        return Objects.nonNull(tokenFromDB) && tokenFromDB.getToken().equals(token.getToken());
    }

    @Override
    public Token getToken(String token) {
        return tokenRepository.getToken(token);
    }
}

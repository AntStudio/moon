package org.moon.support.token;

import java.util.Calendar;
import java.util.Date;

/**
 * 令牌
 * @author GavinCook
 * @date 2015/2/27 0027
 */
public class Token  {

    private Long id;

    private String token;

    private Long userId;

    private Date expiry;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getExpiry() {
        return expiry;
    }

    public void setExpiry(Date expiry) {
        this.expiry = expiry;
    }

    /**
     * 该数据是否有效
     * @return
     */
    public boolean isValid(){
        return expiry != null && expiry.getTime()>System.currentTimeMillis();
    }

    public void expiryIn(int num,int timeUnit){
        Calendar calendar = Calendar.getInstance();
        calendar.add(timeUnit,num);
        this.setExpiry(new Date(calendar.getTimeInMillis()));
    }

}

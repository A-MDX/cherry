package com.madx.cherry.core.shiro.config;


import com.madx.cherry.core.common.entity.SysUserPO;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Component;

/**
 * Created by a-mdx on 2017/8/1.
 */
@Component
public class PasswordHelper {
    private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
    private String algorithmName = "md5";
    private int hashIterations = 2;

    public void entryptPassword(SysUserPO userPO){
        userPO.setSalt(randomNumberGenerator.nextBytes().toHex());
        // 这个加密方式，简单的 用户名 + 盐 然后加上 密码
        String newPassword = new SimpleHash(algorithmName, userPO.getPassword()
                , ByteSource.Util.bytes(userPO.getCredentialsSalt()), hashIterations).toHex();
        userPO.setPassword(newPassword);
    }

    public void setRandomNumberGenerator(RandomNumberGenerator randomNumberGenerator) {
        this.randomNumberGenerator = randomNumberGenerator;
    }

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public void setHashIterations(int hashIterations) {
        this.hashIterations = hashIterations;
    }
}

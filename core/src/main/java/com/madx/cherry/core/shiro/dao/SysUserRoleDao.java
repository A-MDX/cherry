package com.madx.cherry.core.shiro.dao;


import com.madx.cherry.core.common.CommonCode;
import com.madx.cherry.core.shiro.bean.SysUserRolePO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by a-mdx on 2017/7/31.
 */
public interface SysUserRoleDao extends JpaRepository<SysUserRolePO, Integer> {

    @Query(nativeQuery = true, value = "SELECT \n" +
            "    r.id, r.code, r.name\n" +
            "FROM\n" +
            "    sys_role r\n" +
            "        LEFT JOIN\n" +
            "    sys_user_role ur ON ur.role_id = r.id\n" +
            "        LEFT JOIN\n" +
            "    sys_user u ON u.id = ur.user_id\n" +
            "WHERE\n" +
            "    r.status = " + CommonCode.STATUS_YES_OR_VALID +
            "    AND u.login_name = ?")
    List<Object[]> findVaildRoles(String loginName);
}

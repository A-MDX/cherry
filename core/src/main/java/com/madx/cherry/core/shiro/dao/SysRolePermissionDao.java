package com.madx.cherry.core.shiro.dao;


import com.madx.cherry.core.common.CommonCode;
import com.madx.cherry.core.shiro.bean.SysRolePermissionPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by a-mdx on 2017/7/31.
 */
public interface SysRolePermissionDao extends JpaRepository<SysRolePermissionPO, Integer> {

    /**
     * union 查询，不光是 角色与权限，用户与权限也会加入
     * @param loginName
     * @return
     */
    @Query(nativeQuery = true, value = "SELECT \n" +
            "    p.id, p.code,  p.name\n" +
            "FROM\n" +
            "    sys_permission p,\n" +
            "    sys_role_permission rp,\n" +
            "    sys_user_role ur,\n" +
            "    sys_user u\n" +
            "WHERE\n" +
            "    rp.permission_id = p.id\n" +
            "        AND ur.role_id = rp.role_id\n" +
            "        AND u.id = ur.user_id\n" +
            "        AND p.status = " + CommonCode.STATUS_YES_OR_VALID +
            "        AND u.login_name = ? \n" +
            "UNION \n" +
            "SELECT \n" +
            "    p.id, p.code,  p.name\n" +
            "FROM\n" +
            "    sys_permission p,\n" +
            "    sys_user_permission up,\n" +
            "    sys_user u\n" +
            "WHERE\n" +
            "    p.id = up.permission_id\n" +
            "        AND u.id = up.user_id\n" +
            "        AND p.status = " + CommonCode.STATUS_YES_OR_VALID +
            "        AND u.login_name = ?;")
    List<Object[]> queryValidPermissionByLoginName(String loginName, String loginName2);


    default List<Object[]> queryValidPermissionByLoginName(String loginName){
        return queryValidPermissionByLoginName(loginName, loginName);
    }
}

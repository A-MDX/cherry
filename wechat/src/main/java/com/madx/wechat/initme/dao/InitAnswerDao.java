package com.madx.wechat.initme.dao;

import com.madx.wechat.initme.entity.InitAnswerPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Create by A-mdx at 2018/3/21 0021 10:43
 */
public interface InitAnswerDao extends JpaRepository<InitAnswerPo, String> {

    @Query(nativeQuery = true, value = "SELECT \n" +
            "  a.`answer`,\n" +
            "  a.`create_time` \n" +
            "FROM\n" +
            "  init_answer a,\n" +
            "  init_question q \n" +
            "WHERE a.`question_id` = q.`id` \n" +
            "  AND q.`status` = 1 \n" +
            "  AND q.`z_index` = ?1 \n" +
            "  AND a.`you_id` = ?2 \n" +
            "ORDER BY a.`create_time` DESC ")
    Object[][] findAnswerByIndexAndYouId(int index, String youId);

    @Query(nativeQuery = true, value = "SELECT \n" +
            "  a.`answer`,\n" +
            "  a.`create_time` ,\n" +
            "  yo.`name`\n" +
            "FROM\n" +
            "  init_answer a,\n" +
            "  init_question q,\n" +
            "  init_you yo\n" +
            "WHERE a.`question_id` = q.`id` \n" +
            "  AND q.`status` = 1 \n" +
            "  AND yo.`id` = a.`you_id`\n" +
            "  AND q.`z_index` = ?1 \n" +
            "ORDER BY a.`create_time` DESC ")
    Object[][] findAnswerByIndex(int index);

}

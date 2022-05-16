package com.darwin.dog.po.sys;

import com.darwin.dog.annotation.Comment;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Table;
import org.springframework.util.Assert;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * 权限表达式
 */
@Data
@Embeddable
@EqualsAndHashCode
@RequiredArgsConstructor(staticName = "of")
public class PermissionExpression implements Serializable {

    @NonNull
    @Comment("资源类型")
    @Column(name = "resource_type",updatable = false,columnDefinition = "VARCHAR(128)")
    private String resourceType;          // 资源类型

    @NonNull
    @Comment("动作")
    @Column(name = "action",unique = true,updatable = false,columnDefinition = "VARCHAR(128)")
    private String action;              // 动作

    @NonNull
    @Comment("资源的实例对象")
    @Column(name = "resource_id",updatable = false,columnDefinition = "VARCHAR(64)")
    private String resource;      // 资源对象

    public PermissionExpression() {

    }


    /**
     * <h3>解析表达式</h3>
     * <p>Example:</p>
     * <ol>
     *     <li>Apple:delete:*                    删除任意的 Apple</li>
     *     <li>com.example.Person:update:*       更新任意的 Person</li>
     *     <li>com.example.Dog:read:1            查看 ID 为 1 的 Dog</li>
     * </ol>
     * <b>推荐使用全限定类名来表示 targetType</b>
     * @param  expression shiro 权限表达式
     * @return PermissionExpression 解析后的权限表达式对象
     */
    public static PermissionExpression parse(String expression){
        String[] temp = expression.split(":");
        Assert.isTrue(temp.length == 3,"Expression cannot be parsed");
        return of(temp[0],temp[1],temp[2]);
    }


    public boolean canAccess(String resourceType,String action,String resource){
        return equals(of(resourceType, action, resource));
    }



}

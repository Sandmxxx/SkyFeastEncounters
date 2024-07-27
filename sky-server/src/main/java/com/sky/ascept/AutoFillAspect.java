package com.sky.ascept;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 自定义切面类，用于实现公共字段自动填充
 */
@Aspect
@Slf4j
@Component
public class AutoFillAspect {

    /**
     * 切入点
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointcut() {}

    /**
     * 前置通知，在通知中进行公共字段的赋值
     * @param joinPoint
     */
    @Before("autoFillPointcut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("开始进行公共字段自动填充...");

        // 获取当前被拦截的方法上的数据库操作类型
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();// 获取方法签名对象
        AutoFill autoFill = methodSignature.getMethod().getAnnotation(AutoFill.class);// 获取注解对象
        OperationType operationType = autoFill.value();

        // 获取当前被拦截的方法参数---实体对象
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return;
        }
        // 实体对象默认设置为第一个参数
        Object entity = args[0];

        // 获取要插入的数据
        Long currentId = BaseContext.getCurrentId();
        LocalDateTime now = LocalDateTime.now();

        // 根据当前数据库操作类型，为对应的属性赋值
        if(operationType == OperationType.INSERT){
            try {
                // 获取set方法
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                // 调用set方法
                setCreateTime.invoke(entity, now);
                setCreateUser.invoke(entity, currentId);
                setUpdateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, currentId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(operationType == OperationType.UPDATE){
            try {
                // 获取set方法
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                // 调用set方法
                setUpdateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, currentId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

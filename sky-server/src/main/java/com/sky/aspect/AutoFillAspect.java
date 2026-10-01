package com.sky.aspect;

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
 * 自定义切面类，用于实现公共字段的自动填充
 * 背景：createTime / createUser / updateTime / updateUser 这 4 个字段，几乎每张表的新增、
 * 修改都要赋值，如果每个 Service 都手写一遍会产生大量重复代码。
 * 实现思路：1、在 Mapper 方法上贴 AutoFill 注解，注明本次是新增还是更新；
 * 2、本切面拦截所有贴了该注解的 Mapper 方法，在方法执行【前】通过反射给实体赋值。
 * 使用方法：在 Mapper 方法上加 @AutoFill(OperationType.INSERT) 注解即可
 */
@Aspect        // 声明这是一个切面类，Spring 会把它当作 AOP 通知处理
@Component     // 交给 Spring 容器管理
@Slf4j         // 通过 Lombok 生成 log 日志对象
public class AutoFillAspect {

    /**
     * 切入点，定义"在哪些方法上生效"
     * 条件一：execution(* com.sky.mapper.*.*(..))，匹配 com.sky.mapper 包下所有类的所有方法
     * 条件二：@annotation(com.sky.annotation.AutoFill)，被拦截的方法上必须贴了 AutoFill 注解
     * 两个条件用 && 连接，必须同时满足才会被拦截，所以只有手动标了 AutoFill 的 Mapper 方法才会触发自动填充
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..))" +
            "&& @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut() {
    }


    /**
     * 前置通知，在目标 Mapper 方法执行之前完成公共字段的填充
     *
     * @param joinPoint 连接点，可以从中取得被拦截方法的签名与参数
     */
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("开始进行公共字段自动填充...");

        // 1、取得被拦截方法的签名，进而拿到方法上的 AutoFill 注解
        //    getSignature() 返回的是父接口类型 Signature，必须强转成 MethodSignature 才能取得 Method
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);

        // 读取注解上的值，判断本次是新增还是更新
        OperationType operationType = autoFill.value();

        // 2、取得方法的实参。约定：实体对象固定放在 Mapper 方法的第 1 个参数位置
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            // 方法没有参数，无需填充，直接放行
            return;
        }
        Object entity = args[0];

        // 3、准备待填充的值
        //    提前取出，保证同一实体上的多个字段用的是同一个时间戳、同一个操作人 id
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

        // 4、根据操作类型，通过反射调用对应的 setter 完成赋值
        if (operationType == OperationType.INSERT) {
            // 新增：4 个公共字段都要填
            try {
                // 通过「方法名 + 参数类型」定位 setter
                // getDeclaredMethod 只查找本类声明的方法，Lombok 生成的 setter 就在实体类自身上，所以取得到
                Method setCreateTime = entity.getClass().getDeclaredMethod(
                        AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(
                        AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(
                        AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(
                        AutoFillConstant.SET_UPDATE_USER, Long.class);

                // invoke(目标对象, 实参...)：第一个参数是"给哪个对象赋值"，漏掉会抛 IllegalArgumentException
                setCreateTime.invoke(entity, now);
                setCreateUser.invoke(entity, currentId);
                setUpdateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, currentId);
            } catch (Exception e) {
                // 反射调用失败（方法不存在、参数不匹配等），包装成运行时异常抛出
                throw new RuntimeException(e);
            }
        } else if (operationType == OperationType.UPDATE) {
            // 更新：只填 updateTime / updateUser
            // createTime / createUser 表示"由谁在何时创建"，一旦生成就永远不该被改动
            try {
                Method setUpdateTime = entity.getClass().getDeclaredMethod(
                        AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(
                        AutoFillConstant.SET_UPDATE_USER, Long.class);

                setUpdateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, currentId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}

















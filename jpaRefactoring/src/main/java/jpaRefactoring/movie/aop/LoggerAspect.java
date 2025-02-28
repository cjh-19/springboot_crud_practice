package jpaRefactoring.movie.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class LoggerAspect {
    @Pointcut("execution(* jpaRefactoring.movie..controller.*Controller.*(..)) || execution(* jpaRefactoring.movie..service.*ServiceImpl.*(..)) || execution(* jpaRefactoring.movie..mapper.*Mapper.*(..))")
    private void loggerTarget() {

    }

    @Around("loggerTarget()")
    public Object logPrinter(ProceedingJoinPoint joinPoint) throws Throwable {
        String type = "";
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();

        if (className.indexOf("Controller") > -1) {
            type = "[Controller]";
        } else if (className.indexOf("Service") > -1) {
            type = "[Service]";
        } else if (className.indexOf("Mapper") > -1) {
            type = "[Mapper]";
        }

        log.debug(type + " " + className + "." + methodName);
        return joinPoint.proceed();
    }
}

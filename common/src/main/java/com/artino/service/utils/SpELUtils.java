package com.artino.service.utils;

import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.Optional;
public class SpELUtils {
    /**
     * 获取method的class+method 描述
     *
     * @param method
     * @return
     */
    public static String getMethodString(Method method) {
        return String.format("%s#%s", method.getDeclaringClass(), method.getName());
    }

    /**
     * 执行EL表达式
     *
     * @param method
     * @param args
     * @param spEL
     * @return
     */
    public static String evalSpEL(Method method, Object[] args, String spEL) {
        ExpressionParser parser = new SpelExpressionParser();
        DefaultParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
        String[] params = Optional.ofNullable(parameterNameDiscoverer.getParameterNames(method)).orElse(new String[]{});
        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < params.length; i++) context.setVariable(params[i], args[i]);
        Expression expression = parser.parseExpression(spEL);
        return expression.getValue(context, String.class);
    }
}

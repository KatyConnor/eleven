package com.hx.lang.commons.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 功能描述 ：spring 表达式生成工具 和 条件多个运算
 *
 */
public class GenerateSpringExpressionUtil {

    /**
     * 使用自定义key作为spring表达式替换符
     * @param source
     * @return
     */
    public static String GenerateSpringExpress(String source){
        /**
         * SpringExpress #{#A} > 3 && #{#A} == 3  || #{#B} == 3
         */
        if( null == source){
            throw  new IllegalArgumentException("参数不能为空");
        }
        //模板替换
        String template = "#{#A}";
        String newValue = template.replaceAll("A", source);

        return newValue;

    }

    /**
     *  spring表达式 通过map对象进行key替换并得出 表达式 的结果
     *   是否满足条件 满足 就是 true
     * @param springExpressSource
     * @param paramaps
     * @return
     */
    public static boolean  calculateSpringExpressForMap(String springExpressSource, Map paramaps){
        boolean resultFlag = false;
        if(null == springExpressSource){
            throw new IllegalArgumentException("springExpressSource is null");
        }
//        ExpressionParser parser = new SpelExpressionParser();
//        EvaluationContext context = new StandardEvaluationContext();
//        if(null != paramaps && paramaps.size() > 0){
//            Iterator iterator = paramaps.keySet().iterator();
//            while (iterator.hasNext()){
//                String key =(String)iterator.next();
//                context.setVariable(key, paramaps.get(key));
//            }
//
//            String expressionTemplate = springExpressSource;
//            try {
//                String result = parser.parseExpression(expressionTemplate, new TemplateParserContext()).getValue(context,String.class);
//                String value = parser.parseExpression(result).getValue(String.class);
//                System.out.println(value);
//                resultFlag = parser.parseExpression(result).getValue(boolean.class);
//
//            } catch (EvaluationException e) {
//                throw new IllegalArgumentException("spring表达式中变量名 与 传入的map对象key值不一致，请检查");
//            } catch (ParseException e) {
//                throw new IllegalArgumentException("spring表达式中变量名 与 传入的map对象key值不一致，请检查");
//            }
//        }

        return resultFlag;
    }

    public static void main(String[] args) {
        String ss = "{test.url}";
        Map<String,Object> paramaps = new HashMap<>();
        paramaps.put("{test.url}","https://10.187.95.9:20181/pms");//
        boolean fl = calculateSpringExpressForMap(ss,paramaps);
        System.out.println(fl);
    }
}

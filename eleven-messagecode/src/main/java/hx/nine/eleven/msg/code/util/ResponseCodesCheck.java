package hx.nine.eleven.msg.code.util;

import hx.nine.eleven.commons.utils.JSONObjectMapper;
import hx.nine.eleven.msg.code.code.MessageCode;
import hx.nine.eleven.msg.code.code.ApplicationSystemCode;
import hx.nine.eleven.msg.code.exception.SubMessageCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * @Author mingliang
 * @Date 2019-09-04 16:08
 */
public class ResponseCodesCheck {

    private final static Logger LOGGER = LoggerFactory.getLogger(ResponseCodesCheck.class);

    public static <T extends ApplicationSystemCode> void check(Class<T>[] classzz){
        List<Class<T>> classzzs = Arrays.asList(classzz);
        List<Field> fields = new ArrayList<>();
        classzzs.stream().forEach(v-> fields.addAll(Arrays.asList(v.getDeclaredFields())));

        List<MessageCode> responseCodes = fields.stream().map(f -> {
            final List<MessageCode> messageCode = new ArrayList<>();
            classzzs.stream().forEach(v -> {
                try {
                    Object value = f.get(ApplicationSystemCode.class);
                    if (null != value) {
                        messageCode.add((MessageCode) value);
                        return;
                    }
                } catch (IllegalAccessException e) {
                    LOGGER.error("筛选消息编码异常 ,exception:{}", e);
                }
            });
            return messageCode.get(0);
        }).collect(Collectors.toList());

        Map<String,List<MessageCode>> responseCodeMap = responseCodes.stream().collect(groupingBy((MessageCode c)-> c.getCodeType()));
        checkCode(responseCodeMap);

    }

    private static void checkCode(Map<String,List<MessageCode>> responseCodeMap ){
        responseCodeMap.forEach((k,v)->{
            List<String> codes = v.stream().map(pc-> pc.getCode()).collect(Collectors.toList());
            Map<String,List<MessageCode>> groupMap = v.stream().collect(groupingBy((MessageCode pc) -> pc.getCode()));
            if (groupMap.size() != v.size()){
                throw new SubMessageCodeException("响应码存在重复命名，请检查，%s,%s",k, JSONObjectMapper.toJsonString(codes));
            }

            String regV = "^*[A-Z][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]*$";
            Pattern patternV = Pattern.compile( regV );
            String reg = "^.*[，。？：；“” ‘’ ！— …… 、].*|.*(—{2}).*|.*(（）).*|.*(【】).*|.*(｛｝).*|.*(《》).*|.*[-,.?:;'\"!`].*|.*(-{2}).*|.*(\\.{3}).*|.*(\\(\\)).*|.*(\\[\\]).*|.*(\\{\\}).*|.*"
                    + "[、_ =\\ \\\\ [\\ ] ; ' ，. / ~ ! @ # $ % ^ & * ( ) + | ? > < “ :{}].*$";
            Pattern pattern = Pattern.compile( reg );

            v.forEach(value->{
                if (!value.getCode().startsWith(k)){
                    throw new SubMessageCodeException("响应码命名不规范, 必须以分类字母开头,请检查，%s",JSONObjectMapper.toJsonString(value));
                }

                Matcher matcherNum = patternV.matcher( value.getCode() );
                if ((value.getCode().length() != 11 || !matcherNum.matches()) && !value.getCode().equals("AAAAAAA") ){
                    throw new SubMessageCodeException( "消息编必须长度为11位,满足[A-Z][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]命名格式" +
                            "errorCode =%s, length =%s", value.getCode(),value.getCode().length() );
                }

                Matcher matcher = pattern.matcher( value.getMessage() );
                if ( matcher.matches() ) {
                    throw new SubMessageCodeException( "[ message ] 内容不能有标点符号以及特殊符号， errorMessage = %s ", value.getMessage() );
                }
            });
        });
    }
}

package org.franwork.core.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

/**
 * TemplateUtil for template string value converting.
 * 
 * @author Frankie
 */
public abstract class TemplateUtil {
    
	/**
	 * Field Template starting symbol.
	 */
    public static final String FIELD_PREFIX = "${";
    
    /**
     * Field Template ending symbol.
     */
    public static final String FIELD_SUFFIX = "}";
    
    /**
     * Field Method Template starting symbol.
     */
    public static final String FIELD_METHOD_PREFIX = "%{";
    
    /**
     * Field Method Template ending symbol.
     */
    public static final String FIELD_METHOD_SUFFIX = "}";
    
    /**
     * Function name Template starting symbol.
     */
    public static final String FUNCTION_PREFIX = "#{";
    
    /**
     * Function name Template ending symbol.
     */
    public static final String FUNCTION_SUFFIX = "}";

    /**
     * Evaluate the template by given function object and Data bean.
     * 
     * @param functionObj
     * @param dataBean
     * @param template
     * @return
     */
    public static String evalTemplate(Object functionObj, Object dataBean, String template) {
        String evalResult = template;
        evalResult = TemplateUtil.evalFieldTemplate(dataBean, evalResult);
        evalResult = TemplateUtil.evalMethodTemplate(dataBean, evalResult);
        evalResult = TemplateUtil.evalFunctionTemplate(functionObj, dataBean, evalResult);
        return evalResult;
    }
    
    public static String evalFieldTemplate(Object dataBean, String template) {
        Set<String> fieldNames = TemplateUtil.getMsgsFromPrefixToSuffix(template, FIELD_PREFIX, FIELD_SUFFIX);
        if (CollectionUtils.isEmpty(fieldNames)) {
            return template;
        }
        Map<String, Object> fieldValueMap = new HashMap<String, Object>();
        for (String fieldName : fieldNames) {
            String fieldExp = TemplateUtil.trimPrefixAndSuffix(fieldName, FIELD_PREFIX, FIELD_SUFFIX);
            fieldValueMap.put(fieldName, OgnlUtils.getFieldOgnlValue(dataBean, fieldExp));
        }
        return TemplateUtil.replaceTemplatePatterns(fieldValueMap, template);
    }
    
    public static String evalMethodTemplate(Object dataBean, String template) {
        Set<String> methodNames = TemplateUtil.getMsgsFromPrefixToSuffix(template, FIELD_METHOD_PREFIX, FIELD_METHOD_SUFFIX);
        if (CollectionUtils.isEmpty(methodNames)) {
            return template;
        }
        Map<String, Object> methodValueMap = new HashMap<String, Object>();
        for (String methodName : methodNames) {
            String methodExp = TemplateUtil.trimPrefixAndSuffix(methodName, FIELD_METHOD_PREFIX, FIELD_METHOD_SUFFIX);
            methodValueMap.put(methodName, OgnlUtils.getMethodOgnlValue(dataBean, methodExp));
        }
        return TemplateUtil.replaceTemplatePatterns(methodValueMap, template);
    }
    
    public static String evalFunctionTemplate(Object functionObj, Object dataBean, String template) {
        Set<String> funcNamePatterns = TemplateUtil.getMsgsFromPrefixToSuffix(template, FUNCTION_PREFIX, FUNCTION_SUFFIX);
        if (CollectionUtils.isEmpty(funcNamePatterns)) {
            return template;
        }
        Class<?>[] functionArgTypes = new Class<?>[] {Object.class};
        Object[] functionArg = new Object[] {dataBean};
        Map<String, Object> funcValueMap = new HashMap<String, Object>();
        for (String funcNamePattern : funcNamePatterns) {
            String functionName = TemplateUtil.trimPrefixAndSuffix(funcNamePattern, FUNCTION_PREFIX, FUNCTION_SUFFIX);
            Object retResult = BeanUtils.invokeMethod(functionObj, functionName, functionArgTypes, functionArg, true);
            funcValueMap.put(funcNamePattern, retResult);
        }
        return TemplateUtil.replaceTemplatePatterns(funcValueMap, template);
    }
    
    private static String replaceTemplatePatterns(Map<String, Object> valueMap, String template) {
        if (MapUtils.isEmpty(valueMap)) {
            return template;
        }
        for (Map.Entry<String, Object> valueEntry : valueMap.entrySet()) {
            template = StringUtils.replace(template, valueEntry.getKey(), 
                    (valueEntry.getValue() == null ? StringUtils.EMPTY : valueEntry.getValue().toString()));
        }
        return template;
    }
    
    public static Set<String> getMsgsFromPrefixToSuffix(String template, String prefix, String suffix) {
        Set<String> messages = new HashSet<String>();
        if (StringUtils.isBlank(template)) {
            return messages;
        }
        int prefixIndex = 0;
        int suffixIndex = 0;
        while ((prefixIndex = template.indexOf(prefix, prefixIndex)) >= 0) {
            suffixIndex = template.indexOf(suffix, prefixIndex + prefix.length());
            if (suffixIndex < 0) {
                break;
            }
            String message = template.substring(prefixIndex, suffixIndex + suffix.length());
            messages.add(message);
            prefixIndex += message.length();
        }
        return messages;
    }
    
    public static String trimPrefixAndSuffix(String template, String prefix, String suffix) {
        if (StringUtils.isBlank(template)) {
            return StringUtils.EMPTY;
        }
        int prefixIndex = template.indexOf(prefix);
        int suffixIndex = template.indexOf(suffix);
        if (prefixIndex < 0 || suffixIndex < 0 || prefixIndex >= suffixIndex) {
            return StringUtils.EMPTY;
        }
        return template.substring(prefixIndex + prefix.length(), suffixIndex);
    }
}

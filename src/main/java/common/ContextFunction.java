package common;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.Function;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.util.JMeterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContextFunction extends AbstractFunction implements Function {
    private static final Logger logger = LoggerFactory.getLogger(ContextFunction.class);
    private static final List<String> desc = new LinkedList();
    private static Pattern contextRegex = Pattern.compile("\\$\\{\\_\\_Context\\(([a-z|A-Z|0-9| _\\.|\\[|\\]]*)\\)\\}");
    private CompoundVariable contextKey;
    private Map<String, Object> context;

    public ContextFunction() {
    }

    public static String replaceContextFunctions(String content, Map<String, Object> context, int index) {
        content = StringUtils.replace(content, "[i]", "[" + String.valueOf(index) + "]");
        Matcher matcher = contextRegex.matcher(content);
        StringBuilder newContent = new StringBuilder(content.length());

        int lastMatchEnd;
        for(lastMatchEnd = 0; matcher.find(); lastMatchEnd = matcher.end()) {
            content = StringUtils.replace(content, "[i]", "[" + String.valueOf(index) + "]");
            newContent.append(content.substring(lastMatchEnd, matcher.start()));
            ContextFunction contextFunction = new ContextFunction();
            contextFunction.setContext(context);
            Collection<CompoundVariable> params = new ArrayList();
            params.add(new CompoundVariable(matcher.group(1)));

            try {
                contextFunction.setParameters(params);
                newContent.append(contextFunction.execute((SampleResult)null, (Sampler)null));
            } catch (InvalidVariableException var9) {
                logger.error("Unable to replace Context function {}", matcher.group(0), var9);
                throw new RuntimeException("Unable to replace Context function " + matcher.group(0), var9);
            } catch (Exception var10) {
                logger.error("Unable to replace Context function {}", matcher.group(0), var10);
                return null;
            }
        }

        if (lastMatchEnd > 0) {
            newContent.append(content.substring(lastMatchEnd));
            content = newContent.toString();
        }

        if (content == null) {
            return StringUtils.defaultString(content);
        } else {
            if (contextRegex.matcher(content).find()) {
                content = replaceContextFunctions(content, context, index);
            } else if (content.equals("null")) {
                return "";
            }

            return content.trim();
        }
    }

    public static String replaceContextFunctions(String content, Map<String, Object> context) {
        Matcher matcher = contextRegex.matcher(content);
        StringBuilder newContent = new StringBuilder(content.length());

        int lastMatchEnd;
        for(lastMatchEnd = 0; matcher.find(); lastMatchEnd = matcher.end()) {
            newContent.append(content.substring(lastMatchEnd, matcher.start()));
            ContextFunction contextFunction = new ContextFunction();
            contextFunction.setContext(context);
            Collection<CompoundVariable> params = new ArrayList();
            params.add(new CompoundVariable(matcher.group(1)));

            try {
                contextFunction.setParameters(params);
                newContent.append(contextFunction.execute((SampleResult)null, (Sampler)null));
            } catch (InvalidVariableException var8) {
                logger.error("Unable to replace Context function {}", matcher.group(0), var8);
                throw new RuntimeException("Unable to replace Context function " + matcher.group(0), var8);
            } catch (Exception var9) {
                logger.error("Unable to replace Context function {}", matcher.group(0), var9);
                return null;
            }
        }

        if (lastMatchEnd > 0) {
            newContent.append(content.substring(lastMatchEnd));
            content = newContent.toString();
        }

        if (content == null) {
            return StringUtils.defaultString(content);
        } else {
            if (contextRegex.matcher(content).find()) {
                content = replaceContextFunctions(content, context);
            } else if (content.equals("null")) {
                return "";
            }

            return content.trim();
        }
    }

    public String execute(SampleResult previousResult, Sampler currentSampler) {
        try {
            return this.context instanceof Map && this.context.get(this.contextKey.execute().substring(0, this.contextKey.execute().indexOf("."))) == null ? "" : BeanUtils.getProperty(this.context, this.contextKey.execute());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException var4) {
            logger.error("Unable to get property for contextKey {}", this.contextKey, var4);
            throw new RuntimeException("Unable to get property for contextKey " + this.contextKey, var4);
        }
    }

    public void setParameters(Collection<CompoundVariable> parameters) throws InvalidVariableException {
        this.checkParameterCount(parameters, 1, 1);
        Object[] values = parameters.toArray();
        this.contextKey = (CompoundVariable)values[0];
    }

    public String getReferenceKey() {
        return "__Context";
    }

    public List<String> getArgumentDesc() {
        return desc;
    }

    public void setContext(Map<String, Object> context) {
        this.context = context;
    }

    static {
        desc.add(JMeterUtils.getResString("context_key"));
    }
}

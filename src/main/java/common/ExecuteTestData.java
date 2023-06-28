package common;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.Function;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.reflections.Reflections;
import org.reflections.scanners.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class ExecuteTestData {
    private static final Logger logger = LoggerFactory.getLogger(ExecuteTestData.class);
    Map<String, Object> jmeterFunctions;

    public ExecuteTestData() throws Exception {
        Reflections jmeterFuncs = new Reflections("org/apache/jmeter/function", new Scanner[0]);
        Set<Class<? extends Function>> functions = jmeterFuncs.getSubTypesOf(Function.class);
        Reflections customFuncs = new Reflections("common", new Scanner[0]);
        functions.addAll(customFuncs.getSubTypesOf(Function.class));
        logger.debug("Found {} JMeter functions", functions.size());
        Map<String, Object> jmeterFunctions = new HashMap();
        Iterator var5 = functions.iterator();

        while(var5.hasNext()) {
            Class clazz = (Class)var5.next();

            try {
                if (!clazz.isInterface() && !Modifier.isAbstract(clazz.getModifiers())) {
                    Function f = (Function)clazz.newInstance();
                    jmeterFunctions.put(f.getReferenceKey(), f);
                    logger.debug("Added function {} to available functions with class {}", f.getReferenceKey(), clazz.getName());
                }
            } catch (IllegalAccessException | InstantiationException var8) {
                logger.error("Could not instantiate JMeter Function {}", clazz.getName(), var8);
                throw var8;
            }
        }

        this.jmeterFunctions = jmeterFunctions;
    }

    public String getReplacementValue(Object testCaseValue, Map<String, String> sqlStatements, CucumberContext context) throws Exception {
        String newValue = (String)testCaseValue;
        if (testCaseValue != null && testCaseValue instanceof String && testCaseValue.toString().startsWith("${__")) {
            logger.debug("Found Function call {}", testCaseValue);
            String testCaseValueStr = testCaseValue.toString();
            Object function = this.jmeterFunctions.get(testCaseValueStr.substring(2, testCaseValueStr.indexOf(40)));
            if (function != null) {
                logger.debug("Found associated function {}", function.getClass().getName());

                try {
                    Function funcInst = (Function)function.getClass().newInstance();
                    if (function.getClass().isAssignableFrom(SqlQueryFunction.class)) {
                        ((SqlQueryFunction)funcInst).setExecutedQueryAndResult(context.getExecutedQueryAndResult());
                        ((SqlQueryFunction)funcInst).setSqlStatements(sqlStatements);
                    }

                    if (function.getClass().isAssignableFrom(ContextFunction.class)) {
                        ((ContextFunction)funcInst).setContext(context.getTestDataContext());
                    }

                    String paramsStr = testCaseValueStr.substring(testCaseValueStr.indexOf(40) + 1, testCaseValueStr.lastIndexOf(41));
                    String[] paramsArr = paramsStr.split(",");
                    Collection<CompoundVariable> paramsColl = new ArrayList(paramsArr.length);
                    String[] var11 = paramsArr;
                    int var12 = paramsArr.length;

                    for(int var13 = 0; var13 < var12; ++var13) {
                        String param = var11[var13];
                        paramsColl.add(new CompoundVariable(param));
                    }

                    funcInst.setParameters(paramsColl);
                    newValue = funcInst.execute((SampleResult)null, (Sampler)null);
                } catch (IllegalAccessException | InvalidVariableException | InstantiationException var15) {
                    logger.error("Could not execute function {} from placeholder {}", new Object[]{function.getClass().getName(), testCaseValue, var15});
                    throw var15;
                }
            } else {
                logger.error("Could not find associated function for {}", testCaseValue);
            }
        }

        return StringUtils.defaultString(newValue);
    }
}

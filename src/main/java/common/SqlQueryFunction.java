package common;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import base.BaseSetup;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.jmeter.engine.util.CompoundVariable;
import org.apache.jmeter.functions.AbstractFunction;
import org.apache.jmeter.functions.Function;
import org.apache.jmeter.functions.InvalidVariableException;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.util.JMeterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.DBUtils;

public class SqlQueryFunction extends AbstractFunction implements Function {
    private static final Logger logger = LoggerFactory.getLogger(SqlQueryFunction.class);
    private static final List<String> desc = new LinkedList();
    private CompoundVariable query;
    private CompoundVariable pointer;
    private Map<String, String> sqlStatements;
    private Map<String, Object> executedQueryAndResult;

    public SqlQueryFunction() {
    }

    public String execute(SampleResult previousResult, Sampler currentSampler) {
        String queryKey = this.query.execute();
        String query = (String)this.sqlStatements.get(queryKey);
        query = ContextFunction.replaceContextFunctions(query, BaseSetup.context.getTestDataContext());

        try {
            Object result;
            if (this.executedQueryAndResult.containsKey(queryKey)) {
                result = this.executedQueryAndResult.get(queryKey);
            } else {
                result = DBUtils.getListOfRecords(query);
                this.executedQueryAndResult.put(queryKey, result);
            }

            this.pointer = new CompoundVariable(this.pointer.getRawParameters().replaceFirst("/", "[").replaceFirst("/", "]."));

            try {
                return BeanUtils.getProperty(result, this.pointer.execute());
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException var7) {
                logger.error("Unable to get property for contextKey {}", this.pointer, var7);
                throw new RuntimeException("Unable to get property for contextKey " + this.pointer, var7);
            }
        } catch (Exception var8) {
            return "";
        }
    }

    public void setParameters(Collection<CompoundVariable> parameters) throws InvalidVariableException {
        this.checkParameterCount(parameters, 1, 2);
        Object[] values = parameters.toArray();
        this.query = (CompoundVariable)values[0];
        this.pointer = (CompoundVariable)values[1];
    }

    public String getReferenceKey() {
        return "__SqlQuery";
    }

    public List<String> getArgumentDesc() {
        return desc;
    }

    public void setSqlStatements(Map<String, String> sqlStatements) {
        this.sqlStatements = sqlStatements;
    }

    public void setExecutedQueryAndResult(Map<String, Object> executedQueryAndResult) {
        this.executedQueryAndResult = executedQueryAndResult;
    }

    static {
        desc.add(JMeterUtils.getResString("sql_query"));
        desc.add(JMeterUtils.getResString("pointer"));
    }
}

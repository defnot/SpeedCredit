package util;

import play.api.mvc.QueryStringBindable$;
import play.libs.F;
import play.mvc.QueryStringBindable;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by Dany on 2/11/2016.
 */
public class BigDecimalW implements QueryStringBindable<BigDecimalW> {
    public BigDecimal value = null;

    @Override
    public F.Option<BigDecimalW> bind(String key, Map<String, String[]> data) {
        String[] vs = data.get(key);
        if (vs != null && vs.length > 0) {
            String v = vs[0];
            BigDecimal val = new BigDecimal(v);
            value = val;
            return F.Some(this);
        }
        return F.None();
    }

    @Override
    public String unbind(String key) {
        return key + "=" + value;
    }
    @Override
    public String javascriptUnbind() {
        return value.toString();
    }
}

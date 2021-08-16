package su.savings.db;

import java.sql.Date;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class QueryParam {

    private final int type;
    private final Object value;

    public QueryParam(int type, Object value) {
        this.type = type;
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public static QueryParam boolenParam(Boolean value) {
        return new QueryParam(Types.BOOLEAN, value);
    }

    public static QueryParam intParam(int value) {
        return new QueryParam(Types.INTEGER, value);
    }
    public static QueryParam intParam(Integer value) {
        return new QueryParam(Types.INTEGER, value);
    }

    public static QueryParam longParam(long value) {
        return new QueryParam(Types.BIGINT, value);
    }
    public static QueryParam longParam(Long value) {
        return new QueryParam(Types.BIGINT, value);
    }

    public static QueryParam stringParam(String value) {
        return new QueryParam(Types.VARCHAR, value);
    }

    public static QueryParam dateParam(LocalDate value) {
        return new QueryParam(Types.DATE, Date.valueOf(value));
    }

    public static QueryParam dateTime(LocalDateTime value) {
        return new QueryParam(Types.TIMESTAMP, Timestamp.valueOf(value));
    }
}

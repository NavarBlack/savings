package su.savings.oae.classinterface;

import java.sql.ResultSet;

public interface AllActors <T> {
    T mapper(ResultSet rs);
}

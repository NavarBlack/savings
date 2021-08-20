package su.savings.db;

import su.savings.helpers.SQLStartQuery;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class Database {
    public Connection connection;

    private Database() {
        try {
            Class.forName("org.h2.Driver");
            String password = "15935748625";
            String name = "userPlans";
            String url = "jdbc:h2:file:./data/plans;DB_CLOSE_DELAY=-1;INIT=create schema if not exists DATA\\; SET SCHEMA DATA";
            Connection connection = DriverManager.getConnection(url, name, password);
            Statement stm = connection.createStatement();
            stm.execute(SQLStartQuery.createAllPlansTable);
            stm.execute(SQLStartQuery.createKeyPointsTable);
            stm.execute(SQLStartQuery.createAllPeriods);
            stm.execute(SQLStartQuery.createDays);
            stm.execute(SQLStartQuery.createOperations);
            this.connection = connection;
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static class DatabaseSing{
        public static final Database HOLDER_INSTANCE = new Database();
    }

    public static Database getInstance() {
        return DatabaseSing.HOLDER_INSTANCE;
    }

    public <T> Stream<T> executeSelectQuery(String sql, Function<ResultSet, T> mapper, QueryParam ...params) throws SQLException {

        try (
             PreparedStatement statement = connection.prepareStatement(sql)) {

            IntStream.range(0, params.length).forEach(i -> setParam(i+1, params[i], statement));

            List<T> result = new ArrayList<>();

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                result.add(mapper.apply(rs));
            }

            return result.stream();

        } catch (SQLException e) {

            throw new SQLException(e);
        }
    }

    public long executeUpdate(String sql, QueryParam ...params) throws SQLException {

        try (
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            for (int i = 1; i < params.length + 1; i++) {

                if (params[i - 1].getValue() == null)
                    statement.setNull(i, params[i - 1].getType());
                else
                    statement.setObject(i, params[i - 1].getValue(), params[i - 1].getType());

            }

            statement.executeUpdate();

            try(ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong("id");
                } else {
                    return -1;
                }

            }
        }
    }

    private void setParam(int index, QueryParam param, PreparedStatement statement) {
        try {
            if (param.getValue() == null) {
                statement.setNull(index, param.getType());
            } else {
                statement.setObject(index, param.getValue(), param.getType());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}

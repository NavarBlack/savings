package su.savings.oae;

import su.savings.db.Repository;
import su.savings.oae.dto.OperationDTO;

import java.sql.ResultSet;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;

public class Operation extends OperationDTO {

    public static Operation mapper(ResultSet rs) {
        try {
            Operation operation = new Operation();
            operation.setId(rs.getLong("ID"))
            .setName(rs.getString("OPERATION_NAME"))
            .setSum(rs.getLong("SUM"))
            .setPeriodId(rs.getLong("PERIOD_ID"))
            .setExpType(rs.getBoolean("EXP_TYPE"))
            .setNpoDate(Instant.ofEpochMilli(rs.getDate("NPO_DATE").getTime()).atZone(ZoneId.systemDefault()).toLocalDate())
            .setNpoType(rs.getBoolean("NPO_TYPE"));
            return operation;

        } catch (Exception e) {
            throw new RuntimeException("periodMapper", e);
        }
    }

    public static ArrayList<Operation> getBdOperation(Long periodId){
        return new ArrayList<>(Repository.getOperation(periodId)) ;
    }

}

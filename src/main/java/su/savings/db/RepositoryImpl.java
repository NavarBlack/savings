package su.savings.db;

import su.savings.dto.OperationDTO;
import su.savings.dto.PeriodDTO;
import su.savings.dto.PlansDTO;
import su.savings.helpers.Converter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static su.savings.db.QueryParam.*;

public class RepositoryImpl implements Repository {

    private final Database database = Database.getInstance();


    @Override
    public List<PlansDTO> getAllPlans() {
        List<PlansDTO> plansDTOList = database.executeSelectQuery(
                "select *, (select group_concat(KEY_POINTS.DATE_POINT) from DATA.KEY_POINTS where PLAN_ID = DATA.ALL_PLANS.ID) as KEY_POINTS from DATA.ALL_PLANS order by ID desc;",
                this::allPlansMapper).collect(Collectors.toList());
        if (plansDTOList.isEmpty()) return new ArrayList<>();
        else return plansDTOList;
    }

    private PlansDTO allPlansMapper(ResultSet rs) {
        try {
            PlansDTO plansDTO = new PlansDTO();
            plansDTO.setId(rs.getLong("id"))
                    .setPlaneName(rs.getString("PLAN_NAME"))
                    .setStartPlane(Instant.ofEpochMilli(rs.getDate("START_PLAN").getTime()).atZone(ZoneId.systemDefault()).toLocalDate())
                    .setEndPlane(Instant.ofEpochMilli(rs.getDate("END_PLAN").getTime()).atZone(ZoneId.systemDefault()).toLocalDate())
                    .setStartSum(rs.getLong("START_SUM"))
                    .setExpPlanOnDays(rs.getLong("EXP_ON_DEY"))
                    .setPlanDays(rs.getInt("PLAN_DAYS"))
                    .setKeyPoints(Converter.stringToArrayList(rs.getString("KEY_POINTS")))
                    .setPeriods(new ArrayList<>(Objects.requireNonNull(getPeriodOnPlan(rs.getLong("id")))));
            return plansDTO;

        } catch (Exception e) {
            throw new RuntimeException("allPlansMapper", e);
        }
    }

    private List<PeriodDTO> getPeriodOnPlan(Long idPlan) {
        List<PeriodDTO> listPeriod = database.executeSelectQuery(
                "select * from ALL_PERIODS where PLAN_ID = ?;",
                this::periodMapper,
                QueryParam.longParam(idPlan)).collect(Collectors.toList());
        if (listPeriod.isEmpty()) return new ArrayList<>();
        else return listPeriod;
    }

    private PeriodDTO periodMapper(ResultSet rs) {
        try {
            PeriodDTO period = new PeriodDTO();
            period.setId(rs.getLong("id"));
            period.setStartPeriod(Instant.ofEpochMilli(rs.getDate("START_PERIOD").getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
            period.setEndPeriod(Instant.ofEpochMilli(rs.getDate("END_PERIOD").getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
            period.setPeriodDays(rs.getInt("DAYS_PERIOD"));
            period.setStartSum(rs.getLong("START_SUM"));
            period.setEndSum(rs.getLong("END_SUM"));
            period.setExpOnDey(rs.getLong("EXP_ON_DEY"));
            period.setPlanId(rs.getLong("PLAN_ID"));
            period.setOperations(new ArrayList<>(Objects.requireNonNull(getOperation(rs.getLong("id")))));
            return period;

        } catch (Exception e) {
            throw new RuntimeException("periodMapper", e);
        }
    }

    private List<OperationDTO> getOperation(Long idPeriod) {
        List<OperationDTO> listOperation = database.executeSelectQuery(
                "select * from OPERATIONS where PERIOD_ID = ?;",
                this::operationMapper,
                QueryParam.longParam(idPeriod)).collect(Collectors.toList());
        if (listOperation.isEmpty()) return new ArrayList<>();
        else return listOperation;
    }

    private OperationDTO operationMapper(ResultSet rs) {
        try {
            OperationDTO operation = new OperationDTO();
            operation.setId(rs.getLong("ID"));
            operation.setName(rs.getString("OPERATION_NAME"));
            operation.setSum(rs.getLong("SUM"));
            operation.setPeriodId(rs.getLong("PERIOD_ID"));
            operation.setExpType(rs.getBoolean("EXP_TYPE"));
            return operation;

        } catch (Exception e) {
            throw new RuntimeException("periodMapper", e);
        }
    }

    @Override
    public Long savePlan(PlansDTO plansDTO) {
        try {
            Long id = database.executeUpdate("""
                            insert into ALL_PLANS (PLAN_NAME, START_PLAN, END_PLAN, START_SUM, EXP_ON_DEY, PLAN_DAYS) values ( ?,?,?,?,?,? );
                            """,
                    stringParam(plansDTO.getPlaneName()),
                    dateParam(plansDTO.getStartPlane()),
                    dateParam(plansDTO.getEndPlane()),
                    longParam(plansDTO.getStartSum()),
                    longParam(plansDTO.getExpPlanOnDays()),
                    intParam(plansDTO.getPlanDays())
            );
            plansDTO.getKeyPoints().forEach(localDate -> savePlanKeyPoint(localDate, id));
            plansDTO.getPeriods().forEach(periodDTO -> savePeriodPlan(periodDTO, id, plansDTO.getExpPlanOnDays()));
            return id;
        } catch (SQLException ex) {
            ex.forEach(System.out::println);
            return null;
        }
    }


    @Override
    public void savePlanKeyPoint(LocalDate localDate, Long idPlan) {
        try {
            database.executeUpdate("""
                            insert into KEY_POINTS (DATE_POINT, PLAN_ID) values (?,?)
                            """,
                    dateParam(localDate),
                    longParam(idPlan)
            );
        } catch (SQLException ex) {
            ex.forEach(System.out::println);
        }
    }

    @Override
    public void savePeriodPlan(PeriodDTO period, Long idPlan, Long expOnDey) {
        try {
            Long idPeriod = database.executeUpdate("""
                            insert into ALL_PERIODS (
                             START_PERIOD,
                             END_PERIOD,
                             START_SUM,
                             END_SUM,
                             EXP_ON_DEY,
                             DAYS_PERIOD,
                             PLAN_ID
                             ) VALUES (?,?,?,?,?,?,?)
                            """,
                    dateParam(period.getStartPeriod()),
                    dateParam(period.getEndPeriod()),
                    longParam(period.getStartSum()),
                    longParam(period.getEndSum()),
                    longParam(expOnDey),
                    intParam(period.getPeriodDays()),
                    longParam(idPlan)
            );
            period.getOperations().forEach(op -> savaOperationPeriod(op, idPeriod));
        } catch (SQLException ex) {
            ex.forEach(System.out::println);
        }
    }

    @Override
    public void savaOperationPeriod(OperationDTO operation, Long idPeriod) {
        try {
            database.executeUpdate("""
                            insert into OPERATIONS (OPERATION_NAME, SUM, PERIOD_ID, EXP_TAPE) VALUES(?,?,?,?)
                            """,
                    stringParam(operation.getName()),
                    longParam(operation.getSum()),
                    longParam(idPeriod),
                    boolenParam(operation.getExpType())
            );
        } catch (SQLException ex) {
            ex.forEach(System.out::println);
        }
    }

    @Override
    public void upDatePlan(PlansDTO plan) {
        try {
            Long idPlan = database.executeUpdate("""
                            update ALL_PLANS
                            set PLAN_NAME  =?,
                                START_PLAN =?,
                                END_PLAN   =?,
                                START_SUM  =?,
                                PLAN_DAYS  = ?
                            where ID = ?;
                            """,
                    stringParam(plan.getPlaneName()),
                    dateParam(plan.getStartPlane()),
                    dateParam(plan.getEndPlane()),
                    longParam(plan.getStartSum()),
                    intParam(plan.getPlanDays()),
                    longParam(plan.getId())
            );
            database.executeUpdate("delete from KEY_POINTS where PLAN_ID = ?; ", longParam(idPlan));
            plan.getKeyPoints().forEach(localDate -> upDatePlanKeyPoint(localDate, idPlan));
            database.executeUpdate("delete from ALL_PERIODS where PLAN_ID = ?; ", longParam(idPlan));
            plan.getPeriods().forEach(p -> upDatePeriodPlan(p, idPlan, plan.getExpPlanOnDays()));
        } catch (SQLException ex) {
            ex.forEach(System.out::println);
        }
    }

    @Override
    public void upDatePlanKeyPoint(LocalDate localDate, Long idPlan) {
        try {
            database.executeUpdate("""
                            insert into KEY_POINTS (DATE_POINT, PLAN_ID) VALUES(?,?);
                            """,
                    dateParam(localDate),
                    longParam(idPlan)
            );
        } catch (SQLException ex) {
            ex.forEach(System.out::println);
        }
    }

    @Override
    public void upDatePeriodPlan(PeriodDTO period, Long idPlan, Long expOnDey) {
        try {
            Long periodId = database.executeUpdate("""
                            insert into ALL_PERIODS (
                             START_PERIOD,
                             END_PERIOD,
                             START_SUM,
                             END_SUM,
                             EXP_ON_DEY,
                             DAYS_PERIOD,
                             PLAN_ID
                             ) VALUES (?,?,?,?,?,?,?)
                            """,
                    dateParam(period.getStartPeriod()),
                    dateParam(period.getEndPeriod()),
                    longParam(period.getStartSum()),
                    longParam(period.getEndSum()),
                    longParam(expOnDey),
                    intParam(period.getPeriodDays()),
                    longParam(idPlan)
            );
            period.getOperations().forEach(op -> upDateOperationPeriod(op, periodId));
        } catch (SQLException ex) {
            ex.forEach(System.out::println);
        }
    }

    @Override
    public void upDateOperationPeriod(OperationDTO operation, Long idPeriod) {
        try {
            database.executeUpdate("""
                            insert into OPERATIONS (OPERATION_NAME, SUM, PERIOD_ID, EXP_TYPE) VALUES(?,?,?,?)
                                    """,
                    stringParam(operation.getName()),
                    longParam(operation.getSum()),
                    longParam(idPeriod),
                    boolenParam(operation.getExpType())

            );
        } catch (SQLException ex) {
            ex.forEach(System.out::println);
        }
    }
}

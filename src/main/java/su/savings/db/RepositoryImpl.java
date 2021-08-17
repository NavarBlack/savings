package su.savings.db;

import su.savings.dto.actionModels.Operation;
import su.savings.dto.actionModels.Period;
import su.savings.dto.actionModels.Plan;
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
    public List<Plan> getAllPlans() {
        List<Plan> plansDTOList = database.executeSelectQuery(
                "select *, (select group_concat(KEY_POINTS.DATE_POINT) from DATA.KEY_POINTS where PLAN_ID = DATA.ALL_PLANS.ID) as KEY_POINTS from DATA.ALL_PLANS order by ID desc;",
                this::allPlansMapper).collect(Collectors.toList());
        if (plansDTOList.isEmpty()) return new ArrayList<>();
        else return plansDTOList;
    }

    private Plan allPlansMapper(ResultSet rs) {
        try {
            Plan plansDTO = new Plan();
            plansDTO.setId(rs.getLong("id"))
                    .setPlaneName(rs.getString("PLAN_NAME"))
                    .setStartPlane(Instant.ofEpochMilli(rs.getDate("START_PLAN").getTime()).atZone(ZoneId.systemDefault()).toLocalDate())
                    .setEndPlane(Instant.ofEpochMilli(rs.getDate("END_PLAN").getTime()).atZone(ZoneId.systemDefault()).toLocalDate())
                    .setStartSum(rs.getLong("START_SUM"))
                    .setPlanDays(rs.getLong("PLAN_DAYS"))
                    .setKeyPoints(Converter.stringToArrayList(rs.getString("KEY_POINTS")))
                    .setPeriods(new ArrayList<>(Objects.requireNonNull(getPeriodOnPlan(rs.getLong("id")))));
            return plansDTO;

        } catch (Exception e) {
            throw new RuntimeException("allPlansMapper", e);
        }
    }

    private List<Period> getPeriodOnPlan(Long idPlan) {
        List<Period> listPeriod = database.executeSelectQuery(
                "select * from ALL_PERIODS where PLAN_ID = ?;",
                this::periodMapper,
                QueryParam.longParam(idPlan)).collect(Collectors.toList());
        if (listPeriod.isEmpty()) return new ArrayList<>();
        else return listPeriod;
    }

    private Period periodMapper(ResultSet rs) {
        try {
            Period period = new Period();
            period.setId(rs.getLong("id"))
            .setStartPeriod(Instant.ofEpochMilli(rs.getDate("START_PERIOD").getTime()).atZone(ZoneId.systemDefault()).toLocalDate())
            .setEndPeriod(Instant.ofEpochMilli(rs.getDate("END_PERIOD").getTime()).atZone(ZoneId.systemDefault()).toLocalDate())
            .setPeriodDays(rs.getLong("PERIOD_DAYS"))
            .setStartSum(rs.getLong("START_SUM"))
            .setEndSum(rs.getLong("END_SUM"))
            .setExpOnDey(rs.getLong("EXP_ON_DEY"))
            .setPlanId(rs.getLong("PLAN_ID"))
            .setPlanDays(rs.getLong("PLAN_DAYS"))
            .setPastDaysOnPlan(rs.getLong("PAST_DAYS"))
            .setOperations(new ArrayList<>(Objects.requireNonNull(getOperation(rs.getLong("id")))));
            return period;

        } catch (Exception e) {
            throw new RuntimeException("periodMapper", e);
        }
    }

    private List<Operation> getOperation(Long idPeriod) {
        List<Operation> listOperation = database.executeSelectQuery(
                "select * from OPERATIONS where PERIOD_ID = ?;",
                this::operationMapper,
                QueryParam.longParam(idPeriod)).collect(Collectors.toList());
        if (listOperation.isEmpty()) return new ArrayList<>();
        else return listOperation;
    }

    private Operation operationMapper(ResultSet rs) {
        try {
            Operation operation = new Operation();
            operation.setId(rs.getLong("ID"));
            operation.setName(rs.getString("OPERATION_NAME"));
            operation.setSum(rs.getLong("SUM"));
            operation.setPeriodId(rs.getLong("PERIOD_ID"));
            operation.setExpType(rs.getBoolean("EXP_TYPE"));
            operation.setNpoDate(Instant.ofEpochMilli(rs.getDate("NPO_DATE").getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
            operation.setNpoType(rs.getBoolean("NPO_TYPE"));
            return operation;

        } catch (Exception e) {
            throw new RuntimeException("periodMapper", e);
        }
    }

    @Override
    public Long savePlan(Plan plansDTO) {
        try {
            Long id = database.executeUpdate("""
                            insert into ALL_PLANS (PLAN_NAME, START_PLAN, END_PLAN, START_SUM, PLAN_DAYS) values ( ?,?,?,?,? );
                            """,
                    stringParam(plansDTO.getPlaneName()),
                    dateParam(plansDTO.getStartPlane()),
                    dateParam(plansDTO.getEndPlane()),
                    longParam(plansDTO.getStartSum()),
                    longParam(plansDTO.getPlanDays())
            );
            plansDTO.getKeyPoints().forEach(localDate -> saveOrUpdatePlanKeyPoint(localDate, id));
            plansDTO.getPeriods().forEach(periodDTO -> saveOrUpdatePeriodPlan(periodDTO, id, plansDTO.preliminaryExpOnDey(), periodDTO.getPlanDays()));
            return id;
        } catch (SQLException ex) {
            ex.forEach(System.out::println);
            return null;
        }
    }


    @Override
    public void saveOrUpdatePlanKeyPoint(LocalDate localDate, Long idPlan) {
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
    public void saveOrUpdatePeriodPlan(Period period, Long idPlan, Long expOnDey, Long planDays) {
        try {
            Long idPeriod = database.executeUpdate("""
                            insert into ALL_PERIODS (
                             START_PERIOD,
                             END_PERIOD,
                             START_SUM,
                             END_SUM,
                             EXP_ON_DEY,
                             PERIOD_DAYS,
                             PLAN_DAYS,
                             PAST_DAYS,
                             PLAN_ID
                             ) VALUES (?,?,?,?,?,?,?,?,?)
                            """,
                    dateParam(period.getStartPeriod()),
                    dateParam(period.getEndPeriod()),
                    longParam(period.getStartSum()),
                    longParam(period.getEndSum()),
                    longParam(expOnDey),
                    longParam(period.getPeriodDays()),
                    longParam(planDays),
                    longParam(period.getPastDaysOnPlan()),
                    longParam(idPlan)
            );
            period.getOperations().forEach(op -> savaOrUpDateOperationPeriod(op, idPeriod));
        } catch (SQLException ex) {
            ex.forEach(System.out::println);
        }
    }

    @Override
    public void savaOrUpDateOperationPeriod(Operation operation, Long idPeriod) {
        try {
            database.executeUpdate("""
                            insert into OPERATIONS (
                            OPERATION_NAME,
                            SUM,
                            PERIOD_ID,
                            EXP_TAPE,
                            NPO_DATE,
                            NPO_TYPE
                            ) VALUES(?,?,?,?,?,?)
                            """,
                    stringParam(operation.getName()),
                    longParam(operation.getSum()),
                    longParam(idPeriod),
                    boolenParam(operation.getExpType()),
                    dateParam(operation.getNpoDate()),
                    boolenParam(operation.getNpoType())
            );
        } catch (SQLException ex) {
            ex.forEach(System.out::println);
        }
    }

    @Override
    public void deletePlan(Plan plan){
        try {
            database.executeUpdate("delete from ALL_PLANS where ID = ?; ", longParam(plan.getId()));
        } catch (SQLException ex) {
            ex.forEach(System.out::println);
        }
    }

    @Override
    public void upDatePlan(Plan plan) {
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
                    longParam(plan.getPlanDays()),
                    longParam(plan.getId())
            );
            database.executeUpdate("delete from KEY_POINTS where PLAN_ID = ?; ", longParam(idPlan));
            plan.getKeyPoints().forEach(localDate -> saveOrUpdatePlanKeyPoint(localDate, idPlan));
            database.executeUpdate("delete from ALL_PERIODS where PLAN_ID = ?; ", longParam(idPlan));
            plan.getPeriods().forEach(p -> saveOrUpdatePeriodPlan(p, idPlan, plan.preliminaryExpOnDey(), p.getPlanDays()));
        } catch (SQLException ex) {
            ex.forEach(System.out::println);
        }
    }

}

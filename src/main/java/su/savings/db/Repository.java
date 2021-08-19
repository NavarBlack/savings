package su.savings.db;

import su.savings.oae.Operation;
import su.savings.oae.Period;
import su.savings.oae.Plan;
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

public class Repository {

    private static final Database database = Database.getInstance();



    public static List<Plan> getAllPlans() {
        List<Plan> plansDTOList = database.executeSelectQuery(
                "select *, (select group_concat(KEY_POINTS.DATE_POINT) from DATA.KEY_POINTS where PLAN_ID = DATA.ALL_PLANS.ID) as KEY_POINTS from DATA.ALL_PLANS order by ID desc;",
                Plan::mapper).collect(Collectors.toList());
        if (plansDTOList.isEmpty()) return new ArrayList<>();
        else return plansDTOList;
    }


    public static List<Period> getPeriodOnPlan(Long planId) {
        List<Period> listPeriod = database.executeSelectQuery(
                "select * from ALL_PERIODS where PLAN_ID = ?;",
                Period::mapper,
                QueryParam.longParam(planId)).collect(Collectors.toList());
        if (listPeriod.isEmpty()) return new ArrayList<>();
        else return listPeriod;
    }

    public static List<Operation> getOperation(Long periodId) {
        List<Operation> listOperation = database.executeSelectQuery(
                "select * from OPERATIONS where PERIOD_ID = ?;",
                Operation::mapper,
                QueryParam.longParam(periodId)).collect(Collectors.toList());
        if (listOperation.isEmpty()) return new ArrayList<>();
        else return listOperation;
    }


    public static Long savePlan(Plan plansDTO) {
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



    public static void saveOrUpdatePlanKeyPoint(LocalDate localDate, Long idPlan) {
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


    public static void saveOrUpdatePeriodPlan(Period period, Long idPlan, Long expOnDey, Long planDays) {
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


    public static void savaOrUpDateOperationPeriod(Operation operation, Long idPeriod) {
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


    public static void deletePlan(Plan plan){
        try {
            database.executeUpdate("delete from ALL_PLANS where ID = ?; ", longParam(plan.getId()));
        } catch (SQLException ex) {
            ex.forEach(System.out::println);
        }
    }


    public static void upDatePlan(Plan plan) {
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

package su.savings.db;

import su.savings.oae.Days;
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
        try {
            List<Plan> plansDTOList = database.executeSelectQuery(
                    "select *, (select group_concat(POINTS.DATE_POINT) from DATA.POINTS where PLAN_ID = DATA.PLANS.ID) as POINTS from DATA.PLANS order by ID desc;",
                    Plan::mapper).collect(Collectors.toList());
            if (plansDTOList.isEmpty()) return new ArrayList<>();
            else return plansDTOList;
        } catch (SQLException ex) {
            ex.forEach(System.out::println);
            return new ArrayList<>();
        }

    }


    public static List<Period> getPeriodOnPlan(Long planId) {
        try {
            List<Period> listPeriod = database.executeSelectQuery(
                    "select * from PERIODS where PLAN_ID = ?;",
                    Period::mapper,
                    QueryParam.longParam(planId)).collect(Collectors.toList());
            if (listPeriod.isEmpty()) return new ArrayList<>();
            else return listPeriod;
        } catch (SQLException ex) {
            ex.forEach(System.out::println);
            return new ArrayList<>();
        }

    }

    public static List<Operation> getOperation(Long id, boolean flag) {
        try {
            String sql = """
                 select * from OPERATIONS where %s = ?
                """.formatted(flag ? "PERIOD_ID" : "DAY_ID");
            List<Operation> listOperation = database.executeSelectQuery(
                    sql,
                    Operation::mapper,
                    QueryParam.longParam(id)).collect(Collectors.toList());
            if (listOperation.isEmpty()) return new ArrayList<>();
            else return listOperation;
        } catch (SQLException ex) {
            ex.forEach(System.out::println);
            return new ArrayList<>();
        }

    }

    public static List<Days> getDays(Long id) {
        try {
            String sql = """
                 select * from DAYS where PERIOD_ID  = ?
                """;
            List<Days> daysList = database.executeSelectQuery(
                    sql,
                    Days::mapper,
                    longParam(id)
            ).collect(Collectors.toList());
            if (daysList.isEmpty()) return new ArrayList<>();
            else return daysList;
        } catch (SQLException ex) {
            ex.forEach(System.out::println);
            return new ArrayList<>();
        }

    }


    public static Long savePlan(Plan plan) {
        try {
            Long id = database.executeUpdate("""
                            insert into PLANS (PLAN_NAME, START_PLAN, END_PLAN, START_SUM, PLAN_DAYS) values ( ?,?,?,?,? );
                            """,
                    stringParam(plan.getPlaneName()),
                    dateParam(plan.getStartPlane()),
                    dateParam(plan.getEndPlane()),
                    longParam(plan.getStartSum()),
                    longParam(plan.getPlanDays())
            );
            plan.getKeyPoints().forEach(localDate -> saveOrUpdatePlanKeyPoint(localDate, id));
            plan.getPeriods().forEach(periodDTO -> saveOrUpdatePeriodPlan(periodDTO, id, plan.preliminaryExpOnDey()));
            return id;
        } catch (SQLException ex) {
            ex.forEach(System.out::println);
            return null;
        }
    }

    public static void upDatePlan(Plan plan) {
        try {
            Long idPlan = database.executeUpdate("""
                            update PLANS
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
            database.executeUpdate("delete from POINTS where PLAN_ID = ?; ", longParam(idPlan));
            plan.getKeyPoints().forEach(localDate -> saveOrUpdatePlanKeyPoint(localDate, idPlan));
            database.executeUpdate("delete from PERIODS where PLAN_ID = ?; ", longParam(idPlan));
            plan.getPeriods().forEach(p -> saveOrUpdatePeriodPlan(p, idPlan, plan.preliminaryExpOnDey()));
        } catch (SQLException ex) {
            ex.forEach(System.out::println);
        }
    }

    public static void upDatePeriod(Period period) {
        try {
            database.executeUpdate("""
                            update PERIODS
                            set START_SUM = ?,
                                END_SUM = ?,
                                EXP_ON_DEY = ?
                            where ID = ?;
                            """,
                    longParam(period.getStartSum()),
                    longParam(period.getEndSum()),
                    longParam(period.getExpOnDey()),
                    longParam(period.getId())
            );
            database.executeUpdate("delete from OPERATIONS where PERIOD_ID = ?;", longParam(period.getId()));
            period.getOperations().forEach(op -> savaOrUpDateOperationPeriod(op, period.getId(), true));
            database.executeUpdate("delete from DAYS where PERIOD_ID = ?;", longParam(period.getId()));
            period.getDays().forEach(Repository::saveOrUpdateDays);
        } catch (SQLException ex) {
            ex.forEach(System.out::println);
        }
    }

    public static void saveOrUpdatePlanKeyPoint(LocalDate localDate, Long idPlan) {
        try {
            database.executeUpdate("""
                            insert into POINTS (DATE_POINT, PLAN_ID) values (?,?)
                            """,
                    dateParam(localDate),
                    longParam(idPlan)
            );
        } catch (SQLException ex) {
            ex.forEach(System.out::println);
        }
    }


    public static void saveOrUpdatePeriodPlan(Period period, Long idPlan, Long expOnDey) {
        try {
            Long idPeriod = database.executeUpdate("""
                            insert into PERIODS (
                             START_PERIOD,
                             END_PERIOD,
                             START_SUM,
                             END_SUM,
                             EXP_ON_DEY,
                             PERIOD_DAYS,
                             PAST_DAYS,
                             PLAN_ID
                             ) VALUES (?,?,?,?,?,?,?,?)
                            """,
                    dateParam(period.getStartPeriod()),
                    dateParam(period.getEndPeriod()),
                    longParam(period.getStartSum()),
                    longParam(period.getEndSum()),
                    longParam(expOnDey),
                    longParam(period.getPeriodDays()),
                    longParam(period.getPastDaysOnPlan()),
                    longParam(idPlan)
            );
            period.getOperations().forEach(op -> savaOrUpDateOperationPeriod(op, idPeriod, true));
            period.setId(idPeriod);
            period.createDays();
            database.executeUpdate("delete from DAYS where PERIOD_ID = ?;", longParam(idPeriod));
            period.getDays().forEach(Repository::saveOrUpdateDays);
        } catch (SQLException ex) {
            ex.forEach(System.out::println);
        }
    }

    public static void saveOrUpdateDays(Days days) {
        try {
            Long idDay = database.executeUpdate("""
                            insert into DAYS (   DAY_DATE,
                                                 REMAINS_START,
                                                 REMAINS_FACT,
                                                 ACCUMULATION,
                                                 EXP_DAY,
                                                 REMAINS_END,
                                                 REMAINS_END_DEFICIT,
                                                 REMAINS_END_PROFIT,
                                                 PERIOD_ID
                                                 ) VALUES (?,?,?,?,?,?,?,?,?);
                                """,
                    dateParam(days.getDate()),
                    longParam(days.getRemainsStart()),
                    longParam(days.getRemainsFact()),
                    longParam(days.getAccumulation()),
                    longParam(days.getExpDay()),
                    longParam(days.getRemainsEnd()),
                    longParam(days.getRemainsEndDeficit()),
                    longParam(days.getRemainsEndProfit()),
                    longParam(days.getPeriodId())
            );
            days.getOperations().forEach(op -> savaOrUpDateOperationPeriod(op, idDay, false));
        } catch (SQLException ex) {
            ex.forEach(System.out::println);
        }
    }

    public static void savaOrUpDateOperationPeriod(Operation operation, Long idPeriod, boolean flag) {
        try {
            String sql = """
                     insert into OPERATIONS (
                            OPERATION_NAME,
                            SUM,
                            %s,
                            EXP_TYPE,
                            NPO_TYPE
                            ) VALUES(?,?,?,?,?)
                    """.formatted(flag ? "PERIOD_ID" : "DAY_ID");
            database.executeUpdate(
                    sql,
                    stringParam(operation.getName()),
                    longParam(operation.getSum()),
                    longParam(idPeriod),
                    boolenParam(operation.getExpType()),
//                    dateParam(operation.getNpoDate()),
                    boolenParam(operation.getNpoType())
            );
        } catch (SQLException ex) {
            ex.forEach(System.out::println);
        }
    }


    public static void deletePlan(Plan plan) {
        try {
            database.executeUpdate("delete from PLANS where ID = ?; ", longParam(plan.getId()));
        } catch (SQLException ex) {
            ex.forEach(System.out::println);
        }
    }




}

package su.savings.db;


import com.google.inject.ImplementedBy;
import su.savings.actionModels.Operation;
import su.savings.actionModels.Plan;
import su.savings.actionModels.Period;



import java.time.LocalDate;
import java.util.List;

@ImplementedBy(RepositoryImpl.class)
public interface Repository {

    List<Plan> getAllPlans();

    Long savePlan(Plan plan);

    void savePlanKeyPoint(LocalDate localDate, Long idPlan);

    void savePeriodPlan(Period period, Long idPlan, Long expOnDey);

    void savaOperationPeriod(Operation operation, Long idPeriod);

    void upDatePlan(Plan plan);

    void upDatePlanKeyPoint(LocalDate localDate, Long idPlan);

    void upDatePeriodPlan(Period period, Long idPlan, Long expOnDey);

    void upDateOperationPeriod(Operation operation, Long idPeriod);

    void deletePlan(Plan plan);
}

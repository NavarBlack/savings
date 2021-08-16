package su.savings.db;


import com.google.inject.ImplementedBy;
import su.savings.dto.actionModels.Operation;
import su.savings.dto.actionModels.Plan;
import su.savings.dto.actionModels.Period;



import java.time.LocalDate;
import java.util.List;

@ImplementedBy(RepositoryImpl.class)
public interface Repository {


    List<Plan> getAllPlans();

    Long savePlan(Plan plansDTO);

    void deletePlan(Plan plan);

    void upDatePlan(Plan plan);

    void saveOrUpdatePlanKeyPoint(LocalDate localDate, Long idPlan);

    void saveOrUpdatePeriodPlan(Period period, Long idPlan, Long expOnDey, Long planDays);

    void savaOrUpDateOperationPeriod(Operation operation, Long idPeriod);


}

package su.savings.db;


import com.google.inject.ImplementedBy;
import su.savings.dto.OperationDTO;
import su.savings.dto.PeriodDTO;
import su.savings.dto.PlansDTO;


import java.time.LocalDate;
import java.util.List;

@ImplementedBy(RepositoryImpl.class)
public interface Repository {

    List<PlansDTO> getAllPlans();

    Long savePlan(PlansDTO plansDTO);

    void savePlanKeyPoint(LocalDate localDate, Long idPlan);

    void savePeriodPlan(PeriodDTO period, Long idPlan, Long expOnDey);

    void savaOperationPeriod(OperationDTO operation, Long idPeriod);

    void upDatePlan(PlansDTO plan);

    void upDatePlanKeyPoint(LocalDate localDate, Long idPlan);

    void upDatePeriodPlan(PeriodDTO period, Long idPlan, Long expOnDey);

    void upDateOperationPeriod(OperationDTO operation, Long idPeriod);
}

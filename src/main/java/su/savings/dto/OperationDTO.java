package su.savings.dto;

public class OperationDTO {
    private Long id;
    private Long sum;
    private String name = "Операция";
    private Long periodId;
    private Boolean ExpType = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSum() {
        return sum;
    }

    public void setSum(Long sum) {
        this.sum = sum;
    }

    public Long getPeriodId() {
        return periodId;
    }

    public void setPeriodId(Long periodId) {
        this.periodId = periodId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getExpType() {
        return ExpType;
    }

    public void setExpType(Boolean expType) {
        ExpType = expType;
    }

    @Override
    public String toString() {
        return sum.toString();
    }
}

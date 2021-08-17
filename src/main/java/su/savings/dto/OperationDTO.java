package su.savings.dto;

import java.time.LocalDate;

public class OperationDTO {
    protected Long id;
    protected Long sum;
    protected String name = "Операция";
    protected Long periodId;
    protected Boolean ExpType = false;
    protected LocalDate npoDate;
    protected Boolean npoType = false;

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

    public LocalDate getNpoDate() {
        return npoDate;
    }

    public OperationDTO setNpoDate(LocalDate npoDate) {
        this.npoDate = npoDate;
        return this;
    }

    public Boolean getNpoType() {
        return npoType;
    }

    public OperationDTO setNpoType(Boolean npoType) {
        this.npoType = npoType;
        return this;
    }

    @Override
    public String toString() {
        return sum.toString();
    }
}

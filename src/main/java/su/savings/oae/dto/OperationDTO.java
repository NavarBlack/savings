package su.savings.oae.dto;

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

    public OperationDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getSum() {
        return sum;
    }

    public OperationDTO setSum(Long sum) {
        this.sum = sum;
        return this;
    }

    public String getName() {
        return name;
    }

    public OperationDTO setName(String name) {
        this.name = name;
        return this;
    }

    public Long getPeriodId() {
        return periodId;
    }

    public OperationDTO setPeriodId(Long periodId) {
        this.periodId = periodId;
        return this;
    }

    public Boolean getExpType() {
        return ExpType;
    }

    public OperationDTO setExpType(Boolean expType) {
        ExpType = expType;
        return this;
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

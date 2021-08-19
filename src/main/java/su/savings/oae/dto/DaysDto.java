package su.savings.oae.dto;

import java.time.LocalDate;
import java.util.ArrayList;

public class DaysDto {
    protected Long id;
    protected LocalDate dayDate;
    protected ArrayList<Long> exp;
    protected ArrayList<Long> inc;
    protected Long planetRemains;
    protected Long currentRemains;
    protected Long balance;
    protected Long surplus;
    protected Long deficit;

    public Long getId() {
        return id;
    }

    public DaysDto setId(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getDayDate() {
        return dayDate;
    }

    public DaysDto setDayDate(LocalDate dayDate) {
        this.dayDate = dayDate;
        return this;
    }

    public ArrayList<Long> getExp() {
        return exp;
    }

    public DaysDto setExp(ArrayList<Long> exp) {
        this.exp = exp;
        return this;
    }

    public ArrayList<Long> getInc() {
        return inc;
    }

    public DaysDto setInc(ArrayList<Long> inc) {
        this.inc = inc;
        return this;
    }

    public Long getPlanetRemains() {
        return planetRemains;
    }

    public DaysDto setPlanetRemains(Long planetRemains) {
        this.planetRemains = planetRemains;
        return this;
    }

    public Long getCurrentRemains() {
        return currentRemains;
    }

    public DaysDto setCurrentRemains(Long currentRemains) {
        this.currentRemains = currentRemains;
        return this;
    }

    public Long getBalance() {
        return balance;
    }

    public DaysDto setBalance(Long balance) {
        this.balance = balance;
        return this;
    }

    public Long getSurplus() {
        return surplus;
    }

    public DaysDto setSurplus(Long surplus) {
        this.surplus = surplus;
        return this;
    }

    public Long getDeficit() {
        return deficit;
    }

    public DaysDto setDeficit(Long deficit) {
        this.deficit = deficit;
        return this;
    }
}

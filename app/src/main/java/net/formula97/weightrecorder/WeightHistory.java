package net.formula97.weightrecorder;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by f97one on 14/10/31.
 */
@DatabaseTable(tableName = "WeightHistory")
public class WeightHistory {

    public WeightHistory() { }

    @DatabaseField(generatedId = true)
    private Integer _id;
    @DatabaseField(defaultValue = "0.00")
    private double weight;
    @DatabaseField(defaultValue = "0", unique = true)
    private long generatedAt;
    @DatabaseField(defaultValue = "")
    private String reason;


    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public long getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(long generatedAt) {
        this.generatedAt = generatedAt;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}

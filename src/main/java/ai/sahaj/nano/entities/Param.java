package ai.sahaj.nano.entities;

public class Param {
    public String fieldName;
    public String eq;
    public String neq;

    public Param(String fieldName, String eq, String neq) {
        this.fieldName = fieldName;
        this.eq = eq;
        this.neq = neq;
    }

    public Param() {
    }
}
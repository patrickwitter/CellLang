public class ExpDouble extends Exp {

    private Double val;
    protected ExpDouble(Double value) {
        super(value.toString());
        val = value;
    }

    public Double getVal() {
        return val;
        }
    
    public <S, T> T visit(Visitor<S, T> v, S arg) throws VisitException {
        return v.visitExpDouble(this, arg);
        }
    
    public String toString() {
        return Double.toString(val);
        }
    
}

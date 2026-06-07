import org.apache.hadoop.io.WritableComparable;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class CoffeeBean2 implements WritableComparable<CoffeeBean2> {
    double totalSales; //总销售额
    //空参
    public CoffeeBean2() {}
    //序列化
    @Override
    public void write(DataOutput out) throws IOException {
        out.writeDouble(totalSales);
    }
    //反序列化
    @Override
    public void readFields(DataInput in) throws IOException {
        this.totalSales = in.readDouble();
    }
    public double getTotalSales() {
        return totalSales;
    }
    public void setTotalSales(double totalSales) {
        this.totalSales = totalSales;
    }
    @Override
    public int compareTo(CoffeeBean2 o) {
        return Double.compare(o.getTotalSales(), this.getTotalSales());
    }

}

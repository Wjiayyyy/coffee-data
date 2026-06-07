import org.apache.hadoop.io.Writable;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class CoffeeBean3 implements Writable {
    private int count;  //时间段出现的次数

    public CoffeeBean3() {}

    public CoffeeBean3(int count) {
        this.count = count;
    }

    //累加次数
    public void add(int num) {
        this.count += num;
    }

    //Getter和Setter
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(count);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.count = in.readInt();
    }

    @Override
    public String toString() {
        return "出现次数=" + count;
    }
}
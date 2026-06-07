import org.apache.hadoop.io.Writable;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class CoffeeBean5 implements Writable {
    private int count;  // 销量（订单数）

    public CoffeeBean5() {}

    public CoffeeBean5(int count) {
        this.count = count;
    }

    // 累加销量
    public void add(int num) {
        this.count += num;
    }

    // Getter & Setter
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    // 序列化
    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(count);
    }

    // 反序列化
    @Override
    public void readFields(DataInput in) throws IOException {
        this.count = in.readInt();
    }

    // 格式化输出
    @Override
    public String toString() {
        return "销量= " + count;
    }
}

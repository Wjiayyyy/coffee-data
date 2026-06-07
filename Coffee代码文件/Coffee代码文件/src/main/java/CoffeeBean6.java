import org.apache.hadoop.io.Writable;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class CoffeeBean6 implements Writable {
    private int saleCount;  // 咖啡销售次数（出现次数）

    // 空构造器（必须）
    public CoffeeBean6() {}

    // 带参构造器
    public CoffeeBean6(int saleCount) {
        this.saleCount = saleCount;
    }

    // 累加销售次数
    public void add(int count) {
        this.saleCount += count;
    }

    // Getter & Setter
    public int getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(int saleCount) {
        this.saleCount = saleCount;
    }

    // 序列化（按字段顺序写入）
    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(saleCount);
    }

    // 反序列化（与序列化顺序一致）
    @Override
    public void readFields(DataInput in) throws IOException {
        this.saleCount = in.readInt();
    }

    // 输出格式化（便于查看结果）
    @Override
    public String toString() {
        return " " + saleCount;
    }
}

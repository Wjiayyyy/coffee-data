import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;

public class CoffeeMapper6 extends Mapper<LongWritable, Text, Text, CoffeeBean6> {
    private Text k = new Text();  // key: 咖啡名称（coffee_name）
    private CoffeeBean6 v = new CoffeeBean6(1);  // value: 单次计数1

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString().trim();
        if (line.isEmpty()) return;  // 跳过空行

        // 按逗号分割字段（与清洗后的数据格式一致，共11个字段）
        String[] fields = line.split(",");
        if (fields.length != 11) return;  // 过滤字段不完整的脏数据

        try {
            // 解析咖啡名称（coffee_name在第4个字段，索引3）
            String coffeeName = fields[3].trim();
            if (coffeeName.isEmpty()) return;  // 过滤空咖啡名

            // 设置输出key为咖啡名，value为计数1
            k.set(coffeeName);
            v.setSaleCount(1);  // 每笔订单计数1
            context.write(k, v);

        } catch (Exception e) {
            // 捕获解析异常，跳过脏数据
            return;
        }
    }
}
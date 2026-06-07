import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;

public class CoffeeMapper3 extends Mapper<LongWritable, Text, Text, CoffeeBean3> {
    private Text k = new Text();  // key: 时间段（Time_of_Day）
    private CoffeeBean3 v = new CoffeeBean3(1);  // value: 计数1

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString().trim();
        if (line.isEmpty()) return;

        String[] fields = line.split(",");
        if (fields.length != 11) return;  // 过滤字段不完整的数据

        try {
            // 解析时间段（Time_of_Day在第5个字段，索引4）
            String timeOfDay = fields[4].trim();  // 对应清洗后数据的Time_of_Day字段
            if (timeOfDay.isEmpty()) return;  // 过滤空时间段

            // 设置key为时间段，value为计数1
            k.set(timeOfDay);
            v.setCount(1);  // 每次出现计数1
            context.write(k, v);

        } catch (Exception e) {
            // 过滤解析失败的数据
            return;
        }
    }
}


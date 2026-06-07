import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;

public class CoffeeMapper5 extends Mapper<LongWritable, Text, Text, CoffeeBean5> {
    private Text k = new Text();  // key: 工作日/周末
    private CoffeeBean5 v = new CoffeeBean5(1);  // value: 单次计数1

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString().trim();
        if (line.isEmpty()) return;

        String[] fields = line.split(",");
        if (fields.length != 11) return;  // 过滤字段不完整数据

        try {
            // 解析Weekdaysort（第9个字段，索引8）
            int weekdaySort = Integer.parseInt(fields[8].trim());
            if (weekdaySort < 1 || weekdaySort > 7) return;  // 过滤无效星期值

            // 判断是工作日还是周末
            String dayType = getDayType(weekdaySort);
            k.set(dayType);
            v.setCount(1);  // 每笔订单计数1
            context.write(k, v);

        } catch (Exception e) {
            // 跳过解析异常数据
            return;
        }
    }

    // 判断工作日/周末
    private String getDayType(int weekdaySort) {
        if (weekdaySort >= 1 && weekdaySort <= 5) {
            return "工作日";
        } else {  // 6-7
            return "周末";
        }
    }
}

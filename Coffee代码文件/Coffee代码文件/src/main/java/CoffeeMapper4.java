import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;
import java.math.BigDecimal;

public class CoffeeMapper4 extends Mapper<LongWritable, Text, Text, CoffeeBean4> {
    private Text k = new Text();
    private CoffeeBean4 v = new CoffeeBean4(1);

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString().trim();
        if (line.isEmpty()) return;

        String[] fields = line.split(",");
        if (fields.length != 11) return;  // 过滤字段不完整数据

        try {
            // 解析金额（money在第3个字段，索引2）
            BigDecimal money = new BigDecimal(fields[2].trim());
            if (money.compareTo(BigDecimal.ZERO) <= 0) return;  // 过滤无效金额

            // 划分价格区间
            String priceRange = getPriceRange(money);
            k.set(priceRange);
            v.setCount(1);  // 每笔订单计数1
            context.write(k, v);

        } catch (Exception e) {
            // 跳过解析异常数据
            return;
        }
    }

    // 价格区间划分逻辑
    private String getPriceRange(BigDecimal money) {
        double price = money.doubleValue();
        if (price < 5) return "0-5";
        else if (price < 10) return "5-10";
        else if (price < 15) return "10-15";
        else if (price < 20) return "15-20";
        else if (price < 25) return "20-25";
        else if (price < 30) return "25-30";
        else if (price < 35) return "30-35";
        else if (price < 40) return "35-40";
        else return "40+";
    }
}

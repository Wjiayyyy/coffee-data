import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
public class CoffeeReducer1 extends Reducer<Text, Text, Text, Text> {
    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        double summoney = 0.0;        //总销售额
        Set<String> day = new HashSet<>(); //日期
        //遍历Mapper输出的所有Value（M|金额，D|日期）
        for (Text value : values) {
            String[] parts = value.toString().split("\\|", 2);
            if (parts.length != 2) {
                continue;
            }
            //对比两个数据是否相同，求和
            if ("M".equals(parts[0])) {
                summoney += Double.parseDouble(parts[1]);
            }
            //对比两个数据是否相同，并添加
            else if ("D".equals(parts[0])) {
                day.add(parts[1]);
            }
        }
        //计数，算出每月天数
        int Monthdays = day.size();
        //计算日均销售额，判断是否为零，防止为零进行运算
        double avgSales = Monthdays > 0 ? summoney / Monthdays : 0.0;

        //组合输出结果：日期只到月，总销售额，当月天数，日均销售额
        String result = String.format("%.2f\t%d\t%.2f", summoney, Monthdays, avgSales);
        context.write(key, new Text(result));
    }
}

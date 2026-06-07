import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import java.io.IOException;

public class CoffeeReducer6 extends Reducer<Text, CoffeeBean6, Text, CoffeeBean6> {
    private CoffeeBean6 result = new CoffeeBean6();  // 存储最终销量结果

    @Override
    protected void reduce(Text key, Iterable<CoffeeBean6> values, Context context) throws IOException, InterruptedException {
        int totalCount = 0;

        // 累加同一咖啡名的所有销售次数
        for (CoffeeBean6 bean : values) {
            totalCount += bean.getSaleCount();
        }

        // 封装结果并输出
        result.setSaleCount(totalCount);
        context.write(key, result);
    }
}

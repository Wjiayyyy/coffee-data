import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import java.io.IOException;

public class CoffeeReducer4 extends Reducer<Text, CoffeeBean4, Text, CoffeeBean4> {
    private CoffeeBean4 result = new CoffeeBean4();

    @Override
    protected void reduce(Text key, Iterable<CoffeeBean4> values, Context context) throws IOException, InterruptedException {
        int totalCount = 0;
        // 累加同一价格区间的销量
        for (CoffeeBean4 bean : values) {
            totalCount += bean.getCount();
        }
        // 封装结果输出
        result.setCount(totalCount);
        context.write(key, result);
    }
}
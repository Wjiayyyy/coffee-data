import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import java.io.IOException;

public class CoffeeReducer3 extends Reducer<Text, CoffeeBean3, Text, CoffeeBean3> {
    private CoffeeBean3 result = new CoffeeBean3();  // 存储最终计数结果

    @Override
    protected void reduce(Text key, Iterable<CoffeeBean3> values, Context context) throws IOException, InterruptedException {
        int totalCount = 0;

        // 累加同一时间段的所有出现次数
        for (CoffeeBean3 bean : values) {
            totalCount += bean.getCount();
        }

        // 封装结果并输出
        result.setCount(totalCount);
        context.write(key, result);
    }
}

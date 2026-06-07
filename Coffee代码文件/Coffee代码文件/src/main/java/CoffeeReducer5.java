import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import java.io.IOException;

public class CoffeeReducer5 extends Reducer<Text, CoffeeBean5, Text, CoffeeBean5> {
    private CoffeeBean5 result = new CoffeeBean5();

    @Override
    protected void reduce(Text key, Iterable<CoffeeBean5> values, Context context) throws IOException, InterruptedException {
        int totalCount = 0;
        // 累加同一类型（工作日/周末）的销量
        for (CoffeeBean5 bean : values) {
            totalCount += bean.getCount();
        }
        // 封装结果输出
        result.setCount(totalCount);
        context.write(key, result);
    }
}
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
public class CoffeeReducer2 extends Reducer<Text, DoubleWritable, Text, Text> {
    TreeMap<Double, String> treeMap = new TreeMap<>(Collections.reverseOrder());
    @Override
    protected void reduce(Text key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException {
        double total = 0;
        //累加
        for (DoubleWritable value : values) {
            total += value.get();
        }
        treeMap.put(total, key.toString());
        //处理全部时删除
        if (treeMap.size() > 5) {
            treeMap.remove(treeMap.lastKey());
        }
    }
    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        int rank = 1;
        for (Map.Entry<Double, String> entry : treeMap.entrySet()) {
            String output = String.format("第%d名，总销售额：%.2f元", rank, entry.getKey());
            context.write(new Text(entry.getValue()), new Text(output));
            rank++;
        }
    }
}

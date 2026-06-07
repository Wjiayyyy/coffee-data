import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;

public class CoffeeMapper2 extends Mapper<LongWritable, Text, Text, DoubleWritable> {
    Text coffeeName = new Text();
    DoubleWritable money = new DoubleWritable();
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        String[] words = line.split(",");
        if (words.length < 4) return;
        String name = words[3].trim();
        try {
            double sales = Double.parseDouble(words[2].trim());
            coffeeName.set(name);
            money.set(sales);
            context.write(coffeeName, money);
        } catch (NumberFormatException e) {
            return;
        }
    }
}
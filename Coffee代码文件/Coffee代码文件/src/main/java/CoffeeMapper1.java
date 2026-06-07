import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;
import java.util.regex.Pattern;

public class CoffeeMapper1 extends Mapper<LongWritable, Text, Text, Text> {
        private final Text k = new Text(); //月份
        private final Text v = new Text(); //money值
    private static final Pattern DATE = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] words = line.split(",");
            String date = words[9].trim();
            String moneySet = words[2].trim();
            //格式判断为yyyy-MM-dd
            if (!DATE.matcher(date).matches()) {
                return;
            }
            double money;
            try {
                money = Double.parseDouble(moneySet);
            } catch (NumberFormatException e) {
                return;
            }
            //截取yyyy-MM（如2024-03），作为输出Key
            String month = date.substring(0, 7);
            k.set(month); //输出Key（月份）
            //输出金额数据：M|金额，M：金额的标记方便后期对比
            v.set("M|" + money);
            context.write(k, v);
            //输出日期数据：D|日期，D：日期，用于对比和统计天数）
            v.set("D|" + date);
            context.write(k, v);
        }
    }

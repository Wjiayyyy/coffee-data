import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;

public class CoffeeCleanMapper extends Mapper<LongWritable, Text, NullWritable, Text> {
    // 存储清洗后的实体对象
    CoffeeCleanBean cleanBean = new CoffeeCleanBean();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // 1. 读取一行数据并去除首尾空格
        String line = value.toString().trim();
        if (line.isEmpty()) {
            return; // 跳过空行
        }

        // 2. 按逗号分割字段
        String[] fields = line.split(",");

        // 3. 校验字段数量（必须为11个，与数据结构匹配）
        if (fields.length != 11) {
            return;
        }

        try {
            // 4. 解析各字段并进行类型转换
            int hourOfDay = Integer.parseInt(fields[0]);
            String cashType = fields[1];
            BigDecimal money = new BigDecimal(fields[2]);
            String coffeeName = fields[3];
            String timeOfDay = fields[4];
            String weekday = fields[5];
            String monthName = fields[6];
            int weekdaySort = Integer.parseInt(fields[7]);
            int monthSort = Integer.parseInt(fields[8]);
            Date date = Date.valueOf(fields[9]); // 解析日期格式（需符合yyyy-MM-dd）
            String time = fields[10];

            // 5. 数据清洗
            // 小时必须在0-24之间
            if (hourOfDay < 0 || hourOfDay > 24) {
                return;
            }
            // 金额必须大于0
            if (money.compareTo(BigDecimal.ZERO) <= 0) {
                return;
            }
            // 星期排序必须在0-7之间
            if (weekdaySort < 0 || weekdaySort > 7) {
                return;
            }
            // 月份排序必须在1-12之间
            if (monthSort < 1 || monthSort > 12) {
                return;
            }

            // 6. 封装清洗后的数据到Bean对象
            cleanBean.setHour_of_day(hourOfDay);
            cleanBean.setCash_type(cashType);
            cleanBean.setMoney(money);
            cleanBean.setCoffee_name(coffeeName);
            cleanBean.setTime_of_Day(timeOfDay);
            cleanBean.setWeekday(weekday);
            cleanBean.setMonth_name(monthName);
            cleanBean.setWeekdaysort(weekdaySort);
            cleanBean.setMonthsort(monthSort);
            cleanBean.setDate(date);
            cleanBean.setTime(time);

            // 7. 输出清洗后的结果（key为NullWritable表示无分类，value为清洗后的数据字符串）
            context.write(NullWritable.get(), new Text(cleanBean.toString()));

        } catch (Exception e) {
            // 捕获类型转换异常、日期格式错误等，直接跳过脏数据
            return;
        }
    }
}
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import java.io.IOException;

public class CoffeeDriver4 {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://yue:9000");

        Job job = Job.getInstance(conf, "价格区间销量统计");
        job.setJarByClass(CoffeeDriver4.class);

        // 设置Mapper和Reducer
        job.setMapperClass(CoffeeMapper4.class);
        job.setReducerClass(CoffeeReducer4.class);

        // 设置输出类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(CoffeeBean4.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(CoffeeBean4.class);

        // 输入路径：清洗后的数据
        Path inputPath = new Path("/root/mapreduce/output/part-r-00000");
        // 输出路径：价格区间统计结果
        Path outputPath = new Path("/root/mapreduce/output4");

        // 删除已存在的输出路径
        FileSystem fs = FileSystem.get(conf);
        if (fs.exists(outputPath)) {
            fs.delete(outputPath, true);
        }

        FileInputFormat.setInputPaths(job, inputPath);
        FileOutputFormat.setOutputPath(job, outputPath);

        // 提交作业
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
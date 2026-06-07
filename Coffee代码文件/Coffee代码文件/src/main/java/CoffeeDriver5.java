import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class CoffeeDriver5 {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://yue:9000");
        Job job = Job.getInstance(conf, "工作日周末销量统计");
        job.setJarByClass(CoffeeDriver5.class);
        // 设置Mapper和Reducer
        job.setMapperClass(CoffeeMapper5.class);
        job.setReducerClass(CoffeeReducer5.class);
        // 设置输出类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(CoffeeBean5.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(CoffeeBean5.class);
        // 输入路径：清洗后的数据
        Path inputPath = new Path("/root/mapreduce/output/part-r-00000");
        // 输出路径：工作日周末统计结果
        Path outputPath = new Path("/root/mapreduce/output5");
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

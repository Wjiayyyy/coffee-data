import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import java.io.IOException;

public class CoffeeCleanDriver {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://yue:9000");

        // 创建Job实例
        Job job = Job.getInstance(conf, "CoffeeClean");
        job.setJarByClass(CoffeeCleanDriver.class);

        // 只设置Mapper类
        job.setMapperClass(CoffeeCleanMapper.class);

        // 设置输出数据类型（Mapper直接输出清洗结果，key用NullWritable表示无意义）
        job.setMapOutputKeyClass(NullWritable.class);
        job.setMapOutputValueClass(Text.class);
        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(Text.class);

        // 设置输入输出路径
        Path inputPath = new Path("/root/mapreduce/data/Coffe_sales_no_header.csv");
        Path outputPath = new Path("/root/mapreduce/output");

        // 若输出路径已存在则删除
        FileSystem fs = FileSystem.get(conf);
        if (fs.exists(outputPath)) {
            fs.delete(outputPath, true);
        }

        FileInputFormat.setInputPaths(job, inputPath);
        FileOutputFormat.setOutputPath(job, outputPath);

        // 提交作业
        boolean success = job.waitForCompletion(true);
        System.exit(success ? 0 : 1);
    }
}
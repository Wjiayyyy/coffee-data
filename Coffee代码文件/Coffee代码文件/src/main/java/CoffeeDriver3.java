import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import java.io.IOException;

public class CoffeeDriver3 {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://yue:9000");

        Job job = Job.getInstance(conf, "时间段高峰统计");
        job.setJarByClass(CoffeeDriver3.class);

        // 设置Mapper和Reducer
        job.setMapperClass(CoffeeMapper3.class);
        job.setReducerClass(CoffeeReducer3.class);

        // 设置输出类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(CoffeeBean3.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(CoffeeBean3.class);

        Path inputPath = new Path("/root/mapreduce/output/part-r-00000");
        Path outputPath = new Path("/root/mapreduce/output3");

        FileSystem fs = FileSystem.get(conf);
        if (fs.exists(outputPath)) {
            fs.delete(outputPath, true);
        }
        FileInputFormat.setInputPaths(job, inputPath);
        FileOutputFormat.setOutputPath(job, outputPath);
        boolean b = job.waitForCompletion(true);
        System.exit(b?0:1);
    }
}



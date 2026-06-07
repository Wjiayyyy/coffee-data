import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import java.io.IOException;

public class CoffeeDriver6 {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://yue:9000");
        Job job = Job.getInstance(conf, "咖啡销量统计");
        job.setJarByClass(CoffeeDriver6.class);
        job.setMapperClass(CoffeeMapper6.class);
        job.setReducerClass(CoffeeReducer6.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(CoffeeBean6.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(CoffeeBean6.class);
        Path inputPath = new Path("/root/mapreduce/output/part-r-00000");
        Path outputPath = new Path("/root/mapreduce/output6");
        FileSystem fs = FileSystem.get(conf);
        if (fs.exists(outputPath)) {
            fs.delete(outputPath, true);
        }

        FileInputFormat.setInputPaths(job, inputPath);
        FileOutputFormat.setOutputPath(job, outputPath);
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}

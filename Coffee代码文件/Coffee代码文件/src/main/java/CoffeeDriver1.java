import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class CoffeeDriver1 {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS","hdfs://yue:9000");
        Job job = Job.getInstance(conf);
        job.setJarByClass(CoffeeDriver1.class);
        job.setMapperClass(CoffeeMapper1.class);
        job.setReducerClass(CoffeeReducer1.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        Path input = new Path("/root/mapreduce/output/part-r-00000");
        Path output = new Path("/root/mapreduce/output1");
        FileSystem fileSystem = FileSystem.get(conf);
        if(fileSystem.exists(output))
            fileSystem.delete(output,true);
        FileInputFormat.addInputPath(job, input);
        FileOutputFormat.setOutputPath(job, output);
        boolean b = job.waitForCompletion(true);
        System.exit(b?0:1);
    }
}

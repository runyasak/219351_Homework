import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class TwoLinksNodeCount {

	public static class NodeMapper extends Mapper<Object, Text, Text, Text> {

		private Text mapKey = new Text();
		private Text mapValue = new Text();

		@Override
		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
			StringTokenizer itr = new StringTokenizer(value.toString());
			String from = itr.nextToken();
			String to = itr.nextToken();
			mapKey.set(from);
			mapValue.set(to);
			context.write(mapKey , mapValue);
			mapKey.set(to);
			mapValue.set("F"+from);
			context.write(mapKey , mapValue);
		}
	}

	public static class NodeCombiner extends Reducer<Text, Text, Text, Text> {

		private Text combKey = new Text();
		private Text combValue = new Text();

		@Override
		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

			ArrayList<String> list = new ArrayList<String>();
			for (Text value : values) {
				list.add(value.toString());
			}

			for (int i=0; i<list.size(); i++) {
				combKey.set(key);
				combValue = new Text(list.get(i));
				if(combValue.toString().charAt(0) != 'F') {
					context.write(combKey, combValue);
				}
				else {
					String valString = combValue.toString();
					combKey.set(valString.substring(1, valString.length()));
					for(int j=0; j<list.size(); j++) {
						combValue = new Text(list.get(j));
						if(combValue.toString().charAt(0) != 'F') {
							context.write(combKey, combValue);
						}
					}
				}
			}
		}
	}

	public static class NodeReducer extends Reducer<Text, Text, Text, Text> {

		private Text result = new Text();

		@Override
		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

			ArrayList<String> list = new ArrayList<String>();

			for (Text val : values) {
				String valString = val.toString();
				if(!list.contains(valString)) {
					list.add(valString);
				}
			}
			String resultString = list.get(0);
			for(int i=1; i<list.size();i++) {
				resultString += ", "+list.get(i);
			}
			result.set(resultString);
			if(key.toString().equals("619302")) {
				System.out.print("Fuck you bitches! ");
				System.out.println(resultString);
			}
			context.write(key, result);
		}
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "two-links node count");
		System.out.println("TwoLinksNode count");
		job.setJarByClass(TwoLinksNodeCount.class);
		job.setMapperClass(NodeMapper.class);
		job.setCombinerClass(NodeCombiner.class);
		job.setReducerClass(NodeReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		System.out.println("Max split " + FileInputFormat.getMaxSplitSize(job));
		System.out.println("Min split " + FileInputFormat.getMinSplitSize(job));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}

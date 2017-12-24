package run;

import analysis.graph.Graph;
import analysis.metrics.Metrics;
import utils.Structure;

/**
 * 主程序
 * 
 * @author naplues
 *
 */
public class Main {
	public static void main(String[] args) {
		// 构造结构字符串
		String structure = Structure.generateStructure("res/Test2.java");
		// 打印结构
		System.out.println(structure);
		// 打印结构标签
		//Structure.printLabels();
		// 构造控制流图
		Graph graph = new Graph(structure);
		// 输出控制流图
		graph.outputGraph(false);
		//输出所有度量值
		Metrics.outputAllMetrics(graph, structure);

	}

}
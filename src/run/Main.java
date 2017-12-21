package run;
import analysis.graph.Graph;
import analysis.metrics.Metrics;
import utils.Structure;

/**
 * 主程序
 * @author naplues
 *
 */
public class Main {
	public static void main(String[] args) {
		// 通过结构字符串构造控制流图
		String structure = Structure.generateStructure("res/Test2.java");
		Graph graph = new Graph(structure);
		// 输出控制流图
		graph.outputGraph();
		System.out.println(structure);

		int cnum = Metrics.getCyclomaticNumber(graph); // 圈复杂度
		int depth = Metrics.getDepthOfNest(structure); // 嵌套深度
		int num = Metrics.getNumberOfStatements(structure); // 语句数目

		System.out.println();
		System.out.println("圈复杂度(路径)：  " + cnum);
		System.out.println("最大嵌套深度：        " + depth);
		System.out.println("语句数目：                " + num);

	}

}
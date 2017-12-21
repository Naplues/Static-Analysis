/**
 * 主程序
 * @author naplues
 *
 */
public class Main {
	public static void main(String[] args) {
		// 通过结构字符串构造控制流图
		String structure = Parse.generateStructure(FileHandle.readFileToLines("res/Test.java"));
		Graph graph = new Graph(structure);

		// 输出控制流图
		String res = graph.outputGraph();
		System.out.println(res);
		FileHandle.writeStringToFile("C:\\Users\\naplues\\Desktop\\a.dot", res);
		FileHandle.writeStringToFile("out/test.dot", res);
		Graph.drawGraph("");
		System.out.println(structure);

		int cnum = Metrics.getCyclomaticNumber(structure); // 圈复杂度
		int depth = Metrics.getDepthOfNest(structure); // 嵌套深度
		int num = Metrics.getNumberOfStatements(structure); // 语句数目

		System.out.println(Metrics.getCyclomaticNumber(graph));
		System.out.println("圈复杂度(路径)：  " + cnum);
		System.out.println("最大嵌套深度：        " + depth);
		System.out.println("语句数目：                " + num);

	}

}
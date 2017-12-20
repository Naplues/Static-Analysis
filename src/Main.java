import java.util.List;

public class Main {
	public static void main(String[] args) {
		Graph graph = new Graph();
		//1.手动设计控制流图
		/*
		 int[][] data = { { 1, 2 }, { 1, 3 }, { 2, 3 }, { 2, 4 }, { 3, 5 }, { 3, 6 },
		 { 5, 7 }, { 6, 7 }, { 4, 7 } };
		 graph.createGraph(data);
		 */
		
		//3.通过结构字符串构造控制流图
		
		List<String> lines = FileHandle.readFileToLines("res/Test.java");
		String structure = Parse.generateStructure(lines);
		System.out.println(structure);
		graph.constructGraph(structure);

		//输出控制流图
		String res = graph.outputGraph();
		System.out.println(res);
		FileHandle.writeStringToFile("C:\\Users\\naplues\\Desktop\\a.dot", res);
		FileHandle.writeStringToFile("out/test.dot", res);
		Graph.drawGraph("");
		System.out.println(structure);
		
		int cnum = Metrics.getCyclomaticNumber(structure);    //圈复杂度
		int depth =  Metrics.getDepthOfNest(structure);       //嵌套深度
		int num = Metrics.getNumberOfStatements(structure);   //语句数目
		
		System.out.println(Metrics.getCyclomaticNumber(graph));
		System.out.println("圈复杂度：         " + cnum);
		System.out.println("最大嵌套深度： " + depth);
		System.out.println("语句数目：         " + num);
		
		
		
	}

}
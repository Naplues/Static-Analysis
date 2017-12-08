import java.util.List;

//dot -Tsvg test.dot -o test.svg

public class Main {
	public static void main(String[] args) {
		List<String> lines = FileHandle.readFileToLines("res/Test2.java");
		String structure = Parse.generateStructure(lines);
		Graph graph = new Graph();
		// int[][] data = { { 1, 2 }, { 1, 3 }, { 2, 3 }, { 2, 4 }, { 3, 5 }, { 3, 6 },
		// { 5, 7 }, { 6, 7 }, { 4, 7 } };
		// graph.createGraph(data);

		
		// 构造控制流图
		graph.constructGraph(structure);
		String res = graph.outputGraph();
		System.out.println(res);
		FileHandle.writeStringToFile("C:\\Users\\naplues\\Desktop\\a.dot", res);

		System.out.println(structure);
	}

}
package analysis.metrics;
import java.util.List;
import java.util.Stack;

import analysis.graph.Graph;
import utils.Structure;

public class Metrics {

	/**
	 * 最大嵌套层数
	 * 
	 * @param structure
	 * @return
	 */
	public static int getDepthOfNest(String structure) {
		int depth = 0;
		char ch = '0';
		Stack<Character> stack = new Stack<>();
		for (int i = 0; i < structure.length(); i++) {
			ch = structure.charAt(i);
			if (ch == '(') {
				stack.push(ch);
				if (depth < stack.size()) {
					depth++;
				}
			} else if (ch == ')') {
				stack.pop();
			}
		}
		return depth;
	}

	/**
	 * 方法语句数目
	 * 
	 * @param structure
	 * @return
	 */
	public static int getNumberOfStatements(String structure) {
		int number = Structure.getProcedureNumber(structure); // 获取纯过程语句数目
		if (number != -1)
			return number;
		// 非过程实体
		if (Structure.isNest(structure)) {
			// 嵌套结构剥开嵌套层
			return 1 + getNumberOfStatements(Structure.ReduceNest(structure));
		} else {
			// 顺序结构
			List<String> subStructure = Structure.getSubStructure(structure);
			int sum = 0;
			for (int i = 0; i < subStructure.size(); i++)
				sum += getNumberOfStatements(subStructure.get(i));
			return sum;
		}
	}

	/**
	 * 圈复杂度 谓词结点数目+1
	 * 
	 * @param structure
	 * @return
	 */
	public static int getCyclomaticNumber(String structure) {
		int number = 1;
		for (int i = 0; i < structure.length() - 1; i++) {
			if (structure.charAt(i) == 'D' || structure.charAt(i) == 'C')
				number++;
		}
		return number;
	}

	/**
	 * 圈复杂度 e - n + 2
	 * 
	 * @param graph
	 * @return
	 */
	public static int getCyclomaticNumber(Graph graph) {
		int n = graph.getNodeNumber();
		int e = graph.getArcNumber();
		return e - n + 2;

	}

	/**
	 * 本质复杂度,未实现
	 * 
	 * @return
	 */
	public static int getEssentialComplexity(Graph graph) {
		getCyclomaticNumber(graph);
		return 0;
	}	
}

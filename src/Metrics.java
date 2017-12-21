import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

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
		System.out.println(depth);
		return depth;
	}

	/**
	 * 方法语句数目
	 * 
	 * @param structure
	 * @return
	 */
	public static int getNumberOfStatements(String structure) {
		int number = getProcedureNumber(structure); // 获取纯过程语句数目
		if (number != -1)
			return number;
		// 非过程实体
		if (isNest(structure)) {
			// 嵌套结构剥开嵌套层
			return 1 + getNumberOfStatements(ReduceNest(structure));
		} else {
			// 顺序结构
			List<String> subStructure = getSequences(structure);
			int sum = 0;
			for (int i = 0; i < subStructure.size(); i++)
				sum += getNumberOfStatements(subStructure.get(i));
			return sum;
		}
	}

	/**
	 * 圈复杂度   谓词结点数目+1
	 * 
	 * @param structure
	 * @return
	 */
	public static int getCyclomaticNumber(String structure) {
		int number = 1;
		for (int i = 0; i < structure.length() - 1; i++) {
			if (structure.charAt(i) == 'D')
				number++;
		}
		return number;
	}
	/**
	 * 圈复杂度   e - n + 2
	 * @param graph
	 * @return
	 */
	public static int getCyclomaticNumber(Graph graph) {
		int n = graph.getNodeNumber();
		int e = graph.getArcNumber();
		System.out.println(n + " " + e);
		return e - n + 2;
		
	}
	/**
	 * 本质复杂度,未实现
	 * @return
	 */
	public static int getEssentialComplexity(Graph graph) {
		getCyclomaticNumber(graph);
		return 0;
	}

	/********************************************************
	 ************************辅助方法***************************
	 ********************************************************/

	/**
	 * 判断字符串外层是否嵌套
	 * 
	 * @param structure
	 * @return
	 */
	public static boolean isNest(String structure) {
		char ch = '0';
		Stack<Character> stack = new Stack<>();
		for (int i = 0; i < structure.length(); i++) {
			ch = structure.charAt(i);
			if (ch == '(') {
				stack.push(ch);
			}
			if (ch == ')') {
				stack.pop();
				if (stack.isEmpty()) {
					if (structure.length() - 1 == i)
						return true;
					else
						return false;
				}
			}
		}
		return false;
	}

	/**
	 * 获取过程结点数目
	 * 
	 * @param structure
	 * @return
	 */
	public static int getProcedureNumber(String structure) {
		int number = 0;
		for (int i = 0; i < structure.length(); i++) {
			if (structure.charAt(i) != 'P')
				return -1;
			number++;
		}
		return number;
	}

	/**
	 * 去掉外层嵌套
	 * 
	 * @param structure
	 * @return
	 */
	public static String ReduceNest(String structure) {
		return structure.substring(3, structure.length() - 1);
	}

	/**
	 * 获取顺序实体
	 * 
	 * @param structure
	 * @return
	 */
	public static List<String> getSequences(String structure) {
		List<String> list = new ArrayList<>();
		char ch = '0';
		String temp = "";
		Stack<Character> stack = new Stack<>();
		for (int i = 0, start = 0; i < structure.length(); i++) {
			ch = structure.charAt(i);
			if (ch == '|') {
				start = i + 1;
				continue;
			}
			// 添加本层过程结点
			if (stack.isEmpty()) {
				if (ch == 'P') {
					temp += ch;
					start = i + 1;
				}
			}
			if (ch == '(') {
				if (!temp.equals("")) {
					list.add(temp);
					temp = "";
				}
				stack.push(ch);
			}
			if (ch == ')') {
				stack.pop();
				// 获得一个顺序实体
				if (stack.isEmpty()) {
					list.add(structure.substring(start, i + 1));
					start = i + 1;
				}
			}
			// 结尾的过程结点
			if (i == structure.length() - 1 && !temp.equals("")) {
				list.add(temp);
				temp = "";
			}
		}
		return list;
	}

	/**
	 * 打印顺序实体
	 * 
	 * @param list
	 */
	public static void print(List<String> list) {
		for (String s : list) {
			System.out.println(s);
		}
	}
}

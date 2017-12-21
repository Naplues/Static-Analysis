package utils;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
/**
 * 结构字符串工具类
 * @author naplues
 *
 */
public class Structure {

	/**
	 * 构造结构字符串
	 * 
	 * @param lines
	 * @return
	 */
	public static String generateStructure(String filePath) {
		List<String> lines = FileHandle.readFileToLines(filePath);
		String structure = ""; // 开始结点
		Stack<String> type = new Stack<>();
		for (String line : lines) {
			if (line.trim().startsWith("public") || line.trim().equals(""))
				continue;
			// 进入if谓词
			if (line.trim().startsWith("if")) {
				type.push("if"); // 谓词压栈
				structure += "D0(";
				continue;
			}
			// 进入else分支
			if (line.trim().startsWith("else")) {
				type.push("else");
				structure = structure.substring(0, structure.length() - 1);
				structure += "|";
				continue;
			}
			// 进入while和for谓词
			if (line.trim().startsWith("while") || line.trim().startsWith("for")) {
				type.push("while"); // 谓词压栈
				structure += "D2(";
				continue;
			}
			// 进入repeat-until循环do
			if (line.trim().startsWith("do")) {
				type.push("do");
				structure += "D3(";
				continue;
			}
			// 进入switch 开关语句
			if (line.trim().startsWith("switch")) {
				type.push("switch");
				structure += "CA(";
				continue;
			}
			// 开关语句的case或default
			if (line.trim().startsWith("case") || line.trim().startsWith("default")) {
				if (!(structure.charAt(structure.length() - 1) == '('))
					structure += ","; // 分支
				continue;
			}
			// try catch 模块
			if (line.trim().startsWith("try")) {
				type.push("try");
				structure += "T";
				continue;
			}
			if (line.trim().startsWith("catch") || line.trim().startsWith("}catch")) {
				type.push("catch");
				structure += "H";
				continue;
			}
			// break语句
			if (line.trim().startsWith("break")) {
				structure += "b";
				continue;
			}
			// continue语句
			if (line.trim().startsWith("continue")) {
				structure += "c";
				continue;
			}
			// 分隔符
			if (line.trim().equals("{")) {
				continue;
			}
			if (line.trim().startsWith("}")) {
				if (type.size() != 0) {
					type.pop(); // 谓词出栈
					structure += ")";
				}
				continue;
			}
			// 异常模块
			if (line.trim().startsWith("throw")) {
				structure += "t"; // 抛出异常
				continue;
			}
			// return模块
			if (line.trim().startsWith("return")) {
				structure += "r";
				continue;
			}
			// simple语句
			if (!line.trim().startsWith("if") && !line.trim().startsWith("while")) {
				type.push("simple");
				structure += "P";
				type.pop();
				continue;
			}
		}
		return structure;
	}

	/**
	 * 化简字符串,待完成
	 * 
	 * @param origin
	 * @return
	 */
	public static String reduceStructure(String origin) {
		String structure = "";

		return structure;
	}

	public static String modify(String origin) {
		String structure = "";
		for (int i = 0; i < origin.length() - 1; i++) {
			if (origin.charAt(i) == 'D' && origin.charAt(i + 1) == '0') {

			} else {
				structure += origin.charAt(i);
			}
		}
		return structure;
	}

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
	public static List<String> getSubStructure(String structure) {
		List<String> list = new ArrayList<>();
		char ch = '0';
		String temp = "";
		Stack<Character> stack = new Stack<>();
		for (int i = 0, start = 0; i < structure.length(); i++) {
			ch = structure.charAt(i);

			// 栈为空代表在奔曾,添加本层过程结点
			if (stack.isEmpty()) {
				if (ch == 'P') {
					temp += ch;
					start = i + 1;
				}

				if (ch == '|') {
					start = i + 1;
					continue;
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
	 * 打印顺序实体
	 * 
	 * @param list
	 */
	public static void printStructure(List<String> list) {
		for (String s : list) {
			System.out.println(s);
		}
	}
}

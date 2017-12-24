package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 结构字符串工具类
 * 
 * @author naplues
 *
 */
public class Structure {
	public static List<String> labels = null; // 结构标签

	/**
	 * 构造结构字符串
	 * 
	 * @param lines
	 * @return
	 */
	public static String generateStructure(String filePath) {
		List<String> lines = FileHandle.readFileToLines(filePath);
		modifyLines(lines);
		labels = new ArrayList<>(); // 结构标签
		String structure = ""; // 开始结点
		Stack<String> type = new Stack<>();
		for (String line : lines) {
			if (line.trim().startsWith("public") || line.trim().equals(""))
				continue;
			// 进入if分支
			if (line.trim().startsWith("if")) {
				type.push("if"); // 谓词压栈
				structure += "D0(";
				labels.add(line);
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
				labels.add(line);
				continue;
			}
			// 进入repeat-until循环do
			if (line.trim().startsWith("do")) {
				type.push("do");
				structure += "D3(";
				labels.add(line);
				continue;
			}
			// 离开do-while循环
			if (line.trim().startsWith("}while")) {
				labels.add(line);
			}
			// 进入switch 开关语句
			if (line.trim().startsWith("switch")) {
				type.push("switch");
				structure += "CA(";
				labels.add(line);
				continue;
			}
			// 开关语句的case或default
			if (line.trim().startsWith("case") || line.trim().startsWith("default")) {
				if (!(structure.charAt(structure.length() - 1) == '('))
					structure += ","; // 分支
				labels.add(line);
				continue;
			}
			// try catch 模块
			if (line.trim().startsWith("try")) {
				type.push("try");
				// structure += "T";
				continue;
			}
			if (line.trim().startsWith("catch") || line.trim().startsWith("}catch")) {
				type.push("catch");
				// structure += "H";
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
			// 忽略分隔符
			if (line.trim().equals("{")) {
				continue;
			}
			if (line.trim().startsWith("}")) {

				if (type.size() != 0) {
					String t = type.pop(); // 谓词出栈
					if (t.equals("try")) {
						continue;
					}
					if (t.equals("catch")) {
						continue;
					}
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
			if (line.equals("return ")) {
				structure += "r";
				labels.add(line);
				continue;
			}
			// simple语句
			if (!line.trim().startsWith("if") && !line.trim().startsWith("while")) {
				if ( !type.isEmpty() && type.peek().equals("catch")) {
					continue;
				}
				type.push("simple");
				structure += "P";
				type.pop();
				labels.add(line);
				continue;
			}
		}
		modifyLabels();// 修复标签
		structure = modifyStructure(structure);
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

	public static String modifyStructure(String s) {
		StringBuffer structure = new StringBuffer(s);
		Stack<Character> stack = new Stack<>();
		for (int i = 0; i < structure.length(); i++) {
			if (structure.charAt(i) == '|') {
				System.out.println(i);
				for (int j = i - 1; j >= 0; j--) {
					if (structure.charAt(j) == ')') {
						stack.push(')');
						continue;
					}
					if (structure.charAt(j) == '(') {
						if (!stack.isEmpty()) {
							stack.pop();
							continue;
						} else {
							// j-1处0改为1
							structure.setCharAt(j - 1, '1');
							break;
						}

					}
				}
			}
		}
		return structure.toString();
	}

	/**
	 * 判断字符串外层是否嵌套
	 * 
	 * @param structure
	 * @return
	 */
	public static boolean isNest(String structure) {
		if (structure.length() == 0)
			return false;
		char ch = '0';
		if (structure.charAt(0) != 'D' && structure.charAt(0) != 'C')
			return false;
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
				if (ch == ',') {
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
			if (structure.charAt(i) != 'P' )
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

	/**
	 * 修复标签
	 */
	public static void modifyLabels() {
		for (int i = 0; i < labels.size(); i++) {
			String l = labels.get(i).trim();
			if (l.startsWith("}"))
				labels.set(i, l.substring(1, l.length()));
			if (l.endsWith("{"))
				labels.set(i, l.substring(0, l.length() - 1));

		}
		for (int i = 0; i < labels.size(); i++) {
			String l = labels.get(i).trim();
			labels.set(i, l);
		}
	}

	/**
	 * 修复输入文本
	 * 
	 * @param lines
	 */
	public static void modifyLines(List<String> lines) {
		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			line = line.replace("\\", "\\\\");
			line = line.replace("'", "\'");
			line = line.replace("\"", "\\\"");

			lines.set(i, line);
		}
	}
	
	/**
	 * c,b 转化为P
	 * @param string
	 */
	public static String toSimple(String string) {
		StringBuilder builder = new StringBuilder(string);
		for(int i=0;i<builder.length();i++) {
			if(builder.charAt(i)=='c'||builder.charAt(i)=='b')
				builder.setCharAt(i, 'P');
		}
		return builder.toString();
	}
	/**
	 * 打印结构标签
	 */
	public static void printLabels() {
		for (String s : labels) {
			System.out.println(s);
		}
	}
}

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Parse {

	/**
	 * 构造结构字符串
	 * 
	 * @param lines
	 * @return
	 */
	public static String generateStructure(List<String> lines) {
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
	 * 获取结构信息
	 * 生成部分字串
	 * 组合成完整字串
	 * @param origin
	 * @return
	 */
	public static List<String> getSubStructure(String origin) {
		List<String> structure = new ArrayList<>();
		Stack<String> stack = new Stack<>();

		for (int i = 0; i < origin.length(); i++) {
			if (origin.charAt(i) == 'S') {
				i++;
				continue;
			}
			if (origin.charAt(i) == 'D') {
				if (origin.charAt(i + 1) == '0') {
					stack.push("D0");
					continue;
				}
				if (origin.charAt(i + 1) == '2') {
					stack.push("D2");
					continue;
				}
			}
			if (origin.charAt(i) == '(') {
				stack.push("(");
				continue;
			}
			if (origin.charAt(i) == 'P') {
				stack.push("P");
				continue;
			}
		}

		return structure;
	}
}

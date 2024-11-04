package com.hx.nine.eleven.web.tokenizer;


import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

/**
 * @auth wml
 * @date 2024/10/29
 */
public class IkAnalyzerDemo {

	public static void main(String[] args) {
		String text = "中国农业银行股份有限公司重庆市江北区支行";
		try{
			Set<String> keywords = extractKeywords(text);
			System.out.println("关键字：" + keywords);
		}catch (Exception e){

		}
	}

	/**
	 * ik-analyzer
	 * description 提取关键字
	 * @author yanzy
	 * @version 1.0
	 * @date 2024/5/7 17:12
	 */
	private static Set<String> extractKeywords(String text) throws Exception {
		Set<String> keywords = new HashSet<>();
		StringReader reader = new StringReader(text);
		IKSegmenter segmenter = new IKSegmenter(reader, true); // true表示使用智能模式
		Lexeme lexeme;
		while ((lexeme = segmenter.next()) != null) {
			String word = lexeme.getLexemeText(); // 获取分词结果
			// 根据词性和长度筛选关键字
			if (isValidKeyword(word, lexeme.getLexemeType())) {
				keywords.add(word);
			}
		}
		return keywords;
	}

	/**
	 * description 根据自定义条件判断是否为有效关键字
	 *
	 * @author yanzy
	 * @version 1.0
	 * @date 2024/5/7 17:12
	 */
	private static boolean isValidKeyword(String word, int lexemeType) {
		// 这里可以根据你的需求调整筛选规则
		// 一般来说，名词、专有名词、形容词等类型的词可以作为关键词
		// 根据词长和词性来筛选关键词
		// 例如：提取名词（类型4）、专有名词（类型10）以及形容词（类型2），长度大于1的词
		return (lexemeType == Lexeme.TYPE_CNWORD || lexemeType == Lexeme.TYPE_ARABIC) && word.length() > 1;
	}
}

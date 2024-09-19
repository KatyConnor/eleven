package com.hx.nine.eleven.commons.utils;

import java.text.DecimalFormat;

/**
 * 常见的金额处理类
 * 
 * @author 徐向红
 * 
 */
public class MoneyUtil {
	protected static String HanDigiStr[] = new String[] { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };

	protected static String HanDiviStr[] = new String[] { "", "拾", "佰", "仟",
			"万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿",
			"拾", "佰", "仟", "万", "拾", "佰", "仟" };
	
	// 千分位格式，最多带6位小数，四舍五入
	protected static DecimalFormat dfTT1 = new DecimalFormat("###,###,###,###,###,###,###,###.######");
	
	// 千分位格式，不带小数
	protected static DecimalFormat dfTT2 = new DecimalFormat("###,###,###,###,###,###,###,###");
	
	//四舍五入，保留两位小数，不格式化千分位
	protected static DecimalFormat dfTT3 = new DecimalFormat(".##");
	//千分位格式，最多带2位小数，四舍五入
	protected static DecimalFormat dfTT4 = new DecimalFormat("###,###,###,###,###,###,###,##0.00");

	protected static String positiveIntegerToHanStr(String NumStr) {
		// 输入字符串必须正整数，只允许前导空格(必须右对齐)，不宜有前导零
		String RMBStr = "";
		boolean lastzero = false;
		boolean hasvalue = false; // 亿、万进位前有数值标记
		int len, n;
		len = NumStr.length();
		if (len > 15)
			return "数值过大!";

		for (int i = len - 1; i >= 0; i--) {
			if (NumStr.charAt(len - i - 1) == ' ')
				continue;
			n = NumStr.charAt(len - i - 1) - '0';
			if (n < 0 || n > 9)
				return "输入含非数字字符!";

			if (n != 0) {
				if (lastzero)
					RMBStr += HanDigiStr[0]; // 若干零后若跟非零值，只显示一个零
				//if (!(n == 1 && (i % 4) == 1 && i == len - 1)) { // 十进位处于第一位不发壹音
					RMBStr += HanDigiStr[n];
				//}
				RMBStr += HanDiviStr[i]; // 非零值后加进位，个位为空
				hasvalue = true; // 置万进位前有值标记

			} else {
				if ((i % 8) == 0 || ((i % 8) == 4 && hasvalue)) // 亿万之间必须有非零值方显示万
					RMBStr += HanDiviStr[i]; // “亿”或“万”
			}

			if (i % 8 == 0)
				hasvalue = false; // 万进位前有值标记逢亿复位
			lastzero = (n == 0) && (i % 4 != 0);
		}

		if (RMBStr.length() == 0)
			return HanDigiStr[0]; // 输入空字符或"0"，返回"零"
		return RMBStr;
	}

	public static String numToRMBStr(double val) {
		String SignStr = "";
		String TailStr = "";
		long fraction, integer;
		int jiao, fen;

		if (val < 0) {
			val = -val;
			SignStr = "负";
		}

		if (val > 99999999999999.999 || val < -99999999999999.999)
			return "数值位数过大!";
		// 四舍五入到分
		long temp = Math.round(val * 100);
		integer = temp / 100;
		fraction = temp % 100;
		jiao = (int) fraction / 10;
		fen = (int) fraction % 10;

		if (jiao == 0 && fen == 0) {
			TailStr = "整";
		} else {
			TailStr = HanDigiStr[jiao];
			if (jiao != 0)
				TailStr += "角";
			if (integer == 0 && jiao == 0) // 零元后不写零几分
				TailStr = "";
			if (fen != 0)
				TailStr += HanDigiStr[fen] + "分";
		}

		return "￥" + SignStr + positiveIntegerToHanStr(String.valueOf(integer))
				+ "元" + TailStr;
	}
	
	/**
	 * 把金额格式化为万元，格式为###,###.######，最多6位小数，如果小数为0则不显示
	 * 		示例：999,99.990000显示为999,99.99，888,88.888888显示为888,88.888888
	 * 
	 * @param number 要格式化为万元的金额
	 * @return 格式化为万元的金额
	 */
	public static String getTTFormatMoneyWithFraction(String number) {
		return dfTT1.format(new Double(number)/10000);
	}
	
	/**
	 * 把金额格式化为万元，格式为###,###.######，最多6位小数，如果小数为0则不显示
	 * 		示例：999,99.990000显示为999,99.99，888,88.888888显示为888,88.888888
	 * 
	 * @param number 要格式化为万元的金额
	 * @return 格式化为万元的金额
	 */
	public static String getTTFormatMoneyWithFraction(Double number) {
		return dfTT1.format(number/10000);
	}
	
	/**
	 * 把金额格式化为万元，格式为###,###
	 * 
	 * @param number 要格式化为万元的金额
	 * @return 格式化为万元的金额
	 */
	public static String getTTFormatMoneyWithNoFraction(String number) {
		return dfTT1.format(new Double(number)/10000);
	}
	
	/**
	 * 把金额格式化为万元，格式为###,###
	 * 
	 * @param number 要格式化为万元的金额
	 * @return 格式化为万元的金额
	 */
	public static String getTTFormatMoneyWithNoFraction(Double number) {
		return dfTT2.format(number/10000);
	}
	
	/**
	 * 把金额转化为千分位表示，格式为###,###.######
	 * 
	 * @param number
	 * @return 格式化为千分位表示的金额
	 */
	public static String getThousandFormatMoney(Double number) {
		return dfTT1.format(number);
	}
	
	/**
	 * 四舍五入，保留两位小数，不格式化千分位
	 * 
	 * @param number
	 * @return 四舍五入，保留两位小数，不格式化千分位
	 */
	public static String rounding(Double number) {
	    return dfTT3.format(number);
	}
	/**
	 * 四舍五入，保留两位小数，格式化千分位
	 * 
	 * @param number
	 * @return 四舍五入，保留两位小数，格式化千分位
	 */
	public static String getThousandFormatrounding(Double number) {
	    return dfTT4.format(number);
	}
	
	
	public static String getPercentFormat(Double number){
		   DecimalFormat df1 = new DecimalFormat("##.00%");    //##.00%   百分比格式，后面不足2位的用0补齐
		   return df1.format(number); 
	}
	
	
	
	public static void main(String[] args) {
		
		Double a = new Double("1123143436436.9922");
		Double a1 = new Double("2001");
		Double a2 = new Double("1123143436436.9922313222");
		Double a3 = new Double("0.021313");
		/*//System.out.println(getPercentFormat(a));
		//System.out.println(getPercentFormat(a1));
		//System.out.println(getPercentFormat(a2));
		//System.out.println(getPercentFormat(a3));*/
		
	}
}


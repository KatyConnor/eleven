package com.hx.nine.eleven.web.tokenizer;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.py.Pinyin;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;

import java.util.List;

/**
 * @auth wml
 * @date 2024/10/30
 */
public class HanlpDemo {

	public static void main(String[] args) {
		String text = "中国农业银行股份有限公司重庆市江北区支行";
		hanlpTokenizer(text);
		System.out.println("--------------------------------------------");
		convertTopinyin(text);
	}

	public static void  hanlpTokenizer(String text){
		List<Term> termList = StandardTokenizer.segment(text);
		List<Term> termList1 = HanLP.segment(text);
		System.out.println("分词结果：");
		for (Term term:termList){
			System.out.printf("%s, ",term.word);
		}
		System.out.println();
		System.out.println("分词结果1：");
		for (Term term:termList1){
			System.out.printf("%s, ",term.word);
		}
		System.out.println();
	}

	public static void convertTopinyin(String text){
		List<Pinyin> pinyins = HanLP.convertToPinyinList(text);
		System.out.println("原文：");
		System.out.println(text);
		System.out.println("拼音（数字音调）：");
		for(Pinyin pinyin : pinyins){
			System.out.printf("%s，",pinyin);
		}
		System.out.println();
		System.out.println("拼音（符号音调）：");
		for(Pinyin pinyin : pinyins){
			System.out.printf("%s，",pinyin.getPinyinWithToneMark());
		}
		System.out.println();
		System.out.println("拼音（无音调）：");
		for(Pinyin pinyin : pinyins){
			System.out.printf("%s，",pinyin.getPinyinWithoutTone());
		}
		System.out.println();
		System.out.println("拼音声调：");
		for(Pinyin pinyin : pinyins){
			System.out.printf("%s，",pinyin.getTone());
		}
		System.out.println();
		System.out.println("声母：");
		for(Pinyin pinyin : pinyins){
			System.out.printf("%s，",pinyin.getShengmu());
		}
		System.out.println();
		System.out.println("韵母：");
		for(Pinyin pinyin : pinyins){
			System.out.printf("%s，",pinyin.getYunmu());
		}
		System.out.println();
		System.out.println("输入法头：");
		for(Pinyin pinyin : pinyins){
			System.out.printf("%s，",pinyin.getHead());
		}
		System.out.println();
	}
}

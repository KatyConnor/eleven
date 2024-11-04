package com.hx.nine.eleven.web.tokenizer;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;
import com.luues.tool.analyzer.core.IKSegmenter;
import com.luues.tool.analyzer.core.Lexeme;
import com.luues.tool.analyzer.lucene.IKAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @auth wml
 * @date 2024/10/29
 */
public class SimpleChineseTokenizer {

	public static void main(String[] args) {
		String text = "中国农业银行股份有限公司重庆市江北区支行";
//		LuceneIndexAndSearch();
//		analyzer(text);
//		IKSegmenter(text);

	}

	//tool-IKAnalyzer
	public static void LuceneIndexAndSearch(){
		String fieldName = "text";
//		String text = "IK Analyzer是一个结合词典分词和文法分词的中文分词开源工具包。它使用了全新的正向迭代最细粒度切分算法。";
		String text = "中国农业银行股份有限公司重庆市江北区支行";
		Analyzer analyzer = new IKAnalyzer(true);
		Directory directory = null;
		IndexWriter iwriter = null;
		IndexReader ireader = null;
		IndexSearcher isearcher = null;

		try {
			directory = new RAMDirectory();
			IndexWriterConfig iwConfig = new IndexWriterConfig(Version.LUCENE_43, analyzer);
			iwConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
			iwriter = new IndexWriter(directory, iwConfig);
			Document doc = new Document();
			doc.add(new StringField("ID", "10000", Field.Store.YES));
			doc.add(new TextField(fieldName, text, Field.Store.YES));
			iwriter.addDocument(doc);
			iwriter.close();
			ireader = DirectoryReader.open(directory);
			isearcher = new IndexSearcher(ireader);
			String keyword = "中文分词工具包";
			QueryParser qp = new QueryParser(Version.LUCENE_43, fieldName, analyzer);
			qp.setDefaultOperator(QueryParser.AND_OPERATOR);
			Query query = qp.parse(keyword);
			System.out.println("Query = " + query);
			TopDocs topDocs = isearcher.search(query, 5);
			System.out.println("命中：" + topDocs.totalHits);
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;

			for(int i = 0; i < topDocs.totalHits; ++i) {
				Document targetDoc = isearcher.doc(scoreDocs[i].doc);
				System.out.println("内容：" + targetDoc.toString());
			}
		} catch (CorruptIndexException var38) {
			var38.printStackTrace();
		} catch (LockObtainFailedException var39) {
			var39.printStackTrace();
		} catch (IOException var40) {
			var40.printStackTrace();
		} catch (ParseException var41) {
			var41.printStackTrace();
		} finally {
			if (ireader != null) {
				try {
					ireader.close();
				} catch (IOException var37) {
					var37.printStackTrace();
				}
			}

			if (directory != null) {
				try {
					directory.close();
				} catch (IOException var36) {
					var36.printStackTrace();
				}
			}

		}
	}

	//tool-IKAnalyzer
	public static void analyzer(String text){
		Analyzer analyzer = new IKAnalyzer(true);
		TokenStream ts = null;
		//"这是一个中文分词的例子，你可以直接运行它！IKAnalyer can analysis english text too"
		try {
			ts = analyzer.tokenStream("myfield", new StringReader(text));
			OffsetAttribute offset = (OffsetAttribute)ts.addAttribute(OffsetAttribute.class);
			CharTermAttribute term = (CharTermAttribute)ts.addAttribute(CharTermAttribute.class);
			TypeAttribute type = (TypeAttribute)ts.addAttribute(TypeAttribute.class);
			ts.reset();

			while(ts.incrementToken()) {
				System.out.println(offset.startOffset() + " - " + offset.endOffset() + " : " + term.toString() + " | " + type.type());
			}

			ts.end();
		} catch (IOException var14) {
			var14.printStackTrace();
		} finally {
			if (ts != null) {
				try {
					ts.close();
				} catch (IOException var13) {
					var13.printStackTrace();
				}
			}

		}
	}

	//ikanalyzer
	public static void IKSegmenter(String text){
		StringReader reader = new StringReader(text);
		IKSegmenter ikSegmenter = new IKSegmenter(reader,true);
		Lexeme lexeme = null;
		try {
			while ((lexeme = ikSegmenter.next()) != null){
				System.out.println(lexeme.getLexemeText());
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}


}

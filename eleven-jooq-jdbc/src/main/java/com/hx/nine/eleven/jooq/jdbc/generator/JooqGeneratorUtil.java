package com.hx.nine.eleven.jooq.jdbc.generator;

import org.jooq.codegen.GenerationTool;
import org.jooq.meta.jaxb.Configuration;

import javax.xml.bind.JAXB;
import java.io.File;
import java.net.URL;
import java.net.URLDecoder;

public class JooqGeneratorUtil {
	public static void main(String[] args) throws Exception {
		generate("jooqConfig.xml");
	}

	private static void generate(String xml) throws Exception {
		URL url = JooqGeneratorUtil.class.getClassLoader().getResource(xml);
		String s = URLDecoder.decode(url.getFile(), "utf-8");
		Configuration configuration = JAXB.unmarshal(new File(s), Configuration.class);
		GenerationTool.generate(configuration);
	}
}

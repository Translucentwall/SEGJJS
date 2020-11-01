package edu.nju.se.teamnamecannotbeempty.batch.parser.csv.converters;

import com.opencsv.bean.AbstractCsvConverter;
import edu.nju.se.teamnamecannotbeempty.data.domain.Term;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.analysis.en.EnglishMinimalStemFilterFactory;
import org.apache.lucene.analysis.standard.ClassicFilterFactory;
import org.apache.lucene.analysis.standard.ClassicTokenizerFactory;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class ToTerm{
    private static final HashMap<String, Term> saveMap = new HashMap<>();
    /**
     * 保存分词结果
     */
    private static final HashMap<List<String>, Term> tokens = new HashMap<>();
    private static final Analyzer analyzer;

    static {
        Analyzer analyzer1;
        try {
            analyzer1 = CustomAnalyzer.builder()
                    .withTokenizer(ClassicTokenizerFactory.class)
                    .addTokenFilter(EnglishMinimalStemFilterFactory.class)
                    .addTokenFilter(ClassicFilterFactory.class)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            analyzer1 = null;
        }
        analyzer = analyzer1;
    }

    public static Term getTerm(String name){
        name = name.trim();
        if (StringUtils.isBlank(name)) {
            return null;
        }
        //规范化，转换为小写并删除末尾的句点
        name = name.toLowerCase();
        if (name.endsWith(".")) {
            name = name.substring(0, name.length() - 1);
        }

        Term result = saveMap.get(name);
        if (result == null) {
            List<String> token = null;
            try {
                token = getTokenSet(name);
            } catch (IOException e) {
                e.printStackTrace();
            }

            synchronized (saveMap) {
                if ((result = saveMap.get(name)) == null && (result = tokens.get(token)) == null) {
                    result = new Term();
                    result.setContent(name);
                    saveMap.put(name, result);
                    if (token != null) {
                        tokens.put(token, result);
                    }
                }
            }
        }
        return result;
    }

    /**
     * 方法除了如方法名所示地返回了对象集合，同时也清空了解析过程中缓存的token
     *
     * @return term对象集合
     */
    public static Collection<Term> getSaveCollection() {
        tokens.clear();
        return saveMap.values();
    }

    static List<String> getTokenSet(String value) throws IOException {
        TokenStream tokenStream;
        synchronized (analyzer) {
            tokenStream = analyzer.tokenStream("", value);
        }
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        List<String> tokenSet = new ArrayList<>();
        tokenStream.reset();
        while (tokenStream.incrementToken()) {
            tokenSet.add(charTermAttribute.toString());
        }
        tokenStream.end();
        tokenStream.close();
        return tokenSet;
    }
}

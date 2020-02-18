package com.ola.unitTests.nativeIndex;

import com.ola.nativeSearch.JaroWinkler;
import com.ola.nativeSearch.PhraseMatcher;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class WordMatchTests {
    @Test
    public void Similarity_jwTests(){
        var jw = new JaroWinkler();

        Assertions.assertEquals(1.0, jw.getDistance("aamar", "aamar"));
        Assertions.assertEquals(0.9399999976158142, jw.getDistance("aamar", "amaar"));
        Assertions.assertEquals(0.0, jw.getDistance("aamar", "totini"));
    }

    @Test
    public void Phrase_similarity(){
        var phraseMatcher = new PhraseMatcher();

        var query   = "daddy late";
        var phrase1 = "Daddy is always late";
        var phrase2 = "Dad will come home later";

        Assertions.assertTrue(phraseMatcher.GetSimilarity(query, phrase1) > phraseMatcher.GetSimilarity(query, phrase2));
    }

    @Test
    public void Rank_order(){
        var phraseMatcher = new PhraseMatcher();

        var query   = "daddy late";
        var phrase1 = "Daddy is always late";
        var phrase2 = "Dad will come home later";

        var contents = new String[]{phrase2, phrase1};
        var rankOrder = phraseMatcher.GetRankOrder(query, contents);

        Assertions.assertArrayEquals(new int[]{1,0}, rankOrder);
    }
}

package com.ola.unitTests.JWRSearchTests;

import com.ola.JWRSearch.SmithWaterman;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SmithWatermanTests {
    @Test
    public void EditDistanceTests(){
        var sw = new SmithWaterman();
        Assertions.assertEquals(3, sw.GetDistance("", "abc"));
        Assertions.assertEquals(2, sw.GetDistance("b", "abc"));
        Assertions.assertEquals(1, sw.GetDistance("bc", "abc"));
        Assertions.assertEquals(2, sw.GetDistance("ada", "bad"));
        Assertions.assertEquals(3, sw.GetDistance("horse", "ros"));
        Assertions.assertEquals(5, sw.GetDistance("intention", "execution"));
    }

    @Test
    public void SimilarityTests(){
        var sw = new SmithWaterman();
        Assertions.assertEquals(0, sw.GetSimilarity("", "abc"));
        Assertions.assertEquals(0.3333333f, sw.GetSimilarity("b", "abc"));
        Assertions.assertEquals(0.6666666f, sw.GetSimilarity("bc", "abc"));
        Assertions.assertEquals(0.3333333f, sw.GetSimilarity("ada", "bad"));
        Assertions.assertEquals(0.3999999761581421, sw.GetSimilarity("horse", "ros"));
        Assertions.assertEquals(0.4444444179534912, sw.GetSimilarity("intention", "execution"));
    }
}

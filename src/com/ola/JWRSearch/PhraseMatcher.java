package com.ola.JWRSearch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class PhraseMatcher {
    //contains utility methods for scoring word or phrase similarity.
    private JaroWinkler _jwMatcher;
    public PhraseMatcher(){
        _jwMatcher = new JaroWinkler();
    }

    public float GetSimilarity(String query, String content){
        //todo: this is O(n*m). need to improve runtime
        var queryWords   = query.split("\\s+");
        var contentWords = content.split("\\s+");
        var scores       = new float[queryWords.length];
        Arrays.fill(scores, 0);

        for(var i=0; i < queryWords.length; i++){
            for(var cWord: contentWords){
                var similarity = _jwMatcher.GetThresholdSimilarity(queryWords[i], cWord);
                if(scores[i] < similarity) scores[i] = similarity;
            }
        }
        var score = 0.0f;
        for(var s: scores)
            score+=s;
        return score;
    }

    public int[] GetRankOrder(String query, String[] contents){
        var scores = GetSimilarityScores(query, contents);
        var scoresAndIndices = new ArrayList<ScoreAndIndex>();

        for(var i=0; i < scores.length; i++)
            scoresAndIndices.add(new ScoreAndIndex(scores[i], i));

        Collections.sort(scoresAndIndices, Collections.reverseOrder());

        var indices = new int[contents.length];
        for(var i=0; i < scores.length; i++)
            indices[i]= scoresAndIndices.get(i).Index;

        return indices;
    }

    public float[] GetSimilarityScores(String query, String[] contents){
        var scores = new float[contents.length];
        for(var i=0; i < contents.length; i++)
            scores[i]= GetSimilarity(query, contents[i]);
        return scores;
    }
}

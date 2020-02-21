package com.ola.JWRSearch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class JWRInvertedIndex {
    private HashMap<String, ArrayList<Integer>> _tokenToDocIndices;
    private HashMap<String, Float> _tokenSimilarities;
    private JaroWinkler _jw;
    private int _docCount;

    public JWRInvertedIndex(){
        _tokenToDocIndices = new HashMap<>();
        _tokenSimilarities = new HashMap<>();
        _jw = new JaroWinkler();
        _docCount = 0;
    }

    public void Add(String document){
        // if the same token exists multiple times in a document, we will add it multiple times to the inv index.
        // so, if 'xyz' occurs 10 times in doc_i, the inv index entry for 'xyz' will contain doc_i 10 times.
        for(var token: JWRUtilities.GetTokens(document)){
            if(!_tokenToDocIndices.containsKey(token)) {
                _tokenToDocIndices.put(token, new ArrayList<>());
                _tokenSimilarities.put(token, 0.0f);
            }

            var docList = _tokenToDocIndices.get(token);
            docList.add(_docCount);
        }
        _docCount++;
    }

    private void ClearScores(){
        for(var token:_tokenSimilarities.keySet())
            _tokenSimilarities.replace(token, 0.0f);
    }

    public void ScoreIndexTokens(String query){
        ClearScores();
        var queryTokens = JWRUtilities.GetTokens(query);

        for(var qToken : queryTokens){
            for( var iToken : _tokenToDocIndices.keySet()){
                var similarity = _jw.GetThresholdSimilarity(qToken, iToken);
                if (similarity > _tokenSimilarities.get(iToken))
                    _tokenSimilarities.replace(iToken,similarity);
            }
        }
    }

    public float[] GetDocumentScores(String query){
        ScoreIndexTokens(query);
        var docScores = new float[_docCount];//java initializes to default value of 0.0

        for(var tokenToDocs: _tokenToDocIndices.entrySet()){
            var token = tokenToDocs.getKey();
            var docs = tokenToDocs.getValue();

            for (var i: docs) {
                docScores[i]+= _tokenSimilarities.get(token);
            }
        }
        return docScores;
    }


    public int[] Search(String query){
        var threshold = GetThreshold(query);
        var scores = GetDocumentScores(query);
        var scoresAndIndices = new ArrayList<ScoreAndIndex>();
        for(var i=0; i < scores.length; i++)
        {
            if(scores[i] < threshold) continue;
            scoresAndIndices.add(new ScoreAndIndex(scores[i], i));
        }

        Collections.sort(scoresAndIndices, Collections.reverseOrder());
        var indices = new int[scoresAndIndices.size()];
        for(var i=0; i < indices.length; i++)
            indices[i]= scoresAndIndices.get(i).Index;

        return indices;
    }

    private float GetThreshold(String query) {
        var tokens = JWRUtilities.GetTokens(query);
        return tokens.size()*_jw.threshold;
    }
}

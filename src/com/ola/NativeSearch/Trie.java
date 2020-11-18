package com.ola.NativeSearch;

import org.junit.jupiter.params.shadow.com.univocity.parsers.common.DataValidationException;

public class Trie{
    public final char RootChar='*';
    public final char TerminalChar = '#';
    private final int NumBaseIndex = 0;
    private final int AlphaBaseIndex = 10;
    private final int CharSetSize = 36; //  0..9 numbers, 10-35 letters of the alphabet

    private class TrieNode {
        public final char Value;
        public TrieNode[] children;

        public TrieNode (char value){
            Value = value;
            children = new TrieNode[CharSetSize];
        }
    }

    private TrieNode _root;
    public Trie(){
        _root = new TrieNode(RootChar);
    }

    public void Add(String word){
        word = word.toLowerCase();
        var currentNode = _root;
        for(var i=0; i < word.length();i++){
            var c = word.charAt(i);
            var index = getCharIndex(c);
            if(currentNode.children[index]== null) {
                currentNode.children[index] = new TrieNode(c);
            }
            currentNode = currentNode.children[index];
        }
    }

    public boolean Find(String word){
        word = word.toLowerCase();
        var currentNode = _root;
        for(var i=0; i < word.length();i++){
            var c = word.charAt(i);
            var index = getCharIndex(c);
            if(currentNode.children[index]== null) {
                return false;
            }
            currentNode = currentNode.children[index];
        }
        return true;
    }

    private int getCharIndex(char c) {
        if(Character.isDigit(c)){
            return NumBaseIndex + c - '0';
        }
        if(Character.isAlphabetic(c)){
            return AlphaBaseIndex + c - 'a';
        }
        throw new DataValidationException("non alpha-numeric character detected");

    }
}

package com.ola.unitTests.NativeSearchTests;

import com.ola.NativeSearch.Trie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TrieTests {
    @Test
    public void AddAndFind(){
        var trie = new Trie();
        trie.Add("Hello");
        trie.Add("world");

        Assertions.assertTrue(trie.Find("hello"));
    }
}

package com.example.airports;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

// префиксное дерево, хранящее список массивов строк в листах
public class Trie {
    static class TrieNode {
        TreeMap<Character, TrieNode> children = new TreeMap<>();
        List<String[]> leaf = new ArrayList<String[]>();
    }

    TrieNode root = new TrieNode();

    public void put(String[] line, int n) {
        TrieNode node = root;
        var s = line[n];
        for (char ch : s.toLowerCase().toCharArray()) {
            if (!node.children.containsKey(ch)) {
                node.children.put(ch, new TrieNode());
            }
            node = node.children.get(ch);
        }
        node.leaf.add(line);
    }

    public List<String[]> getAllLeafWithStart(String s) {
        TrieNode node = root;
        for (char ch : s.toLowerCase().toCharArray()) {
            if (!node.children.containsKey(ch)) {
                return new ArrayList<>();
            } else {
                node = node.children.get(ch);
            }
        }
        var result = new ArrayList<TrieNode>();
        getAllChildren(node, result);
        return result.stream().flatMap(x -> x.leaf.stream()).collect(Collectors.toList());
    }

    private void getAllChildren(TrieNode currentRoot, ArrayList<TrieNode> result) {
        if (currentRoot.leaf != null) {
            result.add(currentRoot);
        }
        for (var child : currentRoot.children.keySet()) {
            getAllChildren(currentRoot.children.get(child), result);
        }
    }
}
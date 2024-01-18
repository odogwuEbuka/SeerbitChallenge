package com.seerbit.Codingtest;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AlgorithmSolutions {

    public static List<Interval> mergeIntervals(List<Interval> intervals) {
        if (intervals == null || intervals.size() <= 1) {
            return intervals;
        }

        List<Interval> mergedIntervals = new ArrayList<>();
        Interval currentInterval = intervals.get(0);

        for (int i = 1; i < intervals.size(); i++) {
            Interval interval = intervals.get(i);

            if (currentInterval.getEnd().isAfter(interval.getStart())) {
                // Overlapping intervals, merge them
                currentInterval.setEnd(currentInterval.getEnd().isAfter(interval.getEnd()) ?
                        currentInterval.getEnd() : interval.getEnd());
            } else {
                // Non-overlapping interval found, add the current merged interval
                mergedIntervals.add(currentInterval);
                currentInterval = interval;
            }
        }

        // Add the last merged interval
        mergedIntervals.add(currentInterval);

        return mergedIntervals;
    }


    static class Interval {
        private Instant start;
        private Instant end;

        public Interval(Instant start, Instant end) {
            this.start = start;
            this.end = end;
        }

        public Instant getStart() {
            return start;
        }

        public Instant getEnd() {
            return end;
        }

        public void setEnd(Instant end) {
            this.end = end;
        }

        @Override
        public String toString() {
            return "[" + start + " - " + end + "]";
        }
    }

    public static void main(String[] args) {
        AlgorithmSolutions algorithmSolutions = new AlgorithmSolutions();

        // Example usage with timestamp intervals
        List<Interval> intervals = Arrays.asList(
                new Interval(Instant.parse("2022-01-01T00:00:00Z"), Instant.parse("2022-01-03T00:00:00Z")),
                new Interval(Instant.parse("2022-01-02T12:00:00Z"), Instant.parse("2022-01-06T00:00:00Z")),
                new Interval(Instant.parse("2022-01-08T00:00:00Z"), Instant.parse("2022-01-10T00:00:00Z")),
                new Interval(Instant.parse("2022-01-15T00:00:00Z"), Instant.parse("2022-01-18T00:00:00Z"))
        );

        List<Interval> result = algorithmSolutions.mergeIntervals(intervals);

        System.out.println("Merged Intervals:");
        for (Interval interval : result) {
            System.out.println(interval);
        }

        int[] arr = {1,2,3,4};
        int n = arr.length;

        int maxXOR = algorithmSolutions.maxSubarrayXORWithTrie(n, arr);
        System.out.println("Maximum XOR subarray: " + maxXOR);
    }

/*
Problem 2

To achieve this, we can use a Trie data structure to efficiently compute the maximum XOR subarray


 */

    class TrieNode {
        TrieNode left;
        TrieNode right;
        boolean isEndOfPrefixXOR;

        TrieNode() {
            left = null;
            right = null;
            isEndOfPrefixXOR = false;
        }
    }

    public int maxSubarrayXORWithTrie(int n, int[] arr) {
        int[] prefixXORs = new int[n + 1]; // Store prefix XORs for fast lookup
        prefixXORs[0] = 0; // Initialize the first element to 0

        // Calculate prefix XORs
        for (int i = 1; i <= n; i++) {
            prefixXORs[i] = prefixXORs[i - 1] ^ arr[i - 1];
        }

        TrieNode root = new TrieNode();

        // Construct Trie with prefix XORs
        for (int i = 0; i <= n; i++) {
            insertPrefixXOR(root, prefixXORs[i]);
        }

        // Find maximum XOR using Trie
        int maxXOR = 0;
        for (int i = 1; i <= n; i++) {
            int prefixXOR = prefixXORs[i];
            int maxPossibleXOR = findMaxXOR(root, prefixXOR);
            maxXOR = Math.max(maxXOR, maxPossibleXOR);
        }

        return maxXOR;
    }

    // Insert prefix XOR into Trie
    void insertPrefixXOR(TrieNode root, int prefixXOR) {
        TrieNode current = root;
        for (int i = 31; i >= 0; i--) {
            int bit = (prefixXOR >> i) & 1;
            if (bit == 0) {
                if (current.left == null) {
                    current.left = new TrieNode();
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new TrieNode();
                }
                current = current.right;
            }
        }
        current.isEndOfPrefixXOR = true;
    }

    // Find maximum XOR in Trie
    int findMaxXOR(TrieNode root, int prefixXOR) {
        TrieNode current = root;
        int result = 0;
        for (int i = 31; i >= 0; i--) {
            int bit = (prefixXOR >> i) & 1;
            if (bit == 0) {
                if (current.right != null) {
                    result = (result << 1) | 1;
                    current = current.right;
                } else {
                    current = current.left;
                }
            } else {
                if (current.left != null) {
                    result = (result << 1) | 1;
                    current = current.left;
                } else {
                    current = current.right;
                }
            }
        }
        return result;
    }



}


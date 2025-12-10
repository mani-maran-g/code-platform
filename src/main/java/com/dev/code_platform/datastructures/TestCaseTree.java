package com.dev.code_platform.datastructures;

import com.dev.code_platform.model.TestCase;

import java.util.ArrayList;
import java.util.List;

public class TestCaseTree {

    // Inner class representing a node in the BST
    private class TreeNode {
        TestCase data;           // The test case
        TreeNode left;           // Left child (smaller difficulty)
        TreeNode right;          // Right child (larger difficulty)

        TreeNode(TestCase data) {
            this.data = data;
            this.left = null;
            this.right = null;
        }
    }

    private TreeNode root;       // Root of the tree
    private int count;           // Total nodes in tree

    public TestCaseTree() {
        this.root = null;
        this.count = 0;
    }

    /**
     * Insert a test case into the BST based on difficulty level
     * Time Complexity: O(log n) average, O(n) worst case
     */
    public void insert(TestCase testCase) {
        root = insertRecursive(root, testCase);
        count++;
    }

    private TreeNode insertRecursive(TreeNode node, TestCase testCase) {
        // Base case: found the position to insert
        if (node == null) {
            return new TreeNode(testCase);
        }

        // Compare difficulty levels
        int currentDifficulty = testCase.getDifficultyLevel();
        int nodeDifficulty = node.data.getDifficultyLevel();

        if (currentDifficulty < nodeDifficulty) {
            // Go left (smaller difficulty)
            node.left = insertRecursive(node.left, testCase);
        } else {
            // Go right (equal or larger difficulty)
            node.right = insertRecursive(node.right, testCase);
        }

        return node;
    }

    /**
     * Get all test cases in sorted order (easy to hard)
     * Time Complexity: O(n)
     */
    public List<TestCase> inOrderTraversal() {
        List<TestCase> result = new ArrayList<>();
        inOrderRecursive(root, result);
        return result;
    }

    private void inOrderRecursive(TreeNode node, List<TestCase> result) {
        if (node == null) {
            return;
        }

        // In-order: Left -> Root -> Right
        inOrderRecursive(node.left, result);    // Visit left subtree
        result.add(node.data);                   // Visit current node
        inOrderRecursive(node.right, result);   // Visit right subtree
    }

    /**
     * Get total number of test cases
     */
    public int getCount() {
        return count;
    }

    /**
     * Check if tree is empty
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Clear all test cases from tree
     */
    public void clear() {
        root = null;
        count = 0;
    }







    // Temporary test method - DELETE after testing
//    public static void main(String[] args) {
//        TestCaseTree tree = new TestCaseTree();
//
//        // Create 5 test cases with different difficulties
//        TestCase tc1 = new TestCase();
//        tc1.setTestCaseId("tc1");
//        tc1.setDifficultyLevel(3);
//        tc1.setInput("test3");
//
//        TestCase tc2 = new TestCase();
//        tc2.setTestCaseId("tc2");
//        tc2.setDifficultyLevel(1);
//        tc2.setInput("test1");
//
//        TestCase tc3 = new TestCase();
//        tc3.setTestCaseId("tc3");
//        tc3.setDifficultyLevel(5);
//        tc3.setInput("test5");
//
//        TestCase tc4 = new TestCase();
//        tc4.setTestCaseId("tc4");
//        tc4.setDifficultyLevel(2);
//        tc4.setInput("test2");
//
//        TestCase tc5 = new TestCase();
//        tc5.setTestCaseId("tc5");
//        tc5.setDifficultyLevel(4);
//        tc5.setInput("test4");
//
//        // Insert in random order
//        System.out.println("Inserting test cases...");
//        tree.insert(tc1);  // difficulty 3
//        tree.insert(tc2);  // difficulty 1
//        tree.insert(tc3);  // difficulty 5
//        tree.insert(tc4);  // difficulty 2
//        tree.insert(tc5);  // difficulty 4
//
//        System.out.println("Total test cases: " + tree.getCount());
//
//        // Get sorted order
//        System.out.println("\nIn-order traversal (should be sorted):");
//        List<TestCase> sorted = tree.inOrderTraversal();
//        for (TestCase tc : sorted) {
//            System.out.println("Difficulty: " + tc.getDifficultyLevel() +
//                    ", ID: " + tc.getTestCaseId());
//        }
//
//        // Expected output:
//        // Difficulty: 1, ID: tc2
//        // Difficulty: 2, ID: tc4
//        // Difficulty: 3, ID: tc1
//        // Difficulty: 4, ID: tc5
//        // Difficulty: 5, ID: tc3
//    }
}

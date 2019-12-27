package com.github.flyingglass.distributed.chash;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author: fly
 * Created date: 2019/12/27 11:12
 * 缓存一致性hash原理简单测试，关键在于：
 * 1. Hash函数
 * 2. 虚拟节点增强均匀性
 * https://www.cnblogs.com/stateis0/p/9062135.html
 * https://github.com/lexburner/consistent-hash-algorithm
 */
@Slf4j
public class ConsistentHash {

    @ToString
    static class HashObj {
        private String key;

        public HashObj(String key) {
            this.key = key;
        }

        @Override
        public int hashCode() {
            return key.hashCode();
        }
    }


    static class Node {
        private Map<Integer, HashObj> node = new HashMap<>();

        private String name;

        Node(String name) {
            this.name = name;
        }

        void putObj(HashObj obj) {
            node.put(obj.hashCode(), obj);
        }

        HashObj getObj(HashObj obj) {
            return node.get(obj.hashCode());
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }


    static class NoConsistentHashNodeArray {

        Node[] nodes = new Node[1024];
        int size = 0;

        void addNode(Node node) {
            nodes[size++] = node;
        }

        HashObj get(HashObj obj) {
            int idx = obj.hashCode() % size;
            return nodes[idx].getObj(obj);
        }

        void put(HashObj obj) {
            int idx = obj.hashCode() % size;
            nodes[idx].putObj(obj);
        }
    }

    static class NodeArray {

        /** 按照 键 排序*/
        TreeMap<Integer, Node> nodes = new TreeMap<>();

        void addNode(Node node) {
            nodes.put(node.hashCode(), node);
        }

        void put(HashObj obj) {
            int objHashcode = obj.hashCode();
            Node node = nodes.get(objHashcode);
            if (node != null) {
                node.putObj(obj);
                return;
            }

            // 找到比给定 key 大的集合
            SortedMap<Integer, Node> tailMap = nodes.tailMap(objHashcode);
            // 找到最小的节点
            int nodeHashcode = tailMap.isEmpty() ? nodes.firstKey() : tailMap.firstKey();
            nodes.get(nodeHashcode).putObj(obj);

        }

        HashObj get(HashObj obj) {
            Node node = nodes.get(obj.hashCode());
            if (node != null) {
                return node.getObj(obj);
            }

            // 找到比给定 key 大的集合
            SortedMap<Integer, Node> tailMap = nodes.tailMap(obj.hashCode());
            // 找到最小的节点
            int nodeHashcode = tailMap.isEmpty() ? nodes.firstKey() : tailMap.firstKey();
            return nodes.get(nodeHashcode).getObj(obj);
        }
    }

    public static void testConsistentHash() {
        NoConsistentHashNodeArray nodeArray = new NoConsistentHashNodeArray();

        Node[] nodes = {
                new Node("Node--> 1"),
                new Node("Node--> 2"),
                new Node("Node--> 3")
        };

        for (Node node : nodes) {
            nodeArray.addNode(node);
        }

        HashObj[] objs = {
                new HashObj("1"),
                new HashObj("2"),
                new HashObj("3"),
                new HashObj("4"),
                new HashObj("5"),
        };

        for (HashObj obj : objs) {
            nodeArray.put(obj);
        }

        validate(nodeArray, objs);

        // ConsistentHash

        NodeArray nodeArray1 = new NodeArray();

        for (Node node : nodes) {
            nodeArray1.addNode(node);
        }

        for (HashObj obj : objs) {
            nodeArray1.put(obj);
        }

        validate(nodeArray1, objs);
    }


    private static void validate(NoConsistentHashNodeArray nodeArray, HashObj[] objs) {
        for (HashObj obj : objs) {
            log.info("{}", nodeArray.get(obj));
        }

        nodeArray.addNode(new Node("anything1"));
        nodeArray.addNode(new Node("anything2"));

        log.info("========== after  =============");

        for (HashObj obj : objs) {
            log.info("{}", nodeArray.get(obj));
        }
    }


    private static void validate(NodeArray nodeArray, HashObj[] objs) {
        for (HashObj obj : objs) {
            log.info("{}", nodeArray.get(obj));
        }

        nodeArray.addNode(new Node("anything1"));
        nodeArray.addNode(new Node("anything2"));

        log.info("========== after  =============");

        for (HashObj obj : objs) {
            log.info("{}", nodeArray.get(obj));
        }
    }
}

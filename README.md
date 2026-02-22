# Consistent Hashing

Consistent Hashing is a distributed hashing scheme that operates independently of the number of servers or objects in a distributed hash table. It is useful for distributed caching and database sharding because it minimizes reorganization of keys when nodes are added or removed.

## Concepts and Implementation

This project implements a basic Consistent Hashing mechanism in Java. Below is a mapping of the core concepts to the classes in this project:

*   **Hash Ring**: The core data structure that maps both servers (nodes) and keys to a circle (ring) based on their hash values.
    *   Implementation: [`HashRing.java`](src/main/java/org/kopcheski/consistenthashing/HashRing.java) - Manages the ring, adds/removes nodes, and finds the responsible node for a given key.

*   **Node (Server)**: Represents a physical or virtual server in the distributed system that stores data.
    *   Implementation: [`Node.java`](src/main/java/org/kopcheski/consistenthashing/Node.java) - Simulates a storage node with a simple in-memory map.
    *   Identifier: [`NodeId.java`](src/main/java/org/kopcheski/consistenthashing/model/NodeId.java) - Strongly typed identifier for a node.

*   **Virtual Nodes**: To ensure a more uniform distribution of keys, each physical node can be represented by multiple virtual nodes on the ring.
    *   Implementation: Handled within `HashRing.java` (see `addNode` method where virtual nodes are created).

*   **Hash Function**: A function used to map nodes and keys to integer values on the ring.
    *   Implementation: [`HashFunction.java`](src/main/java/org/kopcheski/consistenthashing/HashFunction.java) - Provides the hashing logic.

*   **Client**: The entity that interacts with the distributed system to put and get values.
    *   Implementation: [`Client.java`](src/main/java/org/kopcheski/consistenthashing/Client.java) - Acts as a gateway, using the `HashRing` to route requests to the correct `Node`.

*   **Key**: The identifier for the data being stored.
    *   Implementation: [`Key.java`](src/main/java/org/kopcheski/consistenthashing/model/Key.java) - Strongly typed identifier for a key.

## Usage

This project can be run from the command line using the provided `run.zsh` script.

To run with default values:
```sh
./run.zsh
```

You can also provide an optional argument to specify the number of keys to be generated and stored in the ring. For example, to generate 50,000 keys:
```sh
./run.zsh 50000
```

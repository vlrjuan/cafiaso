# Cafiaso

Cafiaso is a lightweight implementation of a 1.21.4 Minecraft server
without using NMS.

## Strengths

### **1. High Performance**

Cafiaso runs asynchronously using Project Loom virtual threads.
It ensures lightweight connection handling, supporting hundreds or thousands of clients.
Cafiaso is designed to be *fast* and *efficient*, with minimal overhead.

### **2. Lightweight**

Minimal dependencies and a small codebase make Cafiaso easy to understand and extend.
We aim to keep the codebase as clean and simple as possible.

### **3. No NMS**

We don't use any existing NMS (net.minecraft.server) codebase, as well as CraftBukkit or Spigot.
Cafiaso is written *from scratch*, which allows us to have full control over the server's behavior.
This makes Cafiaso more stable and easier to maintain.

Cafiaso is fully compliant with the 
[Minecraft protocol](https://minecraft.wiki/w/Minecraft_Wiki:Projects/wiki.vg_merge/Protocol).

## Getting Started

### Prerequisites

- Java 21 or higher (required for Project Loom virtual threads).
- A modern IDE like IntelliJ IDEA or Eclipse.

### Running the Server

1. Clone the repository.
2. Compile the project using `./gradlew build`.
3. Run the server using `java -jar server.jar`.
4. The server will start listening for connections on `localhost:25565` by default (can be configured using `-h` and `-p` flags).

### Configuration

- The server reads configuration values from `server.properties` in the project resources directory.
- You can modify the server's motd, and other settings in this file.

## Contributing

Contributions are welcome! If you have suggestions or bug fixes, feel free to open a pull request or submit an issue.

## License

This project is licensed under the MIT License. See the LICENSE file for details.



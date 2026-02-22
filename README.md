# Cafiaso

_Written by Juan Valero_

Cafiaso is a lightweight implementation of a 1.21.11 Minecraft server without using NMS.

## Strengths

### High Performance

Cafiaso runs asynchronously. It is designed to be fast and efficient, with a focus on performance.

### Lightweight

Minimal dependencies and a small codebase make Cafiaso easy to understand and extend.
We aim to keep the codebase as clean and simple as possible.

### No NMS

Cafiaso does not use NMS (Net Minecraft Server) code. It is entirely written _from scratch_,
which allows us to have full control over the server's behavior and performance.
This makes it easier to maintain and extend.

Cafiaso aims to be fully compatible with the
latest [Minecraft protocol](https://minecraft.wiki/w/Java_Edition_protocol).

### Well Documented

Cafiaso is well-documented, with clear and concise documentation for all classes and methods.

### Well Tested

Cafiaso is well-tested, with comprehensive suites that cover all aspects of the server.
We aim to maintain a high level of code coverage to ensure the server is stable and reliable (approximately 80%).

## Getting Started

Now that you know what Cafiaso is, let's get started with setting it up.

### Prerequisites

- Java 25+

### Running the Server

1. Clone the repository
    ```bash
    git clone git@github.com:cafiaso/cafiaso.git
    cd cafiaso
    ```
2. Build the project
    ```bash
    ./gradlew build
    ```
3. Run the server
    ```bash
    java -jar build/libs/cafiaso-1.0-SNAPSHOT.jar
    ```

The server will start listening on `localhost` (`127.0.0.1:25565`) by default
(can be configured using `-h` and `-p` flags, respectively).

## License

Cafiaso is licensed under the [MIT License](LICENSE).

## Acknowledgements

- Thanks to [Minestom](https://github.com/Minestom/Minestom) and [Glowstone](https://github.com/GlowstoneMC/Glowstone/)
  which inspired me to create Cafiaso.
- Thanks to the [Minecraft Wiki](https://minecraft.wiki) for the incredible amount of information
  about the Minecraft protocol and game mechanics.

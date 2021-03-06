# Parallelism and Concurrency in Java

This repository contains source code underlying
[Paralellism and Concurrency in Java](https://sebfisch.github.io/java-concurrency/).

You can use you own Java Development Environment to work with the code.
The code has been developed with Java Version 11.

Alternatively, you can install 
[Docker](https://docs.docker.com/get-docker/)
and 
[VS Code](https://code.visualstudio.com/download)
with the 
[Remote Development Extension Pack](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.vscode-remote-extensionpack)
to use a predefined development environment without creating (or adjusting) your own.
To use the predefined environment in VS Code 
download and unpack
[this repository](https://github.com/sebfisch/java-concurrency-code/archive/main.zip)
(or use git to clone it),
open the repository folder in VS Code,
click on the Remote Containers icon in the bottom-left corner, 
and select *Reopen Folder in Container*.

The first time you open the folder in a container will take a long time.
To check if the container was created successfully,
you can open a terminal in VS Code,
and run `mvn test` to run the test suite.
You can also run tests in VS Code
by opening a Java file containing tests
and clicking the *Run Test* link shown with declarations.

As an alternative to VS Code, you can run a development environment 
based on `tmux` and `vim` with the following terminal command.

    docker-compose run --rm dev

[© 2021](https://creativecommons.org/licenses/by-sa/2.0/)
by Sebastian Fischer

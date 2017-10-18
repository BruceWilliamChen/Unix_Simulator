# Unix System Emulator :computer:

This repo is dedicated to the implementation of a simulator for unix system

## Background information :book:

[Unix](https://en.wikipedia.org/wiki/Unix) :blue_book: - Unix system background

[Unix Command-line interface](https://en.wikipedia.org/wiki/Command-line_interface) :fire: - Command-line interface and its basic commands

[Unix File System](https://en.wikipedia.org/wiki/Unix_filesystem) :libra: - Unix file system background, including read, write and delete files on disks

[Multithreading Programming](https://en.wikipedia.org/wiki/Thread_(computing)) :rocket: - Programming design to allow multithreading executions that drastically improve running speed and efficiency

[Multithreading Programming](https://en.wikipedia.org/wiki/Synchronization_(computer_science)) :moon: - Background of synchronizations and how it critiques the resources sharing between processes

## The basic picture :bulb:

### Unix shell emulator :computer:

Implementing a Unix shell emulator which takes in input commands and interacts with the users and utilizing Java multithreading programming API to allow execution concurrency for command pipelines and support background execution for marked command filters

### Unix file system :gem:

Implementing a producer-consumer synchronization model and a Unix file system simulator that simulates read, write, create and delete files on a virtual disk with supporting for large file handling by using the techniques of single, double and triple indirection

## To-Do List :pencil:

- [X] Implemented a unix shell command line interface
- [X] Achieved multithreading execution of commands and allow background processes
- [ ] Implementing a producer-consumer model and introducing client and server objects
- [ ] Implementing the file system
- [ ] Adding support for all I/O operations and support large file handling

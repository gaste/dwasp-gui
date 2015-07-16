# DWASP GUI
A graphical user interface for debugging faulty ASP programs with [gringo-wrapper](https://github.com/gaste/gringo-wrapper) and [DWASP](https://github.com/gaste/wasp).

<p align="center">
<a href="https://github.com/gaste/dwasp-gui/releases/latest"><img src="https://img.shields.io/github/release/gaste/dwasp-gui.svg" alt="Latest Version"></img></a>
</p>


## Table of contents
 - [Building](#building)

## Building
This project is managed using [Apache Maven](https://maven.apache.org/). If all [dependencies](#dependencies) are present, use `mvn compile` to build the project.

###### Dependencies
This project depends on [gringo-wrapper](https://github.com/gaste/gringo-wrapper). Thus to build it, you need to have the gringo-wrapper binaries installed in your local maven repository:

```
git clone https://github.com/gaste/gringo-wrapper
cd gringo-wrapper
mvn install
```

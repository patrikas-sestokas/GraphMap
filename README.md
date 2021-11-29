# GraphMap
Graph simulation using Java.

# Prerequisites
* [Java SE 8](https://www.oracle.com/java/technologies/javase/javase8-archive-downloads.html)
* An IDE of your choice (confirmed to be working on IntelliJ IDEA).

# Usage
Launch using either sample/Draw.java, sample/SimpleDraw.java or B19/SubTree.java.

# Demonstration
## Draw.java 
Draw a graph using either edge matrix (top 2 textboxes) or L and lst arrays (the bottom 2).

### Using 1 2 3 vertice triangle, before hitting "Play" button.
<br>
<img src="https://github.com/patrikas-sestokas/GraphMap/blob/main/assets/Showcase1.png">
</br>

### After hitting "Play" button.
<br>
<img src="https://github.com/patrikas-sestokas/GraphMap/blob/main/assets/Showcase2.png">
</br>
The graph optimizes itself to the nearest local mininmum where vertice repulsion and edge attraction forces balance each other out as defined by:

[Hooke's law](https://en.wikipedia.org/wiki/Spring_(device))

Where edges are "springs" and vertices repel each other.

## SimpleDraw.java

<img src="https://github.com/patrikas-sestokas/GraphMap/blob/main/assets/Showcase3.png">

SimpleDraw provides simplified user interface with only vertice matrix as an input.

## SubTree.java

<img src="https://github.com/patrikas-sestokas/GraphMap/blob/main/assets/Showcase4.png">

SubTree provides interface for calculating subtrees of user provided graph.

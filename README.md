# Hn-Writer
n-Directional H4-Writer

## General Usage
Hn-Writer speeds up text input for controllers by replacing the common (but inefficient) "visual keyboard" with a highly-efficient n-directional keyboard.

This is accomplished by reducing button-presses (or joystick movements) to a theoretical minimum with an n-ary Huffman coding. Your job is to map controller input to an enum `E` with `n` values and pass that to `HnWriter::walk`. `HnWriter` then uses this value to navigate an n-ary tree. If a leaf is navigated to, the tree rewinds and adds the node's content (e.g. the letter `a`) to a running buffer. You can get the current content of the buffer with `HnWriter::getContent`. For display purposes, you can get a `Map<E, String>` of directions and associated content lists with `HnWriter::getContentList`.

### Building the HnWriter

```Java
// Build key indexer -> enum map.
Map<String, TestEnum> contentEnumMap = new HashMap<String, TestEnum>();
contentEnumMap.put("U", TestEnum.UP);
contentEnumMap.put("D", TestEnum.DOWN);
contentEnumMap.put("L", TestEnum.LEFT);
contentEnumMap.put("R", TestEnum.RIGHT);

// Get flat JSON adjacency list source file.
File sourceFile = new File("path/to/file");

// Construct builder that will turn the JSON file to an EnumTree.
EnumTreeBuilder<TestEnum> builder = new FlatJSONTreeBuilder<TestEnum>(
    sourceFile, 
    contentEnumMap,
    TestEnum.class
);

// Construct writer.
writer = new HnWriter<TestEnum>(builder);
```

```Java
enum TestEnum {
    UP, 
    DOWN, 
    LEFT, 
    RIGHT
}
```

### Trying some sequences.

```Java
// Input sequence "hi"
writer.walk(TestEnum.UP);
writer.walk(TestEnum.LEFT); // h
writer.walk(TestEnum.UP);
writer.walk(TestEnum.DOWN); // i
// out: "hi"
System.out.println(writer.getString());

// Input newline "\n"
// This will "confirm" the text, adding it to the history and 
// clearing current string so you can input a new sequence.
writer.walk(TestEnum.LEFT);
writer.walk(TestEnum.UP);   // \n
// out: ""
System.out.println(writer.getString());
// out: "hi"
System.out.println(writer.getHistory(0));
```

## Current task list
- Create a builder that will automagically make a k-ary Huffman-coded tree.
- Return more detailed result from `HnWriter::getContentList`
- More error checking: missing backspace/enter key, missing alphanumerics, leaves that don't contain text, etc.
- Should be able to provide callback func that is async invoked after newline is entered.
- Other callbacks (backspace for error-logging?).

## DIY
If you want to make your own coding (e.g. H4 + diagonals, modified H4, etc.), here's what to do:

1. Create a file containing the coding to be used. Currently this is done with a flat JSON adjacency list, where each entry is structured as `"key": "valueString"`, where `key` is the key to write, and `valueString` is a sequence of single-character "key indexers". There should be a maximum of `n` unique key indexers in the entire file.
2. Create an enum `E` with `n` values.
3. Create a `Map<String, E>` that maps every value in `E` to one of the `n` key indexers.
4. Pass all of this to a class extending `EnumTreeBuilder<E>`. For a flat JSON adjacency list, `ca.zach_bright.FlatJSONTreeBuilder<E>` will work perfectly.
5. Instantiate an `HnWriter<E>` with the `EnumTreeBuilder<E>`.
6. Proceed as normal.

## Theory
The H4-Writer is an input method that lets users type with just four buttons. Essentially, each key on the keyboard is given a minimized 
input sequence based on its usage frequency, producing a 4-ary Huffman coding. Each value (0, 1, 2, 3) in the coding is then assigned a button on the input device. Inputting a sequence of button presses then results in writing the chosen key to the screen. To allow for error correction and capitalization a shift, capslock, and easily-accessable backspace key are provided as well. See MacKenzie et al. (2011) (http://www.yorku.ca/mack/uist2011.html) for more information.

I and two other students (https://github.com/nnguyen259 and https://github.com/LucasJPond) created an implementation of the original H4-Writer concept for the purposes of comparing it to a generic controller input method, which we called "soft keyboard". We used a 4-ary tree to represent the key space, where each node contained either a key string or up to four children nodes, indexed with an EnumMap. Buttons from the controller were converted to an enum (UP, DOWN, LEFT, RIGHT) and passed to the tree. See https://github.com/zach-bright/CS4065-Project for details.

This project basically takes the tree system from https://github.com/zach-bright/CS4065-Project and abstracts it to work with any n-ary coding. Thus given an enum `E`, each node contains either a key string or up to `n` children nodes indexed by an `EnumMap<E, Node>`. 

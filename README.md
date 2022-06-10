# Swift code editor

A simple Swift code editor written in Java.\
The project was developed in **IntelliJ IDEA 2022.1.1 (Ultimate Edition)** using **Zulu 16 JDK**.

### How to run
There are two options to run the project
- clone the project to your local machine, open it using IntelliJ IDEA or any other Java IDE and run the project locally
- download and double click this [runnable JAR file](./out/artifacts/SwiftCodeEditor_jar/SwiftCodeEditor.jar)


### Required functionality
- editor pane and output pane
- the script is written to `foo.swift` and executed using `/usr/bin/env swift foo.swift`
- the output pane shows **live output** of the script
  - `setbuf(__stdoutp, nil)` must be present at the top of the script to disable buffering in Swift
- the output pane shows **compilation errors** as well as the exit code of the script
- a colored **status message** indicates whether the script is running, or whether the execution was successful or not
- **exit code** is displayed in the output pane

### Additional functionality
- **syntax highlighting** for keywords, strings and numbers, which can be further extended using regular expressions
- **compilation errors** are highlighted, **clickable** and scroll to the specific line of the error in the code
- **execution** of the script **multiple times** in a row is supported (progress bar and estimated time remaining also implemented)
  - the input field right next to the three buttons in the toolbar determines how many times the script should be executed

by Tomáš Boďa

# Swift code editor

A simple Swift code editor written in Java.\
The project was developed in **IntelliJ IDEA 2022.1.1 (Ultimate Edition)** using **Zulu 16 JDK**.

### How to run
There are two options to run the project
- clone the project to your local machine, open it using IntelliJ IDEA or any other Java IDE and run the project locally
- download this [runnable JAR file](./out/artifacts/SwiftCodeEditor_jar/SwiftCodeEditor.jar) and double click it to run


### Required functionality
:white_check_mark: editor pane and output pane\
:white_check_mark: the script is written to `foo.swift` and executed using `/usr/bin/env swift foo.swift`\
:white_check_mark: the output pane shows live output, however, the `setbuf(__stdoutp, nil)` must be set in the script in order to disable buffering\
:white_check_mark: the output pane shows errors as well as the exit code of the script\
:white_check_mark: a colored status message indicates whether the script is running, or whether the execution was successful or not\
:white_check_mark: exit code is displayed in the output pane

### Additional functionality
:white_check_mark: working syntax highlighting for keywords, strings and numbers, which can be further extended using regular expressions\
:white_check_mark: script errors are highlighted, clickable and scroll to the specific line of the error in the code\
:white_check_mark: the editor supports execution of the script multiple times in a row (progress bar and estimated time remaining also implemented)
\
\
by Tomáš Boďa

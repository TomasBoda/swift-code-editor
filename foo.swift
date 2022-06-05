import Dispatch
import Foundation
setbuf(__stdoutp, nil)

let args = CommandLine.arguments
let index = Int(args[1]) ?? 0

let sentences: [String] = [
    "Welcome to the Swift Code Editor!",
    "Let's take a look at what you can do with it.",
    "The RUN button in the upper-left corner runs\nthe code that is written in this editor pane.",
    "The STOP button terminates the currently\nrunning code.",
    "The CLEAR button clears the editor pane,\nso that you can start writing code from\nscratch.",
    "The black input field right next to the\nbutton determines how many times should\nthe script be executed. It only accepts\nwhole numbers.",
    "That's it! Have fun! Oh, one more thing.",
    "In order to show you live output of the\nscript, the very first two lines of Swift\ncode have to be present.",
    "They disable buffering, so the output goes\ninstantly and directly to the console pane.",
    "Okay, time to write some code!"
]

let lettersPerSecond = 30
let sentence: String = sentences[index]
let length = sentence.count
let duration: UInt32 = UInt32(length / lettersPerSecond)

print(sentences[index])
sleep(duration)
package file_system

import commands.Command
import files.Directory

object SimpleVirtualFileSystem extends App {
  io.Source.stdin
    .getLines()
    .foldLeft(State(Directory.ROOT, Directory.ROOT))((currentState, newLine) => {
      currentState.show
      Command.from(newLine).apply(currentState)})
}

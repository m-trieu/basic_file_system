package commands.display

import commands.Command
import file_system.State
import files.FileSystemEntry

class ListSegments extends Command {
  override def apply(currentState: State): State = {
    val contents = currentState.wd.contents
    val prettyString = prettyPrint(contents)
    currentState.setMessage(prettyString)
  }

  private def prettyPrint(contents: List[FileSystemEntry]): String = {
    if(contents.isEmpty) ""
    else s"${contents.head.name}[${contents.head.entryType}]\n${prettyPrint(contents.tail)}"
  }
}

object ListSegments {
  val  CMD = "ls"
}

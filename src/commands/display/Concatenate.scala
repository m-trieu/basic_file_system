package commands.display

import commands.Command
import file_system.State
import files.File

class Concatenate(fileName: String) extends Command {
  override def apply(currentState: State): State = {
    val entry = currentState.wd.find(fileName)
    if(entry == null || !entry.is[File]) currentState.setMessage(s"$fileName does not exist")
    else currentState.setMessage(entry.asFile.contents)
  }
}

object Concatenate {
  val CMD = "cat"
}

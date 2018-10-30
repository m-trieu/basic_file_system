package commands.display

import commands.Command
import file_system.State

class PrintWorkingDirectory extends Command {
  override def apply(currentState: State): State = currentState.setMessage(currentState.wd.path)
}

object PrintWorkingDirectory {
  val CMD = "pwd"
}

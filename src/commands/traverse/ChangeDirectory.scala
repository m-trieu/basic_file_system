package commands.navigate

import commands.Command
import file_system.State

class ChangeDirectory extends Command{
  override def apply(currentState: State): State = ???
}

object ChangeDirectory {
  val CMD = "cd"
}

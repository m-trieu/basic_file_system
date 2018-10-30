package commands
import file_system.State

class MakeFile extends Command {
  override def apply(currentState: State): State = {
    val wd = currentState.wd
  }
}

object MakeFile {
  val CMD = "touch"
}

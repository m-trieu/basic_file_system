package commands

import file_system.State

class UnknownCommand extends Command {
  override def apply(currentState: State): State = currentState.setMessage("Command not found")
}

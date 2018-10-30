package commands

import file_system.State
import files.Directory

class RemoveEntry(name: String) extends Command {
  override def apply(currentState: State): State = {
    val wd = currentState.wd
    val absPath =
      if(name.startsWith(Directory.SEPARATOR)) name
      else if(wd.isRoot) wd.path + name
      else wd.path + Directory.SEPARATOR + name

    if(absPath.equals(Directory.ROOT_PATH)) currentState.setMessage("FATALITY")
    else execute(currentState, absPath)
  }

  def execute(state: State, path: String): State = {
    def remove(current: Directory, path: List[String]): Directory =
      if(path.isEmpty) current
      else if(path.tail.isEmpty) current.removeEntry(path.head)
      else {
        val next = current.find(path.head)
        if(!next.is[Directory]) current
        else {
          val nextNext = remove(next.asDirectory, path.tail)
          if(nextNext == next) current else current.swap(path.head, nextNext)
        }
      }

    val newRoot: Directory = remove(state.root, Command.tokenize(path))
    if(newRoot == state.root) state.setMessage(s"$path : no such file or directory")
    else State(newRoot, newRoot.findChild(state.wd.path.substring(1)))
  }
}

object RemoveEntry {
  val CMD = "rm"
}

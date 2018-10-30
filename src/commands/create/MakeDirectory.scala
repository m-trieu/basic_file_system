package commands.create

import file_system.State
import files.{Directory, FileSystemEntry}

class MakeDirectory(name: String) extends CreateEntry(name) {
  override def createEntry(currentState: State): FileSystemEntry =
    Directory.empty(currentState.wd.path, name)
}

object MakeDirectory {
  val CMD = "mkdir"
}

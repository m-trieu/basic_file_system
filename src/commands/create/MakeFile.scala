package commands.create

import file_system.State
import files.{File, FileSystemEntry}

class MakeFile(name: String) extends CreateEntry(name) {
  override def createEntry(currentState: State): FileSystemEntry =
    File.empty(currentState.wd.path, name)
}

object MakeFile {
  val CMD = "touch"
}

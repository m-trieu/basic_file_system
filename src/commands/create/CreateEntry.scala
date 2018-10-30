package commands.create

import commands.{Command}
import file_system.State
import files.{Directory, FileSystemEntry}

abstract class CreateEntry(name: String) extends Command {
  override def apply(currentState: State): State = {
    val wd = currentState.wd
    if(wd.hasEntry(name)) currentState.setMessage(s"Entry $name already exists!")
    else if(name.contains(Directory.SEPARATOR)) currentState.setMessage(s"$name: must not contain separators")
    else if (!valid(name)) currentState.setMessage(s"$name: illegal entry name")
    else execute(currentState, name)
  }

  def valid(name: String): Boolean = !name.contains(".")

  def execute(currentState: State, name: String): State = {
    def updateStruct(currentDirectory: Directory, path: List[String], newEntry: FileSystemEntry): Directory = {
      if(path.isEmpty) currentDirectory.add(newEntry)
      else {
        val oldEntry = currentDirectory.find(path.head).asDirectory
        currentDirectory.swap(oldEntry.name, updateStruct(oldEntry, path.tail, newEntry))
      }
    }

    val wd = currentState.wd
    val allDirectoriesInPath = wd.foldersInPath
    val newEntry: FileSystemEntry = createEntry(currentState)
    val newRoot = updateStruct(currentState.root, allDirectoriesInPath, newEntry)
    val newWd = newRoot.findChild(allDirectoriesInPath)
    State(newRoot, newWd)
  }

  def createEntry(currentState: State): FileSystemEntry
}

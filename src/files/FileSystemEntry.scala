package files

abstract class FileSystemEntry(val parentPath: String, val name: String){
  def path: String = {
    val empty  = ""
    s"$parentPath${if(Directory.ROOT_PATH.equals(parentPath)) empty else Directory.SEPARATOR}$name"
  }
  def entryType: EntryType.Value
  def asDirectory: Directory
  def asFile: File
  def is[Type]: Boolean = this.isInstanceOf[Type]
}

object EntryType extends Enumeration {
  val DIRECTORY, FILE = Value
}



rem %LOCALAPPDATA%\Android\sdk\platform-tools\adb.exe pull /data/data/com.romanpulov.violetnote/databases/basic_note.db
%LOCALAPPDATA%\Android\sdk\platform-tools\adb.exe shell "run-as com.romanpulov.violetnote cat /data/data/com.romanpulov.violetnote/databases/basic_note.db" > basic_note.db

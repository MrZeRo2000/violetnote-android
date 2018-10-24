REM 
REM Version v1
REM Resolved permission denied issue
REM

"%LOCALAPPDATA%\Android\sdk\platform-tools\adb.exe" exec-out "run-as com.romanpulov.violetnote cat /data/data/com.romanpulov.violetnote/databases/basic_note.db" > basic_note.db

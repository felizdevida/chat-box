rem ɾ�����ļ�
del /f /s /q  ".\chat-platform.jar"
del /f /s /q  ".\chat-server.jar"
del /f /s /q  ".\chat-ui.zip"
rd  /s /q  ".\chat-ui"

rem ����java��Ŀ
call mvn clean package -f ../pom.xml

rem ����jar��
copy  "..\chat-platform\target\chat-platform.jar"   ".\"
copy  "..\chat-server\target\chat-server.jar"   ".\"

rem ���ǰ����Դ
call npm run build --prefix  ..\chat-ui\
md chat-ui
xcopy  "..\chat-ui\dist"   ".\chat-ui" /e /y

echo  ������..........
pause


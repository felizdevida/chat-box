rem ɾ�����ļ�
del /f /s /q  ".\im-platform.jar"
del /f /s /q  ".\im-server.jar"
del /f /s /q  ".\im-ui.zip"
rd  /s /q  ".\im-ui"

rem ����java��Ŀ
call mvn clean package -f ../pom.xml

rem ����jar��
copy  "..\im-platform\target\im-platform.jar"   ".\"
copy  "..\im-server\target\im-server.jar"   ".\"

rem ���ǰ����Դ
call npm run build --prefix  ..\im-ui\
md im-ui
xcopy  "..\im-ui\dist"   ".\im-ui" /e /y 

echo  ������..........
pause


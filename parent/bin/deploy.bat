@echo off
rem /**
rem  * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
rem  *
rem  * Author: ThinkGem@163.com
rem  */
echo.
echo [��Ϣ] ���𹤳̰汾��Nexus��������
echo.


%~d0
cd %~dp0

cd ..
call mvn clean deploy -Dmaven.test.skip=true -Pdeploy

cd bin
cmd /c msg %username% /time:0 /w "�������..."
pause
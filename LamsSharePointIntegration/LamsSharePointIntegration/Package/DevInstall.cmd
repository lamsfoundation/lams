cd ..

@SET TEMPLATEDIR="c:\program files\common files\microsoft shared\web server extensions\12\Template"
@SET STSADM="c:\program files\common files\microsoft shared\web server extensions\12\bin\stsadm"
@SET GACUTIL="c:\Program Files\Microsoft Visual Studio 8\SDK\v2.0\Bin\gacutil.exe"

Echo Installing CustomApplicationPages.dll in GAC
%GACUTIL% -if bin\debug\CustomApplicationPages.dll

Echo Copying files to TEMPLATE directory
xcopy /e /y TEMPLATE\* %TEMPLATEDIR%

Echo Installing feature
%STSADM% -o installfeature -filename  CustomApplicationPages\feature.xml -force

IISRESET
REM cscript c:\windows\system32\iisapp.vbs /a "SharePointDefaultAppPool" /r

cd Package
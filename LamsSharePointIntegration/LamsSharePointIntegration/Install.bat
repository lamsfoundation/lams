
@SET TEMPLATEDIR="c:\program files\common files\microsoft shared\web server extensions\12\Template"
@SET STSADM="c:\program files\common files\microsoft shared\web server extensions\12\bin\stsadm"
@SET GACUTIL="C:\Program Files\Microsoft SDKs\Windows\v6.0A\Bin\gacutil.exe"

Echo Installing LamsSharePointIntegration.dll in GAC
%GACUTIL% -u LamsSharePointIntegration
%GACUTIL% -i bin\debug\LamsSharePointIntegration.dll

Echo Copying files to TEMPLATE directory
xcopy /e /y TEMPLATE\* %TEMPLATEDIR%

Echo Installing feature
%STSADM% -o installfeature -filename  LamsSharePointIntegration\feature.xml -force

IISRESET
REM cscript c:\windows\system32\iisapp.vbs /a "SharePoint - 80" /r

REM "c:\program files\common files\microsoft shared\web server extensions\12\bin\stsadm" -o uninstallfeature -filename  LamsSharePointIntegration\feature.xml -force

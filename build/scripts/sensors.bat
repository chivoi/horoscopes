@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem

@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  sensors startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Resolve any "." and ".." in APP_HOME to make it shorter.
for %%i in ("%APP_HOME%") do set APP_HOME=%%~fi

@rem Add default JVM options here. You can also use JAVA_OPTS and SENSORS_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto execute

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto execute

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\sensors-1.0.0-SNAPSHOT.jar;%APP_HOME%\lib\vertx-web-client-4.2.4.jar;%APP_HOME%\lib\vertx-web-openapi-4.2.4.jar;%APP_HOME%\lib\vertx-web-validation-4.2.4.jar;%APP_HOME%\lib\vertx-web-4.2.4.jar;%APP_HOME%\lib\vertx-hazelcast-4.2.4.jar;%APP_HOME%\lib\vertx-pg-client-4.2.4.jar;%APP_HOME%\lib\log4j-core-2.17.1.jar;%APP_HOME%\lib\log4j-api-2.17.1.jar;%APP_HOME%\lib\resteasy-vertx-6.0.0.Final.jar;%APP_HOME%\lib\netty-resolver-dns-native-macos-4.1.73.Final-osx-x86_64.jar;%APP_HOME%\lib\vertx-sql-client-4.2.4.jar;%APP_HOME%\lib\vertx-web-common-4.2.4.jar;%APP_HOME%\lib\vertx-auth-common-4.2.4.jar;%APP_HOME%\lib\vertx-bridge-common-4.2.4.jar;%APP_HOME%\lib\vertx-json-schema-4.2.4.jar;%APP_HOME%\lib\vertx-core-4.2.4.jar;%APP_HOME%\lib\netty-handler-proxy-4.1.73.Final.jar;%APP_HOME%\lib\netty-codec-http2-4.1.73.Final.jar;%APP_HOME%\lib\netty-codec-http-4.1.73.Final.jar;%APP_HOME%\lib\netty-resolver-dns-classes-macos-4.1.73.Final.jar;%APP_HOME%\lib\netty-resolver-dns-4.1.73.Final.jar;%APP_HOME%\lib\netty-handler-4.1.73.Final.jar;%APP_HOME%\lib\netty-transport-native-unix-common-4.1.73.Final.jar;%APP_HOME%\lib\netty-codec-socks-4.1.73.Final.jar;%APP_HOME%\lib\netty-codec-dns-4.1.73.Final.jar;%APP_HOME%\lib\netty-codec-4.1.73.Final.jar;%APP_HOME%\lib\netty-transport-4.1.73.Final.jar;%APP_HOME%\lib\netty-buffer-4.1.73.Final.jar;%APP_HOME%\lib\netty-resolver-4.1.73.Final.jar;%APP_HOME%\lib\netty-common-4.1.73.Final.jar;%APP_HOME%\lib\netty-tcnative-classes-2.0.46.Final.jar;%APP_HOME%\lib\jackson-core-2.13.1.jar;%APP_HOME%\lib\jackson-dataformat-yaml-2.13.1.jar;%APP_HOME%\lib\jackson-databind-2.13.1.jar;%APP_HOME%\lib\jackson-annotations-2.13.1.jar;%APP_HOME%\lib\resteasy-client-6.0.0.Final.jar;%APP_HOME%\lib\resteasy-client-api-6.0.0.Final.jar;%APP_HOME%\lib\resteasy-core-6.0.0.Final.jar;%APP_HOME%\lib\resteasy-core-spi-6.0.0.Final.jar;%APP_HOME%\lib\jboss-logging-3.4.2.Final.jar;%APP_HOME%\lib\hazelcast-4.2.2.jar;%APP_HOME%\lib\jakarta.annotation-api-2.0.0.jar;%APP_HOME%\lib\jboss-jaxrs-api_3.0_spec-1.0.0.Final.jar;%APP_HOME%\lib\jakarta.xml.bind-api-3.0.1.jar;%APP_HOME%\lib\reactive-streams-1.0.3.jar;%APP_HOME%\lib\jakarta.validation-api-3.0.0.jar;%APP_HOME%\lib\httpclient-4.5.13.jar;%APP_HOME%\lib\commons-codec-1.15.jar;%APP_HOME%\lib\commons-io-2.9.0.jar;%APP_HOME%\lib\snakeyaml-1.28.jar;%APP_HOME%\lib\jakarta.activation-api-2.0.0.jar;%APP_HOME%\lib\asyncutil-0.1.0.jar;%APP_HOME%\lib\httpcore-4.4.13.jar;%APP_HOME%\lib\commons-logging-1.2.jar


@rem Execute sensors
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %SENSORS_OPTS%  -classpath "%CLASSPATH%" io.vertx.core.Launcher %*

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable SENSORS_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%SENSORS_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega

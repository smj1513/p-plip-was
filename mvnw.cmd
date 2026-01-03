@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.  See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership.  The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License.  You may obtain a copy of the License at
@REM
@REM    https://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied.  See the License for the
@REM specific language governing permissions and limitations
@REM under the License.
@REM ----------------------------------------------------------------------------
@REM ----------------------------------------------------------------------------
@REM Apache Maven Wrapper startup batch script, version 3.2.0
@REM
@REM Optional ENV vars
@REM   MVNW_REPOURL - repo url base for downloading maven distribution
@REM   MVNW_USERNAME - username for basic auth to download maven distribution
@REM   MVNW_PASSWORD - password for basic auth to download maven distribution
@REM ----------------------------------------------------------------------------

@ECHO OFF

SETLOCAL

SET MVNW_REPOURL=https://repo.maven.apache.org/maven2

SET MAVEN_WRAPPER_JAR=maven-wrapper.jar
SET WRAPPER_JAR_PATH="%~dp0.mvn\wrapper\%MAVEN_WRAPPER_JAR%"

SET DOWNLOAD_URL=%MVNW_REPOURL%/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar

IF NOT EXIST %WRAPPER_JAR_PATH% (
    ECHO Downloading %DOWNLOAD_URL%
    powershell -Command "if (-not (Test-Path -Path '%~dp0.mvn\wrapper')) { New-Item -ItemType Directory -Path '%~dp0.mvn\wrapper' | Out-Null }; try { Invoke-WebRequest -Uri '%DOWNLOAD_URL%' -OutFile '%WRAPPER_JAR_PATH%' } catch { Write-Host 'Failed to download maven-wrapper.jar'; exit 1 }"
)

SET MAVEN_CMD_LINE_ARGS=%*
java -jar %WRAPPER_JAR_PATH% %MAVEN_CMD_LINE_ARGS%

ENDLOCAL
EXIT /B %ERRORLEVEL%
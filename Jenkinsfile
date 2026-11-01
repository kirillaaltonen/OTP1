pipeline {
    agent any

    stages {

        stage('Checkout') {
            steps {
                git 'https://github.com/kirillaaltonen/OTP1'
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean install'
            }
        }

        stage('Test') {
            steps {
                bat 'mvn test'
            }
        }

        stage('Code Coverage') {
            steps {
                bat 'mvn jacoco:report'
            }
        }

        stage('Publish Test Results') {
            steps {
                junit '**/target/surefire-reports/*.xml'
            }
        }

        stage('Publish Coverage Report') {
            steps {
                recordCoverage(
                    tools: [[
                            parser: 'JACOCO',
                            pattern: 'target/site/jacoco/jacoco.xml'
                        ]]
                )
            }
        }
        stage('Check Docker') {
            steps {
                bat '"C:\\Users\\alokas\\AppData\\Local\\Programs\\DockerDesktop\\resources\\bin\\docker.exe" --version'
            }
        }
        stage('Build Docker Image') {
            steps {
                bat '"C:\\Users\\alokas\\AppData\\Local\\Programs\\DockerDesktop\\resources\\bin\\docker.exe" build -t holuses/temperature-converter:latest .'
            }
        }

        stage('Push Docker Image') {
            steps {
                withCredentials([
                        string(
                            credentialsId: 'dockerhub_token',
                            variable: 'DOCKER_TOKEN'
                        )
                    ]) {
                    powershell '''
                $docker = "C:\\Users\\alokas\\AppData\\Local\\Programs\\DockerDesktop\\resources\\bin\\docker.exe"

                $dockerConfig = Join-Path $env:WORKSPACE ".docker"
                New-Item -ItemType Directory -Force -Path $dockerConfig | Out-Null

                $loginString = "holuses:" + $env:DOCKER_TOKEN.Trim()
                $bytes = [System.Text.Encoding]::UTF8.GetBytes($loginString)
                $auth = [Convert]::ToBase64String($bytes)

                $config = @{
                    auths = @{
                        "https://index.docker.io/v1/" = @{
                            auth = $auth
                        }
                    }
                }

                $config | ConvertTo-Json -Depth 5 | Set-Content -Encoding ASCII "$dockerConfig\\config.json"

                $env:DOCKER_CONFIG = $dockerConfig

                & $docker push holuses/temperature-converter:latest

                if ($LASTEXITCODE -ne 0) {
                    exit $LASTEXITCODE
                }
            '''
                }
            }
        }
    }
}
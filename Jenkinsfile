pipeline {
    agent any

    environment {
        DOCKER_USER = 'selimselmi'
        BACK_IMAGE  = "${DOCKER_USER}/projets-backend"
        FRONT_IMAGE = "${DOCKER_USER}/projets-frontend"
        TAG         = "${env.BUILD_NUMBER}"
    }

    stages {
        stage('Checkout') {
            steps { checkout scm }
        }

        stage('Build Backend') {
            steps {
                dir('backend') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Unit Tests') {
            steps {
                dir('backend') {
                    sh 'mvn test'
                }
            }
            post {
                always {
                    junit 'backend/target/surefire-reports/*.xml'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                dir('backend') {
                    withSonarQubeEnv('SonarQube') {
                        sh 'mvn clean verify sonar:sonar -Dsonar.projectKey=projets-backend'
                    }
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Build Frontend') {
            steps {
                dir('frontend') {
                    sh 'npm install'
                    sh 'npm run build'
                }
            }
        }

        stage('Archive') {
            steps {
                archiveArtifacts artifacts: 'backend/target/*.jar', fingerprint: true
            }
        }

        stage('Docker Login') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-creds',
                        usernameVariable: 'U', passwordVariable: 'P')]) {
                    sh 'echo $P | docker login -u $U --password-stdin'
                }
            }
        }

        stage('Build Docker Images') {
            steps {
                sh "docker build -t ${BACK_IMAGE}:${TAG} -t ${BACK_IMAGE}:latest ./backend"
                sh "docker build -t ${FRONT_IMAGE}:${TAG} -t ${FRONT_IMAGE}:latest ./frontend"
            }
        }

        stage('Push Images') {
            steps {
                sh "docker push ${BACK_IMAGE}:${TAG}"
                sh "docker push ${BACK_IMAGE}:latest"
                sh "docker push ${FRONT_IMAGE}:${TAG}"
                sh "docker push ${FRONT_IMAGE}:latest"
            }
        }

        stage('Déploiement MySQL') {
            steps {
                sh 'docker compose up -d db'
                sh 'sleep 15'
            }
        }

        stage('Déploiement Backend & Frontend') {
            steps {
                sh 'docker compose up -d --build backend frontend'
            }
        }

        stage('Vérification du déploiement') {
            steps {
                sh 'docker ps'
                sh 'docker logs appprojets-backend-1'
            }
        }
    }

    post {
        always {
            sh 'docker logout || true'
        }
        success { echo 'Pipeline OK: app is on http://localhost:4200' }
        failure { echo 'Pipeline FAILED: check the console output' }
    }
}
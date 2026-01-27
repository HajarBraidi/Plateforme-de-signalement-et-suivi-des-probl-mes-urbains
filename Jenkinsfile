pipeline {
    agent any

    tools {
        maven 'Maven-3.8'
        jdk 'JDK-11'
    }

    environment {
        MAVEN_OPTS = '-Dmaven.test.failure.ignore=true'
        PROJECT_NAME = 'Plateforme Signalement Urbain'
    }

    stages {

        stage('Informations') {
            steps {
                echo "=========================================="
                echo "DÉBUT DU PIPELINE CI/CD"
                echo "=========================================="
                echo "Projet: ${env.PROJECT_NAME}"
                echo "Branche: ${env.BRANCH_NAME}"
                echo "Build: ${env.BUILD_NUMBER}"
                echo "=========================================="
            }
        }

        stage('Cloner le Repository') {
            steps {
                echo "Clonage du repository depuis GitHub..."
                checkout scm
                echo "Repository cloné avec succès!"
            }
        }

        stage('Nettoyage') {
            steps {
                echo "Nettoyage des fichiers de build précédents..."
                bat 'mvn clean'
                echo "Nettoyage terminé!"
            }
        }

        stage('Compilation') {
            steps {
                echo "Compilation du code source..."
                bat 'mvn compile'
                echo "Compilation réussie!"
            }
        }

        stage('Tests Unitaires') {
            steps {
                echo "Exécution des tests unitaires..."
                bat 'mvn test'
                echo "Tests terminés!"
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Package') {
            steps {
                echo "Création du fichier WAR..."
                bat 'mvn package -DskipTests'
                echo "Package créé avec succès!"
            }
            post {
                success {
                    archiveArtifacts artifacts: 'target/*.war', fingerprint: true
                }
            }
        }

        stage('Rapport de Build') {
            steps {
                echo "=========================================="
                echo "RAPPORT DE BUILD"
                echo "=========================================="

                bat '''
                echo Fichiers WAR générés :
                dir target\\*.war

                echo.
                echo Taille du WAR :
                for %%F in (target\\*.war) do echo %%~zF octets
                '''

                echo "=========================================="
            }
        }
    }

    post {
        success {
            echo "=========================================="
            echo "BUILD RÉUSSI!"
            echo "=========================================="
            echo "Artifact WAR disponible dans Jenkins"
            echo "=========================================="
        }

        failure {
            echo "=========================================="
            echo "BUILD ÉCHOUÉ!"
            echo "=========================================="
            echo "Vérifiez les logs ci-dessus"
            echo "=========================================="
        }

        always {
            echo "=========================================="
            echo "Nettoyage post-build..."
            echo "=========================================="
            cleanWs()
        }
    }
}

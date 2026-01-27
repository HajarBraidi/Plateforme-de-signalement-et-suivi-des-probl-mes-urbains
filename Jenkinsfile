pipeline {
    agent any  // Exécute sur n'importe quel agent Jenkins disponible

    tools {
        maven 'Maven-3.8'
        jdk 'JDK-11'
    }

    environment {
        MAVEN_OPTS = '-Dmaven.test.failure.ignore=true'  // Continue même si tests échouent
        PROJECT_NAME = 'Plateforme Signalement Urbain'
    }

     stages {
        stage('Informations') {
            steps {
                script {
                    echo "=========================================="
                    echo "DÉBUT DU PIPELINE CI/CD"
                    echo "=========================================="
                    echo "Projet: ${env.PROJECT_NAME}"
                    echo "Branche: ${env.BRANCH_NAME}"
                    echo "Build: ${env.BUILD_NUMBER}"
                    echo "Démarré par: ${env.BUILD_USER}"
                    echo "=========================================="
                }
            }
        }
        
        stage('Cloner le Repository') {
            steps {
                script {
                    echo "Clonage du repository depuis GitHub..."
                    checkout scm
                    echo "Repository cloné avec succès!"
                }
            }
        }
        
        stage('Nettoyage') {
            steps {
                script {
                    echo "Nettoyage des fichiers de build précédents..."
                    bat 'mvn clean'
                    echo "Nettoyage terminé!"
                }
            }
        }
        
        stage('Compilation') {
            steps {
                script {
                    echo "Compilation du code source..."
                    bat 'mvn compile'
                    echo "Compilation réussie!"
                }
            }
        }
        
        stage('Tests Unitaires') {
            steps {
                script {
                    echo "Exécution des tests unitaires..."
                    bat 'mvn test'
                    echo "Tests terminés!"
                }
            }
            post {
                always {
                    // Publier les résultats des tests
                    junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('Package') {
            steps {
                script {
                    echo "Création du fichier WAR..."
                    bat 'mvn package -DskipTests'
                    echo "Package créé avec succès!"
                }
            }
            post {
                success {
                    // Archiver le fichier WAR généré
                    archiveArtifacts artifacts: '**/target/*.war', fingerprint: true
                }
            }
        }
        
        stage('Analyse SonarQube') {
            steps {
                script {
                    echo "Préparation de l'analyse SonarQube..."
                    echo "SonarQube sera configuré à la prochaine étape"
                    // Cette étape sera complétée dans l'étape 3
                }
            }
        }
        
        stage('Rapport de Build') {
            steps {
                script {
                    echo "=========================================="
                    echo "RAPPORT DE BUILD"
                    echo "=========================================="
                    bat '''
                        echo "Fichiers WAR générés:"
                        find target -name "*.war" -type f
                        echo ""
                        echo "Taille du WAR:"
                        du -h target/*.war || echo "Aucun WAR trouvé"
                    '''
                    echo "=========================================="
                }
            }
        }
    }
      post {
        success {
            echo "=========================================="
            echo "BUILD RÉUSSI!"
            echo "=========================================="
            echo "Le pipeline s'est terminé avec succès"
            echo "Artifact disponible dans Jenkins"
            echo "=========================================="
        }
        
        failure {
            echo "=========================================="
            echo "BUILD ÉCHOUÉ!"
            echo "=========================================="
            echo "Vérifiez les logs ci-dessus pour identifier l'erreur"
            echo "=========================================="
        }
        
        always {
            echo "=========================================="
            echo "Nettoyage post-build..."
            echo "=========================================="
            // Nettoyage si nécessaire
            cleanWs(deleteDirs: true, patterns: [[pattern: 'target/surefire-reports', type: 'EXCLUDE']])
        }
    }
}
    

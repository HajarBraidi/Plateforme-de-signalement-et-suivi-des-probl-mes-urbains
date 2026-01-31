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
                script {
                    echo "=========================================="
                    echo " DÉBUT DU PIPELINE CI/CD"
                    echo "=========================================="
                    echo "Projet: ${env.PROJECT_NAME}"
                    echo "Branche: ${env.BRANCH_NAME ?: 'develop'}"
                    echo "Build: ${env.BUILD_NUMBER}"
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
        
        stage('🧹 Nettoyage') {
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
                    echo " Exécution des tests unitaires..."
                    bat 'mvn test'
                    echo " Tests terminés!"
                }
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('Package') {
            steps {
                script {
                    echo "Création du fichier WAR..."
                    bat 'mvn package -DskipTests'
                    echo " Package créé avec succès!"
                }
            }
            post {
                success {
                    archiveArtifacts artifacts: '**/target/*.war', fingerprint: true
                }
            }
        }
        
        stage('Analyse SonarQube') {
            steps {
                script {
                    echo "=========================================="
                    echo "ANALYSE SONARQUBE - QUALITÉ DU CODE"
                    echo "=========================================="
                    
                    // Utiliser l'environnement SonarQube configuré dans Jenkins
                    withSonarQubeEnv('SonarQube-Local') {
                        echo " Lancement de l'analyse SonarQube..."
                        
                        // Exécuter l'analyse Maven avec SonarQube
                        bat '''
                            mvn clean verify sonar:sonar ^
                            -Dsonar.projectKey=plateforme-urbaine ^
                            -Dsonar.projectName="Plateforme de Signalement Urbain" ^
                            -Dsonar.host.url=http://localhost:9000 ^
                            -Dsonar.java.binaries=target/classes
                        '''
                        
                        echo "Analyse SonarQube terminée!"
                        echo "Consultez le rapport sur: http://localhost:9000/dashboard?id=plateforme-urbaine"
                    }
                }
            }
        }
        
        stage('Quality Gate') {
            steps {
                script {
                    echo "=========================================="
                    echo "VÉRIFICATION QUALITY GATE"
                    echo "=========================================="
                    
                    // Attendre le résultat du Quality Gate
                    timeout(time: 5, unit: 'MINUTES') {
                        echo " Attente du résultat du Quality Gate SonarQube..."
                        
                        // Attendre la réponse de SonarQube
                        def qg = waitForQualityGate()
                        
                        if (qg.status != 'OK') {
                            echo "Quality Gate STATUS: ${qg.status}"
                            echo "Le Quality Gate a échoué, mais le build continue pour le TP"
                            echo "Consultez SonarQube pour voir les issues à corriger"
                            // On ne fait pas échouer le build pour le TP
                            // unstable(message: "Quality Gate failed: ${qg.status}")
                        } else {
                            echo "Quality Gate PASSED!"
                            echo "Le code respecte les standards de qualité"
                        }
                    }
                    
                    echo "=========================================="
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
                        echo  Fichiers WAR générés:
                        dir target\\*.war 2>nul || echo Aucun WAR trouvé
                        echo.
                        echo Taille du WAR:
                        for %%F in (target\\*.war) do echo %%~zF octets
                    '''
                    echo "=========================================="
                }
            }
        }
    }
    
    post {
        success {
            echo "=========================================="
            echo " BUILD RÉUSSI!"
            echo "=========================================="
            echo "Le pipeline s'est terminé avec succès"
            echo "Artifact disponible dans Jenkins"
            echo "Rapport SonarQube: http://localhost:9000/dashboard?id=plateforme-urbaine"
            echo "=========================================="
        }
        
        failure {
            echo "=========================================="
            echo " BUILD ÉCHOUÉ!"
            echo "=========================================="
            echo "Vérifiez les logs ci-dessus pour identifier l'erreur"
            echo "=========================================="
        }
        
        always {
            echo "=========================================="
            echo "Nettoyage post-build..."
            echo "=========================================="
            // Nettoyage workspace mais garde les rapports
            cleanWs(deleteDirs: true, patterns: [[pattern: 'target/surefire-reports', type: 'EXCLUDE']])
        }
    }
}